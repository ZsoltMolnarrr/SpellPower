package net.spell_power.internals;

import java.util.List;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;

public class AttributeUtil {
    public static boolean attributesIntersect(
            List<EnchantmentAttributeEffect> enchantmentAttributes,
            ItemAttributeModifiers componentAttributes) {
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
