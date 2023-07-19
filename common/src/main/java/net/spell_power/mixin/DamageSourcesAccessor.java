package net.spell_power.mixin;

import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DamageSources.class)
public interface DamageSourcesAccessor {
    @Accessor
    Registry<DamageType> getRegistry();
}
