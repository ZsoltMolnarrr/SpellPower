package net.spelldamage.api.enchantment;

import net.minecraft.item.ItemStack;

public interface CustomConditionalEnchantment {
    interface Condition {
        boolean isAcceptableItem(ItemStack stack);
    }
    void setCondition(Condition condition);
}