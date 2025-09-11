package net.spell_power.neoforge;

import net.neoforged.fml.common.Mod;

import net.spell_power.ExampleMod;

@Mod(ExampleMod.MOD_ID)
public final class ExampleModNeoForge {
    public ExampleModNeoForge() {
        // Run our common setup.
        ExampleMod.init();
    }
}
