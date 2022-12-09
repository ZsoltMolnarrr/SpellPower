package net.spell_damage.api;

import net.minecraft.entity.attribute.EntityAttribute;
import net.spell_damage.internals.Attributes;

import java.util.HashMap;
import java.util.Map;

public class EntityAttributes_SpellDamage {
    public static final EntityAttribute CRITICAL_CHANCE = Attributes.all.get(Attributes.CRITICAL_CHANCE).attribute;
    public static final EntityAttribute CRITICAL_DAMAGE = Attributes.all.get(Attributes.CRITICAL_DAMAGE).attribute;
    public static final EntityAttribute HASTE = Attributes.all.get(Attributes.HASTE).attribute;
    public static final Map<MagicSchool, EntityAttribute> DAMAGE;

    static {
        DAMAGE = new HashMap<>();
        for(var school: MagicSchool.values()) {
            var attribute = Attributes.all.get(school.spellName()).attribute;
            DAMAGE.put(school, attribute);
        }
        HASTE.setTracked(true);
    }
}
