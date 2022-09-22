package net.spelldamage.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.spelldamage.Platform;

import static net.spelldamage.Platform.Type.FABRIC;

public class PlatformImpl {
    public static Platform.Type getPlatformType() {
        return FABRIC;
    }

    public static boolean isModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }
}
