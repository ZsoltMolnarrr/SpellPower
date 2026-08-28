package net.spell_power;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
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
    public static boolean requiresMatchingAttribute(Holder<Enchantment> enchantment) {
        return SpellPowerMod.attributesConfig.safeValue().enchantments_require_matching_attribute
                && enchantment.is(SpellPowerTags.Enchantments.REQUIRES_MATCHING_ATTRIBUTE)
                && !grantedAttributes(enchantment).isEmpty();
    }

    /// Condition: `item` carries at least one attribute that intersects the attributes `enchantment`
    /// would grant. An enchantment that grants no attributes trivially matches.
    public static boolean itemHasMatchingAttribute(Holder<Enchantment> enchantment, ItemStack item) {
        List<EnchantmentAttributeEffect> enchantmentAttributes = grantedAttributes(enchantment);
        if (enchantmentAttributes.isEmpty()) {
            return true;
        }
        var itemAttributes = item.getComponents().get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (itemAttributes == null) {
            return false;
        }
        if (itemAttributes.modifiers().isEmpty()) {
            itemAttributes = item.getItem().components().getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
        }
        return AttributeUtil.attributesIntersect(enchantmentAttributes, itemAttributes);
    }

    /// Whether Spell Power permits `enchantment` on `item`: the conjunction of every restriction rule
    /// (currently only the matching-attribute rule). Both loaders call this so the decision lives in
    /// exactly one place — add future rules here as further `&&` terms.
    public static boolean isAllowed(Holder<Enchantment> enchantment, ItemStack item) {
        return !requiresMatchingAttribute(enchantment) || itemHasMatchingAttribute(enchantment, item);
    }

    private static List<EnchantmentAttributeEffect> grantedAttributes(Holder<Enchantment> enchantment) {
        var effects = enchantment.value().effects().get(EnchantmentEffectComponents.ATTRIBUTES);
        return effects != null ? effects : List.of();
    }
}
