package net.spell_power.api.attributes;

import net.spell_power.api.MagicSchool;

import java.util.HashMap;
import java.util.Map;

public class EntityAttributes_SpellPower {
    public static final CustomEntityAttribute CRITICAL_CHANCE = SpellAttributes.CRITICAL_CHANCE.attribute;
    public static final CustomEntityAttribute CRITICAL_DAMAGE = SpellAttributes.CRITICAL_DAMAGE.attribute;
    public static final CustomEntityAttribute HASTE = SpellAttributes.HASTE.attribute;
    public static final Map<MagicSchool, CustomEntityAttribute> POWER;

    static {
        POWER = new HashMap<>();
        for(var school: MagicSchool.values()) {
            var attribute = SpellAttributes.all.get(school.spellName()).attribute;
            POWER.put(school, attribute);
        }
        HASTE.setTracked(true);
    }
}
