package net.spell_power.api.attributes;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.MagicSchool;
import net.spell_power.internals.SpellStatusEffect;

public class SpellAttributeEntry {
    public final String name;
    public final Identifier id;
    public final CustomEntityAttribute attribute;
    public final SpellStatusEffect statusEffect;


    public SpellAttributeEntry(MagicSchool school) {
        this(school.spellName(),
                new AttributeData(0, 0, 2048),
                SpellPowerMod.effectsConfig.value.power.get(school.spellName()));
    }

    public SpellAttributeEntry(String name, float defaultValue) {
        this(name,
                new AttributeData(defaultValue, defaultValue, defaultValue * 10),
                SpellPowerMod.effectsConfig.value.rating.get(name));
    }

    public SpellAttributeEntry(String name, AttributeData attributeData, SpellStatusEffect.Config effectConfig) {
        this.name = name;
        this.id = new Identifier(SpellPowerMod.ID, name);
        var translationPrefix = "attribute.name." + SpellPowerMod.ID + ".";
        this.attribute = (CustomEntityAttribute) new CustomEntityAttribute(translationPrefix + name, attributeData.defaultValue, attributeData.min, attributeData.max, id).setTracked(true);
        this.statusEffect = new SpellStatusEffect(StatusEffectCategory.BENEFICIAL, effectConfig.color);
        this.statusEffect.addAttributeModifier(
                this.attribute,
                effectConfig.udid,
                effectConfig.bonus_per_stack,
                EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        this.statusEffect.preferredRawId = effectConfig.raw_id;
    }

    public record AttributeData(float defaultValue, float min, float max) { };
}
