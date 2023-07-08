package net.spell_power.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.function.Consumer;

import static net.spell_power.api.MagicSchool.FIRE;

public class SpellDamageSource extends DamageSource { 
    private static DamageSources item;    

    public static SpellDamageSource create(MagicSchool school, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player) {
            return player(school, player);
        } else {
            return mob(school, attacker);
        }
    }

    public static SpellDamageSource mob(MagicSchool school, LivingEntity attacker) {
        return SpellDamageSource.create(school, attacker, (RegistryEntry<DamageType>) DamageTypes.MAGIC);
    }

    public static SpellDamageSource player(MagicSchool school, PlayerEntity attacker) {
        return SpellDamageSource.create(school, attacker, (RegistryEntry<DamageType>) DamageTypes.MAGIC);
    }

    private static SpellDamageSource create(MagicSchool school, Entity attacker, RegistryEntry<DamageType> type) {
        var damageSource = new SpellDamageSource(type, attacker, school);
        school.damageSourceConfigurator().accept(damageSource);
        if (school == FIRE) {
            item.onFire();
        }
        return damageSource;
    }

    public static class Configurator {
        public static Consumer<SpellDamageSource> MAGIC = source -> {
            item.magic();
        };

        public static Consumer<SpellDamageSource> MELEE = source -> {
        };
    }

    private MagicSchool school;

    public SpellDamageSource(RegistryEntry<DamageType> type, Entity attacker, MagicSchool school) {
        super(type, attacker);
        this.school = school;
    }

    public MagicSchool getMagicSchool() {
        return school;
    }
}
