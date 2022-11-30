package net.spelldamage.api.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.spelldamage.SpellDamageMod;
import net.spelldamage.api.MagicSchool;
import net.spelldamage.config.EnchantmentsConfig;
import net.spelldamage.internals.AmplifierEnchantment;
import net.spelldamage.internals.Attributes;
import net.spelldamage.internals.SchoolFilteredEnchantment;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static net.minecraft.enchantment.EnchantmentTarget.BREAKABLE;
import static net.spelldamage.api.MagicSchool.*;
import static net.spelldamage.internals.AmplifierEnchantment.Operation.ADD;
import static net.spelldamage.internals.AmplifierEnchantment.Operation.MULTIPLY;

public class Enchantments_SpellDamage {

    // Damage enchants

    public static final String spellPowerName = "spell_power";
    public static final Identifier spellPowerId = new Identifier(SpellDamageMod.MOD_ID, spellPowerName);
    public static final SchoolFilteredEnchantment SPELL_POWER = new SchoolFilteredEnchantment(
            Enchantment.Rarity.UNCOMMON,
            MULTIPLY,
            config().spell_power,
            EnumSet.allOf(MagicSchool.class),
            BREAKABLE,
            EquipmentSlot.values());

    public static final String soulfrostName = "soulfrost";
    public static final Identifier soulfrostId = new Identifier(SpellDamageMod.MOD_ID, soulfrostName);
    public static final SchoolFilteredEnchantment SOULFROST = new SchoolFilteredEnchantment(
            Enchantment.Rarity.RARE,
            MULTIPLY,
            config().soulfrost,
            EnumSet.of(SOUL, FROST),
            BREAKABLE,
            EquipmentSlot.values());

    public static final String sunfireName = "sunfire";
    public static final Identifier sunfireId = new Identifier(SpellDamageMod.MOD_ID, sunfireName);
    public static final SchoolFilteredEnchantment SUNFIRE = new SchoolFilteredEnchantment(
            Enchantment.Rarity.RARE,
            MULTIPLY,
            config().sunfire,
            EnumSet.of(ARCANE, FIRE),
            BREAKABLE,
            EquipmentSlot.values());

    public static final String energizeName = "energize";
    public static final Identifier energizeId = new Identifier(SpellDamageMod.MOD_ID, energizeName);
    public static final SchoolFilteredEnchantment ENERGIZE = new SchoolFilteredEnchantment(
            Enchantment.Rarity.RARE,
            MULTIPLY,
            config().energize,
            EnumSet.of(HEALING, LIGHTNING),
            BREAKABLE,
            EquipmentSlot.values());

    // Rating enchants

    public static final Identifier criticalChanceId = new Identifier(SpellDamageMod.MOD_ID, Attributes.CRITICAL_CHANCE);
    public static final AmplifierEnchantment CRITICAL_CHANCE = new AmplifierEnchantment(
            Enchantment.Rarity.UNCOMMON,
            ADD,
            config().critical_chance,
            BREAKABLE,
            EquipmentSlot.values());

    public static final Identifier criticalDamageId = new Identifier(SpellDamageMod.MOD_ID, Attributes.CRITICAL_DAMAGE);
    public static final AmplifierEnchantment CRITICAL_DAMAGE = new AmplifierEnchantment(
            Enchantment.Rarity.UNCOMMON,
            ADD,
            config().critical_damage,
            BREAKABLE,
            EquipmentSlot.values());

    public static final Identifier hasteId = new Identifier(SpellDamageMod.MOD_ID, Attributes.HASTE);
    public static final AmplifierEnchantment HASTE = new AmplifierEnchantment(
            Enchantment.Rarity.RARE,
            ADD,
            config().haste,
            BREAKABLE,
            EquipmentSlot.values());

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
        return SpellDamageMod.enchantmentConfig.value;
    }
}
