package net.spelldamage.internals;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class SpellStatusEffect extends StatusEffect {
    public SpellStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    public static class AttributeModifierProperties {
        public int color = 0xFFFFFF;
        public String udid;
        public float bonus_per_stack = 0.1F;

        public AttributeModifierProperties() { }

        public AttributeModifierProperties(int color, String udid, float bonus_per_stack) {
            this.color = color;
            this.udid = udid;
            this.bonus_per_stack = bonus_per_stack;
        }
    }
}
