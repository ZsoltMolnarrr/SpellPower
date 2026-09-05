package net.spell_power.internals;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.item.Item;
import net.tiny_config.models.EnchantmentConfig;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/// A level-scaled bonus enchantment whose effect is read at query time (`amplified(level)`), not applied
/// as an attribute modifier. Applicability = required item tag (+ subclass rules); `EnchantmentTarget`
/// is only the permissive vanilla placeholder.
public class AmplifierEnchantment extends Enchantment {
    /// Config is resolved lazily so the enchantment can be constructed in static initializers before the
    /// config file is read (TinyConfig `safeValue()` loads it on first access).
    private final Supplier<? extends EnchantmentConfig> config;
    @Nullable protected TagKey<Item> requiredTag;
    /// Enchantments sharing a non-null group are mutually exclusive (modern exclusive-set tags).
    @Nullable protected String exclusiveGroup;

    public AmplifierEnchantment(Rarity weight, Supplier<? extends EnchantmentConfig> config, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
        this.config = config;
    }

    public EnchantmentConfig config() {
        return config.get();
    }

    /// The multiplier bonus `level` grants: `level * bonus_per_level` (e.g. 3 * 0.04 = 0.12).
    public double amplified(int level) {
        return ((double) level) * config().bonus_per_level;
    }

    public AmplifierEnchantment requireTag(TagKey<Item> tag) {
        this.requiredTag = tag;
        return this;
    }

    public AmplifierEnchantment exclusiveGroup(String group) {
        this.exclusiveGroup = group;
        return this;
    }

    public boolean matchesRequiredTag(ItemStack stack) {
        return requiredTag == null || stack.isIn(requiredTag);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return config().enabled && matchesRequiredTag(stack);
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return super.isAvailableForEnchantedBookOffer() && config().enabled;
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return super.isAvailableForRandomSelection() && config().enabled;
    }

    @Override
    public int getMaxLevel() {
        if (!config().enabled) {
            return 0;
        }
        return config().max_level;
    }

    @Override
    public int getMinPower(int level) {
        return config().min_cost + (level - 1) * config().step_cost;
    }

    @Override
    public int getMaxPower(int level) {
        return getMinPower(level) + config().step_cost;
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        if (exclusiveGroup != null
                && other instanceof AmplifierEnchantment amplifier
                && exclusiveGroup.equals(amplifier.exclusiveGroup)) {
            return false;
        }
        return super.canAccept(other);
    }
}
