package net.spell_power.api;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;

public class SpellPowerTags {
    public static class DamageTypes {
        public static final TagKey<DamageType> ALL = TagKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(SpellPowerMod.ID, "all"));
        /// `c:is_magic` — what Magic Protection counts as magic damage (ships as `#spell_power:all`).
        public static final TagKey<DamageType> IS_MAGIC = TagKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("c", "is_magic"));
    }

    // No `Enchantments` tag class on 1.20.1: enchantments are Java classes here, so the modern
    // `requires_matching_attribute` / `multi_school` / `exclusive_set/spell_critical` tags are expressed as
    // `canAccept` / `isAcceptableItem` overrides (see `SpellPowerEnchantments`).

    public static class Items {
        /// Item tags gating each enchantment. On 1.20.1 these live at the plural path
        /// `data/spell_power/tags/items/enchantable/<name>.json`.
        ///
        /// The armor / multi-school enchantments (Sunfire, Soulfrost, Energize) additionally support every
        /// `ArmorItem` through `AmplifierEnchantment#supportWholeTarget`, because 1.20.1 has no
        /// `#minecraft:enchantable/armor` counterpart to base them on — see that method's docs. These tags
        /// are therefore an *extension* seam for datapacks, not the sole eligibility source for armor.
        public static class Enchantable {
            private static TagKey<Item> tag(String name) {
                return TagKey.of(RegistryKeys.ITEM, new Identifier(SpellPowerMod.ID, "enchantable/" + name));
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
