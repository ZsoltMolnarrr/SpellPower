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

@Mod(SpellPowerMod.ID)
public final class ForgeMod {

    // FMLJavaModLoadingContext.get() is flagged for removal by late 47.x builds, but the
    // constructor-injected replacement doesn't exist on early 47.x; get() works on all of [47,).
    @SuppressWarnings("removal")
    public ForgeMod() {
        SpellPowerMod.init();

        var modBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Forge locks vanilla registries outside their RegisterEvent window ("Can not register to a locked
        // registry"), so the Fabric `<clinit>`-TAIL mixins are not shipped on Forge; the same idempotent common
        // registration functions run from the per-registry event instead. Inside the window the vanilla wrapper
        // registry delegates `Registry.register` into the ForgeRegistry, so the common code needs no Forge API.
        // Explicit event class: Forge 47's plain addListener(Consumer) infers the event type from the lambda via
        // TypeTools, which is fragile; the 4-arg overload takes it directly.
        modBus.addListener(EventPriority.NORMAL, false, RegisterEvent.class, event -> {
            var key = event.getRegistryKey();
            if (key.equals(ForgeRegistries.Keys.ATTRIBUTES)) {
                SpellPowerMod.registerAttributes();
            } else if (key.equals(ForgeRegistries.Keys.MOB_EFFECTS)) {
                SpellPowerMod.registerStatusEffects();
            } else if (key.equals(ForgeRegistries.Keys.POTIONS)) {
                SpellPowerMod.registerPotionsInternal();
            } else if (key.equals(ForgeRegistries.Keys.ENCHANTMENTS)) {
                SpellPowerMod.registerEnchantments();
            }
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
