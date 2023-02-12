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
                    return SpellPowerEnchanting.isArmorRegistered(armor)
                            || !SpellPowerEnchanting.relevantSchools(stack, armor.getSlotType()).isEmpty();
                }
                return false;
            }
            case MAGICAL_WEAPON -> {
                return SpellPowerEnchanting.isAllowedForWeapon(stack);
            }
        }
        return true;
    }
}