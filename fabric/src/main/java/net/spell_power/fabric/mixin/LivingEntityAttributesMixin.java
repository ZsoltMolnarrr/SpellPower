package net.spell_power.fabric.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.spell_power.SpellPowerMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/// Fabric: attach Spell Power's attributes to every living entity's default attribute container.
/// (Forge uses `EntityAttributeModificationEvent` instead — see `ForgeMod`.)
@Mixin(LivingEntity.class)
public abstract class LivingEntityAttributesMixin {
    @Inject(
            method = "createLivingAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;",
            require = 1, allow = 1, at = @At("RETURN")
    )
    private static void addAttributes_SpellPower(final CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        var builder = info.getReturnValue();
        for (var attribute : SpellPowerMod.attributesToAttach()) {
            builder.add(attribute);
        }
    }
}
