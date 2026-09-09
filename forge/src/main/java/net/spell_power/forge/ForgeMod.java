package net.spell_power.forge;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import net.spell_power.SpellPowerMod;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellResistance;
import net.spell_power.api.SpellSchools;
import net.spell_power.api.enchantment.SpellPowerEnchantments;

@Mod(SpellPowerMod.ID)
public final class ForgeMod {

    // FMLJavaModLoadingContext.get() is flagged for removal by late 47.x builds, but the
    // constructor-injected replacement doesn't exist on early 47.x; get() works on all of [47,).
    @SuppressWarnings("removal")
    public ForgeMod() {
        SpellPowerMod.init();

        var modBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Registration is duplicated here rather than delegated to `common`'s registerX() methods, because a
        // plain `Registry.register` is not usable on this loader: Forge only clears the vanilla registry's own
        // lock from 47.4.0 onwards, so on 47.0-47.3 and NeoForge 1.20.1 it throws "Can not register to a locked
        // registry" even inside the correct RegisterEvent window. The helper this event hands out is the API
        // every build of [47,) sanctions, so Forge iterates the same content `common` exposes and registers it
        // itself. `common` keeps its own vanilla-shaped registration for Fabric.
        //
        // `event.register` is a no-op unless its key matches the event's registry, so all four blocks are
        // declared unconditionally; Forge posts one event per registry and each block runs in exactly its own.
        //
        // Explicit event class: Forge 47's plain addListener(Consumer) infers the event type from the lambda via
        // TypeTools, which is fragile; the 4-arg overload takes it directly.
        modBus.addListener(EventPriority.NORMAL, false, RegisterEvent.class, event -> {
            event.register(ForgeRegistries.Keys.ATTRIBUTES, helper -> {
                for (var mechanic : SpellPowerMechanics.all.values()) {
                    helper.register(mechanic.id, mechanic.attribute);
                }
                for (var resistance : SpellResistance.Attributes.all) {
                    helper.register(resistance.id, resistance.attribute);
                }
                for (var school : SpellSchools.all()) {
                    if (school.ownsAttribute()) {
                        helper.register(school.id, school.ownedAttribute());
                    }
                }
                // The helper returns void, so the RegistryEntry fields gameplay reads are filled in afterwards.
                SpellPowerMod.linkAttributeEntries();
            });

            event.register(ForgeRegistries.Keys.MOB_EFFECTS, helper -> {
                SpellPowerMod.configureStatusEffects();
                for (var school : SpellSchools.all()) {
                    if (school.ownedBoostEffect != null && school.ownedAttribute() != null) {
                        helper.register(school.id, school.ownedBoostEffect);
                    }
                }
                for (var mechanic : SpellPowerMechanics.all.values()) {
                    helper.register(mechanic.id, mechanic.boostEffect);
                }
            });

            event.register(ForgeRegistries.Keys.POTIONS, helper -> {
                if (SpellPowerMod.attributesConfig.safeValue().register_potions) {
                    SpellPowerMod.potionsToRegister().forEach(helper::register);
                }
            });

            event.register(ForgeRegistries.Keys.ENCHANTMENTS, helper ->
                    SpellPowerEnchantments.all.forEach(helper::register));
        });

        // Replaces Fabric's `createLivingAttributes` RETURN mixin: fired after registration, once every living
        // entity type's default attributes are built.
        modBus.addListener(EventPriority.NORMAL, false, EntityAttributeModificationEvent.class, event -> {
            var attributes = SpellPowerMod.attributesToAttach();
            for (var entityType : event.getTypes()) {
                for (var attribute : attributes) {
                    if (!event.has(entityType, attribute)) {
                        event.add(entityType, attribute);
                    }
                }
            }
        });

        // Player-join attribute migration (replaces Fabric's ServerPlayConnectionEvents.JOIN).
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, PlayerEvent.PlayerLoggedInEvent.class, event -> {
            if (event.getEntity() instanceof ServerPlayerEntity player) {
                SpellPowerMod.onPlayerJoin(player);
            }
        });

        // Enchantment applicability (tags, matching attributes, EnchantmentRestriction) is enforced by the
        // loader-neutral EnchantmentMixin + EnchantmentHelperMixin pair from the common mixin config.
    }
}
