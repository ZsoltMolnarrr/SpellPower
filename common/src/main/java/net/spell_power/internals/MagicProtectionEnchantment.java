package net.spell_power.internals;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.tinyconfig.models.EnchantmentConfig;

public class MagicProtectionEnchantment extends ProtectionEnchantment {
    public EnchantmentConfig config;
    public MagicProtectionEnchantment(Rarity weight, EnchantmentConfig config, EquipmentSlot... slotTypes) {
        super(weight,
                ProtectionEnchantment.Type.ALL, // Ignored due to overrides
                slotTypes);
        this.config = config;
    }
    @Override
    public int getMinPower(int level) {
        return config.min_cost + (level - 1) * config.step_cost;
    }

    @Override
    public int getMaxPower(int level) {
        return this.getMinPower(level) + config.step_cost;
    }

    @Override
    public int getMaxLevel() {
        return config.max_level;
    }

    @Override
    public int getProtectionAmount(int level, DamageSource source) {
        if (source.getType().equals(DamageTypes.OUT_OF_WORLD)) {
            return 0;
        }
        if (source.getType().equals(DamageTypes.MAGIC)) {
            return Math.round((float)level * config.bonus_per_level);
        }
        return 0;
    }

    @Override
    public boolean canAccept(Enchantment other) {
        if (other instanceof MagicProtectionEnchantment) {
            return false;
        }
        if (other instanceof ProtectionEnchantment) {
            ProtectionEnchantment protectionEnchantment = (ProtectionEnchantment)other;
//            if (this.protectionType == protectionEnchantment.protectionType) {
//                return false;
//            }
            return this.protectionType == ProtectionEnchantment.Type.FALL || protectionEnchantment.protectionType == ProtectionEnchantment.Type.FALL;
        }
        return super.canAccept(other);
    }
}
