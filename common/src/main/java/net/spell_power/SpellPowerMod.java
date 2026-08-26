package net.spell_power;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.alchemy.Potion;
import net.spell_power.api.*;
import net.spell_power.config.AttributesConfig;
import net.tiny_config.ConfigManager;

import java.util.List;

public class SpellPowerMod {
    public static final String ID = "spell_power";

    public static final ConfigManager<AttributesConfig> attributesConfig = new ConfigManager<>
            ("attributes", AttributesConfig.defaults())
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .validate(AttributesConfig::isValid)
            .build();

    public static void init() {
        // Event wiring lives in the loader entrypoints (FabricMod / NeoForgeMod), which forward to the
        // loader-neutral callbacks below. This keeps `common` free of any loader event API.
    }

    /// Player-join hook: migrate legacy attribute base values when enabled. Wired to
    /// `ServerPlayConnectionEvents.JOIN` on Fabric and `PlayerEvent.PlayerLoggedInEvent` on NeoForge.
    public static void onPlayerJoin(ServerPlayer player) {
        if (attributesConfig.safeValue().migrate_attributes_base) {
            migrateAttributes(player);
        }
    }

    /**
     * For internal use only!
     */
    public static void registerAttributes() {
        for (var entry : SpellPowerMechanics.all.entrySet()) {
            entry.getValue().registerAttribute();
        }
        for(var resistance: SpellResistance.Attributes.all) {
            resistance.registerAttribute();
        }

        for(var school: SpellSchools.all()) {
            school.registerAttribute();
        }
        // Enchantment-applicability restrictions moved to SpellPowerEnchanting; wired per loader
        // (Fabric: EnchantmentEvents.ALLOW_ENCHANTING; NeoForge: IItemExtension#supportsEnchantment mixin).
    }

    /**
     * For internal use only!
     */
    public static void registerStatusEffects() {
        var modifierId = Identifier.fromNamespaceAndPath(ID, "potion_effect");
        var bonus_per_stack = 0.1F;
        for(var school: SpellSchools.all()) {
            var id = school.id;
            if (school.ownedBoostEffect != null && school.attributeEntry != null) {
                school.ownedBoostEffect.addAttributeModifier(
                        school.attributeEntry,
                        modifierId,
                        bonus_per_stack,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

                Registry.register(BuiltInRegistries.MOB_EFFECT, id.toString(), school.ownedBoostEffect);
            }
        }

        var safeConfig = attributesConfig.safeValue();
        for(var entry: SpellPowerMechanics.all.entrySet()) {
            var secondary = entry.getValue();
            var id = secondary.id;

            var config = safeConfig.secondary_effects.get(secondary.name);
            if (config != null) {
                bonus_per_stack = config.bonus_per_stack;
            }
            secondary.boostEffect.addAttributeModifier(
                    secondary.attributeEntry,
                    modifierId,
                    bonus_per_stack,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
            Registry.register(BuiltInRegistries.MOB_EFFECT, id.toString(), secondary.boostEffect);
        }
    }

    public static void registerPotionsInternal() {
        if (attributesConfig.safeValue().register_potions) {
            registerPotions();
        }
    }

    private static boolean potionsRegistered = false;
    public static void registerPotions() {
        if (potionsRegistered) {
            return;
        }
        potionsRegistered = true;

        for(var school: SpellSchools.all()) {
            if (school.archetype == SpellSchool.Archetype.MAGIC
                    && !school.id.getPath().contains("generic")) {
                school.registerPotion();
            }
        }
        for (var secondary: SpellPowerMechanics.all.entrySet()) {
            var mechanic = secondary.getValue();
            var entry = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(mechanic.boostEffect);
            if (entry != null) {
                var potionId = potionIdFrom(mechanic.id);
                var potion = new Potion(potionId.getPath(), new MobEffectInstance(entry, 3600));
                Registry.register(BuiltInRegistries.POTION, potionId, potion);
            }
        }
    }

    public static Identifier potionIdFrom(Identifier id) {
        return Identifier.fromNamespaceAndPath(id.getNamespace(), id.getNamespace() + "." + id.getPath());
    }

    @Deprecated(forRemoval = true)
    public static AttributesConfig.AttributeScope attributeScopeOverride = null;
    @Deprecated(forRemoval = true)
    public static AttributesConfig.AttributeScope attributeScope() {
        return attributeScopeOverride;
        // return attributeScopeOverride != null ? attributeScopeOverride : attributesConfig.value.attributes_container_injection_scope;
    }

    public static void migrateAttributes(ServerPlayer player) {
        var attributes = SpellSchools.all().stream()
                .filter(school -> school.isMagicArchetype() && school.ownsAttribute())
                .map(school -> school.attributeEntry)
                .toList();
        for (var attribute: attributes) {
            if (attribute == null) {
                continue;
            }
            var instance = player.getAttribute(attribute);
            if (instance == null) {
                continue;
            }
            var defaultValue = attribute.value().getDefaultValue();
            if (instance.getBaseValue() != defaultValue) {
                instance.setBaseValue(defaultValue);
            }
        }
    }
}