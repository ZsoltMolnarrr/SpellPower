package net.spell_power.api;

import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;

public class SpellPowerTags {
    public static class DamageType {
        public static final TagKey<net.minecraft.entity.damage.DamageType> IS_SPELL = TagKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(SpellPowerMod.ID, "is_spell"));
    }
}
