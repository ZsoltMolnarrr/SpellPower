package net.spell_power.internals;

import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.enchantment.effect.AttributeEnchantmentEffect;

import java.util.List;

public class AttributeUtil {
    public static boolean attributesIntersect(
            List<AttributeEnchantmentEffect> enchantmentAttributes,
            AttributeModifiersComponent componentAttributes) {
        for (var enchantmentModifier: enchantmentAttributes) {
            for (var componentModifier: componentAttributes.modifiers()) {
                if (enchantmentModifier.attribute().equals(componentModifier.attribute())) {
                    return true;
                }
            }
        }
        return false;
    }
}
