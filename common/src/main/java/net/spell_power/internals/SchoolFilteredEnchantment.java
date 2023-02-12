package net.spell_power.internals;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.enchantment.SpellPowerEnchanting;
import net.spell_power.config.EnchantmentsConfig;

import java.util.EnumSet;

public class SchoolFilteredEnchantment extends AmplifierEnchantment {
    private EnumSet<MagicSchool> schools;

    public boolean canBeAppliedFor(MagicSchool givenSchool) {
        return schools.contains(givenSchool);
    }
    
    public SchoolFilteredEnchantment(Rarity weight, Operation operation, EnchantmentsConfig.ExtendedEnchantmentConfig config, EnumSet<MagicSchool> schools, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, operation, config, type, slotTypes);
        this.schools = schools;
        this.setCondition(stack -> {
            var itemTypeRequirement = this.config.requires;
            return itemTypeRequirement == null || (itemTypeRequirement.matches(stack) && schoolsMatch(stack));
        });
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return !(other instanceof SchoolFilteredEnchantment) && super.canAccept(other);
    }

    protected boolean schoolsMatch(ItemStack stack) {
        var item = stack.getItem();
        EquipmentSlot slot = EquipmentSlot.MAINHAND;
        if (item instanceof ArmorItem armor) {
            slot = armor.getSlotType();
        }
        var schools = SpellPowerEnchanting.relevantSchools(stack, slot);
        for (var school : schools) {
            if (this.schools.contains(school)) {
                return true;
            }
        }
        return false;
    }
}