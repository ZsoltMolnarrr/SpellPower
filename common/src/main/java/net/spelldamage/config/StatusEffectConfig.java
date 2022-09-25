package net.spelldamage.config;

import net.spelldamage.api.MagicSchool;
import net.spelldamage.internals.SpellStatusEffect;

import java.util.Map;

import static java.util.Map.entry;
import static net.spelldamage.internals.Attributes.*;

public class StatusEffectConfig {
    public Map<String, SpellStatusEffect.AttributeModifierProperties> damage = Map.ofEntries(
            entry(MagicSchool.FIRE.spellName(), new SpellStatusEffect.AttributeModifierProperties(
                    0xff3300,
                    "3c06c1d4-3cce-11ed-a261-0242ac120002",
                    0.2F)),
            entry(MagicSchool.FROST.spellName(), new SpellStatusEffect.AttributeModifierProperties(
                    0xccffff,
                    "41569be6-3cce-11ed-a261-0242ac120002",
                    0.2F)),
            entry(MagicSchool.SHADOW.spellName(), new SpellStatusEffect.AttributeModifierProperties(
                    0x660066,
                    "44ede7a0-3cce-11ed-a261-0242ac120002",
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
