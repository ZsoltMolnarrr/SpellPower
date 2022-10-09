package net.spelldamage.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.spelldamage.api.SpellDamageHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.spelldamage.api.MagicSchool.FIRE;
import static net.spelldamage.api.MagicSchool.FROST;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    public void asd(Entity target, CallbackInfo ci) {
        var player = (PlayerEntity) ((Object)this);
        var spellDamage = SpellDamageHelper.getSpellDamage(FROST, player);
        System.out.println("Player attack spell damage: " + spellDamage);
    }
}
