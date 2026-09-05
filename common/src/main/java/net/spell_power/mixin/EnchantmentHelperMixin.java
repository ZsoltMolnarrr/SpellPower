package net.spell_power.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

/// The 1.20.1 enchanting table only consults `EnchantmentTarget` (vanilla) / `canApplyAtEnchantingTable` (Forge),
/// never `Enchantment#isAcceptableItem`. This post-processes the candidate list so `isAcceptableItem` — where
/// Spell Power's tag/attribute rules and `EnchantmentRestriction` overrides live — is authoritative on both loaders.
@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "getPossibleEntries", at = @At("RETURN"))
    private static void getPossibleEntries_RETURN_SpellPower(int power, ItemStack stack, boolean treasureAllowed, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
        var currentEntries = cir.getReturnValue();
        boolean isBook = stack.isOf(Items.BOOK);

        // 1. REMOVING ENCHANT ENTRIES ADDED INCORRECTLY
        if (!isBook) {
            var toRemove = new ArrayList<EnchantmentLevelEntry>();
            for (var entry: currentEntries) {
                if (!entry.enchantment.isAcceptableItem(stack)) {
                    toRemove.add(entry);
                }
            }
            currentEntries.removeAll(toRemove);
        }

        // 2. ADDING ENCHANT ENTRIES LEFT OUT INITIALLY
        // Mirrors the vanilla loop, substituting `enchantment.isAcceptableItem(stack)` for the target check.
        for (Enchantment enchantment : Registries.ENCHANTMENT) {
            boolean alreadyAdded = currentEntries.stream().anyMatch(entry -> entry.enchantment.equals(enchantment));
            if (alreadyAdded) { continue; }

            if ((enchantment.isTreasure() && !treasureAllowed)
                    || !enchantment.isAvailableForRandomSelection()
                    || (!enchantment.isAcceptableItem(stack) && !isBook)) {
                continue;
            }
            for (int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {
                if (power < enchantment.getMinPower(i) || power > enchantment.getMaxPower(i)) continue;
                currentEntries.add(new EnchantmentLevelEntry(enchantment, i));
                break;
            }
        }
    }
}
