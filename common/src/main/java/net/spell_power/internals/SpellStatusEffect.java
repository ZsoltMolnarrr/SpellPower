package net.spell_power.internals;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class SpellStatusEffect extends StatusEffect {
    public SpellStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    public static class Config {
        public float bonus_per_stack = 0.1F;

        public Config() { }

        public Config(float bonus_per_stack) {
            this.bonus_per_stack = bonus_per_stack;
        }
    }
}
