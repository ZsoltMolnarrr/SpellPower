package net.spell_power.api;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.attributes.EntityAttributes_SpellPower;
import net.spell_power.api.enchantment.Enchantments_SpellPower;
import net.spell_power.api.enchantment.SpellPowerEnchanting;
import net.spell_power.api.statuseffects.VulnerabilityEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static net.spell_power.api.attributes.SpellAttributes.PERCENT_ATTRIBUTE_BASELINE;

public class SpellPower {
    public record Result(MagicSchool school, double baseValue, double criticalChance, double criticalDamage) {
        public static Result empty(MagicSchool school) {
            return new Result(school, 0, 0, 0);
        }
        private static Random rng = new Random();
        private enum CriticalStrikeMode {
            DISABLED, ALLOWED, FORCED
        }

        public double randomValue() {
            return value(CriticalStrikeMode.ALLOWED, Vulnerability.none);
        }

        public double randomValue(Vulnerability vulnerability) {
            return value(CriticalStrikeMode.ALLOWED, vulnerability);
        }

        public double nonCriticalValue() {
            return value(CriticalStrikeMode.DISABLED, Vulnerability.none);
        }

        public double forcedCriticalValue() {
            return value(CriticalStrikeMode.FORCED, Vulnerability.none);
        }

        private double value(CriticalStrikeMode mode, Vulnerability vulnerability) {
            var value = baseValue * (1F + vulnerability.powerBaseMultiplier);
            if (mode != CriticalStrikeMode.DISABLED) {
                boolean isCritical = (mode == CriticalStrikeMode.FORCED) || (rng.nextFloat() < (criticalChance + vulnerability.criticalChanceBonus));
                if (isCritical) {
                    value *= (criticalDamage + vulnerability.criticalDamageBonus);
                }
            }
            return value;
        }
    }

    public record VulnerabilityQuery(LivingEntity entity, MagicSchool school) { }
    public static final ArrayList<Function<VulnerabilityQuery, List<Vulnerability>>> vulnerabilitySources = new ArrayList<Function<VulnerabilityQuery, List<Vulnerability>>>(
            Arrays.asList(
                    (query -> {
                        var vulnerabilities = new ArrayList<Vulnerability>();
                        for(var effect: query.entity.getStatusEffects()) {
                            if (effect.getEffectType() instanceof VulnerabilityEffect vulnerabilityEffect) {
                                vulnerabilities.add(vulnerabilityEffect.getVulnerability(query.school, effect.getAmplifier()));
                            }
                        }
                        return vulnerabilities;
                    })
            ));

    public static Vulnerability getVulnerability(LivingEntity livingEntity, MagicSchool school) {
        var query = new VulnerabilityQuery(livingEntity, school);
        var elements = new ArrayList<Vulnerability>();
        for(var source: vulnerabilitySources) {
            elements.addAll(source.apply(query));
        }
        return Vulnerability.sum(elements);
    }

    public record Vulnerability(float powerBaseMultiplier, float criticalChanceBonus, float criticalDamageBonus) {
        public static final Vulnerability none = new Vulnerability(0, 0, 0);
        public static Vulnerability sum(List<Vulnerability> elements) {
            var value = none;
            for(var element: elements) {
                value = new Vulnerability(
                        value.powerBaseMultiplier + element.powerBaseMultiplier,
                        value.criticalChanceBonus + element.criticalChanceBonus,
                        value.criticalDamageBonus + element.criticalDamageBonus
                );
            }
            return value;
        }

        public Vulnerability multiply(float value) {
            return new Vulnerability(powerBaseMultiplier * value, criticalChanceBonus * value, criticalDamageBonus * value);
        }
    }

    public static Result getSpellPower(MagicSchool school, LivingEntity entity) {
        return getSpellPower(school, entity, null);
    }

    public static Result getSpellPower(MagicSchool school, LivingEntity entity, ItemStack provisionedWeapon) {
        EntityAttribute attribute;
        if (school.isExternalAttribute()) {
            attribute = Registries.ATTRIBUTE.get(school.attributeId());
        } else {
            attribute = EntityAttributes_SpellPower.POWER.get(school);
        }
        if (attribute == null) {
            return Result.empty(school);
        }

        var value = entity.getAttributeValue(attribute);
        for (var booster: SpellPowerEnchanting.boostersFor(school)) {
            var level = getEnchantmentLevel(booster.enchantment(), entity, provisionedWeapon);
            value = booster.amplifier().apply(value, level);
        }

        return new Result(
                school,
                value,
                getCriticalChance(entity, provisionedWeapon),
                getCriticalMultiplier(entity, provisionedWeapon));
    }

