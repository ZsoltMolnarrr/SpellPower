package net.spelldamage.api;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.spelldamage.SpellDamage;

import java.util.Random;

import static net.spelldamage.internals.Attributes.PERCENT_ATTRIBUTE_BASELINE;

public class SpellDamageHelper {
    private static Random rng = new Random();

    public static double getSpellDamage(MagicSchool school, LivingEntity entity) {
        return getSpellDamage(school, entity, true);
    }

    public static double getSpellDamage(MagicSchool school, LivingEntity entity, boolean allowCriticalStrike) {
        var attribute = EntityAttributes_SpellDamage.DAMAGE.get(school);
        var value = entity.getAttributeValue(attribute);
        for (var entry: Enchantments_SpellDamage.damageEnchants.entrySet()) {
            var enchantment = entry.getValue();
            if (enchantment.canBeAppliedFor(school)) {
                var level = EnchantmentHelper.getEquipmentLevel(enchantment, entity);
                value = enchantment.amplify(value, level);
            }
        }

        if (allowCriticalStrike) {
            boolean isCritical = false;
            if (rng.nextFloat() < getCriticalChance(entity)) {
                isCritical = true;
            }
            if (isCritical) {
                value *= getCriticalDamage(entity);
            }
        }

        return value;
    }

    public static double getCriticalChance(LivingEntity entity) {
        var base = SpellDamage.attributesConfig.currentConfig.base_spell_critical_chance_percentage;
        return (base + entity.getAttributeValue(EntityAttributes_SpellDamage.CRITICAL_CHANCE)  // 100 + 5% -> 105
                - PERCENT_ATTRIBUTE_BASELINE)                         // -100
                / PERCENT_ATTRIBUTE_BASELINE;                         // /100 -> 0.05
    }

    public static double getCriticalDamage(LivingEntity entity) {
        var base = SpellDamage.attributesConfig.currentConfig.base_spell_critical_damage_percentage;
        var percent = base + entity.getAttributeValue(EntityAttributes_SpellDamage.CRITICAL_DAMAGE);
        return percent / PERCENT_ATTRIBUTE_BASELINE;
    }
}
