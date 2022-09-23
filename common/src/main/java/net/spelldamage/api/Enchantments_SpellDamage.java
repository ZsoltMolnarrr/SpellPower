package net.spelldamage.api;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.spelldamage.SpellDamage;
import net.spelldamage.enchantments.AmplifierEnchantment;

import static net.minecraft.enchantment.EnchantmentTarget.WEAPON;
import static net.spelldamage.enchantments.AmplifierEnchantment.Operation.MULTIPLY;

public class Enchantments_SpellDamage {
    public static final String spellPowerName = "spell_power";
    public static final Identifier spellPowerId = new Identifier(SpellDamage.MOD_ID, spellPowerName);
    public static final AmplifierEnchantment SPELL_POWER = new AmplifierEnchantment(
            Enchantment.Rarity.UNCOMMON,
            MULTIPLY, SpellDamage.enchantmentConfig.currentConfig.spell_power,
            WEAPON,
            new EquipmentSlot[]{ EquipmentSlot.MAINHAND });
}
