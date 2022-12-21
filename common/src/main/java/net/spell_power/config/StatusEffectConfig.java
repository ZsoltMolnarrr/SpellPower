package net.spell_power.config;

import net.spell_power.api.MagicSchool;
import net.spell_power.internals.SpellStatusEffect;

import java.util.Map;

import static java.util.Map.entry;
import static net.spell_power.internals.Attributes.*;

public class StatusEffectConfig {
    public Map<String, SpellStatusEffect.AttributeModifierProperties> damage = Map.ofEntries(
            entry(MagicSchool.ARCANE.spellName(), new SpellStatusEffect.AttributeModifierProperties(
                    MagicSchool.ARCANE.color(),
                    "e8222db4-6c3c-4bbe-bacb-6e8d07e96e8b",
                    0.2F)),
            entry(MagicSchool.FIRE.spellName(), new SpellStatusEffect.AttributeModifierProperties(
                    MagicSchool.FIRE.color(),
                    "3c06c1d4-3cce-11ed-a261-0242ac120002",
                    0.2F)),
            entry(MagicSchool.FROST.spellName(), new SpellStatusEffect.AttributeModifierProperties(
                    MagicSchool.FROST.color(),
                    "41569be6-3cce-11ed-a261-0242ac120002",
                    0.2F)),
            entry(MagicSchool.HEALING.spellName(), new SpellStatusEffect.AttributeModifierProperties(
                    MagicSchool.HEALING.color(),
                    "4eccd93c-4a63-11ed-b878-0242ac120002",
                    0.2F)),
            entry(MagicSchool.LIGHTNING.spellName(), new SpellStatusEffect.AttributeModifierProperties(
                    MagicSchool.LIGHTNING.color(),
                    "48a773c8-4a63-11ed-b878-0242ac120002",
                    0.2F)),
            entry(MagicSchool.SOUL.spellName(), new SpellStatusEffect.AttributeModifierProperties(
                    MagicSchool.SOUL.color(),
                    "5515bafc-4a63-11ed-b878-0242ac120002",
                    0.2F))
            );
    public Map<String, SpellStatusEffect.AttributeModifierProperties> rating = Map.ofEntries(
            entry(CRITICAL_CHANCE, new SpellStatusEffect.AttributeModifierProperties(
                    0xffffcc,
                    "0e0ddd12-0646-42b7-8daf-36b4ccf524df",
                    0.05F)),
            entry(CRITICAL_DAMAGE, new SpellStatusEffect.AttributeModifierProperties(
                    0xffcccc,
                    "0612ed2a-3ce5-11ed-b878-0242ac120002",
                    0.1F)),
            entry(HASTE, new SpellStatusEffect.AttributeModifierProperties(
                    0xffccff,
                    "092f4f58-3ce5-11ed-b878-0242ac120002",
                    0.05F))
    );
}
