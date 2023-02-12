package net.spell_power.api.enchantment;

import net.spell_power.api.MagicSchool;
import org.jetbrains.annotations.Nullable;

@Deprecated
public interface MagicalItemStack {
    @Nullable
    MagicSchool getMagicSchool();
}
