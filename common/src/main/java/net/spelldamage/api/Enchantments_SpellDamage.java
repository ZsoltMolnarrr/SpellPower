package net.spelldamage.api;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.spelldamage.SpellDamage;
import net.spelldamage.config.EnchantmentConfig;
import net.spelldamage.internals.AmplifierEnchantment;
import net.spelldamage.internals.Attributes;
import net.spelldamage.internals.SchoolFilteredEnchantment;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static net.minecraft.enchantment.EnchantmentTarget.WEAPON;
import static net.spelldamage.api.MagicSchool.FROST;
import static net.spelldamage.api.MagicSchool.SHADOW;
import static net.spelldamage.internals.AmplifierEnchantment.Operation.ADD;
import static net.spelldamage.internals.AmplifierEnchantment.Operation.MULTIPLY;

public class Enchantments_SpellDamage {

    // Damage enchants

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

    // Rating enchants

    public static final Identifier criticalChanceId = new Identifier(SpellDamage.MOD_ID, Attributes.CRITICAL_CHANCE);
    public static final AmplifierEnchantment CRITICAL_CHANCE = new AmplifierEnchantment(
            Enchantment.Rarity.UNCOMMON,
            ADD,
            config().critical_chance,
            WEAPON,
            new EquipmentSlot[]{ EquipmentSlot.MAINHAND });

    public static final Identifier criticalDamageId = new Identifier(SpellDamage.MOD_ID, Attributes.CRITICAL_DAMAGE);
    public static final AmplifierEnchantment CRITICAL_DAMAGE = new AmplifierEnchantment(
            Enchantment.Rarity.UNCOMMON,
            ADD,
            config().critical_damage,
            WEAPON,
            new EquipmentSlot[]{ EquipmentSlot.MAINHAND });

    public static final Identifier hasteId = new Identifier(SpellDamage.MOD_ID, Attributes.HASTE);
    public static final AmplifierEnchantment HASTE = new AmplifierEnchantment(
            Enchantment.Rarity.RARE,
            ADD,
            config().haste,
            WEAPON,
            new EquipmentSlot[]{ EquipmentSlot.MAINHAND });


    // Helpers

    public static final Map<Identifier, SchoolFilteredEnchantment> damageEnchants;
    public static final Map<Identifier, AmplifierEnchantment> all;
    static {
        damageEnchants = new HashMap<>();
        damageEnchants.put(spellPowerId, SPELL_POWER);
        damageEnchants.put(soulfrostId, SOULFROST);

        Map<Identifier, AmplifierEnchantment> secondaries = new HashMap<>();
        secondaries.put(criticalChanceId, CRITICAL_CHANCE);
        secondaries.put(criticalDamageId, CRITICAL_DAMAGE);
        secondaries.put(hasteId, HASTE);

        all = new HashMap<>();
        all.putAll(damageEnchants);
        all.putAll(secondaries);
    }

    private static EnchantmentConfig config() {
        return SpellDamage.enchantmentConfig.currentConfig;
    }
}
