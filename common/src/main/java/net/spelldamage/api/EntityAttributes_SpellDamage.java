package net.spelldamage.api;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.Identifier;
import net.spelldamage.SpellDamage;

import java.util.*;

public class EntityAttributes_SpellDamage {
    private static final String spellTranslationPrefix = "attribute.name.spell.";

    public static final String criticalChanceName = "critical_chance";
    public static final Identifier criticalChanceId = new Identifier(SpellDamage.MOD_ID, criticalChanceName);
    public static final double criticalChanceBaseline = 100;
    public static final EntityAttribute CRITICAL_CHANCE = new ClampedEntityAttribute(spellTranslationPrefix + criticalChanceName, criticalChanceBaseline, criticalChanceBaseline, criticalChanceBaseline * 2);

    public static final String criticalDamageName = "critical_damage";
    public static final Identifier criticalDamageId = new Identifier(SpellDamage.MOD_ID, criticalDamageName);
    public static final double criticalDamageBaseline = 100;
    public static final EntityAttribute CRITICAL_DAMAGE = new ClampedEntityAttribute(spellTranslationPrefix + criticalDamageName, criticalDamageBaseline, criticalDamageBaseline, criticalDamageBaseline * 10);

    public static final String hasteName = "haste";
    public static final Identifier hasteId = new Identifier(SpellDamage.MOD_ID, hasteName);
    public static final EntityAttribute HASTE = new ClampedEntityAttribute(spellTranslationPrefix + hasteName, 0, 0.0, 2048.0);

    private static final String spellDamageTranslationPrefix = "attribute.name.spell_damage.";
    private static EntityAttribute createAttribute(MagicSchool MagicSchool) {
        return new ClampedEntityAttribute(spellDamageTranslationPrefix + MagicSchool.spellName(), 0, 0.0, 2048.0);
    }

    public static final Map<Identifier, EntityAttribute> all;
    public static final Map<MagicSchool, EntityAttribute> DAMAGE;

    static {
        all = new HashMap<>();
        DAMAGE = new HashMap<>();
        for(var school: MagicSchool.values()) {
            var attribute = createAttribute(school);
            DAMAGE.put(school, attribute);
            all.put(school.attributeId(), attribute);
        }
        all.put(criticalChanceId, CRITICAL_CHANCE);
        all.put(criticalDamageId, CRITICAL_DAMAGE);
        all.put(hasteId, HASTE);
    }
}
