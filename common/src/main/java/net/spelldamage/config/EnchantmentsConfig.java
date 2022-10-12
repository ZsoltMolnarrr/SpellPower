package net.spelldamage.config;

import net.spelldamage.api.Enchantments_SpellDamage;
import net.tinyconfig.models.EnchantmentConfig;

import java.util.Map;

import static java.util.Map.entry;

public class EnchantmentsConfig {

    // Fields

    public Map<String, EnchantmentConfig> damage_enchantments = Map.ofEntries(
            entry(Enchantments_SpellDamage.spellPowerName, new EnchantmentConfig(5, 10, 9, 0.1F)),
            entry(Enchantments_SpellDamage.soulfrostName, new EnchantmentConfig(5, 10, 9, 0.1F)),
            entry(Enchantments_SpellDamage.sunfireName, new EnchantmentConfig(5, 10, 9, 0.1F))
    );
    public EnchantmentConfig critical_chance = new EnchantmentConfig(5, 10, 12, 0.05F);
    public EnchantmentConfig critical_damage = new EnchantmentConfig(5, 10, 12, 0.1F);
    public EnchantmentConfig haste = new EnchantmentConfig(5, 15, 17, 0.05F);

    // Helper

    public void apply() {
        for(var entry: Enchantments_SpellDamage.damageEnchants.entrySet()) {
            var configKey = entry.getKey().getPath();
            var properties = damage_enchantments.get(configKey);
            entry.getValue().config = properties;
        }
        Enchantments_SpellDamage.CRITICAL_CHANCE.config = critical_chance;
        Enchantments_SpellDamage.CRITICAL_DAMAGE.config = critical_damage;
        Enchantments_SpellDamage.HASTE.config = haste;
    }
}
