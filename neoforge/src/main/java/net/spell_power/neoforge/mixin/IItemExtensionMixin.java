package net.spell_power.neoforge.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import net.spell_power.SpellPowerEnchanting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/// Enforces Spell Power's enchantment restrictions on the enchanting-table path. NeoForge routes the
/// table through `IItemExtension#supportsEnchantment(ItemStack, Holder)` (via `isPrimaryItemFor`), which
/// carries the enchantment `RegistryEntry` directly — so the {@link SpellPowerEnchanting} decision (the
/// same one the Fabric event uses) can be applied here without any extra plumbing.
///
/// The anvil path (raw `Enchantment#isAcceptableItem`) is intentionally NOT covered on NeoForge: a
/// Minecraft-targeting mixin for it does not remap in the dev environment, so the anvil restriction
/// stays Fabric-only.
@Mixin(IItemExtension.class)
public interface IItemExtensionMixin {
    @Inject(method = "supportsEnchantment", at = @At("RETURN"), cancellable = true)
    private void spellPower$requireMatchingAttribute(ItemStack stack, RegistryEntry<Enchantment> enchantment, CallbackInfoReturnable<Boolean> cir) {
        // Only ever tightens applicability: if it was going to be allowed, deny when a rule blocks it.
        if (cir.getReturnValueZ() && !SpellPowerEnchanting.isAllowed(enchantment, stack)) {
            cir.setReturnValue(false);
        }
    }
}
