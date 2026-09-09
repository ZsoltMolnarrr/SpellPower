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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    /// Populates the `attributeEntry` fields from the registry, for a loader that registered the attributes
    /// itself rather than through {@link #registerAttributes()}.
    ///
    /// Those entries are read all over gameplay (`SpellPower`, `SpellPowerEnchanting`, `SpellResistance`,
    /// `LivingEntityMixin`), so they have to be set on both loaders. Fabric gets them as the return value of
    /// `Registry.registerReference`; Forge's `RegisterEvent` helper returns void, so it calls this straight
    /// after its registration loop instead. Idempotent, and safe to call when everything is already linked.
    public static void linkAttributeEntries() {
        for (var entry : SpellPowerMechanics.all.entrySet()) {
            entry.getValue().linkAttributeEntry();
        }
        for(var resistance: SpellResistance.Attributes.all) {
            resistance.linkAttributeEntry();
        }
        for(var school: SpellSchools.all()) {
            school.linkAttributeEntry();
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
    private static boolean statusEffectsConfigured = false;

    /// Attaches each boosting effect's attribute modifier. Creation only — nothing is registered here, so a
    /// loader that registers the effects itself calls this first. Split out of {@link #registerStatusEffects()}
    /// so the Forge side only has to duplicate the registration loop, not this config work.
    ///
    /// Modifiers take the raw `EntityAttribute` object, so this does not depend on attribute registration order.
    public static void configureStatusEffects() {
        if (statusEffectsConfigured) {
            return;
        }
        statusEffectsConfigured = true;

        // 1.20.1 keys status effect modifiers by UUID string; derived from the modern `spell_power:potion_effect` id.
        var modifierUUID = ModifierDefinitions.POTION_EFFECT_UUID.toString();
        var safeConfig = attributesConfig.safeValue();
        var bonus_per_stack = safeConfig.spell_power_effect != null ? safeConfig.spell_power_effect.bonus_per_stack : 0.1F;
        for(var school: SpellSchools.all()) {
            var attribute = school.ownedAttribute();
            if (school.ownedBoostEffect != null && attribute != null) {
                school.ownedBoostEffect.addAttributeModifier(
                        attribute,
                        modifierUUID,
                        bonus_per_stack,
                        EntityAttributeModifier.Operation.MULTIPLY_BASE);
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
        }
    }

    public static void registerStatusEffects() {
        if (statusEffectsRegistered) {
            return;
        }
        statusEffectsRegistered = true;
        configureStatusEffects();

        for(var school: SpellSchools.all()) {
            if (school.ownedBoostEffect != null && school.ownedAttribute() != null) {
                Registry.register(Registries.STATUS_EFFECT, school.id, school.ownedBoostEffect);
            }
        }
        for(var entry: SpellPowerMechanics.all.entrySet()) {
            var secondary = entry.getValue();
            Registry.register(Registries.STATUS_EFFECT, secondary.id, secondary.boostEffect);
        }
    }

    public static void registerPotionsInternal() {
        if (attributesConfig.safeValue().register_potions) {
            registerPotions();
        }
    }

    private static boolean potionsRegistered = false;
    private static Map<Identifier, Potion> potionsToRegister = null;

    /// Builds every potion Spell Power adds, keyed by the id it registers under. Creation only — nothing is
    /// registered here, so a loader that registers potions itself iterates this map instead of duplicating the
    /// construction. Built once; repeated calls return the same map.
    public static Map<Identifier, Potion> potionsToRegister() {
        if (potionsToRegister != null) {
            return potionsToRegister;
        }
        var potions = new LinkedHashMap<Identifier, Potion>();
        for(var school: SpellSchools.all()) {
            if (school.archetype == SpellSchool.Archetype.MAGIC
                    && !school.id.getPath().contains("generic")
                    && school.ownedBoostEffect != null) {
                potions.put(potionIdFrom(school.id), new Potion(new StatusEffectInstance(school.ownedBoostEffect, 3600)));
            }
        }
        for (var secondary: SpellPowerMechanics.all.entrySet()) {
            var mechanic = secondary.getValue();
            potions.put(potionIdFrom(mechanic.id), new Potion(new StatusEffectInstance(mechanic.boostEffect, 3600)));
        }
        potionsToRegister = potions;
        return potionsToRegister;
    }

    public static void registerPotions() {
        if (potionsRegistered) {
            return;
        }
        potionsRegistered = true;

        for (var entry: potionsToRegister().entrySet()) {
            Registry.register(Registries.POTION, entry.getKey(), entry.getValue());
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
