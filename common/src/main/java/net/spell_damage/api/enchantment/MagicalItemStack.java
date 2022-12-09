package net.spell_damage.api.enchantment;

import net.spell_damage.api.MagicSchool;
import org.jetbrains.annotations.Nullable;

public interface MagicalItemStack {
    @Nullable
    MagicSchool getMagicSchool();
}
