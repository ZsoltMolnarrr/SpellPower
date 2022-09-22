package net.spelldamage;

import net.minecraft.util.registry.Registry;
import net.spelldamage.api.Enchantments_SpellDamage;
import net.spelldamage.api.EntityAttributes_SpellDamage;

public class SpellDamage {
    public static final String MOD_ID = "spelldamage";

    public static void init() {
    }

    public static void registerAttributes() {
        for(var entry: EntityAttributes_SpellDamage.types.entrySet()) {
            Registry.register(Registry.ATTRIBUTE, entry.getKey().attributeId(), entry.getValue());
        }
    }

    public static void registerEnchantments() {
        Registry.register(Registry.ENCHANTMENT, Enchantments_SpellDamage.spellPowerId, Enchantments_SpellDamage.SPELL_POWER);
    }
}