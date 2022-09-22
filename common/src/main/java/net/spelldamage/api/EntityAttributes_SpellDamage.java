package net.spelldamage.api;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.Identifier;
import net.spelldamage.SpellDamage;

import java.util.*;

public class EntityAttributes_SpellDamage {

    // spell crit
    // spell haste

    public static List<EntityAttribute> all;
    public static Map<Type, EntityAttribute> types;

    private static EntityAttribute createAttribute(Type type) {
        return new ClampedEntityAttribute("attribute.name.spell_damage." + type.spellName(), 0, 0.0, 2048.0);
    }

    static {
        all = new ArrayList<>();
        types = new HashMap<>();
        for(var type: Type.values()) {
            var attribute= createAttribute(type);
            types.put(type, attribute);
            all.add(attribute);
        }


//        all = Arrays.stream(Type.values()).map(EntityAttributes_SpellDamage::createAttribute).toList();
    }

    public enum Type {
        FIRE, FROST, SHADOW;

        public String spellName() {
            return this.toString().toLowerCase();
        }

        public Identifier attributeId() {
            return new Identifier(SpellDamage.MOD_ID + ":" + spellName());
        }
    }
}
