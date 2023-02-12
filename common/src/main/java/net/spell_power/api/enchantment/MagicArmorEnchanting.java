package net.spell_power.api.enchantment;

import net.minecraft.item.ArmorItem;

import java.util.List;

@Deprecated
public final class MagicArmorEnchanting {
    @Deprecated
    public static void register(ArmorItem... items) {
        SpellPowerEnchanting.registerArmor(items);
    }

    @Deprecated
    public static void register(List<ArmorItem> items) {
        SpellPowerEnchanting.registerArmor(items);
    }
}
