package net.spell_power.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import net.spell_power.SpellPowerMod;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        SpellPowerMod.init();

        // Attributes, status effects and potions are registered from the `<clinit>`-TAIL mixins
        // (spell_power.fabric.mixins.json) during Bootstrap; enchantments are plain init-time registrations.
        SpellPowerMod.registerEnchantments();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                SpellPowerMod.onPlayerJoin(handler.getPlayer()));

        // Enchantment applicability (tags, matching attributes, EnchantmentRestriction) is enforced by the
        // loader-neutral EnchantmentMixin + EnchantmentHelperMixin pair; no Fabric API event is needed.
    }
}
