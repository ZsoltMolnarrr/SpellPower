package net.spelldamage.api;

import net.minecraft.item.ItemStack;

public interface ConditionalEnchantment {
    boolean isAcceptableItem(ItemStack stack);
}
