package net.spelldamage.fabric;

import net.spelldamage.SpellDamage;
import net.fabricmc.api.ModInitializer;

public class SpellDamageFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SpellDamage.init();
    }
}