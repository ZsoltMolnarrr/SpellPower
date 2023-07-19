package net.spell_power.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.spell_power.mixin.DamageSourcesAccessor;

public class SpellDamageSource {
    public static DamageSource create(MagicSchool school, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player) {
            return player(school, player);
        } else {
            return mob(school, attacker);
        }
    }

    public static DamageSource mob(MagicSchool school, LivingEntity attacker) {
        return create(school, "mob", attacker);
    }

    public static DamageSource player(MagicSchool school, PlayerEntity attacker) {
        return create(school, "player", attacker);
    }

    private static DamageSource create(MagicSchool school, String name, Entity attacker) {
        var key = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, school.damageTypeId());
        var registry = ((DamageSourcesAccessor)attacker.getDamageSources()).getRegistry();
        return new DamageSource(registry.entryOf(key), attacker);
    }

    public static boolean isSpell(DamageSource source) {
        return source.isIn(SpellPowerTags.DamageType.IS_SPELL);
    }
}
