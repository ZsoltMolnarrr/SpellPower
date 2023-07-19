package net.spell_power.mixin;

import net.minecraft.registry.Registries;
import net.spell_power.Platform;
import net.spell_power.SpellPowerMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Registries.class)
public class RegistryMixin {
    @Inject(method = "freezeRegistries", at = @At("HEAD"))
    private static void freezeRegistries_HEAD_SpellPower(CallbackInfo ci) {
        if (Platform.Forge) {
            SpellPowerMod.loadConfig(); // Nice...
            SpellPowerMod.registerAttributes();
            SpellPowerMod.registerEnchantments();
        }
    }
}
