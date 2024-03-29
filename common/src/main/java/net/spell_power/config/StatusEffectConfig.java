package net.spell_power.config;

import net.spell_power.api.MagicSchool;
import net.spell_power.internals.SpellStatusEffect;

import java.util.Map;

import static java.util.Map.entry;
import static net.spell_power.api.attributes.SpellAttributes.*;

public class StatusEffectConfig {
    private static final float powerPerStack = 0.15F;
    public Map<String, SpellStatusEffect.Config> effects = Map.ofEntries(
            entry(POWER.get(MagicSchool.ARCANE).name, new SpellStatusEffect.Config(
                    730,
                    MagicSchool.ARCANE.color(),
                    "e8222db4-6c3c-4bbe-bacb-6e8d07e96e8b",
                    powerPerStack)),
            entry(POWER.get(MagicSchool.FIRE).name, new SpellStatusEffect.Config(
                    731,
                    MagicSchool.FIRE.color(),
                    "3c06c1d4-3cce-11ed-a261-0242ac120002",
                    powerPerStack)),
            entry(POWER.get(MagicSchool.FROST).name, new SpellStatusEffect.Config(
                    732,
                    MagicSchool.FROST.color(),
                    "41569be6-3cce-11ed-a261-0242ac120002",
                    powerPerStack)),
            entry(POWER.get(MagicSchool.HEALING).name, new SpellStatusEffect.Config(
                    733,
                    MagicSchool.HEALING.color(),
                    "4eccd93c-4a63-11ed-b878-0242ac120002",
                    powerPerStack)),
            entry(POWER.get(MagicSchool.LIGHTNING).name, new SpellStatusEffect.Config(
                    734,
                    MagicSchool.LIGHTNING.color(),
                    "48a773c8-4a63-11ed-b878-0242ac120002",
                    powerPerStack)),
            entry(POWER.get(MagicSchool.SOUL).name, new SpellStatusEffect.Config(
                    735,
                    MagicSchool.SOUL.color(),
                    "5515bafc-4a63-11ed-b878-0242ac120002",
                    powerPerStack)),
            entry(CRITICAL_CHANCE.name, new SpellStatusEffect.Config(
                    736,
                    0xffffcc,
                    "0e0ddd12-0646-42b7-8daf-36b4ccf524df",
                    0.05F)),
            entry(CRITICAL_DAMAGE.name, new SpellStatusEffect.Config(
                    737,
                    0xffcccc,
                    "0612ed2a-3ce5-11ed-b878-0242ac120002",
                    0.1F)),
            entry(HASTE.name, new SpellStatusEffect.Config(
                    738,
                    0xffccff,
                    "092f4f58-3ce5-11ed-b878-0242ac120002",
                    0.05F))
    );

    public boolean isValid() {
        return effects != null && !effects.isEmpty();
    }
}
