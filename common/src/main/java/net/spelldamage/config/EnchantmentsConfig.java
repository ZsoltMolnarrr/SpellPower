package net.spelldamage.config;

import net.spelldamage.api.Enchantments_SpellDamage;
import net.tinyconfig.models.EnchantmentConfig;
import org.jetbrains.annotations.Nullable;

import static net.spelldamage.config.EnchantmentsConfig.ExtendedEnchantmentConfig.Requirement.MAGICAL_ARMOR;
import static net.spelldamage.config.EnchantmentsConfig.ExtendedEnchantmentConfig.Requirement.MAGICAL_WEAPON;

public class EnchantmentsConfig {

    // Fields

    public ExtendedEnchantmentConfig spell_power = new ExtendedEnchantmentConfig(MAGICAL_WEAPON,5, 10, 9, 0.1F);
    public ExtendedEnchantmentConfig soulfrost = new ExtendedEnchantmentConfig(MAGICAL_ARMOR, 5, 10, 9, 0.1F);
    public ExtendedEnchantmentConfig sunfire = new ExtendedEnchantmentConfig(MAGICAL_ARMOR,5, 10, 9, 0.1F);
    public ExtendedEnchantmentConfig energize = new ExtendedEnchantmentConfig(MAGICAL_ARMOR,5, 10, 9, 0.1F);
    public ExtendedEnchantmentConfig critical_chance = new ExtendedEnchantmentConfig(MAGICAL_ARMOR, 5, 10, 12, 0.05F);
    public ExtendedEnchantmentConfig critical_damage = new ExtendedEnchantmentConfig(MAGICAL_ARMOR, 5, 10, 12, 0.1F);
    public ExtendedEnchantmentConfig haste = new ExtendedEnchantmentConfig(MAGICAL_WEAPON, 5, 15, 17, 0.05F);

    // Helper

    public void apply() {
        Enchantments_SpellDamage.SPELL_POWER.config = spell_power;
        Enchantments_SpellDamage.SOULFROST.config = soulfrost;
        Enchantments_SpellDamage.SUNFIRE.config = sunfire;
        Enchantments_SpellDamage.ENERGIZE.config = energize;
        Enchantments_SpellDamage.CRITICAL_CHANCE.config = critical_chance;
        Enchantments_SpellDamage.CRITICAL_DAMAGE.config = critical_damage;
        Enchantments_SpellDamage.HASTE.config = haste;
    }

    public static class ExtendedEnchantmentConfig extends EnchantmentConfig {
        @Nullable public Requirement requires;
        public enum Requirement {
            ARMOR, MAGICAL_ARMOR, MAGICAL_WEAPON
        }
        public ExtendedEnchantmentConfig(Requirement requires, int max_level, int min_cost, int step_cost, float bonus_per_level) {
            super(max_level, min_cost, step_cost, bonus_per_level);
            this.requires = requires;
        }
    }
}
