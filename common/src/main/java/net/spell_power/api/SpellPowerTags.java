package net.spell_power.api;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;

public class SpellPowerTags {
    public static class DamageTypes {
        public static final TagKey<DamageType> ALL = TagKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(SpellPowerMod.ID, "all"));
    }
    public static class Enchantments {
        public static final TagKey<Enchantment> REQUIRES_MATCHING_ATTRIBUTE = TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SpellPowerMod.ID, "requires_matching_attribute"));
        // Exclusive set tag
        public static final TagKey<Enchantment> MULTI_SCHOOL = TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SpellPowerMod.ID, "multi_school"));
        public static final TagKey<Enchantment> SPELL_CRITICAL_EXCLUSIVE = TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SpellPowerMod.ID, "exclusive_set/spell_critical"));
    }
    public static class Items {
        public static class Enchantable {
            private static TagKey<Item> tag(String name) {
                return TagKey.of(RegistryKeys.ITEM, Identifier.of(SpellPowerMod.ID, "enchantable/" + name));
            }
            public static final TagKey<Item> SPELL_POWER_GENERIC = tag("spell_power_generic");
            public static final TagKey<Item> SPELL_POWER_SUNFIRE = tag("spell_power_sunfire");
            public static final TagKey<Item> SPELL_POWER_SOULFROST = tag("spell_power_soulfrost");
            public static final TagKey<Item> SPELL_POWER_ENERGIZE = tag("spell_power_energize");

            public static final TagKey<Item> HASTE = tag("haste");
            public static final TagKey<Item> CRITICAL_CHANCE = tag("critical_chance");
            public static final TagKey<Item> CRITICAL_DAMAGE = tag("critical_damage");
        }
    }
}
