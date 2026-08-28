package net.spell_power.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import net.spell_power.api.ModifierDefinitions;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellResistance;
import net.spell_power.api.SpellSchools;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {
    LivingEntityMixin(final EntityType<?> type, final Level world) {
        super(type, world);
    }

    @Inject(
            method = "createLivingAttributes()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;",
            require = 1, allow = 1, at = @At("RETURN")
    )
    private static void addAttributes(final CallbackInfoReturnable<AttributeSupplier.Builder> info) {
        // Disabled AttributeScope-ing, as mob mods and resistance attributes complicate things
//        if (SpellPowerMod.attributeScope() == AttributesConfig.AttributeScope.LIVING_ENTITY) {
        for (var entry : SpellPowerMechanics.all.entrySet()) {
            var secondary = entry.getValue();
            info.getReturnValue().add(secondary.attributeEntry);
        }
        for (var school: SpellSchools.all()) {
            if (school.ownsAttribute()) {
                var attribute = school.attributeEntry;
                info.getReturnValue().add(attribute);
            }
        }
        for (var resistance: SpellResistance.Attributes.all) {
            info.getReturnValue().add(resistance.attributeEntry);
        }
    }

    // init tail
    @Inject(method = "<init>", at = @At("TAIL"))
    private void onConstructed(EntityType entityType, Level world, CallbackInfo ci) {
        var attributes = ((LivingEntity)(Object)this).getAttributes();
        for (var mechanic : SpellPowerMechanics.all.values()) {
            if (mechanic.innateModifier != null
                    && !attributes.hasModifier(mechanic.attributeEntry, ModifierDefinitions.INNATE_BONUS)) {
                attributes
                        .getInstance(mechanic.attributeEntry)
                        .addPermanentModifier(mechanic.innateModifier);
            }
        }
    }

    @ModifyVariable(method = "hurtServer", at = @At("HEAD"), ordinal = 0)
    private float damage_resistance(float amount, ServerLevel world, DamageSource source) {
        var entity = (LivingEntity)(Object)this;
        // NOTE: deliberately does NOT call `isInvulnerableTo` as an early-out guard.
        // In this ecosystem that method is not a pure query: Spell Engine hooks it at RETURN
        // (`LivingEntityProtectionEffectMixin` -> `Protection.tryProtect`) and *spends* a
        // damage-protection charge there, playing the pop FX and decrementing the effect.
        // Vanilla already calls it once per hit, so a speculative call here consumed a second
        // charge per blocked hit — and on the last remaining charge it consumed the charge
        // itself, leaving vanilla's own check with nothing left, so the hit landed anyway.
        // Skipping the guard is safe: `SpellResistance.resist` is a pure multiplier with no
        // side effects, so scaling the amount of a hit that never lands is a no-op.
        if (entity.isDeadOrDying()) {
            return amount;
        }
        return (float) SpellResistance.resist(entity, amount, source);
    }
}