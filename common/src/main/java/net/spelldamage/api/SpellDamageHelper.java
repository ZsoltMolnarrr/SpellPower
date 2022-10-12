package net.spelldamage.api;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.spelldamage.SpellDamage;

import java.util.Random;

import static net.spelldamage.internals.Attributes.PERCENT_ATTRIBUTE_BASELINE;

public class SpellDamageHelper {
    private static Random rng = new Random();

    public static EffectValue getSpellDamage(MagicSchool school, LivingEntity entity) {
        return getSpellDamage(school, entity, true);
    }

    public record EffectValue(double value, double criticalMultiplier) { }

    public static EffectValue getSpellDamage(MagicSchool school, LivingEntity entity, boolean allowCriticalStrike) {
        var attribute = EntityAttributes_SpellDamage.DAMAGE.get(school);
        var value = entity.getAttributeValue(attribute);
        for (var entry: Enchantments_SpellDamage.damageEnchants.entrySet()) {
            var enchantment = entry.getValue();
            if (enchantment.canBeAppliedFor(school)) {
                var level = EnchantmentHelper.getEquipmentLevel(enchantment, entity);
                value = enchantment.amplify(value, level);
            }
        }

        double criticalMultiplier = 1;
        if (allowCriticalStrike) {
            boolean isCritical = false;
            var critChance = getCriticalChance(entity);
            var rand = rng.nextFloat();
            if (rand < critChance) {
                isCritical = true;
            }
            if (isCritical) {
                criticalMultiplier = getCriticalMultiplier(entity);
                value *= criticalMultiplier;
            }
            System.out.println("Critical chance: " + critChance + " rand:" + rand + " criticalMultiplier: " + criticalMultiplier);
        }

        return new EffectValue(value, criticalMultiplier);
    }

    public static double getCriticalChance(LivingEntity entity) {
        var base = SpellDamage.attributesConfig.value.base_spell_critical_chance_percentage; // 5
        double value = entity.getAttributeValue(EntityAttributes_SpellDamage.CRITICAL_CHANCE); // For example: 115
        var enchantment = Enchantments_SpellDamage.CRITICAL_CHANCE;
        var level = EnchantmentHelper.getEquipmentLevel(enchantment, entity); // For example: 3
        value += enchantment.amplify(0, level) * PERCENT_ATTRIBUTE_BASELINE; // For example: +15
        value += base;
        return (value - PERCENT_ATTRIBUTE_BASELINE) / PERCENT_ATTRIBUTE_BASELINE; // For example: (135-100)/100 = 0.35
    }

    public static double getCriticalMultiplier(LivingEntity entity) {
        double value = entity.getAttributeValue(EntityAttributes_SpellDamage.CRITICAL_DAMAGE); // For example: 150 (with +50% modifier)
        var enchantment = Enchantments_SpellDamage.CRITICAL_DAMAGE;
        var level = EnchantmentHelper.getEquipmentLevel(enchantment, entity); // For example: level 3
        value += enchantment.amplify(0, level) * PERCENT_ATTRIBUTE_BASELINE; // For example: +30
        value += SpellDamage.attributesConfig.value.base_spell_critical_damage_percentage; // +50
        return value / PERCENT_ATTRIBUTE_BASELINE; // For example: 230/100 = 2.3
    }

    public static double getHaste(LivingEntity entity) {
        double value = entity.getAttributeValue(EntityAttributes_SpellDamage.HASTE); // For example: 110 (with +10% modifier)
        var enchantment = Enchantments_SpellDamage.HASTE;
        var level = EnchantmentHelper.getEquipmentLevel(enchantment, entity); // For example: level 4
        value += enchantment.amplify(0, level) * PERCENT_ATTRIBUTE_BASELINE;  // For example: +20
        return value / PERCENT_ATTRIBUTE_BASELINE;  // For example: 130/100 = 1.3
    }
}
