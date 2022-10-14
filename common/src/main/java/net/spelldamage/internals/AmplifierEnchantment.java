package net.spelldamage.internals;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.spelldamage.api.ConditionalEnchantment;
import net.spelldamage.api.MagicalArmor;
import net.spelldamage.api.MagicalItemStack;
import net.spelldamage.config.EnchantmentsConfig;

public class AmplifierEnchantment extends Enchantment implements ConditionalEnchantment {
    public Operation operation;
    public enum Operation {
        ADD, MULTIPLY;
    }

    public EnchantmentsConfig.ExtendedEnchantmentConfig config;

    public double amplify(double value, int level) {
        switch (operation) {
            case ADD -> {
                return value += ((float)level) * config.bonus_per_level;
            }
            case MULTIPLY -> {
                return value *= 1F + ((float)level) * config.bonus_per_level;
            }
        }
        assert true;
        return 0F;
    }

    public AmplifierEnchantment(Rarity weight, Operation operation, EnchantmentsConfig.ExtendedEnchantmentConfig config, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
        this.operation = operation;
        this.config = config;
    }

    public int getMaxLevel() {
        return config.max_level;
    }

    public int getMinPower(int level) {
        return config.min_cost + (level - 1) * config.step_cost;
    }

    public int getMaxPower(int level) {
        return super.getMinPower(level) + 50;
    }

    public boolean isAcceptableItem(ItemStack stack) {
        var requirement = config.requires;
        if (requirement == null) {
            return true;
        }
        switch (requirement) {
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

    protected boolean isValidMagicalStack(ItemStack stack) {
        var object = (Object)stack;
        if (object instanceof MagicalItemStack magicalItemStack) {
            var school = magicalItemStack.getMagicSchool();
            return school != null;
        }
        return false;
    }
}
