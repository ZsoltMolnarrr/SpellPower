package net.spelldamage.internals;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.spelldamage.api.MagicSchool;
import net.spelldamage.api.enchantment.MagicalItemStack;
import net.spelldamage.config.EnchantmentsConfig;

import java.util.EnumSet;

public class SchoolFilteredEnchantment extends AmplifierEnchantment {
    private EnumSet<MagicSchool> schools;

    public boolean canBeAppliedFor(MagicSchool givenSchool) {
        return schools.contains(givenSchool);
    }
    
    public SchoolFilteredEnchantment(Rarity weight, Operation operation, EnchantmentsConfig.ExtendedEnchantmentConfig config, EnumSet<MagicSchool> schools, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, operation, config, type, slotTypes);
        this.schools = schools;
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return !(other instanceof SchoolFilteredEnchantment) && super.canAccept(other);
    }

    @Override
    protected boolean isValidMagicalStack(ItemStack stack) {
        var object = (Object)stack;
        if (object instanceof MagicalItemStack magicalItemStack) {
            var school = magicalItemStack.getMagicSchool();
            if (school != null) {
                return schools.contains(school);
            }
        }
        return false;
    }
}
