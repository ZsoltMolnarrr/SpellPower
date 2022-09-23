package net.spelldamage.api;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.spelldamage.SpellDamage;

import java.util.Random;

public class SpellDamageHelper {
    private static Random rng = new Random();

    public static double getSpellDamage(PlayerEntity player, MagicSchool school) {
        return getSpellDamage(player, school, true);
    }

    public static double getSpellDamage(PlayerEntity player, MagicSchool school, boolean allowCriticalStrike) {
        var attribute = EntityAttributes_SpellDamage.types.get(school);
        var value = player.getAttributeValue(attribute);
        value = Enchantments_SpellDamage.SPELL_POWER.apply(value, EnchantmentHelper.getEquipmentLevel(Enchantments_SpellDamage.SPELL_POWER, player));

        if (allowCriticalStrike) {
            boolean isCritical = false;
            if (rng.nextFloat() < getCriticalChance(player)) {
                isCritical = true;
            }
            if (isCritical) {
                value *= getCriticalDamage(player);
            }
        }

        return value;
    }

    public static double getCriticalChance(PlayerEntity player) {
        var base = SpellDamage.attributesConfig.currentConfig.base_spell_critical_chance_percentage;
        return (base + player.getAttributeValue(EntityAttributes_SpellDamage.CRITICAL_CHANCE)  // 100 + 5% -> 105
                - EntityAttributes_SpellDamage.criticalChanceBaseline)                         // -100
                / EntityAttributes_SpellDamage.criticalChanceBaseline;                         // /100 -> 0.05
    }

    public static double getCriticalDamage(PlayerEntity player) {
        var base = SpellDamage.attributesConfig.currentConfig.base_spell_critical_damage_percentage;
        var percent = base + player.getAttributeValue(EntityAttributes_SpellDamage.CRITICAL_DAMAGE);
        return percent /  EntityAttributes_SpellDamage.criticalChanceBaseline;
    }
}
