package net.spell_damage.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.spell_damage.Platform;

import static net.spell_damage.Platform.Type.FABRIC;

public class PlatformImpl {
    public static Platform.Type getPlatformType() {
        return FABRIC;
    }

    public static boolean isModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }
}
