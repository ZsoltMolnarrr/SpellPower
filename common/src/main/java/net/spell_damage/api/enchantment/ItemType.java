package net.spell_damage.api.enchantment;

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
                return (stack.getItem() instanceof ArmorItem) && (stack.getItem() instanceof MagicalArmor);
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