package net.spell_power.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.spell_power.api.enchantment.EnchantmentRestriction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    @Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
    private void isAcceptableItem_HEAD_SpellPower(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        var enchantment = (Enchantment)((Object)this);

        if (EnchantmentRestriction.isProhibited(enchantment, stack)) {
            cir.setReturnValue(false);
            cir.cancel();
            return;
        }

        if (EnchantmentRestriction.isPermitted(enchantment, stack)) {
            cir.setReturnValue(true);
            cir.cancel();
            return; // Keeping this `return` just to make safe adding any further code below
        }
    }
}
