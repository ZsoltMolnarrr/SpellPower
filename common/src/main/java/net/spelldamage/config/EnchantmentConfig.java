package net.spelldamage.config;

import net.spelldamage.api.Enchantments_SpellDamage;
import net.spelldamage.enchantments.AmplifierEnchantment;

import java.util.Map;

import static java.util.Map.entry;

public class EnchantmentConfig {
    public Map<String, AmplifierEnchantment.Properties> damage_enchantments = Map.ofEntries(
            entry(Enchantments_SpellDamage.spellPowerName, new AmplifierEnchantment.Properties(5, 10, 9, 0.1F)),
            entry(Enchantments_SpellDamage.soulfrostName, new AmplifierEnchantment.Properties(5, 10, 9, 0.1F))
    );
}
