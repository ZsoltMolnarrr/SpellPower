package net.spell_damage.fabric;

import net.spell_damage.SpellDamageMod;
import net.fabricmc.api.ModInitializer;

public class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        SpellDamageMod.init();
        SpellDamageMod.registerAttributes();
        SpellDamageMod.registerEnchantments();
        SpellDamageMod.configureEnchantments();
        SpellDamageMod.registerStatusEffects();
    }
}