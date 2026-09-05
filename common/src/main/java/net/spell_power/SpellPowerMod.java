package net.spell_power;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.spell_power.api.*;
import net.spell_power.api.enchantment.SpellPowerEnchantments;
import net.spell_power.config.AttributesConfig;
import net.spell_power.config.EnchantmentsConfig;
import net.tiny_config.ConfigManager;

import java.util.ArrayList;
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

    public static final ConfigManager<EnchantmentsConfig> enchantmentConfig = new ConfigManager<>
            ("enchantments", new EnchantmentsConfig())
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .validate(EnchantmentsConfig::isValid)
            .build();

    public static void init() {
        // Event wiring lives in the loader entrypoints (FabricMod / ForgeMod), which forward to the
        // loader-neutral callbacks below. This keeps `common` free of any loader event API.
        //
        // Registration seam (all idempotent):
        //   Fabric: `<clinit>`-TAIL mixins on EntityAttributes / StatusEffects / Potions call registerAttributes /
        //           registerStatusEffects / registerPotionsInternal; FabricMod.onInitialize calls registerEnchantments;
        //           LivingEntityAttributesMixin attaches `attributesToAttach()` in createLivingAttributes.
        //   Forge:  RegisterEvent (mod bus) drives the same four functions per registry key, and
        //           EntityAttributeModificationEvent attaches `attributesToAttach()` to every living entity type.
    }

    /// Player-join hook: migrate legacy attribute base values when enabled. Wired to
    /// `ServerPlayConnectionEvents.JOIN` on Fabric and `PlayerEvent.PlayerLoggedInEvent` on Forge.
    public static void onPlayerJoin(ServerPlayerEntity player) {
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
    }

    /// Every attribute Spell Power adds to living entities' default attribute containers.
    /// Must be called after {@link #registerAttributes()}.
    public static List<EntityAttribute> attributesToAttach() {
        var attributes = new ArrayList<EntityAttribute>();
        for (var entry : SpellPowerMechanics.all.entrySet()) {
            attributes.add(entry.getValue().attribute);
        }
        for (var school: SpellSchools.all()) {
            var owned = school.ownedAttribute();
            if (owned != null) {
                attributes.add(owned);
            }
        }
        for (var resistance: SpellResistance.Attributes.all) {
            attributes.add(resistance.attribute);
        }
        return attributes;
    }

    private static boolean statusEffectsRegistered = false;

    /**
     * For internal use only!
     */
    public static void registerStatusEffects() {
        if (statusEffectsRegistered) {
            return;
        }
        statusEffectsRegistered = true;

        // 1.20.1 keys status effect modifiers by UUID string; derived from the modern `spell_power:potion_effect` id.
        var modifierUUID = ModifierDefinitions.POTION_EFFECT_UUID.toString();
        var safeConfig = attributesConfig.safeValue();
        var bonus_per_stack = safeConfig.spell_power_effect != null ? safeConfig.spell_power_effect.bonus_per_stack : 0.1F;
        for(var school: SpellSchools.all()) {
            var id = school.id;
            var attribute = school.ownedAttribute();
            if (school.ownedBoostEffect != null && attribute != null) {
                // Modifiers take the raw attribute object, so this does not depend on attribute registration order.
                school.ownedBoostEffect.addAttributeModifier(
                        attribute,
                        modifierUUID,
                        bonus_per_stack,
                        EntityAttributeModifier.Operation.MULTIPLY_BASE);

                Registry.register(Registries.STATUS_EFFECT, id, school.ownedBoostEffect);
            }
        }

        for(var entry: SpellPowerMechanics.all.entrySet()) {
            var secondary = entry.getValue();

            var config = safeConfig.secondary_effects.get(secondary.name);
            if (config != null) {
                bonus_per_stack = config.bonus_per_stack;
            }
            secondary.boostEffect.addAttributeModifier(
                    secondary.attribute,
                    modifierUUID,
                    bonus_per_stack,
                    EntityAttributeModifier.Operation.MULTIPLY_BASE);
            secondary.registerEffect();
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
            var potion = new Potion(new StatusEffectInstance(mechanic.boostEffect, 3600));
            Registry.register(Registries.POTION, potionIdFrom(mechanic.id), potion);
        }
    }

    private static boolean enchantmentsRegistered = false;

    /**
     * For internal use only!
     * Fabric: called from the mod initializer. Forge: called from RegisterEvent for the enchantment registry.
     */
    public static void registerEnchantments() {
        if (enchantmentsRegistered) {
            return;
        }
        enchantmentsRegistered = true;

        for (var entry: SpellPowerEnchantments.all.entrySet()) {
            Registry.register(Registries.ENCHANTMENT, entry.getKey(), entry.getValue());
        }
    }

    public static Identifier potionIdFrom(Identifier id) {
        return new Identifier(id.getNamespace(), id.getNamespace() + "." + id.getPath());
    }

    @Deprecated(forRemoval = true)
    public static AttributesConfig.AttributeScope attributeScopeOverride = null;
    @Deprecated(forRemoval = true)
    public static AttributesConfig.AttributeScope attributeScope() {
        return attributeScopeOverride;
    }

    public static void migrateAttributes(ServerPlayerEntity player) {
        var attributes = SpellSchools.all().stream()
                .filter(school -> school.isMagicArchetype() && school.ownsAttribute())
                .map(school -> school.attributeEntry)
                .toList();
        for (var attribute: attributes) {
            if (attribute == null) {
                continue;
            }
            var instance = player.getAttributeInstance(attribute.value());
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
