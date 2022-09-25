package net.spelldamage;

import net.minecraft.util.registry.Registry;
import net.spelldamage.api.Enchantments_SpellDamage;
import net.spelldamage.api.EntityAttributes_SpellDamage;
import net.spelldamage.api.StatusEffects_SpellDamage;
import net.spelldamage.config.AttributesConfig;
import net.spelldamage.config.EnchantmentConfig;
import net.spelldamage.config.StatusEffectConfig;
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

    public static ConfigManager<StatusEffectConfig> effectsConfig = new ConfigManager<StatusEffectConfig>
            ("status_effects", new StatusEffectConfig())
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .build();

    public static void init() {
        enchantmentConfig.refresh();
        attributesConfig.refresh();
        effectsConfig.refresh();
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
        enchantmentConfig.currentConfig.apply();
    }

    public static void registerStatusEffects() {
        for(var entry: StatusEffects_SpellDamage.all.entrySet()) {
            Registry.register(Registry.STATUS_EFFECT, entry.getKey(), entry.getValue());
        }
    }
}