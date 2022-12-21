package net.spell_power.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.spell_power.Platform;

import static net.spell_power.Platform.Type.FABRIC;

public class PlatformImpl {
    public static Platform.Type getPlatformType() {
        return FABRIC;
    }

    public static boolean isModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }
}
