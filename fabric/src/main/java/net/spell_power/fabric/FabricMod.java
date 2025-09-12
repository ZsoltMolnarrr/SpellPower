package net.spell_power.fabric;

import net.fabricmc.api.ModInitializer;

import net.spell_power.SpellPowerMod;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        SpellPowerMod.init();
    }
}
