package net.spell_power;

import net.minecraft.util.registry.Registry;
import net.spell_power.api.enchantment.Enchantments_SpellPower;
import net.spell_power.config.AttributesConfig;
import net.spell_power.config.EnchantmentsConfig;
import net.spell_power.config.StatusEffectConfig;
import net.spell_power.api.attributes.SpellAttributes;
import net.tinyconfig.ConfigManager;

public class SpellPowerMod {
    public static final String ID = "spell_power";

    public static final ConfigManager<EnchantmentsConfig> enchantmentConfig = new ConfigManager<EnchantmentsConfig>
            ("enchantments", new EnchantmentsConfig())
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    public static final ConfigManager<AttributesConfig> attributesConfig = new ConfigManager<AttributesConfig>
            ("attributes", new AttributesConfig())
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    public static final ConfigManager<StatusEffectConfig> effectsConfig = new ConfigManager<StatusEffectConfig>
            ("status_effects", new StatusEffectConfig())
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .validate(StatusEffectConfig::isValid)
            .build();

    public static void init() {
        loadConfig();
        configureStatusEffects();
    }

    public static void loadConfig() {
        enchantmentConfig.refresh();
        attributesConfig.refresh();
        effectsConfig.refresh();
    }

    private static boolean registeredAttributes = false;
    public static void registerAttributes() {
        if (registeredAttributes) {
            return;
        }
        attributesConfig.refresh();
        for(var entry: SpellAttributes.all.entrySet()) {
            Registry.register(Registry.ATTRIBUTE, entry.getValue().id, entry.getValue().attribute);
        }
        registeredAttributes = true;
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
        for(var entry: SpellAttributes.all.entrySet()) {
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

    public static void configureStatusEffects() {
        for (var entry: SpellAttributes.all.entrySet()) {
            var config = effectsConfig.value.effects.get(entry.getKey());
            var attribute = entry.getValue();
            attribute.setupStatusEffect(config);
        }
    }
}