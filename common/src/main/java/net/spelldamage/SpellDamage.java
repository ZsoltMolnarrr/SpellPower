package net.spelldamage;

import net.minecraft.util.registry.Registry;
import net.spelldamage.api.Enchantments_SpellDamage;
import net.spelldamage.api.EntityAttributes_SpellDamage;
import net.spelldamage.config.AttributesConfig;
import net.spelldamage.config.EnchantmentConfig;
import net.tinyconfig.ConfigManager;

public class SpellDamage {
    public static final String MOD_ID = "spelldamage";

    public static ConfigManager<EnchantmentConfig> enchantmentConfig = new ConfigManager<EnchantmentConfig>
            ("enchantments", new EnchantmentConfig())
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .build();

    public static ConfigManager<AttributesConfig> attributesConfig = new ConfigManager<AttributesConfig>
            ("attributes", new AttributesConfig())
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .build();

    public static void init() {
        enchantmentConfig.refresh();
        attributesConfig.refresh();
    }

    public static void registerAttributes() {
        for(var entry: EntityAttributes_SpellDamage.all.entrySet()) {
            Registry.register(Registry.ATTRIBUTE, entry.getKey(), entry.getValue());
        }
    }

    public static void registerEnchantments() {
        for(var entry: Enchantments_SpellDamage.all.entrySet()) {
            Registry.register(Registry.ENCHANTMENT, entry.getKey(), entry.getValue());
        }
    }

    public static void configureEnchantments() {
        for(var entry: Enchantments_SpellDamage.damageEnchants.entrySet()) {
            var configKey = entry.getKey().getPath();
            var properties = enchantmentConfig.currentConfig.damage_enchantments.get(configKey);
            entry.getValue().properties = properties;
        }
    }
}