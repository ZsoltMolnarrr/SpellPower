package net.spell_power.api.statuseffects;

import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;
import net.spell_power.internals.SpellStatusEffect;

import java.util.HashMap;
import java.util.Map;

public class StatusEffects_SpellPower {
    public static final SpellStatusEffect CRITICAL_CHANCE = SpellAttributes.CRITICAL_CHANCE.statusEffect;
    public static final SpellStatusEffect CRITICAL_DAMAGE = SpellAttributes.CRITICAL_DAMAGE.statusEffect;
    public static final SpellStatusEffect HASTE = SpellAttributes.HASTE.statusEffect;
    public static final Map<MagicSchool, SpellStatusEffect> DAMAGE;
    static {
        DAMAGE = new HashMap<>();
        for(var school: MagicSchool.values()) {
            var statusEffect = SpellAttributes.all.get(school.spellName()).statusEffect;
            DAMAGE.put(school, statusEffect);
        }
    }
}
