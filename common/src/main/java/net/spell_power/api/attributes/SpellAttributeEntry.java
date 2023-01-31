package net.spell_power.api.attributes;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.MagicSchool;
import net.spell_power.internals.SpellStatusEffect;
import org.jetbrains.annotations.Nullable;

public class SpellAttributeEntry {
    public final String name;
    public final Identifier id;
    public final CustomEntityAttribute attribute;
    @Nullable
    public SpellStatusEffect statusEffect;


    public SpellAttributeEntry(MagicSchool school) {
        this(school.spellName(), new AttributeData(0, 0, 2048));
    }

    public SpellAttributeEntry(String name, float defaultValue) {
        this(name, new AttributeData(defaultValue, defaultValue, defaultValue * 10));
    }

    public SpellAttributeEntry(String name, AttributeData attributeData) {
        this.name = name;
        this.id = new Identifier(SpellPowerMod.ID, name);
        var translationPrefix = "attribute.name." + SpellPowerMod.ID + ".";
        this.attribute = (CustomEntityAttribute) new CustomEntityAttribute(translationPrefix + name, attributeData.defaultValue, attributeData.min, attributeData.max, id).setTracked(true);
    }

    public void setupStatusEffect(@Nullable SpellStatusEffect.Config config) {
        var color = 0xFFFFFF;
        if (config != null) {
            color = config.color;
        }
        this.statusEffect = new SpellStatusEffect(StatusEffectCategory.BENEFICIAL, 0xFFFFFF);
        if (config != null) {
            this.statusEffect.addAttributeModifier(
                    this.attribute,
                    config.udid,
                    config.bonus_per_stack,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
            this.statusEffect.preferredRawId = config.raw_id;
        }
    }

    public record AttributeData(float defaultValue, float min, float max) { };
}
