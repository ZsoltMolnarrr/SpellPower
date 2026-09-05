package net.spell_power;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import net.spell_power.api.enchantment.EnchantmentRestriction;
import net.spell_power.api.enchantment.SpellPowerEnchantments;
import net.spell_power.internals.AmplifierEnchantment;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/// Spell Power's enchantment plumbing on 1.20.1:
/// - applicability decisions (`isAllowed`, `relevantSchools`) used by the enchantment classes and the
///   `Enchantment#isAcceptableItem` / `EnchantmentHelper#getPossibleEntries` mixins on both loaders;
/// - level lookups (`getEnchantmentLevel`, `getEnchantmentLevelEquipmentSum`);
/// - the query-time bonus math that stands in for the modern attribute-modifier enchantment effects.
public final class SpellPowerEnchanting {
    private SpellPowerEnchanting() {}

    // MARK: Applicability

    /// Whether Spell Power permits `enchantment` on `item`: the enchantment's own `isAcceptableItem` rules
    /// (tag + matching-attribute for spell power enchantments) plus the generic `EnchantmentRestriction` overrides.
    public static boolean isAllowed(Enchantment enchantment, ItemStack item) {
        if (EnchantmentRestriction.isProhibited(enchantment, item)) {
            return false;
        }
        if (EnchantmentRestriction.isPermitted(enchantment, item)) {
            return true;
        }
        return enchantment.isAcceptableItem(item);
    }

    /// Schools whose attribute the stack carries as an item attribute modifier (in its natural slot).
    public static Set<SpellSchool> relevantSchools(ItemStack stack) {
        var item = stack.getItem();
        EquipmentSlot slot = EquipmentSlot.MAINHAND;
        if (item instanceof ArmorItem armor) {
            slot = armor.getSlotType();
        }
        return relevantSchools(stack, slot);
    }

    public static Set<SpellSchool> relevantSchools(ItemStack stack, EquipmentSlot slot) {
        var schools = new HashSet<SpellSchool>();
        var attributes = stack.getAttributeModifiers(slot);
        for (var attribute: attributes.keySet()) {
            for (var school: SpellSchools.all()) {
                var entry = school.attributeEntry;
                if (entry != null && entry.value().equals(attribute)) {
                    schools.add(school);
                }
            }
        }
        return schools;
    }

    // MARK: Levels

    /// Effective level of `enchantment` on `entity`: summed over the enchantment's equipment slots when
    /// `allow_stacking` is on (default), otherwise the highest single level (vanilla semantics).
    public static int getEnchantmentLevel(Enchantment enchantment, LivingEntity entity) {
        if (SpellPowerMod.enchantmentConfig.safeValue().allow_stacking) {
            return getEnchantmentLevelEquipmentSum(enchantment, entity);
        } else {
            return EnchantmentHelper.getEquipmentLevel(enchantment, entity);
        }
    }

    /// Sum of levels over the slots the enchantment was declared for (`Enchantment#getEquipment`), so a
    /// MAINHAND-only enchantment never counts armor pieces and vice versa — matching the modern per-slot
    /// attribute effects.
    public static int getEnchantmentLevelEquipmentSum(Enchantment enchantment, LivingEntity entity) {
        int level = 0;
        for (var stack: enchantment.getEquipment(entity).values()) {
            if (stack != null && !stack.isEmpty()) {
                level += EnchantmentHelper.getLevel(enchantment, stack);
            }
        }
        return level;
    }

    // MARK: Query-time bonuses (emulating modern `ADD_MULTIPLIED_BASE` attribute effects)

    /// The attribute-value delta an `ADD_MULTIPLIED_BASE` modifier of `multiplier` would add to `instance`:
    /// `(base + Σ ADDITION) * multiplier * Π (1 + MULTIPLY_TOTAL)`.
    public static double emulatedMultipliedBase(EntityAttributeInstance instance, double multiplier) {
        if (multiplier == 0) {
            return 0;
        }
        double flat = instance.getBaseValue();
        double total = 1;
        for (var modifier: instance.getModifiers()) {
            switch (modifier.getOperation()) {
                case ADDITION -> flat += modifier.getValue();
                case MULTIPLY_TOTAL -> total *= (1 + modifier.getValue());
                default -> { }
            }
        }
        return flat * multiplier * total;
    }

    @Nullable
    private static EntityAttributeInstance instance(LivingEntity entity, @Nullable RegistryEntry<EntityAttribute> attribute) {
        if (attribute == null) {
            return null;
        }
        return entity.getAttributes().getCustomInstance(attribute);
    }

    /// Attribute-value delta the school power enchantments (sunfire / soulfrost / energize / spell_power)
    /// grant on `school`'s attribute for `entity`. Summed as if each were an `ADD_MULTIPLIED_BASE` modifier.
    public static double schoolPowerEnchantmentBonus(SpellSchool school, LivingEntity entity) {
        double multiplier = 0;
        for (var enchantment: SpellPowerEnchantments.schoolPower) {
            if (enchantment.poweredSchools().contains(school)) {
                var level = getEnchantmentLevel(enchantment, entity);
                if (level > 0) {
                    multiplier += enchantment.amplified(level);
                }
            }
        }
        if (multiplier == 0) {
            return 0;
        }
        var instance = instance(entity, school.attributeEntry);
        return instance != null ? emulatedMultipliedBase(instance, multiplier) : 0;
    }

    /// Attribute-value delta a mechanics enchantment (haste / critical chance / critical damage) grants on
    /// its mechanic attribute for `entity` (e.g. 100 * 0.04 * 3 = 12 for Haste III).
    public static double mechanicEnchantmentBonus(SpellPowerMechanics.Entry mechanic, AmplifierEnchantment enchantment, LivingEntity entity) {
        var level = getEnchantmentLevel(enchantment, entity);
        if (level <= 0) {
            return 0;
        }
        var instance = instance(entity, mechanic.attributeEntry);
        return instance != null ? emulatedMultipliedBase(instance, enchantment.amplified(level)) : 0;
    }
}
