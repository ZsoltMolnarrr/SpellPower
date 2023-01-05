package net.spell_power;

import net.minecraft.util.registry.Registry;
import net.spell_power.api.enchantment.Enchantments_SpellPower;
import net.spell_power.config.AttributesConfig;
import net.spell_power.config.EnchantmentsConfig;
import net.spell_power.config.StatusEffectConfig;
import net.spell_power.internals.Attributes;
import net.tinyconfig.ConfigManager;

public class SpellPowerMod {
    public static final String ID = "spell_power";

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
        for(var entry: Enchantments_SpellPower.all.entrySet()) {
            Registry.register(Registry.ENCHANTMENT, entry.getKey(), entry.getValue());
        }
    }

    public static void configureEnchantments() {
        enchantmentConfig.value.apply();
    }

    public static void registerStatusEffects() {
        for(var entry: Attributes.all.entrySet()) {
            var rawId = entry.getValue().statusEffect.preferredRawId;
            var id = entry.getValue().id;
            var effect = entry.getValue().statusEffect;
            if (rawId > 0) {
                Registry.register(Registry.STATUS_EFFECT, rawId, id.toString(), effect);
            } else {
                Registry.register(Registry.STATUS_EFFECT, id, effect);
            }
        }
    }
}