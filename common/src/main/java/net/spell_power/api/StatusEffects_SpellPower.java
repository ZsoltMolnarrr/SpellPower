package net.spell_power.api;

import net.spell_power.internals.Attributes;
import net.spell_power.internals.SpellStatusEffect;

import java.util.HashMap;
import java.util.Map;

public class StatusEffects_SpellPower {
    public static final SpellStatusEffect CRITICAL_CHANCE = Attributes.all.get(Attributes.CRITICAL_CHANCE).statusEffect;
    public static final SpellStatusEffect CRITICAL_DAMAGE = Attributes.all.get(Attributes.CRITICAL_DAMAGE).statusEffect;
    public static final SpellStatusEffect HASTE = Attributes.all.get(Attributes.HASTE).statusEffect;
    public static final Map<MagicSchool, SpellStatusEffect> DAMAGE;
    static {
        DAMAGE = new HashMap<>();
        for(var school: MagicSchool.values()) {
            var statusEffect = Attributes.all.get(school.spellName()).statusEffect;
            DAMAGE.put(school, statusEffect);
        }
    }
}
