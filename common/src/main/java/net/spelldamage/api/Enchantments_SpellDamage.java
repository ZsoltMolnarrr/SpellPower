package net.spelldamage.api;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.spelldamage.SpellDamage;
import net.spelldamage.config.EnchantmentConfig;
import net.spelldamage.enchantments.AmplifierEnchantment;
import net.spelldamage.enchantments.SchoolFilteredEnchantment;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static net.minecraft.enchantment.EnchantmentTarget.WEAPON;
import static net.spelldamage.api.MagicSchool.FROST;
import static net.spelldamage.api.MagicSchool.SHADOW;
import static net.spelldamage.enchantments.AmplifierEnchantment.Operation.MULTIPLY;

public class Enchantments_SpellDamage {
    public static final String spellPowerName = "spell_power";
    public static final Identifier spellPowerId = new Identifier(SpellDamage.MOD_ID, spellPowerName);
    public static final SchoolFilteredEnchantment SPELL_POWER = new SchoolFilteredEnchantment(
            Enchantment.Rarity.UNCOMMON,
            MULTIPLY,
            config().damage_enchantments.get(spellPowerName),
            EnumSet.allOf(MagicSchool.class),
            WEAPON,
            new EquipmentSlot[]{ EquipmentSlot.MAINHAND });

    public static final String soulfrostName = "soulfrost";
    public static final Identifier soulfrostId = new Identifier(SpellDamage.MOD_ID, soulfrostName);
    public static final SchoolFilteredEnchantment SOULFROST = new SchoolFilteredEnchantment(
            Enchantment.Rarity.UNCOMMON,
            MULTIPLY,
            config().damage_enchantments.get(soulfrostName),
            EnumSet.of(FROST, SHADOW),
            WEAPON,
            new EquipmentSlot[]{ EquipmentSlot.MAINHAND });

    // Helpers

    public static final Map<Identifier, SchoolFilteredEnchantment> damageEnchants;
    public static final Map<Identifier, AmplifierEnchantment> all;
    static {
        damageEnchants = new HashMap<>();
        damageEnchants.put(spellPowerId, SPELL_POWER);
        damageEnchants.put(soulfrostId, SOULFROST);
        all = new HashMap<>();
        all.putAll(damageEnchants);
    }

    private static EnchantmentConfig config() {
        return SpellDamage.enchantmentConfig.currentConfig;
    }
}
