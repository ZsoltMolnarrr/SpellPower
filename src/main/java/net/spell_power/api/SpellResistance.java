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
    public enum Curve { LINEAR, SQUARE }
    public static class Attributes {
        public static final ArrayList<Entry> all = new ArrayList<>();

        public static Entry entry(String name, String tagName, Curve curve, double maxValue, boolean tracked) {
            return entry("resistance." + name, Identifier.of(SpellPowerMod.ID, tagName), curve, maxValue, tracked);
        }
        public static Entry entry(String name, Identifier damageTagId, Curve curve, double maxValue, boolean tracked) {
            var tag = TagKey.of(RegistryKeys.DAMAGE_TYPE, damageTagId);
            var entry = new Entry(name, tag, curve, maxValue, tracked);
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
            public Curve curve;

            @Nullable
            public RegistryEntry<EntityAttribute> attributeEntry;

            public Entry(String name, TagKey<DamageType> tag, Curve curve, double maxValue, boolean tracked) {
                this.id = Identifier.of(SpellPowerMod.ID, name);
                this.translationKey = "attribute.name." + SpellPowerMod.ID + "." + name;

                double baseValue = 0;
                double minValue = 0;
                this.attribute = new ClampedEntityAttribute(translationKey, baseValue, minValue, maxValue).setTracked(tracked);
                this.baseValue = baseValue;
                this.maxValue = maxValue;
                this.curve = curve;
                this.damageTypes = tag;
            }

            public void registerAttribute() {
                attributeEntry = Registry.registerReference(Registries.ATTRIBUTE, id, attribute);
            }
        }

        public static final Entry GENERIC = entry("generic", "resistable", Curve.LINEAR, 100, true);
    }

    public static double resist(LivingEntity target, double damage, DamageSource source) {
        double modifier = 1;
        for (var resistance : Attributes.all) {
            if (target.getAttributes().hasAttribute(resistance.attributeEntry) && source.isIn(resistance.damageTypes)) {
                var value = target.getAttributeValue(resistance.attributeEntry);
                var maxValue = resistance.maxValue;
                switch (resistance.curve) {
                    case LINEAR -> {
                        modifier *= 1 - Math.min( (value / maxValue) * SpellPowerMod.attributesConfig.value.resistance_multiplier,
                                SpellPowerMod.attributesConfig.value.resistance_reduction_cap);
                    }
                    case SQUARE ->  {
                        // https://www.wolframalpha.com/input?i=sqrt%28x*100%29+%3D+100
                        var sqrt = Math.sqrt(value * maxValue);
                        modifier *= 1 - Math.min( (sqrt / maxValue) * SpellPowerMod.attributesConfig.value.resistance_multiplier,
                                SpellPowerMod.attributesConfig.value.resistance_reduction_cap);
                    }
                }
            }
        }
        // System.out.println("RESIST Damage: " + damage + " Modifier: " + modifier);
        return damage * modifier;
    }
}
