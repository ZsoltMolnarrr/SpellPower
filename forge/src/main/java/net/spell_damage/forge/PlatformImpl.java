package net.spell_damage.forge;

import net.minecraftforge.fml.ModList;
import net.spell_damage.Platform;

import static net.spell_damage.Platform.Type.FORGE;

public class PlatformImpl {
    public static Platform.Type getPlatformType() {
        return FORGE;
    }

    public static boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }
}