    public static double getCriticalChance(LivingEntity entity) {
        return getCriticalChance(entity, null);
    }

    public static double getCriticalChance(LivingEntity entity, ItemStack provisionedWeapon) {
        var base = SpellPowerMod.attributesConfig.value.base_spell_critical_chance_percentage; // 5
        double value = entity.getAttributeValue(EntityAttributes_SpellPower.CRITICAL_CHANCE); // For example: 115
        var enchantment = Enchantments_SpellPower.CRITICAL_CHANCE;
        var level = getEnchantmentLevel(enchantment, entity, provisionedWeapon); // For example: 3
        value += enchantment.amplify(0, level) * PERCENT_ATTRIBUTE_BASELINE; // For example: +15
        value += base;
        return (value - PERCENT_ATTRIBUTE_BASELINE) / PERCENT_ATTRIBUTE_BASELINE; // For example: (135-100)/100 = 0.35
    }

    public static double getCriticalMultiplier(LivingEntity entity) {
        return getCriticalMultiplier(entity, null);
    }

    public static double getCriticalMultiplier(LivingEntity entity, ItemStack provisionedWeapon) {
        double value = entity.getAttributeValue(EntityAttributes_SpellPower.CRITICAL_DAMAGE); // For example: 150 (with +50% modifier)
        var enchantment = Enchantments_SpellPower.CRITICAL_DAMAGE;
        var level = getEnchantmentLevel(enchantment, entity, provisionedWeapon); // For example: level 3
        value += enchantment.amplify(0, level) * PERCENT_ATTRIBUTE_BASELINE; // For example: +30
        value += SpellPowerMod.attributesConfig.value.base_spell_critical_damage_percentage; // +50
        return value / PERCENT_ATTRIBUTE_BASELINE; // For example: 230/100 = 2.3
    }

    public static double getHaste(LivingEntity entity) {
        return getHaste(entity, null);
    }

    public static double getHaste(LivingEntity entity, ItemStack provisionedWeapon) {
        double value = entity.getAttributeValue(EntityAttributes_SpellPower.HASTE); // For example: 110 (with +10% modifier)
        var enchantment = Enchantments_SpellPower.HASTE;
        var level = getEnchantmentLevel(enchantment, entity, provisionedWeapon); // For example: level 4
        value += enchantment.amplify(0, level) * PERCENT_ATTRIBUTE_BASELINE;  // For example: +20
        return value / PERCENT_ATTRIBUTE_BASELINE;  // For example: 130/100 = 1.3
    }

    private static int getEnchantmentLevel(Enchantment enchantment, LivingEntity entity, ItemStack provisionedWeapon) {
        int level;
        if (SpellPowerMod.enchantmentConfig.value.allow_stacking) {
            level = getEnchantmentLevelEquipmentSum(enchantment, entity);
        } else {
            level = EnchantmentHelper.getEquipmentLevel(enchantment, entity);
        }
        if (provisionedWeapon != null && !provisionedWeapon.isEmpty()) {
            var mainHandStack = entity.getMainHandStack();
            if (mainHandStack != null && !mainHandStack.isEmpty()) {
                level -= EnchantmentHelper.getLevel(enchantment, mainHandStack);
            }
            level += EnchantmentHelper.getLevel(enchantment, provisionedWeapon);
        }
        return level;
    }

    private static int getEnchantmentLevelEquipmentSum(Enchantment enchantment, LivingEntity entity) {
        int level = 0;
        for (var itemStack: entity.getArmorItems()) {
            level += EnchantmentHelper.getLevel(enchantment, itemStack);
        }
        var mainHandStack = entity.getMainHandStack();
        if (mainHandStack != null) {
            level += EnchantmentHelper.getLevel(enchantment, mainHandStack);
        }
        var offHandStack = entity.getOffHandStack();
        if (offHandStack != null) {
            level += EnchantmentHelper.getLevel(enchantment, offHandStack);
        }
        return level;
    }
}