package net.spell_power.mixin;

import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchools;
import net.spell_power.config.AttributesConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(
            method = "createPlayerAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;",
            require = 1, allow = 1, at = @At("RETURN")
    )
    private static void addAttributes(final CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        if (SpellPowerMod.attributeScope() == AttributesConfig.AttributeScope.PLAYER_ENTITY) {
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
        }
    }
}
