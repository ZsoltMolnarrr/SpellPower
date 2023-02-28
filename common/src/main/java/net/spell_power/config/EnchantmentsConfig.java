package net.spell_power.config;

import net.spell_power.api.enchantment.Enchantments_SpellPower;
import net.spell_power.api.enchantment.ItemType;
import net.tinyconfig.models.EnchantmentConfig;
import net.tinyconfig.versioning.VersionableConfig;
import org.jetbrains.annotations.Nullable;

import static net.spell_power.api.enchantment.ItemType.MAGICAL_ARMOR;
import static net.spell_power.api.enchantment.ItemType.MAGICAL_WEAPON;

public class EnchantmentsConfig extends VersionableConfig {

    // Fields

    public ExtendedEnchantmentConfig spell_power = new ExtendedEnchantmentConfig(MAGICAL_WEAPON,5, 10, 9, 0.05F);
    public ExtendedEnchantmentConfig soulfrost = new ExtendedEnchantmentConfig(MAGICAL_ARMOR, 5, 10, 9, 0.05F);
    public ExtendedEnchantmentConfig sunfire = new ExtendedEnchantmentConfig(MAGICAL_ARMOR,5, 10, 9, 0.05F);
    public ExtendedEnchantmentConfig energize = new ExtendedEnchantmentConfig(MAGICAL_ARMOR,5, 10, 9, 0.05F)
            .setEnabled(false); // Remove when healer content is added
    public ExtendedEnchantmentConfig critical_chance = new ExtendedEnchantmentConfig(MAGICAL_ARMOR, 5, 10, 12, 0.02F);
    public ExtendedEnchantmentConfig critical_damage = new ExtendedEnchantmentConfig(MAGICAL_ARMOR, 5, 10, 12, 0.1F);
    public ExtendedEnchantmentConfig haste = new ExtendedEnchantmentConfig(MAGICAL_WEAPON, 5, 15, 17, 0.05F);

    public EnchantmentConfig magic_protection = new EnchantmentConfig(4, 3, 6, 2);

    // Helper

    public void apply() {
        Enchantments_SpellPower.SPELL_POWER.config = spell_power;
        Enchantments_SpellPower.SOULFROST.config = soulfrost;
        Enchantments_SpellPower.SUNFIRE.config = sunfire;
        Enchantments_SpellPower.ENERGIZE.config = energize;
        Enchantments_SpellPower.CRITICAL_CHANCE.config = critical_chance;
        Enchantments_SpellPower.CRITICAL_DAMAGE.config = critical_damage;
        Enchantments_SpellPower.HASTE.config = haste;
        Enchantments_SpellPower.MAGIC_PROTECTION.config = magic_protection;
    }

    public static class ExtendedEnchantmentConfig extends EnchantmentConfig {
        @Nullable public ItemType requires;
        public ExtendedEnchantmentConfig(ItemType requires, int max_level, int min_cost, int step_cost, float bonus_per_level) {
            super(max_level, min_cost, step_cost, bonus_per_level);
            this.requires = requires;
        }

        public ExtendedEnchantmentConfig setEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }
    }
}