package net.spell_damage;

import net.minecraft.util.registry.Registry;
import net.spell_damage.api.enchantment.Enchantments_SpellDamage;
import net.spell_damage.config.AttributesConfig;
import net.spell_damage.config.EnchantmentsConfig;
import net.spell_damage.config.StatusEffectConfig;
import net.spell_damage.internals.Attributes;
import net.tinyconfig.ConfigManager;

public class SpellDamageMod {
    public static final String ID = "spell_damage";

    public static ConfigManager<EnchantmentsConfig> enchantmentConfig = new ConfigManager<EnchantmentsConfig>
            ("enchantments", new EnchantmentsConfig())
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    public static ConfigManager<AttributesConfig> attributesConfig = new ConfigManager<AttributesConfig>
            ("attributes", new AttributesConfig())
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    public static ConfigManager<StatusEffectConfig> effectsConfig = new ConfigManager<StatusEffectConfig>
            ("status_effects", new StatusEffectConfig())
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    public static void init() {
        loadConfig();
    }

    public static void loadConfig() {
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