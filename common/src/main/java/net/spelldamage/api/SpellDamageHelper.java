package net.spelldamage.api;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;

public class SpellDamageHelper {
    public static double getSpellDamage(PlayerEntity player, MagicSchool school) {
        var attribute = EntityAttributes_SpellDamage.types.get(school);
        var value = player.getAttributeValue(attribute);
        value *= EnchantmentHelper.getEquipmentLevel(Enchantments_SpellDamage.SPELL_POWER, player);
        return value;
    }
}
