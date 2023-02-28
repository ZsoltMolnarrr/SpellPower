package net.spell_power.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Consumer;

import static net.spell_power.api.MagicSchool.FIRE;

public class SpellDamageSource extends EntityDamageSource {
    public static SpellDamageSource create(MagicSchool school, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player) {
            return player(school, player);
        } else {
            return mob(school, attacker);
        }
    }

    public static SpellDamageSource mob(MagicSchool school, LivingEntity attacker) {
        return SpellDamageSource.create(school, "mob", attacker);
    }

    public static SpellDamageSource player(MagicSchool school, PlayerEntity attacker) {
        return SpellDamageSource.create(school, "player", attacker);
    }

    private static SpellDamageSource create(MagicSchool school, String name, Entity source) {
        var damageSource = new SpellDamageSource(name, source, school);
        school.damageSourceConfigurator().accept(damageSource);
        if (school == FIRE) {
            damageSource.setFire();
        }
        return damageSource;
    }

    public static class Configurator {
        public static Consumer<SpellDamageSource> MAGIC = source -> {
            source.setUsesMagic();
            source.setBypassesArmor();
        };

        public static Consumer<SpellDamageSource> MELEE = source -> {
        };
    }

    private MagicSchool school;

    public SpellDamageSource(String name, Entity source, MagicSchool school) {
        super(name, source);
        this.school = school;
    }

    public MagicSchool getMagicSchool() {
        return school;
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
