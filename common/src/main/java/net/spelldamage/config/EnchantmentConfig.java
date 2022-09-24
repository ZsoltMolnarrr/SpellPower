package net.spelldamage.config;

import net.spelldamage.api.Enchantments_SpellDamage;
import net.spelldamage.enchantments.AmplifierEnchantment;

import java.util.Map;

import static java.util.Map.entry;

public class EnchantmentConfig {

    // Fields

    public Map<String, AmplifierEnchantment.Properties> damage_enchantments = Map.ofEntries(
            entry(Enchantments_SpellDamage.spellPowerName, new AmplifierEnchantment.Properties(5, 10, 9, 0.1F)),
            entry(Enchantments_SpellDamage.soulfrostName, new AmplifierEnchantment.Properties(5, 10, 9, 0.1F))
    );
    public AmplifierEnchantment.Properties critical_chance = new AmplifierEnchantment.Properties(5, 10, 9, 0.1F);
    public AmplifierEnchantment.Properties critical_damage = new AmplifierEnchantment.Properties(5, 10, 9, 0.1F);

    // Helper

    public void apply() {
        for(var entry: Enchantments_SpellDamage.damageEnchants.entrySet()) {
            var configKey = entry.getKey().getPath();
            var properties = damage_enchantments.get(configKey);
            entry.getValue().properties = properties;
        }
        Enchantments_SpellDamage.CRITICAL_CHANCE.properties = critical_chance;
        Enchantments_SpellDamage.CRITICAL_DAMAGE.properties = critical_damage;
    }
}
