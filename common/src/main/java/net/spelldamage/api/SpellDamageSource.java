package net.spelldamage.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;

import static net.spelldamage.api.MagicSchool.FIRE;

public class SpellDamageSource extends EntityDamageSource {
    public static SpellDamageSource create(MagicSchool school, Entity source) {
        var damageSource = new SpellDamageSource(school.spellName(), source);
        damageSource.setUsesMagic();
        if (school == FIRE) {
            damageSource.setFire();
        }
        damageSource.setBypassesArmor();
        damageSource.setUnblockable();
        return damageSource;
    }

    public SpellDamageSource(String name, Entity source) {
        super(name, source);
    }

    @Override
    public DamageSource setFire() {
        return super.setFire();
    }

    @Override
    public DamageSource setBypassesArmor() {
        return super.setBypassesArmor();
    }

    @Override
    public DamageSource setUnblockable() {
        return super.setUnblockable();
    }
}
