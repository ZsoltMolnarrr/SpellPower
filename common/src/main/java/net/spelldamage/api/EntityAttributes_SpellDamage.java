package net.spelldamage.api;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class EntityAttributes_SpellDamage {
    public static String genericSpellDamageId = "generic.spell_damage";
    public static final EntityAttribute GENERIC_SPELL_DAMAGE = register(genericSpellDamageId, new ClampedEntityAttribute("attribute.name.generic.spell_damage", 0, 0.0, 1024.0));

    public static List<EntityAttribute> all;
    static {
        all = List.of(GENERIC_SPELL_DAMAGE);
    }

    private static EntityAttribute register(String id, EntityAttribute attribute) {
        return Registry.register(Registry.ATTRIBUTE, id, attribute);
    }
}
