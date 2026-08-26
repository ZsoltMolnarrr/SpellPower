package net.spell_power.api;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.spell_power.SpellPowerMod;

public class SpellPowerTags {
    public static class DamageTypes {
        public static final TagKey<DamageType> ALL = TagKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(SpellPowerMod.ID, "all"));
    }
    public static class Enchantments {
        public static final TagKey<Enchantment> REQUIRES_MATCHING_ATTRIBUTE = TagKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(SpellPowerMod.ID, "requires_matching_attribute"));
        // Exclusive set tag
        public static final TagKey<Enchantment> MULTI_SCHOOL = TagKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(SpellPowerMod.ID, "multi_school"));
        public static final TagKey<Enchantment> SPELL_CRITICAL_EXCLUSIVE = TagKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(SpellPowerMod.ID, "exclusive_set/spell_critical"));
    }
    public static class Items {
        public static class Enchantable {
            private static TagKey<Item> tag(String name) {
                return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(SpellPowerMod.ID, "enchantable/" + name));
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
