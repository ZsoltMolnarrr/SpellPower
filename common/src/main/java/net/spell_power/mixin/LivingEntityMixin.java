package net.spell_power.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import net.spell_power.api.ModifierDefinitions;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellResistance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/// Loader-neutral LivingEntity hooks. Default-attribute attachment is per loader
/// (Fabric: `LivingEntityAttributesMixin`; Forge: `EntityAttributeModificationEvent`).
@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {
    LivingEntityMixin(final EntityType<?> type, final World world) {
        super(type, world);
    }

    // init tail
    @Inject(method = "<init>", at = @At("TAIL"))
    private void onConstructed(EntityType entityType, World world, CallbackInfo ci) {
        var attributes = ((LivingEntity)(Object)this).getAttributes();
        for (var mechanic : SpellPowerMechanics.all.values()) {
            if (mechanic.innateModifier == null || mechanic.attributeEntry == null) {
                continue;
            }
            if (!attributes.hasAttribute(mechanic.attributeEntry)
                    || attributes.hasModifierForAttribute(mechanic.attributeEntry, ModifierDefinitions.INNATE_BONUS_UUID)) {
                continue;
            }
            var instance = attributes.getCustomInstance(mechanic.attributeEntry);
            if (instance != null) {
                instance.addPersistentModifier(mechanic.innateModifier);
            }
        }
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0)
    private float damage_resistance(float amount, DamageSource source) {
        var entity = (LivingEntity)(Object)this;
        if (entity.isInvulnerableTo(source) || entity.isDead()) {
            return amount;
        }
        return (float) SpellResistance.resist(entity, amount, source);
    }
}
