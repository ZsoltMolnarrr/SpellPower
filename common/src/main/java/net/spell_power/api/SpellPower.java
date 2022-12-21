package net.spell_power.api;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.enchantment.Enchantments_SpellPower;

import java.util.Random;

import static net.spell_power.internals.Attributes.PERCENT_ATTRIBUTE_BASELINE;

public class SpellPower {
    public record Result(MagicSchool school, double baseValue, double criticalChance, double criticalMultiplier) {
        private static Random rng = new Random();
        private enum CriticalStrikeMode {
            DISABLED, ALLOWED, FORCED
        }

        public double randomValue() {
            return value(CriticalStrikeMode.ALLOWED);
        }

        public double nonCriticalValue() {
            return value(CriticalStrikeMode.DISABLED);
        }

        public double forcedCriticalValue() {
            return value(CriticalStrikeMode.FORCED);
        }

        private double value(CriticalStrikeMode mode) {
            var value = baseValue;
            if (mode != CriticalStrikeMode.DISABLED) {
                boolean isCritical = (mode == CriticalStrikeMode.FORCED) || (rng.nextFloat() < criticalChance);
                if (isCritical) {
                    value *= criticalMultiplier;
                }
            }
            return value;
        }
    }

    public static Result getSpellDamage(MagicSchool school, LivingEntity entity) {
        return getSpellDamage(school, entity, null);
    }

    public static Result getSpellDamage(MagicSchool school, LivingEntity entity, ItemStack provisionedWeapon) {
        var attribute = EntityAttributes_SpellPower.POWER.get(school);
        var value = entity.getAttributeValue(attribute);
        for (var entry: Enchantments_SpellPower.damageEnchants.entrySet()) {
            var enchantment = entry.getValue();
            if (enchantment.canBeAppliedFor(school)) {
                var level = getEnchantmentLevel(enchantment, entity, provisionedWeapon);
                value = enchantment.amplify(value, level);
            }
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
        int level = EnchantmentHelper.getEquipmentLevel(enchantment, entity);
        if (provisionedWeapon != null && !provisionedWeapon.isEmpty()) {
            var mainHandStack = entity.getMainHandStack();
            if (mainHandStack != null && !mainHandStack.isEmpty()) {
                level -= EnchantmentHelper.getLevel(enchantment, mainHandStack);
            }
            level += EnchantmentHelper.getLevel(enchantment, provisionedWeapon);
        }
        return level;
    }
}