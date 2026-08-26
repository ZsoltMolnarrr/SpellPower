package net.spell_power.api;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.spell_power.mixin.DamageSourcesAccessor;

public class SpellDamageSource {
    public static DamageSource create(SpellSchool school, LivingEntity attacker) {
        if (attacker instanceof Player player) {
            return player(school, player);
        } else {
            return mob(school, attacker);
        }
    }

    public static DamageSource mob(SpellSchool school, LivingEntity attacker) {
        return create(school, "mob", attacker);
    }

    public static DamageSource player(SpellSchool school, Player attacker) {
        return create(school, "player", attacker);
    }

    private static DamageSource create(SpellSchool school, String name, Entity attacker) {
        var registry = ((DamageSourcesAccessor)attacker.damageSources()).getDamageTypes();
        return new DamageSource(registry.getOrThrow(school.damageType), attacker);
    }
}
