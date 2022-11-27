package net.spelldamage.api.enchantment;

import net.minecraft.item.ItemStack;

public interface ConditionalEnchantment {
    boolean isAcceptableItem(ItemStack stack);
}
