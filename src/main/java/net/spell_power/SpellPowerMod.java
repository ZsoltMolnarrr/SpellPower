package net.spell_power;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.spell_power.api.*;
import net.spell_power.config.AttributesConfig;
import net.spell_power.internals.AttributeUtil;
import net.spell_power.internals.CrossFunctionalAttributes;
import net.tinyconfig.ConfigManager;

public class SpellPowerMod implements ModInitializer {
    public static final String ID = "spell_power";

    public static final ConfigManager<AttributesConfig> attributesConfig = new ConfigManager<AttributesConfig>
            ("attributes", AttributesConfig.defaults())
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .validate(AttributesConfig::isValid)
            .build();

    @Override
    public void onInitialize() {
        attributesConfig.refresh();
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

        var genericSpellSchool = SpellSchools.GENERIC;
        for(var school: SpellSchools.all()) {
            school.registerAttribute();
            if (school != genericSpellSchool && school.ownsAttribute()) {
                CrossFunctionalAttributes.power(school.attributeEntry, genericSpellSchool.attributeEntry);
            }
        }

        EnchantmentEvents.ALLOW_ENCHANTING.register((enchantment, target, enchantingContext) -> {
            if (enchantment.isIn(SpellPowerTags.Enchantments.REQUIRES_MATCHING_ATTRIBUTE))  {
                // System.out.println("Spell Power - School Filtering check: " + enchantment);
                var enchantmentAttributes = enchantment.value().effects().get(EnchantmentEffectComponentTypes.ATTRIBUTES);
                if (enchantmentAttributes != null && !enchantmentAttributes.isEmpty()) {
                    var itemAttributes = target.getComponents().get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
                    if (itemAttributes.modifiers().isEmpty()) {
                        itemAttributes = target.getItem().getAttributeModifiers();
                    }
                    // System.out.println("Spell Power - School Filtering | enchantmentAttributes: " + enchantmentAttributes + " | itemAttributes: " + itemAttributes);

                    var intersect = AttributeUtil.attributesIntersect(enchantmentAttributes, itemAttributes);
                    // System.out.println("Spell Power - Intersect: " + intersect + " | " + enchantmentAttributes + " | " + itemAttributes);
                    if (!intersect) {
                        return TriState.FALSE;
                    }
                }
            }
            return TriState.DEFAULT;
        });
    }

    /**
     * For internal use only!
     */
    public static void registerStatusEffects() {
        var modifierId = Identifier.of(ID, "potion_effect");
        var bonus_per_stack = 0.1F;
        for(var school: SpellSchools.all()) {
            var id = school.id;
            if (school.ownedBoostEffect != null && school.attributeEntry != null) {
                school.ownedBoostEffect.addAttributeModifier(
                        school.attributeEntry,
                        modifierId,
                        bonus_per_stack,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);

                Registry.register(Registries.STATUS_EFFECT, id.toString(), school.ownedBoostEffect);
            }
        }

        for(var entry: SpellPowerMechanics.all.entrySet()) {
            var secondary = entry.getValue();
            var id = secondary.id;

            var config = attributesConfig.value.secondary_effects.get(secondary.name);
            if (config != null) {
                bonus_per_stack = config.bonus_per_stack;
            }
            secondary.boostEffect.addAttributeModifier(
                    secondary.attributeEntry,
                    modifierId,
                    bonus_per_stack,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
            Registry.register(Registries.STATUS_EFFECT, id.toString(), secondary.boostEffect);
        }
    }

    public static void registerPotionsInternal() {
        attributesConfig.refresh();
        if (attributesConfig.value.register_potions) {
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
            var entry = Registries.STATUS_EFFECT.getEntry(mechanic.boostEffect);
            if (entry != null) {
                var potion = new Potion(new StatusEffectInstance(entry, 3600));
                Registry.register(Registries.POTION, potionIdFrom(mechanic.id), potion);
            }
        }
    }

    public static Identifier potionIdFrom(Identifier id) {
        return Identifier.of(id.getNamespace(), id.getNamespace() + "." + id.getPath());
    }

    public static AttributesConfig.AttributeScope attributeScopeOverride = null;
    public static AttributesConfig.AttributeScope attributeScope() {
        return attributeScopeOverride != null ? attributeScopeOverride : attributesConfig.value.attributes_container_injection_scope;
    }
}