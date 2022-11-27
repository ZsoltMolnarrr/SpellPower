package net.spelldamage.api;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.spelldamage.SpellDamage;
import net.spelldamage.api.enchantment.Enchantments_SpellDamage;

import java.util.Random;

import static net.spelldamage.internals.Attributes.PERCENT_ATTRIBUTE_BASELINE;

public class SpellDamageHelper {
    private static Random rng = new Random();

    public static EffectValue getSpellDamage(MagicSchool school, LivingEntity entity) {
        return getSpellDamage(school, entity, CriticalStrikeMode.ALLOWED);
    }

    public enum CriticalStrikeMode {
        DISABLED, ALLOWED, FORCED
    }

    public record EffectValue(double value, double criticalMultiplier) { }

    public static EffectValue getSpellDamage(MagicSchool school, LivingEntity entity, CriticalStrikeMode critMode) {
        return getSpellDamage(school, entity, critMode, null);
    }

    public static EffectValue getSpellDamage(MagicSchool school, LivingEntity entity, CriticalStrikeMode critMode, ItemStack provisionedWeapon) {
        var attribute = EntityAttributes_SpellDamage.DAMAGE.get(school);
        var value = entity.getAttributeValue(attribute);
        for (var entry: Enchantments_SpellDamage.damageEnchants.entrySet()) {
            var enchantment = entry.getValue();
            if (enchantment.canBeAppliedFor(school)) {
                var level = getEnchantmentLevel(enchantment, entity, provisionedWeapon);
                value = enchantment.amplify(value, level);
            }
        }

        double criticalMultiplier = 1;
        if (critMode != CriticalStrikeMode.DISABLED) {
            boolean isCritical = critMode == CriticalStrikeMode.FORCED;
            var critChance = getCriticalChance(entity, provisionedWeapon);
            var rand = rng.nextFloat();
            if (rand < critChance) {
                isCritical = true;
            }
            if (isCritical) {
                criticalMultiplier = getCriticalDamage(entity, provisionedWeapon);
                value *= criticalMultiplier;
            }
            // System.out.println("Critical chance: " + critChance + " rand:" + rand + " criticalMultiplier: " + criticalMultiplier);
        }

        return new EffectValue(value, criticalMultiplier);
    }

    public static double getCriticalChance(LivingEntity entity) {
        return getCriticalChance(entity, null);
    }

    public static double getCriticalChance(LivingEntity entity, ItemStack provisionedWeapon) {
        var base = SpellDamage.attributesConfig.value.base_spell_critical_chance_percentage; // 5
        double value = entity.getAttributeValue(EntityAttributes_SpellDamage.CRITICAL_CHANCE); // For example: 115
        var enchantment = Enchantments_SpellDamage.CRITICAL_CHANCE;
        var level = getEnchantmentLevel(enchantment, entity, provisionedWeapon); // For example: 3
        value += enchantment.amplify(0, level) * PERCENT_ATTRIBUTE_BASELINE; // For example: +15
        value += base;
        return (value - PERCENT_ATTRIBUTE_BASELINE) / PERCENT_ATTRIBUTE_BASELINE; // For example: (135-100)/100 = 0.35
    }

    public static double getCriticalDamage(LivingEntity entity) {
        return getCriticalDamage(entity, null);
    }

    public static double getCriticalDamage(LivingEntity entity, ItemStack provisionedWeapon) {
        double value = entity.getAttributeValue(EntityAttributes_SpellDamage.CRITICAL_DAMAGE); // For example: 150 (with +50% modifier)
        var enchantment = Enchantments_SpellDamage.CRITICAL_DAMAGE;
        var level = getEnchantmentLevel(enchantment, entity, provisionedWeapon); // For example: level 3
        value += enchantment.amplify(0, level) * PERCENT_ATTRIBUTE_BASELINE; // For example: +30
        value += SpellDamage.attributesConfig.value.base_spell_critical_damage_percentage; // +50
        return value / PERCENT_ATTRIBUTE_BASELINE; // For example: 230/100 = 2.3
    }

    public static double getHaste(LivingEntity entity) {
        return getHaste(entity, null);
    }

    public static double getHaste(LivingEntity entity, ItemStack provisionedWeapon) {
        double value = entity.getAttributeValue(EntityAttributes_SpellDamage.HASTE); // For example: 110 (with +10% modifier)
        var enchantment = Enchantments_SpellDamage.HASTE;
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