package net.spelldamage.fabric;

import net.spelldamage.SpellDamage;
import net.fabricmc.api.ModInitializer;

public class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        SpellDamage.init();
        SpellDamage.registerAttributes();
        SpellDamage.registerEnchantments();
        SpellDamage.configureEnchantments();
    }
}