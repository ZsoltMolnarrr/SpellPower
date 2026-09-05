package net.spell_power.config;

import net.tiny_config.models.EnchantmentConfig;

/// Enchantment tuning (`config/spell_power/enchantments.json`). Defaults reproduce the modern (1.21.1)
/// data-driven definitions: level bonuses, max levels and level costs from `SpellPowerModDataGenerator`.
///
/// Costs: the enchanting-table window for a level is `[min_cost + (level-1) * step_cost, that + step_cost]`
/// (modern `leveledCost(min, step)` for the minimum; the maximum is one step above it).
public class EnchantmentsConfig {
    /// Sum enchantment levels across all equipped slots (true) or take the highest single level (false).
    public boolean allow_stacking = true;

    public PowerEnchantmentConfig spell_power = new PowerEnchantmentConfig(false, 5, 1, 11, 0.05F);
    public PowerEnchantmentConfig sunfire = new PowerEnchantmentConfig(true, 5, 1, 11, 0.03F);
    public PowerEnchantmentConfig soulfrost = new PowerEnchantmentConfig(true, 5, 1, 11, 0.03F);
    public PowerEnchantmentConfig energize = new PowerEnchantmentConfig(true, 5, 1, 11, 0.03F);

    public EnchantmentConfig haste = new EnchantmentConfig(5, 5, 12, 0.04F);
    public EnchantmentConfig critical_chance = new EnchantmentConfig(5, 5, 12, 0.04F);
    public EnchantmentConfig critical_damage = new EnchantmentConfig(5, 5, 12, 0.1F);
    /// `bonus_per_level` is the armor EPF (enchantment protection factor) per level against magic damage.
    public EnchantmentConfig magic_protection = new EnchantmentConfig(4, 3, 6, 2);

    public static class PowerEnchantmentConfig extends EnchantmentConfig {
        /// Only enchantable onto items that already carry one of the schools' attributes
        /// (modern `#spell_power:requires_matching_attribute`). Also gated by the global
        /// `attributes.json` → `enchantments_require_matching_attribute` switch.
        public boolean requires_related_attributes = false;

        public PowerEnchantmentConfig() { }

        public PowerEnchantmentConfig(boolean requires_related_attributes, int max_level, int min_cost, int step_cost, float bonus_per_level) {
            super(max_level, min_cost, step_cost, bonus_per_level);
            this.requires_related_attributes = requires_related_attributes;
        }
    }

    public boolean isValid() {
        return spell_power != null && sunfire != null && soulfrost != null && energize != null
                && haste != null && critical_chance != null && critical_damage != null && magic_protection != null;
    }
}
