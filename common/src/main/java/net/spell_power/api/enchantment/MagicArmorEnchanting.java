package net.spell_power.api.enchantment;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.EntityAttributes_SpellPower;
import net.spell_power.api.attributes.SpellAttributes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public final class MagicArmorEnchanting {
    private static final ArrayList<ArmorItem> armor = new ArrayList<>();

    public static void register(ArmorItem item) {
        armor.add(item);
    }

    public static void register(ArmorItem... items) {
        for(var item: items) {
            register(item);
        }
    }

    public static void register(List<ArmorItem> items) {
        for(var item: items) {
            register(item);
        }
    }

    public static boolean isRegistered(Item item) {
        return armor.contains(item);
    }

    public static EnumSet<MagicSchool> relevantSchools(ItemStack item, EquipmentSlot slot) {
        var schools = EnumSet.noneOf(MagicSchool.class);
        var attributes = item.getAttributeModifiers(slot);
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
