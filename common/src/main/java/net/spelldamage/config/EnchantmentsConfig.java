package net.spelldamage.config;

import net.spelldamage.api.Enchantments_SpellDamage;
import net.tinyconfig.models.EnchantmentConfig;

import java.util.Map;

import static java.util.Map.entry;

public class EnchantmentsConfig {

    // Fields
    public EnchantmentConfig spellPower = new EnchantmentConfig(5, 10, 9, 0.1F);
    public EnchantmentConfig soulfrost = new EnchantmentConfig(5, 10, 9, 0.1F);
    public EnchantmentConfig sunfire = new EnchantmentConfig(5, 10, 9, 0.1F);
    public EnchantmentConfig energize = new EnchantmentConfig(5, 10, 9, 0.1F);
    public EnchantmentConfig critical_chance = new EnchantmentConfig(5, 10, 12, 0.05F);
    public EnchantmentConfig critical_damage = new EnchantmentConfig(5, 10, 12, 0.1F);
    public EnchantmentConfig haste = new EnchantmentConfig(5, 15, 17, 0.05F);

    // Helper

    public void apply() {
        Enchantments_SpellDamage.SPELL_POWER.config = spellPower;
        Enchantments_SpellDamage.SOULFROST.config = soulfrost;
        Enchantments_SpellDamage.SUNFIRE.config = sunfire;
        Enchantments_SpellDamage.ENERGIZE.config = energize;
        Enchantments_SpellDamage.CRITICAL_CHANCE.config = critical_chance;
        Enchantments_SpellDamage.CRITICAL_DAMAGE.config = critical_damage;
        Enchantments_SpellDamage.HASTE.config = haste;
    }
}
