package net.spell_power.api.enchantment;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Function;

public class SpellPowerEnchanting {
    private static final ArrayList<ArmorItem> armor = new ArrayList<>();

    public static void registerArmor(ArmorItem... items) {
        for(var item: items) {
            registerArmor(item);
        }
    }

    public static void registerArmor(List<ArmorItem> items) {
        for(var item: items) {
            registerArmor(item);
        }
    }

    public static void registerArmor(ArmorItem item) {
        armor.add(item);
    }

    public static boolean isArmorRegistered(Item item) {
        return armor.contains(item);
    }

    private static final ArrayList<Function<ItemStack, Boolean>> weaponConditions = new ArrayList<>();

    public static void allowForWeapon(Function<ItemStack, Boolean> condition) {
        weaponConditions.add(condition);
    }

    public static boolean isAllowedForWeapon(ItemStack stack) {
        for (var condition: weaponConditions) {
            if (condition.apply(stack)) {
                return true;
            }
        }
        return false;
    }

    public static EnumSet<MagicSchool> relevantSchools(ItemStack stack, EquipmentSlot slot) {
        var schools = EnumSet.noneOf(MagicSchool.class);
        var attributes = stack.getAttributeModifiers(slot);
        for (var entry: attributes.entries()) {
            var attributeId = Registry.ATTRIBUTE.getId(entry.getKey());
            for (var powerEntry: SpellAttributes.POWER.entrySet()) {
                if (powerEntry.getValue().id.equals(attributeId)) {
                    schools.add(powerEntry.getKey());
                }
            }
        }
        return schools;
    }
}
