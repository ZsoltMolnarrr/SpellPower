package net.spelldamage.api;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.spelldamage.SpellDamage;
import net.spelldamage.enchantments.AmplifierEnchantment;

import static net.minecraft.enchantment.EnchantmentTarget.WEAPON;

public class Enchantments_SpellDamage {
    public static final String spellPowerName = "spell_power";
    public static final Identifier spellPowerId = new Identifier(SpellDamage.MOD_ID + ":" + spellPowerName);
    public static final AmplifierEnchantment SPELL_POWER = new AmplifierEnchantment(
            Enchantment.Rarity.UNCOMMON,
            5, 10, 9,
            1, 0.1F,
            WEAPON,
            new EquipmentSlot[]{ EquipmentSlot.MAINHAND });
}
