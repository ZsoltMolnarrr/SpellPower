package net.spell_power.api;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.spell_power.SpellPowerMod;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class SpellResistance {
    public static class Attributes {
        public static final ArrayList<Entry> all = new ArrayList<>();

        public static Entry entry(String name, String tagName, double maxValue, boolean tracked) {
            return entry("resistance." + name, Identifier.fromNamespaceAndPath(SpellPowerMod.ID, tagName), maxValue, tracked);
        }
        public static Entry entry(String name, Identifier damageTagId, double maxValue, boolean tracked) {
            var tag = TagKey.create(Registries.DAMAGE_TYPE, damageTagId);
            var entry = new Entry(name, tag, maxValue, tracked);
            all.add(entry);
            return entry;
        }

        public static class Entry {
            public final Identifier id;
            public final String translationKey;
            public final Attribute attribute;
            public final double baseValue;

            public final TagKey<DamageType> damageTypes;
            public final double maxValue;

            @Nullable
            public Holder<Attribute> attributeEntry;

            public Entry(String name, TagKey<DamageType> tag, double maxValue, boolean tracked) {
                this.id = Identifier.fromNamespaceAndPath(SpellPowerMod.ID, name);
                this.translationKey = "attribute.name." + SpellPowerMod.ID + "." + name;

                double baseValue = 0;
                double minValue = 0;
                this.attribute = new RangedAttribute(translationKey, baseValue, minValue, maxValue).setSyncable(tracked);
                this.baseValue = baseValue;
                this.maxValue = maxValue;
                this.damageTypes = tag;
            }

            public void registerAttribute() {
                attributeEntry = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, id, attribute);
            }
        }

        public static final Entry GENERIC = entry("generic", "resistable", 1024, true);
    }

    public static double resist(LivingEntity target, double damage, DamageSource source) {
        double modifier = 1;
        var config = SpellPowerMod.attributesConfig.safeValue();

        for (var resistanceType : Attributes.all) {
            if (target.getAttributes().hasAttribute(resistanceType.attributeEntry) && source.is(resistanceType.damageTypes)) {
                var resistancePoints = (float)target.getAttributeValue(resistanceType.attributeEntry);
                var reduction = 0F;
                switch (config.resistance_curve) {
                    case LINEAR -> {
                        // r / C
                        reduction = resistancePoints / config.resistance_tuning_constant;
                    }
                    case QUADRATIC ->  {
                        // sqrt(r * C) / C
                        reduction = (float)Math.sqrt(resistancePoints * config.resistance_tuning_constant) * config.resistance_tuning_constant;
                    }
                    case HYPERBOLIC -> {
                        // r / (r + C)
                        reduction = resistancePoints / (resistancePoints + config.resistance_tuning_constant);
                    }
                }
                reduction = Math.min(reduction * config.resistance_multiplier, config.resistance_reduction_cap);
                modifier *= (1 - reduction);
            }
        }
        // System.out.println("RESIST Damage: " + damage + " Modifier: " + modifier);
        return damage * modifier;
    }
}
