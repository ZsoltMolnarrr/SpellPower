package net.spell_power.neoforge;

import net.neoforged.fml.common.Mod;

import net.spell_power.SpellPowerMod;

@Mod(SpellPowerMod.ID)
public final class NeoForgeMod {
    public NeoForgeMod() {
        SpellPowerMod.init();
    }
}
