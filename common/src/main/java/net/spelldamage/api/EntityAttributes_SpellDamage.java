package net.spelldamage.api;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.Identifier;
import net.spelldamage.SpellDamage;

import java.util.*;

public class EntityAttributes_SpellDamage {
    private static final String translationPrefix = "attribute.name.spell_damage.";
    public static final String criticalChanceName = "critical_chance";
    public static final Identifier criticalChanceId = new Identifier(SpellDamage.MOD_ID, criticalChanceName);
    public static final EntityAttribute CRITICAL_CHANCE = new ClampedEntityAttribute(translationPrefix + criticalChanceName, 0, 0.0, 2048.0);

    public static final String criticalDamageName = "critical_damage";
    public static final Identifier criticalDamageId = new Identifier(SpellDamage.MOD_ID, criticalDamageName);
    public static final EntityAttribute CRITICAL_DAMAGE = new ClampedEntityAttribute(translationPrefix + criticalDamageName, 0, 0.0, 2048.0);

    public static final String hasteName = "haste";
    public static final Identifier hasteId = new Identifier(SpellDamage.MOD_ID, hasteName);
    public static final EntityAttribute HASTE = new ClampedEntityAttribute(translationPrefix + hasteName, 0, 0.0, 2048.0);


    // spell crit
    // spell haste

    public static List<EntityAttribute> all;
    public static Map<MagicSchool, EntityAttribute> types;

    private static EntityAttribute createAttribute(MagicSchool MagicSchool) {
        return new ClampedEntityAttribute(translationPrefix + MagicSchool.spellName(), 0, 0.0, 2048.0);
    }

    static {
        all = new ArrayList<>();
        types = new HashMap<>();
        for(var MagicSchool: MagicSchool.values()) {
            var attribute= createAttribute(MagicSchool);
            types.put(MagicSchool, attribute);
            all.add(attribute);
        }
        all.add(CRITICAL_CHANCE);
        all.add(CRITICAL_DAMAGE);
        all.add(HASTE);
    }
}
