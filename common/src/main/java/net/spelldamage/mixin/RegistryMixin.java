package net.spelldamage.mixin;

import net.minecraft.util.registry.Registry;
import net.spelldamage.Platform;
import net.spelldamage.SpellDamage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Registry.class)
public class RegistryMixin {
    @Inject(method = "freezeRegistries", at = @At("HEAD"))
    private static void pre_freezeRegistries(CallbackInfo ci) {
        if (Platform.Forge) {
            SpellDamage.registerAttributes();
            SpellDamage.registerEnchantments();
        }
    }
}
