package net.spelldamage.config;

import net.spelldamage.api.MagicSchool;
import net.spelldamage.api.StatusEffects_SpellDamage;

import java.util.Map;

import static java.util.Map.entry;

public class StatusEffectConfig {
    public Map<String, StatusEffects_SpellDamage.AttributeModifierProperties> damage_effects = Map.ofEntries(
            entry(MagicSchool.FIRE.spellName(), new StatusEffects_SpellDamage.AttributeModifierProperties(
                    0xff3300,
                    "3c06c1d4-3cce-11ed-a261-0242ac120002",
                    0.2F)),
            entry(MagicSchool.FROST.spellName(), new StatusEffects_SpellDamage.AttributeModifierProperties(
                    0xccffff,
                    "41569be6-3cce-11ed-a261-0242ac120002",
                    0.2F)),
            entry(MagicSchool.SHADOW.spellName(), new StatusEffects_SpellDamage.AttributeModifierProperties(
                    0x660066,
                    "44ede7a0-3cce-11ed-a261-0242ac120002",
                    0.2F))
    );
}
