package net.spell_power.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellResistance;
import net.spell_power.api.SpellSchools;
import net.spell_power.config.AttributesConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {
    LivingEntityMixin(final EntityType<?> type, final World world) {
        super(type, world);
    }

    @Inject(
            method = "createLivingAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;",
            require = 1, allow = 1, at = @At("RETURN")
    )
    private static void addAttributes(final CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        if (SpellPowerMod.attributeScope() == AttributesConfig.AttributeScope.LIVING_ENTITY) {
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