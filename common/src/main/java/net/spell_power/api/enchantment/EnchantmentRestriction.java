package net.spell_power.api.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

/// Generic enchantment-applicability overrides for *any* enchantment (including vanilla ones), consulted by
/// `EnchantmentMixin` (`Enchantment#isAcceptableItem` HEAD) and, through it, by `EnchantmentHelperMixin`
/// for the enchanting table. Spell Power's own enchantments implement their rules directly in
/// `isAcceptableItem`; this registry exists for other mods (e.g. permitting Knockback/Looting/Fire Aspect on staves).
public final class EnchantmentRestriction {
    public interface Condition {
        boolean appliesForItemStack(ItemStack itemStack);
    }
    private static final HashMap<Enchantment, ArrayList<Condition>> permissions = new HashMap<>();
    private static final HashMap<Enchantment, ArrayList<Condition>> prohibitions = new HashMap<>();

    /**
     * Forces the given enchantment to accept ItemStacks, for those the given condition meets.
     */
    public static void permit(Enchantment enchantment, Condition condition) {
        permissions.computeIfAbsent(enchantment, e -> new ArrayList<>()).add(condition);
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
     */
    public static void prohibit(Enchantment enchantment, Condition condition) {
        prohibitions.computeIfAbsent(enchantment, e -> new ArrayList<>()).add(condition);
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
