package net.spelldamage.api.enchantment;

import net.spelldamage.api.MagicSchool;
import org.jetbrains.annotations.Nullable;

public interface MagicalItemStack {
    @Nullable
    MagicSchool getMagicSchool();
}
