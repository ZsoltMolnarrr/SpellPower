package net.spelldamage.internals;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.spelldamage.SpellDamageMod;
import net.spelldamage.api.MagicSchool;

public class AttributeFamily {
    public final String name;
    public final Identifier id;
    public final EntityAttribute attribute;
    public final SpellStatusEffect statusEffect;


    public AttributeFamily(MagicSchool school) {
        this(school.spellName(),
                "attribute.name.spell.damage.",
                new AttributeData(0, 0, 2048),
                SpellDamageMod.effectsConfig.value.damage.get(school.spellName()));
    }

    public AttributeFamily(String name, float defaultValue) {
        this(name,
                "attribute.name.spell.",
                new AttributeData(defaultValue, defaultValue, defaultValue * 10),
                SpellDamageMod.effectsConfig.value.rating.get(name));
    }

    public AttributeFamily(String name, String translationPrefix, AttributeData attributeData, SpellStatusEffect.AttributeModifierProperties effectConfig) {
        this.name = name;
        this.id = new Identifier(SpellDamageMod.MOD_ID, name);
        this.attribute = new ClampedEntityAttribute(translationPrefix + name, attributeData.defaultValue, attributeData.min, attributeData.max).setTracked(true);
        this.statusEffect = new SpellStatusEffect(StatusEffectCategory.BENEFICIAL, effectConfig.color);
        this.statusEffect.addAttributeModifier(
                this.attribute,
                effectConfig.udid,
                effectConfig.bonus_per_stack,
                EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    public record AttributeData(float defaultValue, float min, float max) { };
}
