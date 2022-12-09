package net.spell_damage.internals;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.spell_damage.api.enchantment.CustomConditionalEnchantment;
import net.spell_damage.config.EnchantmentsConfig;

public class AmplifierEnchantment extends Enchantment implements CustomConditionalEnchantment {
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
        this.setCondition(stack -> {
            var itemTypeRequirement = this.config.requires;
            return itemTypeRequirement == null || itemTypeRequirement.matches(stack);
        });
    }

    public int getMaxLevel() {
        if (!config.enabled) {
            return 0;
        }
        return config.max_level;
    }

    public int getMinPower(int level) {
        return config.min_cost + (level - 1) * config.step_cost;
    }

    public int getMaxPower(int level) {
        return super.getMinPower(level) + 50;
    }

    // MARK: CustomConditionalEnchantment

    private Condition condition;

    @Override
    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        if (condition != null) {
            return condition.isAcceptableItem(stack);
        }
        return super.isAcceptableItem(stack);
    }
}
