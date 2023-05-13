package net.spell_power.api.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public final class EnchantmentRestriction {
    public interface Condition {
        boolean appliesForItemStack(ItemStack itemStack);
    }
    private static HashMap<Enchantment, ArrayList<Condition>> permissions = new HashMap<>();
    private static HashMap<Enchantment, ArrayList<Condition>> prohibitions = new HashMap<>();

    /**
     * Forces the given enchantment to accept ItemStacks, for those the given condition meets.
     * @param enchantment
     * @param condition
     */
    public static void permit(Enchantment enchantment, Condition condition) {
        var conditions = permissions.get(enchantment);
        if (conditions == null) {
            conditions = new ArrayList<>();
        }
        conditions.add(condition);
        permissions.put(enchantment, conditions);
    }

    public static boolean isPermitted(Enchantment enchantment, ItemStack itemStack) {
        var conditions = permissions.get(enchantment);
        if (conditions != null) {
            for(var permittingCondition: conditions) {
               if (permittingCondition.appliesForItemStack(itemStack)) {
                   return true;
               }
            }
        }
        return false;
    }

    /**
     * Forces the given enchantment to NOT accept ItemStacks, for those the given condition meets.
     * @param enchantment
     * @param condition
     */
    public static void prohibit(Enchantment enchantment, Condition condition) {
        var conditions = prohibitions.get(enchantment);
        if (conditions == null) {
            conditions = new ArrayList<>();
        }
        conditions.add(condition);
        prohibitions.put(enchantment, conditions);
    }

    public static boolean isProhibited(Enchantment enchantment, ItemStack itemStack) {
        var conditions = prohibitions.get(enchantment);
        if (conditions != null) {
            for(var prohibitingCondition: conditions) {
                if (prohibitingCondition.appliesForItemStack(itemStack)) {
                    return true;
                }
            }
        }
        return false;
    }
}