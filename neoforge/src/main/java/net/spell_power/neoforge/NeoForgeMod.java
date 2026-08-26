package net.spell_power.neoforge;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import net.spell_power.SpellPowerMod;

@Mod(SpellPowerMod.ID)
public final class NeoForgeMod {
    public NeoForgeMod() {
        SpellPowerMod.init();

        // Player-join attribute migration (replaces Fabric's ServerPlayConnectionEvents.JOIN).
        NeoForge.EVENT_BUS.addListener(PlayerEvent.PlayerLoggedInEvent.class, event -> {
            if (event.getEntity() instanceof ServerPlayer player) {
                SpellPowerMod.onPlayerJoin(player);
            }
        });

        // Enchantment restrictions (enchantments_require_matching_attribute): NeoForge has no global
        // enchant-applicability event, so the enchanting-table path is enforced by IItemExtensionMixin
        // (supportsEnchantment), calling SpellPowerEnchanting — the same decision code the Fabric event
        // uses. The anvil path is NOT enforced on NeoForge (it checks raw Enchantment#isAcceptableItem,
        // which would need a Minecraft-targeting mixin that does not remap in the dev environment);
        // the anvil restriction remains Fabric-only.
    }
}
