package net.spell_power.api.enchantment;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class SpellPowerEnchanting {

    // MARK: Armor
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

    // Defines connection between enchantments and powered schools
    public record AttributeBoost(Enchantment enchantment, BiFunction<Double, Integer, Double> amplifier) { }
    private static final Multimap<MagicSchool, AttributeBoost> powerMap = HashMultimap.create();
    public static void boostSchool(MagicSchool school, Enchantment enchantment, BiFunction<Double, Integer, Double> amplifier) {
        powerMap.put(school, new AttributeBoost(enchantment, amplifier));
    }

    public static Collection<AttributeBoost> boostersFor(MagicSchool school) {
        return powerMap.get(school);
    }

    public static EnumSet<MagicSchool> relevantSchools(ItemStack stack, EquipmentSlot slot) {
        var schools = EnumSet.noneOf(MagicSchool.class);
        var attributes = stack.getAttributeModifiers(slot);
        for (var entry: attributes.entries()) {
            var attributeId = Registries.ATTRIBUTE.getId(entry.getKey());
            for (var powerEntry: SpellAttributes.POWER.entrySet()) {
                if (powerEntry.getValue().id.equals(attributeId)) {
                    schools.add(powerEntry.getKey());
                }
            }
        }
        return schools;
    }

    static {
        for(var entry: Enchantments_SpellPower.damageEnchants.entrySet()) {
            var enchantment = entry.getValue();
            for (var school: enchantment.poweredSchools()) {
                boostSchool(school, enchantment, enchantment::amplify);
            }
        }
        boostSchool(MagicSchool.PHYSICAL_MELEE, Enchantments.SHARPNESS, (value, level) -> value * (1 + ((0.05) * level)));
    }
}
