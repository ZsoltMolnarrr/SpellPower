package net.spelldamage.internals;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.tinyconfig.models.EnchantmentConfig;

public class AmplifierEnchantment extends Enchantment {
    public Operation operation;
    public enum Operation {
        ADD, MULTIPLY;
    }

    public EnchantmentConfig config;

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

    public AmplifierEnchantment(Rarity weight, Operation operation, EnchantmentConfig config, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
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
}
