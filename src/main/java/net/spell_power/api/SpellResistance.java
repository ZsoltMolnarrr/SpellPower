package net.spell_power.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class SpellResistance {
    public static class Attributes {
        public static final ArrayList<Entry> all = new ArrayList<>();

        public static Entry entry(String name, String tagName, double maxValue, boolean tracked) {
            return entry("resistance." + name, Identifier.of(SpellPowerMod.ID, tagName), maxValue, tracked);
        }
        public static Entry entry(String name, Identifier damageTagId, double maxValue, boolean tracked) {
            var tag = TagKey.of(RegistryKeys.DAMAGE_TYPE, damageTagId);
            var entry = new Entry(name, tag, maxValue, tracked);
            all.add(entry);
            return entry;
        }

        public static class Entry {
            public final Identifier id;
            public final String translationKey;
            public final EntityAttribute attribute;
            public final double baseValue;

            public final TagKey<DamageType> damageTypes;
            public final double maxValue;

            @Nullable
            public RegistryEntry<EntityAttribute> attributeEntry;

            public Entry(String name, TagKey<DamageType> tag, double maxValue, boolean tracked) {
                this.id = Identifier.of(SpellPowerMod.ID, name);
                this.translationKey = "attribute.name." + SpellPowerMod.ID + "." + name;

                double baseValue = 0;
                double minValue = 0;
                this.attribute = new ClampedEntityAttribute(translationKey, baseValue, minValue, maxValue).setTracked(tracked);
                this.baseValue = baseValue;
                this.maxValue = maxValue;
                this.damageTypes = tag;
            }

            public void registerAttribute() {
                attributeEntry = Registry.registerReference(Registries.ATTRIBUTE, id, attribute);
            }
        }

        public static final Entry GENERIC = entry("generic", "resistable", 1024, true);
    }

    public static double resist(LivingEntity target, double damage, DamageSource source) {
        double modifier = 1;
        var config = SpellPowerMod.attributesConfig.value;

        for (var resistanceType : Attributes.all) {
            if (target.getAttributes().hasAttribute(resistanceType.attributeEntry) && source.isIn(resistanceType.damageTypes)) {
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
