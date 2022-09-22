package net.spelldamage.forge;

import net.minecraftforge.fml.ModList;
import net.spelldamage.Platform;

import static net.spelldamage.Platform.Type.FORGE;

public class PlatformImpl {
    public static Platform.Type getPlatformType() {
        return FORGE;
    }

    public static boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }
}
