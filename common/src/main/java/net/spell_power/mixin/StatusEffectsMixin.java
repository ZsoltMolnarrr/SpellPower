package net.spell_power.mixin;

import net.minecraft.entity.effect.StatusEffects;
import net.spell_power.SpellPowerMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StatusEffects.class)
public class StatusEffectsMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void static_tail_SpellPower(CallbackInfo ci) {
        SpellPowerMod.registerStatusEffects();
    }
}
