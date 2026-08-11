package net.spell_power;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.AttributeEnchantmentEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.spell_power.api.SpellPowerTags;
import net.spell_power.internals.AttributeUtil;

import java.util.List;

/// Spell Power's enchantment-applicability restrictions, kept in one loader-neutral place so both
/// loaders enforce identical rules without duplicating the decision logic.
///
/// The rules are exposed as individual conditions plus a combined {@link #isAllowed} so each loader
/// can call whichever it needs: Fabric composes them behind its `EnchantmentEvents.ALLOW_ENCHANTING`
/// handler; NeoForge — which has no global applicability event — calls them from a mixin on
/// `IItemExtension#supportsEnchantment(ItemStack, Holder)`, the single chokepoint the anvil and (via
/// `isPrimaryItemFor`) the enchanting table both funnel through.
public final class SpellPowerEnchanting {
    private SpellPowerEnchanting() {}

    /// Condition: `enchantment` is subject to the "requires matching attribute" restriction — the
    /// feature is enabled, the enchantment is tagged, and it actually grants attributes to match against.
    public static boolean requiresMatchingAttribute(RegistryEntry<Enchantment> enchantment) {
        return SpellPowerMod.attributesConfig.value.enchantments_require_matching_attribute
                && enchantment.isIn(SpellPowerTags.Enchantments.REQUIRES_MATCHING_ATTRIBUTE)
                && !grantedAttributes(enchantment).isEmpty();
    }

    /// Condition: `item` carries at least one attribute that intersects the attributes `enchantment`
    /// would grant. An enchantment that grants no attributes trivially matches.
    public static boolean itemHasMatchingAttribute(RegistryEntry<Enchantment> enchantment, ItemStack item) {
        List<AttributeEnchantmentEffect> enchantmentAttributes = grantedAttributes(enchantment);
        if (enchantmentAttributes.isEmpty()) {
            return true;
        }
        var itemAttributes = item.getComponents().get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        if (itemAttributes == null) {
            return false;
        }
        if (itemAttributes.modifiers().isEmpty()) {
            itemAttributes = item.getItem().getAttributeModifiers();
        }
        return AttributeUtil.attributesIntersect(enchantmentAttributes, itemAttributes);
    }

    /// Whether Spell Power permits `enchantment` on `item`: the conjunction of every restriction rule
    /// (currently only the matching-attribute rule). Both loaders call this so the decision lives in
    /// exactly one place — add future rules here as further `&&` terms.
    public static boolean isAllowed(RegistryEntry<Enchantment> enchantment, ItemStack item) {
        return !requiresMatchingAttribute(enchantment) || itemHasMatchingAttribute(enchantment, item);
    }

    private static List<AttributeEnchantmentEffect> grantedAttributes(RegistryEntry<Enchantment> enchantment) {
        var effects = enchantment.value().effects().get(EnchantmentEffectComponentTypes.ATTRIBUTES);
        return effects != null ? effects : List.of();
    }
}
