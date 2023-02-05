package net.spell_power.api.enchantment;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

public enum ItemType {
    ARMOR, MAGICAL_ARMOR, MAGICAL_WEAPON;

    public boolean matches(ItemStack stack) {
        switch (this) {
            case ARMOR -> {
                return stack.getItem() instanceof ArmorItem;
            }
            case MAGICAL_ARMOR -> {
                if (stack.getItem() instanceof ArmorItem armor) {
                    return MagicArmorEnchanting.isRegistered(armor)
                            || !MagicArmorEnchanting.relevantSchools(stack, armor.getSlotType()).isEmpty();
                }
                return false;
            }
            case MAGICAL_WEAPON -> {
                return isValidMagicalStack(stack);
            }
        }
        return true;
    }

    private boolean isValidMagicalStack(ItemStack stack) {
        var object = (Object) stack;
        if (object instanceof MagicalItemStack magicalItemStack) {
            var school = magicalItemStack.getMagicSchool();
            return school != null;
        }
        return false;
    }
}