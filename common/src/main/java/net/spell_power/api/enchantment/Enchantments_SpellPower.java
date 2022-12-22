package net.spell_power.api.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.MagicSchool;
import net.spell_power.config.EnchantmentsConfig;
import net.spell_power.internals.AmplifierEnchantment;
import net.spell_power.internals.Attributes;
import net.spell_power.internals.MagicProtectionEnchantment;
import net.spell_power.internals.SchoolFilteredEnchantment;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static net.minecraft.enchantment.EnchantmentTarget.BREAKABLE;
import static net.spell_power.api.MagicSchool.*;
import static net.spell_power.internals.AmplifierEnchantment.Operation.ADD;
import static net.spell_power.internals.AmplifierEnchantment.Operation.MULTIPLY;

public class Enchantments_SpellPower {

    // Damage enchants

    public static final String spellPowerName = "spell_power";
    public static final Identifier spellPowerId = new Identifier(SpellPowerMod.ID, spellPowerName);
    public static final SchoolFilteredEnchantment SPELL_POWER = new SchoolFilteredEnchantment(
            Enchantment.Rarity.UNCOMMON,
            MULTIPLY,
            config().spell_power,
            EnumSet.allOf(MagicSchool.class),
            BREAKABLE,
            EquipmentSlot.values());

    public static final String soulfrostName = "soulfrost";
    public static final Identifier soulfrostId = new Identifier(SpellPowerMod.ID, soulfrostName);
    public static final SchoolFilteredEnchantment SOULFROST = new SchoolFilteredEnchantment(
            Enchantment.Rarity.RARE,
            MULTIPLY,
            config().soulfrost,
            EnumSet.of(SOUL, FROST),
            BREAKABLE,
            EquipmentSlot.values());

    public static final String sunfireName = "sunfire";
    public static final Identifier sunfireId = new Identifier(SpellPowerMod.ID, sunfireName);
    public static final SchoolFilteredEnchantment SUNFIRE = new SchoolFilteredEnchantment(
            Enchantment.Rarity.RARE,
            MULTIPLY,
            config().sunfire,
            EnumSet.of(ARCANE, FIRE),
            BREAKABLE,
            EquipmentSlot.values());

    public static final String energizeName = "energize";
    public static final Identifier energizeId = new Identifier(SpellPowerMod.ID, energizeName);
    public static final SchoolFilteredEnchantment ENERGIZE = new SchoolFilteredEnchantment(
            Enchantment.Rarity.RARE,
            MULTIPLY,
            config().energize,
            EnumSet.of(HEALING, LIGHTNING),
            BREAKABLE,
            EquipmentSlot.values());

    // Rating enchants

    public static final Identifier criticalChanceId = new Identifier(SpellPowerMod.ID, Attributes.CRITICAL_CHANCE);
    public static final AmplifierEnchantment CRITICAL_CHANCE = new AmplifierEnchantment(
            Enchantment.Rarity.UNCOMMON,
            ADD,
            config().critical_chance,
            BREAKABLE,
            EquipmentSlot.values());

    public static final Identifier criticalDamageId = new Identifier(SpellPowerMod.ID, Attributes.CRITICAL_DAMAGE);
    public static final AmplifierEnchantment CRITICAL_DAMAGE = new AmplifierEnchantment(
            Enchantment.Rarity.UNCOMMON,
            ADD,
            config().critical_damage,
            BREAKABLE,
            EquipmentSlot.values());

    public static final Identifier hasteId = new Identifier(SpellPowerMod.ID, Attributes.HASTE);
    public static final AmplifierEnchantment HASTE = new AmplifierEnchantment(
            Enchantment.Rarity.RARE,
            ADD,
            config().haste,
            BREAKABLE,
            EquipmentSlot.values());

    // Resistance

    public static EquipmentSlot[] ALL_ARMOR = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    public static final String magicProtectionName = "magic_protection";
    public static final Identifier magicProtectionId = new Identifier(SpellPowerMod.ID, magicProtectionName);
    public static final MagicProtectionEnchantment MAGIC_PROTECTION = new MagicProtectionEnchantment(Enchantment.Rarity.RARE, config().magic_protection, ALL_ARMOR);

    // Helpers

    public static final Map<Identifier, SchoolFilteredEnchantment> damageEnchants;
    public static final Map<Identifier, Enchantment> all;
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
        all.put(magicProtectionId, MAGIC_PROTECTION);
    }

    private static EnchantmentsConfig config() {
        return SpellPowerMod.enchantmentConfig.value;
    }
}
