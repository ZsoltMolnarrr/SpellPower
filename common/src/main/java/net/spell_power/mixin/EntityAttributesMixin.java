package net.spell_power.mixin;

import net.minecraft.entity.attribute.EntityAttributes;
import net.spell_power.SpellPowerMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityAttributes.class, priority = 10000) // Low priority to be applied as last
public class EntityAttributesMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void static_tail_SpellPower(CallbackInfo ci) {
        SpellPowerMod.registerAttributes();
    }
}
