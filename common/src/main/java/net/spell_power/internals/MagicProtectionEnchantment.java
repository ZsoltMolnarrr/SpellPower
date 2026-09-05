package net.spell_power.internals;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.DamageTypeTags;
import net.spell_power.api.SpellPowerTags;
import net.tiny_config.models.EnchantmentConfig;

import java.util.function.Supplier;

/// Armor protection against magic damage. Damage reduction runs through vanilla's EPF pipeline
/// (`EnchantmentHelper.getProtectionAmount` → `DamageUtil.getInflictedDamage`); this class only reports
/// EPF per level — `bonus_per_level` (2 by default) for `#c:is_magic` sources, mirroring the modern
/// `minecraft:damage_protection` effect with its `c:is_magic` + `!bypasses_invulnerability` predicate.
public class MagicProtectionEnchantment extends ProtectionEnchantment {
    private final Supplier<EnchantmentConfig> config;

    public MagicProtectionEnchantment(Rarity weight, Supplier<EnchantmentConfig> config, EquipmentSlot... slotTypes) {
        super(weight,
                ProtectionEnchantment.Type.ALL, // Ignored due to overrides
                slotTypes);
        this.config = config;
    }

    public EnchantmentConfig config() {
        return config.get();
    }

    @Override
    public int getMinPower(int level) {
        return config().min_cost + (level - 1) * config().step_cost;
    }

    @Override
    public int getMaxPower(int level) {
        return this.getMinPower(level) + config().step_cost;
    }

    @Override
    public int getMaxLevel() {
        if (!config().enabled) {
            return 0;
        }
        return config().max_level;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return super.isAvailableForEnchantedBookOffer() && config().enabled;
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return super.isAvailableForRandomSelection() && config().enabled;
    }

    @Override
    public int getProtectionAmount(int level, DamageSource source) {
        if (source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return 0;
        }
        if (source.isIn(SpellPowerTags.DamageTypes.IS_MAGIC)) {
            return Math.round((float)level * config().bonus_per_level);
        }
        return 0;
    }

    /// Modern `#minecraft:exclusive_set/armor`: incompatible with itself and with the armor protection
    /// family, but Feather Falling may coexist.
    @Override
    public boolean canAccept(Enchantment other) {
        if (other instanceof MagicProtectionEnchantment) {
            return false;
        }
        if (other instanceof ProtectionEnchantment protectionEnchantment) {
            return protectionEnchantment.protectionType == ProtectionEnchantment.Type.FALL;
        }
        return super.canAccept(other);
    }
}
