package net.spelldamage;

import net.minecraft.util.registry.Registry;
import net.spelldamage.api.Enchantments_SpellDamage;
import net.spelldamage.config.AttributesConfig;
import net.spelldamage.config.EnchantmentsConfig;
import net.spelldamage.config.StatusEffectConfig;
import net.spelldamage.internals.Attributes;
import net.tinyconfig.ConfigManager;

public class SpellDamage {
    public static final String MOD_ID = "spelldamage";

    public static ConfigManager<EnchantmentsConfig> enchantmentConfig = new ConfigManager<EnchantmentsConfig>
            ("enchantments", new EnchantmentsConfig())
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
        for(var entry: Attributes.all.entrySet()) {
            Registry.register(Registry.ATTRIBUTE, entry.getValue().id, entry.getValue().attribute);
        }
    }

    public static void registerEnchantments() {
        for(var entry: Enchantments_SpellDamage.all.entrySet()) {
            Registry.register(Registry.ENCHANTMENT, entry.getKey(), entry.getValue());
        }
    }

    public static void configureEnchantments() {
        enchantmentConfig.value.apply();
    }

    public static void registerStatusEffects() {
        for(var entry: Attributes.all.entrySet()) {
            Registry.register(Registry.STATUS_EFFECT, entry.getValue().id, entry.getValue().statusEffect);
        }
    }
}