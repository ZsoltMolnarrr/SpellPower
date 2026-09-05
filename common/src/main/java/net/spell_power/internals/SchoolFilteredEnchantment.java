package net.spell_power.internals;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.spell_power.SpellPowerEnchanting;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.SpellSchool;
import net.spell_power.config.EnchantmentsConfig;

import java.util.Set;
import java.util.function.Supplier;

/// A spell power enchantment boosting the attributes of a set of schools.
public class SchoolFilteredEnchantment extends AmplifierEnchantment {
    private Set<SpellSchool> schools;

    /// Schools whose attribute this enchantment boosts.
    public Set<SpellSchool> poweredSchools() {
        return schools;
    }

    public void setPoweredSchools(Set<SpellSchool> schools) {
        this.schools = schools;
    }

    public SchoolFilteredEnchantment(Rarity weight, Supplier<EnchantmentsConfig.PowerEnchantmentConfig> config, Set<SpellSchool> schools, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, config, type, slotTypes);
        this.schools = schools;
    }

    /// Modern `#spell_power:requires_matching_attribute` semantics: enabled globally and per enchantment.
    public boolean requiresRelatedAttributes() {
        return SpellPowerMod.attributesConfig.safeValue().enchantments_require_matching_attribute
                && ((EnchantmentsConfig.PowerEnchantmentConfig) config()).requires_related_attributes;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return super.isAcceptableItem(stack)
                && (!requiresRelatedAttributes() || schoolsIntersect(schools, stack));
    }

    public static boolean schoolsIntersect(Set<SpellSchool> schools, ItemStack stack) {
        var itemStackSchools = SpellPowerEnchanting.relevantSchools(stack);
        for (var school : itemStackSchools) {
            if (schools.contains(school)) {
                return true;
            }
        }
        return false;
    }
}
