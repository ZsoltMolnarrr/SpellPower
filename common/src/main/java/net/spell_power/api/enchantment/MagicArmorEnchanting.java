package net.spell_power.api.enchantment;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;

import java.util.ArrayList;
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
}
