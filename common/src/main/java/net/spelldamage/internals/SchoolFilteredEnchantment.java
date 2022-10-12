package net.spelldamage.internals;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.spelldamage.api.MagicSchool;
import net.spelldamage.api.MagicalItemStack;
import net.tinyconfig.models.EnchantmentConfig;

import java.util.EnumSet;

public class SchoolFilteredEnchantment extends AmplifierEnchantment {
    private EnumSet<MagicSchool> schools;

    public boolean canBeAppliedFor(MagicSchool givenSchool) {
        return schools.contains(givenSchool);
    }
    
    public SchoolFilteredEnchantment(Rarity weight, Operation operation, EnchantmentConfig config, EnumSet<MagicSchool> schools, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, operation, config, type, slotTypes);
        this.schools = schools;
    }

    public boolean isAcceptableItem(ItemStack stack) {
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
