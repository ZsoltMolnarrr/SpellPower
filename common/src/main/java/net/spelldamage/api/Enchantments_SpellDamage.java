package net.spelldamage.api;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.spelldamage.SpellDamage;
import net.spelldamage.config.EnchantmentsConfig;
import net.spelldamage.internals.AmplifierEnchantment;
import net.spelldamage.internals.Attributes;
import net.spelldamage.internals.SchoolFilteredEnchantment;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static net.minecraft.enchantment.EnchantmentTarget.WEAPON;
import static net.spelldamage.api.MagicSchool.*;
import static net.spelldamage.internals.AmplifierEnchantment.Operation.ADD;
import static net.spelldamage.internals.AmplifierEnchantment.Operation.MULTIPLY;

public class Enchantments_SpellDamage {

    // Damage enchants

    public static final String spellPowerName = "spell_power";
    public static final Identifier spellPowerId = new Identifier(SpellDamage.MOD_ID, spellPowerName);
    public static final SchoolFilteredEnchantment SPELL_POWER = new SchoolFilteredEnchantment(
            Enchantment.Rarity.UNCOMMON,
            MULTIPLY,
            config().spell_power,
            EnumSet.allOf(MagicSchool.class),
            WEAPON,
            new EquipmentSlot[]{ EquipmentSlot.MAINHAND });

    public static final String soulfrostName = "soulfrost";
    public static final Identifier soulfrostId = new Identifier(SpellDamage.MOD_ID, soulfrostName);
    public static final SchoolFilteredEnchantment SOULFROST = new SchoolFilteredEnchantment(
            Enchantment.Rarity.UNCOMMON,
            MULTIPLY,
            config().soulfrost,
            EnumSet.of(SOUL, FROST),
            WEAPON,
            new EquipmentSlot[]{ EquipmentSlot.MAINHAND });

    public static final String sunfireName = "sunfire";
    public static final Identifier sunfireId = new Identifier(SpellDamage.MOD_ID, sunfireName);
    public static final SchoolFilteredEnchantment SUNFIRE = new SchoolFilteredEnchantment(
            Enchantment.Rarity.UNCOMMON,
            MULTIPLY,
            config().sunfire,
            EnumSet.of(ARCANE, FIRE),
            WEAPON,
            new EquipmentSlot[]{ EquipmentSlot.MAINHAND });

    public static final String energizeName = "energize";
    public static final Identifier energizeId = new Identifier(SpellDamage.MOD_ID, energizeName);
    public static final SchoolFilteredEnchantment ENERGIZE = new SchoolFilteredEnchantment(
            Enchantment.Rarity.UNCOMMON,
            MULTIPLY,
            config().energize,
            EnumSet.of(HEALING, LIGHTNING),
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
        damageEnchants.put(sunfireId, SUNFIRE);
        damageEnchants.put(energizeId, ENERGIZE);

        Map<Identifier, AmplifierEnchantment> secondaries = new HashMap<>();
        secondaries.put(criticalChanceId, CRITICAL_CHANCE);
        secondaries.put(criticalDamageId, CRITICAL_DAMAGE);
        secondaries.put(hasteId, HASTE);

        all = new HashMap<>();
        all.putAll(damageEnchants);
        all.putAll(secondaries);
    }

    private static EnchantmentsConfig config() {
        return SpellDamage.enchantmentConfig.value;
    }
}
