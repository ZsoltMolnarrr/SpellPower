package net.spell_power.api;

import net.spell_power.api.attributes.CustomEntityAttribute;
import net.spell_power.internals.Attributes;

import java.util.HashMap;
import java.util.Map;

public class EntityAttributes_SpellPower {
    public static final CustomEntityAttribute CRITICAL_CHANCE = Attributes.all.get(Attributes.CRITICAL_CHANCE).attribute;
    public static final CustomEntityAttribute CRITICAL_DAMAGE = Attributes.all.get(Attributes.CRITICAL_DAMAGE).attribute;
    public static final CustomEntityAttribute HASTE = Attributes.all.get(Attributes.HASTE).attribute;
    public static final Map<MagicSchool, CustomEntityAttribute> POWER;

    static {
        POWER = new HashMap<>();
        for(var school: MagicSchool.values()) {
            var attribute = Attributes.all.get(school.spellName()).attribute;
            POWER.put(school, attribute);
        }
        HASTE.setTracked(true);
    }
}
