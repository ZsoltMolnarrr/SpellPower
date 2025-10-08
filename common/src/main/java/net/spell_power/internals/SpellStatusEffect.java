package net.spell_power.internals;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class SpellStatusEffect extends StatusEffect {
    public SpellStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }
}
