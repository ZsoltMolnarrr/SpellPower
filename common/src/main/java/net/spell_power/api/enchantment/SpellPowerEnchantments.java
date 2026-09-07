package net.spell_power.api.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellPowerTags;
import net.spell_power.api.SpellSchools;
import net.spell_power.config.EnchantmentsConfig;
import net.spell_power.internals.AmplifierEnchantment;
import net.spell_power.internals.MagicProtectionEnchantment;
import net.spell_power.internals.SchoolFilteredEnchantment;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.minecraft.enchantment.EnchantmentTarget.BREAKABLE;

/// The eight Spell Power enchantments as Java classes (1.20.1 has no data-driven enchantments).
/// Ids, rarities (modern weights: 10 → COMMON, 5 → UNCOMMON, 2 → RARE), slots, item tags and exclusivity
/// mirror the modern (1.21.1) data-driven definitions; numbers come from `EnchantmentsConfig`.
public class SpellPowerEnchantments {
    public static final EquipmentSlot[] MAINHAND = new EquipmentSlot[]{ EquipmentSlot.MAINHAND };
    public static final EquipmentSlot[] ARMOR = new EquipmentSlot[]{ EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };

    public static final String MULTI_SCHOOL_GROUP = "multi_school";
    public static final String SPELL_CRITICAL_GROUP = "spell_critical";

    private static EnchantmentsConfig config() {
        return SpellPowerMod.enchantmentConfig.safeValue();
    }

    // School power enchantments

    public static final Identifier SPELL_POWER_ID = new Identifier(SpellPowerMod.ID, "spell_power");
    /// Boosts the GENERIC attribute, i.e. all schools at once (through the generic multiplier in `SpellPower.getSpellPower`).
    public static final SchoolFilteredEnchantment SPELL_POWER = (SchoolFilteredEnchantment) new SchoolFilteredEnchantment(
            Enchantment.Rarity.COMMON,
            () -> config().spell_power,
            Set.of(SpellSchools.GENERIC),
            BREAKABLE,
            MAINHAND)
            .requireTag(SpellPowerTags.Items.Enchantable.SPELL_POWER_GENERIC);

    public static final Identifier SUNFIRE_ID = new Identifier(SpellPowerMod.ID, "sunfire");
    public static final SchoolFilteredEnchantment SUNFIRE = (SchoolFilteredEnchantment) new SchoolFilteredEnchantment(
            Enchantment.Rarity.RARE,
            () -> config().sunfire,
            Set.of(SpellSchools.ARCANE, SpellSchools.FIRE),
            EnchantmentTarget.ARMOR,
            ARMOR)
            .requireTag(SpellPowerTags.Items.Enchantable.SPELL_POWER_SUNFIRE)
            .supportWholeTarget()
            .exclusiveGroup(MULTI_SCHOOL_GROUP);

    public static final Identifier SOULFROST_ID = new Identifier(SpellPowerMod.ID, "soulfrost");
    public static final SchoolFilteredEnchantment SOULFROST = (SchoolFilteredEnchantment) new SchoolFilteredEnchantment(
            Enchantment.Rarity.RARE,
            () -> config().soulfrost,
            Set.of(SpellSchools.SOUL, SpellSchools.FROST),
            EnchantmentTarget.ARMOR,
            ARMOR)
            .requireTag(SpellPowerTags.Items.Enchantable.SPELL_POWER_SOULFROST)
            .supportWholeTarget()
            .exclusiveGroup(MULTI_SCHOOL_GROUP);

    public static final Identifier ENERGIZE_ID = new Identifier(SpellPowerMod.ID, "energize");
    public static final SchoolFilteredEnchantment ENERGIZE = (SchoolFilteredEnchantment) new SchoolFilteredEnchantment(
            Enchantment.Rarity.RARE,
            () -> config().energize,
            Set.of(SpellSchools.HEALING, SpellSchools.LIGHTNING),
            EnchantmentTarget.ARMOR,
            ARMOR)
            .requireTag(SpellPowerTags.Items.Enchantable.SPELL_POWER_ENERGIZE)
            .supportWholeTarget()
            .exclusiveGroup(MULTI_SCHOOL_GROUP);

    // Mechanics enchantments

    public static final Identifier HASTE_ID = new Identifier(SpellPowerMod.ID, SpellPowerMechanics.HASTE.name);
    public static final AmplifierEnchantment HASTE = new AmplifierEnchantment(
            Enchantment.Rarity.UNCOMMON,
            () -> config().haste,
            BREAKABLE,
            MAINHAND)
            .requireTag(SpellPowerTags.Items.Enchantable.HASTE);

    public static final Identifier CRITICAL_CHANCE_ID = new Identifier(SpellPowerMod.ID, SpellPowerMechanics.CRITICAL_CHANCE.name);
    public static final AmplifierEnchantment CRITICAL_CHANCE = new AmplifierEnchantment(
            Enchantment.Rarity.UNCOMMON,
            () -> config().critical_chance,
            BREAKABLE,
            MAINHAND)
            .requireTag(SpellPowerTags.Items.Enchantable.CRITICAL_CHANCE)
            .exclusiveGroup(SPELL_CRITICAL_GROUP);

    public static final Identifier CRITICAL_DAMAGE_ID = new Identifier(SpellPowerMod.ID, SpellPowerMechanics.CRITICAL_DAMAGE.name);
    public static final AmplifierEnchantment CRITICAL_DAMAGE = new AmplifierEnchantment(
            Enchantment.Rarity.UNCOMMON,
            () -> config().critical_damage,
            BREAKABLE,
            MAINHAND)
            .requireTag(SpellPowerTags.Items.Enchantable.CRITICAL_DAMAGE)
            .exclusiveGroup(SPELL_CRITICAL_GROUP);

    // Resistance

    public static final Identifier MAGIC_PROTECTION_ID = new Identifier(SpellPowerMod.ID, "magic_protection");
    public static final MagicProtectionEnchantment MAGIC_PROTECTION = new MagicProtectionEnchantment(
            Enchantment.Rarity.UNCOMMON,
            () -> config().magic_protection,
            ARMOR);

    // Collections

    /// Enchantments that boost school attributes, consulted by the query-time power sources.
    public static final List<SchoolFilteredEnchantment> schoolPower = List.of(SPELL_POWER, SUNFIRE, SOULFROST, ENERGIZE);

    public static final Map<Identifier, Enchantment> all;
    static {
        all = new LinkedHashMap<>();
        all.put(SPELL_POWER_ID, SPELL_POWER);
        all.put(SUNFIRE_ID, SUNFIRE);
        all.put(SOULFROST_ID, SOULFROST);
        all.put(ENERGIZE_ID, ENERGIZE);
        all.put(HASTE_ID, HASTE);
        all.put(CRITICAL_CHANCE_ID, CRITICAL_CHANCE);
        all.put(CRITICAL_DAMAGE_ID, CRITICAL_DAMAGE);
        all.put(MAGIC_PROTECTION_ID, MAGIC_PROTECTION);
    }
}
