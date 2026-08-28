package net.spell_power.neoforge.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import net.spell_power.SpellPowerEnchanting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/// Enforces Spell Power's enchantment restrictions on NeoForge. NeoForge patches the anvil
/// (`AnvilMenu`), loot enchanting (`EnchantRandomlyFunction`) and `/enchant` (`EnchantCommand`) to route
/// through `IItemExtension#supportsEnchantment(ItemStack, Holder)`, and the enchanting table reaches it
/// via `isPrimaryItemFor` — so this single method is the exact twin of Fabric's
/// `EnchantmentEvents.ALLOW_ENCHANTING` with `EnchantingContext.ACCEPTABLE`. It carries the enchantment
/// `Holder` directly, so the {@link SpellPowerEnchanting} decision (the same one the Fabric event uses)
/// applies here with no extra plumbing, and the two loaders end up symmetric — anvil included.
@Mixin(IItemExtension.class)
public interface IItemExtensionMixin {
    @Inject(method = "supportsEnchantment", at = @At("RETURN"), cancellable = true)
    private void spellPower$requireMatchingAttribute(ItemStack stack, Holder<Enchantment> enchantment, CallbackInfoReturnable<Boolean> cir) {
        // Only ever tightens applicability: if it was going to be allowed, deny when a rule blocks it.
        if (cir.getReturnValueZ() && !SpellPowerEnchanting.isAllowed(enchantment, stack)) {
            cir.setReturnValue(false);
        }
    }
}
