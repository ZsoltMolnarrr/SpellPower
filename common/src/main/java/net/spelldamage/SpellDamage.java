package net.spelldamage;

import net.minecraft.util.registry.Registry;
import net.spelldamage.api.Enchantments_SpellDamage;
import net.spelldamage.api.EntityAttributes_SpellDamage;
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


    public static void init() {
        enchantmentConfig.refresh();
    }

    public static void registerAttributes() {
        for(var entry: EntityAttributes_SpellDamage.types.entrySet()) {
            Registry.register(Registry.ATTRIBUTE, entry.getKey().attributeId(), entry.getValue());
        }
    }

    public static void registerEnchantments() {
        Registry.register(Registry.ENCHANTMENT, Enchantments_SpellDamage.spellPowerId, Enchantments_SpellDamage.SPELL_POWER);
    }

    public static void configureEnchantments() {
    }
}