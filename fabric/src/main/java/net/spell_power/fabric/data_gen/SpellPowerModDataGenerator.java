package net.spell_power.fabric.data_gen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.advancements.predicates.DamageSourcePredicate;
import net.minecraft.advancements.predicates.TagPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellPowerTags;
import net.spell_power.api.SpellSchools;

import java.util.concurrent.CompletableFuture;

public class SpellPowerModDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(EnchantmentGenerator::new);
    }

    private static class EnchantmentGenerator extends FabricDynamicRegistryProvider {

        public EnchantmentGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(HolderLookup.Provider registries, Entries entries) {
            var spell_power = "spell_power";


            HolderGetter<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);
            HolderGetter<Enchantment> enchantmentLookup = registries.lookupOrThrow(Registries.ENCHANTMENT);


            var eid = ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(SpellPowerMod.ID, spell_power));
            Enchantment.Builder builder = Enchantment.enchantment(
                    Enchantment.definition(
                            itemLookup.getOrThrow(SpellPowerTags.Items.Enchantable.SPELL_POWER_GENERIC),
                            10, 5,
                            Enchantment.dynamicCost(1, 11),
                            Enchantment.dynamicCost(12, 11),
                    1,
                            EquipmentSlotGroup.MAINHAND)
                    )
                    .withEffect(
                            EnchantmentEffectComponents.ATTRIBUTES,
                            new EnchantmentAttributeEffect(
                                    Identifier.fromNamespaceAndPath(SpellPowerMod.ID, spell_power),
                                    SpellSchools.GENERIC.attributeEntry,
                                    LevelBasedValue.perLevel(0.05F),
                                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    );
            entries.add(eid, builder.build(eid.identifier()));

            var specializedBonus = 0.03F;

            var sunfireId = ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(SpellPowerMod.ID, "sunfire"));
            Enchantment.Builder sunfire = Enchantment.enchantment(
                            Enchantment.definition(
                                    itemLookup.getOrThrow(SpellPowerTags.Items.Enchantable.SPELL_POWER_SUNFIRE),
                                    2, 5,
                                    Enchantment.dynamicCost(1, 11),
                                    Enchantment.dynamicCost(12, 11),
                                    1,
                                    EquipmentSlotGroup.ARMOR)
                    )
                    .exclusiveWith(enchantmentLookup.getOrThrow(SpellPowerTags.Enchantments.MULTI_SCHOOL))
                    .withEffect(
                            EnchantmentEffectComponents.ATTRIBUTES,
                            new EnchantmentAttributeEffect(
                                    Identifier.fromNamespaceAndPath(SpellPowerMod.ID, "sunfire_enchantment"),
                                    SpellSchools.ARCANE.attributeEntry,
                                    LevelBasedValue.perLevel(specializedBonus),
                                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    )
                    .withEffect(
                            EnchantmentEffectComponents.ATTRIBUTES,
                            new EnchantmentAttributeEffect(
                                    Identifier.fromNamespaceAndPath(SpellPowerMod.ID, "sunfire_enchantment"),
                                    SpellSchools.FIRE.attributeEntry,
                                    LevelBasedValue.perLevel(specializedBonus),
                                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    );
            entries.add(sunfireId, sunfire.build(sunfireId.identifier()));

            var soulfrostId = ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(SpellPowerMod.ID, "soulfrost"));
            Enchantment.Builder soulfrost = Enchantment.enchantment(
                            Enchantment.definition(
                                    itemLookup.getOrThrow(SpellPowerTags.Items.Enchantable.SPELL_POWER_SOULFROST),
                                    2, 5,
                                    Enchantment.dynamicCost(1, 11),
                                    Enchantment.dynamicCost(12, 11),
                                    1,
                                    EquipmentSlotGroup.ARMOR)
                    )
                    .exclusiveWith(enchantmentLookup.getOrThrow(SpellPowerTags.Enchantments.MULTI_SCHOOL))
                    .withEffect(
                            EnchantmentEffectComponents.ATTRIBUTES,
                            new EnchantmentAttributeEffect(
                                    Identifier.fromNamespaceAndPath(SpellPowerMod.ID, "soulfrost_enchantment"),
                                    SpellSchools.SOUL.attributeEntry,
                                    LevelBasedValue.perLevel(specializedBonus),
                                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    )
                    .withEffect(
                            EnchantmentEffectComponents.ATTRIBUTES,
                            new EnchantmentAttributeEffect(
                                    Identifier.fromNamespaceAndPath(SpellPowerMod.ID, "soulfrost_enchantment"),
                                    SpellSchools.FROST.attributeEntry,
                                    LevelBasedValue.perLevel(specializedBonus),
                                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    );
            entries.add(soulfrostId, soulfrost.build(soulfrostId.identifier()));


            var energizeId = ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(SpellPowerMod.ID, "energize"));
            Enchantment.Builder energize = Enchantment.enchantment(
                            Enchantment.definition(
                                    itemLookup.getOrThrow(SpellPowerTags.Items.Enchantable.SPELL_POWER_ENERGIZE),
                                    2, 5,
                                    Enchantment.dynamicCost(1, 11),
                                    Enchantment.dynamicCost(12, 11),
                                    1,
                                    EquipmentSlotGroup.ARMOR)
                    )
                    .exclusiveWith(enchantmentLookup.getOrThrow(SpellPowerTags.Enchantments.MULTI_SCHOOL))
                    .withEffect(
                            EnchantmentEffectComponents.ATTRIBUTES,
                            new EnchantmentAttributeEffect(
                                    Identifier.fromNamespaceAndPath(SpellPowerMod.ID, "energize_enchantment"),
                                    SpellSchools.HEALING.attributeEntry,
                                    LevelBasedValue.perLevel(specializedBonus),
                                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    )
                    .withEffect(
                            EnchantmentEffectComponents.ATTRIBUTES,
                            new EnchantmentAttributeEffect(
                                    Identifier.fromNamespaceAndPath(SpellPowerMod.ID, "energize_enchantment"),
                                    SpellSchools.LIGHTNING.attributeEntry,
                                    LevelBasedValue.perLevel(specializedBonus),
                                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    );
            entries.add(energizeId, energize.build(energizeId.identifier()));


            var hasteId = ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(SpellPowerMod.ID, "haste"));
            Enchantment.Builder haste = Enchantment.enchantment(
                    Enchantment.definition(
                                    itemLookup.getOrThrow(SpellPowerTags.Items.Enchantable.HASTE),
                                    5, 5,
                                    Enchantment.dynamicCost(5, 12),
                                    Enchantment.dynamicCost(15, 15),
                                    3,
                                    EquipmentSlotGroup.MAINHAND)
                    )
                    .withEffect(
                            EnchantmentEffectComponents.ATTRIBUTES,
                            new EnchantmentAttributeEffect(
                                    Identifier.fromNamespaceAndPath(SpellPowerMod.ID, "enchantment"),
                                    SpellPowerMechanics.HASTE.attributeEntry,
                                    LevelBasedValue.perLevel(0.04F),
                                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    );
            entries.add(hasteId, haste.build(hasteId.identifier()));

            var critical_chanceId = ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(SpellPowerMod.ID, "critical_chance"));
            Enchantment.Builder critical_chance = Enchantment.enchantment(
                    Enchantment.definition(
                                    itemLookup.getOrThrow(SpellPowerTags.Items.Enchantable.CRITICAL_CHANCE),
                                    4, 5,
                                    Enchantment.dynamicCost(5, 12),
                                    Enchantment.dynamicCost(15, 15),
                                    3,
                                    EquipmentSlotGroup.MAINHAND)
                    )
                    .withEffect(
                            EnchantmentEffectComponents.ATTRIBUTES,
                            new EnchantmentAttributeEffect(
                                    Identifier.fromNamespaceAndPath(SpellPowerMod.ID, "enchantment"),
                                    SpellPowerMechanics.CRITICAL_CHANCE.attributeEntry,
                                    LevelBasedValue.perLevel(0.04F),
                                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    )
                    .exclusiveWith(enchantmentLookup.getOrThrow(SpellPowerTags.Enchantments.SPELL_CRITICAL_EXCLUSIVE));
            entries.add(critical_chanceId, critical_chance.build(critical_chanceId.identifier()));

            var critical_damageId = ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(SpellPowerMod.ID, "critical_damage"));
            Enchantment.Builder critical_damage = Enchantment.enchantment(
                            Enchantment.definition(
                                    itemLookup.getOrThrow(SpellPowerTags.Items.Enchantable.CRITICAL_DAMAGE),
                                    5, 5,
                                    Enchantment.dynamicCost(5, 12),
                                    Enchantment.dynamicCost(15, 15),
                                    3,
                                    EquipmentSlotGroup.MAINHAND)
                    )
                    .withEffect(
                            EnchantmentEffectComponents.ATTRIBUTES,
                            new EnchantmentAttributeEffect(
                                    Identifier.fromNamespaceAndPath(SpellPowerMod.ID, "enchantment"),
                                    SpellPowerMechanics.CRITICAL_DAMAGE.attributeEntry,
                                    LevelBasedValue.perLevel(0.1F),
                                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    )
                    .exclusiveWith(enchantmentLookup.getOrThrow(SpellPowerTags.Enchantments.SPELL_CRITICAL_EXCLUSIVE));
            entries.add(critical_damageId, critical_damage.build(critical_damageId.identifier()));

            var protectionId = ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(SpellPowerMod.ID, "magic_protection"));
            Enchantment.Builder protection = Enchantment.enchantment(
                            Enchantment.definition(
                                    itemLookup.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                                    5,
                                    4,
                                    Enchantment.dynamicCost(3, 6),
                                    Enchantment.dynamicCost(9, 6),
                                    2,
                                    EquipmentSlotGroup.ARMOR
                            )
                    )
                    .exclusiveWith(enchantmentLookup.getOrThrow(EnchantmentTags.ARMOR_EXCLUSIVE))
                    .withEffect(
                            EnchantmentEffectComponents.DAMAGE_PROTECTION,
                            new AddValue(LevelBasedValue.perLevel(2.0F)),
                            DamageSourceCondition.hasDamageSource(
                                    DamageSourcePredicate.Builder.damageType()
                                            .tag(TagPredicate.is(damageTypeTag("c:is_magic")))
                                            .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
                            )
                    );
            entries.add(protectionId, protection.build(protectionId.identifier()));
        }

        private TagKey<Item> requirementTag(String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(SpellPowerMod.ID, "enchantable/" + name));
        }

        private TagKey<DamageType> damageTypeTag(String id) {
            return TagKey.create(Registries.DAMAGE_TYPE, Identifier.parse(id));
        }

        @Override
        public String getName() {
            return "enchantments";
        }
    }
}