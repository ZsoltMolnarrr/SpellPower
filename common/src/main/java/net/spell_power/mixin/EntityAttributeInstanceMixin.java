package net.spell_power.mixin;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.spell_power.api.ModifierDefinitions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AttributeInstance.class)
abstract class EntityAttributeInstanceMixin {
    @Shadow public abstract boolean hasModifier(Identifier id);

    @Inject(
            method = "addPermanentModifier", require = 1, allow = 1,
            at = @At("HEAD"), cancellable = true
    )
    private void spellPower_skipDuplicateInnate(AttributeModifier modifier, CallbackInfo ci) {
        // The innate bonus is seeded in the LivingEntity constructor. On End-portal return vanilla
        // builds a fresh ServerPlayer (constructor seeds it again) and then copies the old player's
        // permanent modifiers via restoreFrom -> assignPermanentModifiers -> addPermanentModifiers,
        // which would throw "Modifier is already applied on this attribute!". Re-adding the innate
        // bonus when it is already present is a no-op (fixes GitHub issue #56).
        if (ModifierDefinitions.INNATE_BONUS.equals(modifier.id()) && hasModifier(modifier.id())) {
            ci.cancel();
        }
    }
}
