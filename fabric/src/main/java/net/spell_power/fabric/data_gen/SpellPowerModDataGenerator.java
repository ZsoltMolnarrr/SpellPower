package net.spell_power.fabric.data_gen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.AttributeEnchantmentEffect;
import net.minecraft.enchantment.effect.value.AddEnchantmentEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.loot.condition.DamageSourcePropertiesLootCondition;
import net.minecraft.predicate.TagPredicate;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
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

        public EnchantmentGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
            var spell_power = "spell_power";


            RegistryEntryLookup<Item> itemLookup = registries.createRegistryLookup().getOrThrow(RegistryKeys.ITEM);
            RegistryEntryLookup<Enchantment> enchantmentLookup = registries.createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);


            var eid = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SpellPowerMod.ID, spell_power));
            Enchantment.Builder builder = Enchantment.builder(
                    Enchantment.definition(
                            itemLookup.getOrThrow(SpellPowerTags.Items.Enchantable.SPELL_POWER_GENERIC),
                            10, 5,
                            Enchantment.leveledCost(1, 11),
                            Enchantment.leveledCost(12, 11),
                    1,
                            AttributeModifierSlot.MAINHAND)
                    )
                    .addEffect(
                            EnchantmentEffectComponentTypes.ATTRIBUTES,
                            new AttributeEnchantmentEffect(
                                    Identifier.of(SpellPowerMod.ID, spell_power),
                                    SpellSchools.GENERIC.attributeEntry,
                                    EnchantmentLevelBasedValue.linear(0.05F),
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    );
            entries.add(eid, builder.build(eid.getValue()));

            var specializedBonus = 0.03F;

            var sunfireId = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SpellPowerMod.ID, "sunfire"));
            Enchantment.Builder sunfire = Enchantment.builder(
                            Enchantment.definition(
                                    itemLookup.getOrThrow(SpellPowerTags.Items.Enchantable.SPELL_POWER_SUNFIRE),
                                    2, 5,
                                    Enchantment.leveledCost(1, 11),
                                    Enchantment.leveledCost(12, 11),
                                    1,
                                    AttributeModifierSlot.ARMOR)
                    )
                    .exclusiveSet(enchantmentLookup.getOrThrow(SpellPowerTags.Enchantments.MULTI_SCHOOL))
                    .addEffect(
                            EnchantmentEffectComponentTypes.ATTRIBUTES,
                            new AttributeEnchantmentEffect(
                                    Identifier.of(SpellPowerMod.ID, "sunfire_enchantment"),
                                    SpellSchools.ARCANE.attributeEntry,
                                    EnchantmentLevelBasedValue.linear(specializedBonus),
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    )
                    .addEffect(
                            EnchantmentEffectComponentTypes.ATTRIBUTES,
                            new AttributeEnchantmentEffect(
                                    Identifier.of(SpellPowerMod.ID, "sunfire_enchantment"),
                                    SpellSchools.FIRE.attributeEntry,
                                    EnchantmentLevelBasedValue.linear(specializedBonus),
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    );
            entries.add(sunfireId, sunfire.build(sunfireId.getValue()));

            var soulfrostId = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SpellPowerMod.ID, "soulfrost"));
            Enchantment.Builder soulfrost = Enchantment.builder(
                            Enchantment.definition(
                                    itemLookup.getOrThrow(SpellPowerTags.Items.Enchantable.SPELL_POWER_SOULFROST),
                                    2, 5,
                                    Enchantment.leveledCost(1, 11),
                                    Enchantment.leveledCost(12, 11),
                                    1,
                                    AttributeModifierSlot.ARMOR)
                    )
                    .exclusiveSet(enchantmentLookup.getOrThrow(SpellPowerTags.Enchantments.MULTI_SCHOOL))
                    .addEffect(
                            EnchantmentEffectComponentTypes.ATTRIBUTES,
                            new AttributeEnchantmentEffect(
                                    Identifier.of(SpellPowerMod.ID, "soulfrost_enchantment"),
                                    SpellSchools.SOUL.attributeEntry,
                                    EnchantmentLevelBasedValue.linear(specializedBonus),
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    )
                    .addEffect(
                            EnchantmentEffectComponentTypes.ATTRIBUTES,
                            new AttributeEnchantmentEffect(
                                    Identifier.of(SpellPowerMod.ID, "soulfrost_enchantment"),
                                    SpellSchools.FROST.attributeEntry,
                                    EnchantmentLevelBasedValue.linear(specializedBonus),
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    );
            entries.add(soulfrostId, soulfrost.build(soulfrostId.getValue()));


            var energizeId = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SpellPowerMod.ID, "energize"));
            Enchantment.Builder energize = Enchantment.builder(
                            Enchantment.definition(
                                    itemLookup.getOrThrow(SpellPowerTags.Items.Enchantable.SPELL_POWER_ENERGIZE),
                                    2, 5,
                                    Enchantment.leveledCost(1, 11),
                                    Enchantment.leveledCost(12, 11),
                                    1,
                                    AttributeModifierSlot.ARMOR)
                    )
                    .exclusiveSet(enchantmentLookup.getOrThrow(SpellPowerTags.Enchantments.MULTI_SCHOOL))
                    .addEffect(
                            EnchantmentEffectComponentTypes.ATTRIBUTES,
                            new AttributeEnchantmentEffect(
                                    Identifier.of(SpellPowerMod.ID, "energize_enchantment"),
                                    SpellSchools.HEALING.attributeEntry,
                                    EnchantmentLevelBasedValue.linear(specializedBonus),
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    )
                    .addEffect(
                            EnchantmentEffectComponentTypes.ATTRIBUTES,
                            new AttributeEnchantmentEffect(
                                    Identifier.of(SpellPowerMod.ID, "energize_enchantment"),
                                    SpellSchools.LIGHTNING.attributeEntry,
                                    EnchantmentLevelBasedValue.linear(specializedBonus),
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    );
            entries.add(energizeId, energize.build(energizeId.getValue()));


            var hasteId = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SpellPowerMod.ID, "haste"));
            Enchantment.Builder haste = Enchantment.builder(
                    Enchantment.definition(
                                    itemLookup.getOrThrow(SpellPowerTags.Items.Enchantable.HASTE),
                                    5, 5,
                                    Enchantment.leveledCost(5, 12),
                                    Enchantment.leveledCost(15, 15),
                                    3,
                                    AttributeModifierSlot.MAINHAND)
                    )
                    .addEffect(
                            EnchantmentEffectComponentTypes.ATTRIBUTES,
                            new AttributeEnchantmentEffect(
                                    Identifier.of(SpellPowerMod.ID, "enchantment"),
                                    SpellPowerMechanics.HASTE.attributeEntry,
                                    EnchantmentLevelBasedValue.linear(0.04F),
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    );
            entries.add(hasteId, haste.build(hasteId.getValue()));

            var critical_chanceId = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SpellPowerMod.ID, "critical_chance"));
            Enchantment.Builder critical_chance = Enchantment.builder(
                    Enchantment.definition(
                                    itemLookup.getOrThrow(SpellPowerTags.Items.Enchantable.CRITICAL_CHANCE),
                                    4, 5,
                                    Enchantment.leveledCost(5, 12),
                                    Enchantment.leveledCost(15, 15),
                                    3,
                                    AttributeModifierSlot.MAINHAND)
                    )
                    .addEffect(
                            EnchantmentEffectComponentTypes.ATTRIBUTES,
                            new AttributeEnchantmentEffect(
                                    Identifier.of(SpellPowerMod.ID, "enchantment"),
                                    SpellPowerMechanics.CRITICAL_CHANCE.attributeEntry,
                                    EnchantmentLevelBasedValue.linear(0.04F),
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    )
                    .exclusiveSet(enchantmentLookup.getOrThrow(SpellPowerTags.Enchantments.SPELL_CRITICAL_EXCLUSIVE));
            entries.add(critical_chanceId, critical_chance.build(critical_chanceId.getValue()));

            var critical_damageId = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SpellPowerMod.ID, "critical_damage"));
            Enchantment.Builder critical_damage = Enchantment.builder(
                            Enchantment.definition(
                                    itemLookup.getOrThrow(SpellPowerTags.Items.Enchantable.CRITICAL_DAMAGE),
                                    5, 5,
                                    Enchantment.leveledCost(5, 12),
                                    Enchantment.leveledCost(15, 15),
                                    3,
                                    AttributeModifierSlot.MAINHAND)
                    )
                    .addEffect(
                            EnchantmentEffectComponentTypes.ATTRIBUTES,
                            new AttributeEnchantmentEffect(
                                    Identifier.of(SpellPowerMod.ID, "enchantment"),
                                    SpellPowerMechanics.CRITICAL_DAMAGE.attributeEntry,
                                    EnchantmentLevelBasedValue.linear(0.1F),
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    )
                    .exclusiveSet(enchantmentLookup.getOrThrow(SpellPowerTags.Enchantments.SPELL_CRITICAL_EXCLUSIVE));
            entries.add(critical_damageId, critical_damage.build(critical_damageId.getValue()));

            var protectionId = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SpellPowerMod.ID, "magic_protection"));
            Enchantment.Builder protection = Enchantment.builder(
                            Enchantment.definition(
                                    itemLookup.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                                    5,
                                    4,
                                    Enchantment.leveledCost(3, 6),
                                    Enchantment.leveledCost(9, 6),
                                    2,
                                    AttributeModifierSlot.ARMOR
                            )
                    )
                    .exclusiveSet(enchantmentLookup.getOrThrow(EnchantmentTags.ARMOR_EXCLUSIVE_SET))
                    .addEffect(
                            EnchantmentEffectComponentTypes.DAMAGE_PROTECTION,
                            new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(2.0F)),
                            DamageSourcePropertiesLootCondition.builder(
                                    DamageSourcePredicate.Builder.create()
                                            .tag(TagPredicate.expected(damageTypeTag("c:is_magic")))
                                            .tag(TagPredicate.unexpected(DamageTypeTags.BYPASSES_INVULNERABILITY))
                            )
                    );
            entries.add(protectionId, protection.build(protectionId.getValue()));
        }

        private TagKey<Item> requirementTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(SpellPowerMod.ID, "enchantable/" + name));
        }

        private TagKey<DamageType> damageTypeTag(String id) {
            return TagKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(id));
        }

        @Override
        public String getName() {
            return "enchantments";
        }
    }
}