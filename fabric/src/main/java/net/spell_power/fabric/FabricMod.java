package net.spell_power.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.util.TriState;

import net.spell_power.SpellPowerEnchanting;
import net.spell_power.SpellPowerMod;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        SpellPowerMod.init();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                SpellPowerMod.onPlayerJoin(handler.getPlayer()));

        // Deny attribute-restricted enchantments on non-matching items; decision lives in SpellPowerEnchanting.
        EnchantmentEvents.ALLOW_ENCHANTING.register((enchantment, target, enchantingContext) ->
                SpellPowerEnchanting.isAllowed(enchantment, target) ? TriState.DEFAULT : TriState.FALSE);
    }
}
