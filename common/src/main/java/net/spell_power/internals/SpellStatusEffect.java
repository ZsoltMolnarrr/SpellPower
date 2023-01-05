package net.spell_power.internals;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class SpellStatusEffect extends StatusEffect {
    public SpellStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    public int preferredRawId = 0;

    public static class Config {
        public int raw_id;
        public int color = 0xFFFFFF;
        public String udid;
        public float bonus_per_stack = 0.1F;

        public Config() { }

        public Config(int rawId, int color, String udid, float bonus_per_stack) {
            this.raw_id = rawId;
            this.color = color;
            this.udid = udid;
            this.bonus_per_stack = bonus_per_stack;
        }
    }
}
