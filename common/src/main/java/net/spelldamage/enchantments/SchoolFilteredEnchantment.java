package net.spelldamage.enchantments;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.spelldamage.api.MagicSchool;

import java.util.EnumSet;

public class SchoolFilteredEnchantment extends AmplifierEnchantment {
    private EnumSet<MagicSchool> schools;

    public boolean canBeAppliedFor(MagicSchool givenSchool) {
        return schools.contains(givenSchool);
    }
    
    public SchoolFilteredEnchantment(Rarity weight, Operation operation, Properties properties, EnumSet<MagicSchool> schools, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, operation, properties, type, slotTypes);
        this.schools = schools;
    }
}
