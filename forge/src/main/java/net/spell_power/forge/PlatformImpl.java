package net.spell_power.forge;

import net.minecraftforge.fml.ModList;
import net.spell_power.Platform;

import static net.spell_power.Platform.Type.FORGE;

public class PlatformImpl {
    public static Platform.Type getPlatformType() {
        return FORGE;
    }

    public static boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }
}
