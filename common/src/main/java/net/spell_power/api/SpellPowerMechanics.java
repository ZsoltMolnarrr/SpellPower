package net.spell_power.api;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;
import net.spell_power.internals.CustomEntityAttribute;
import net.spell_power.internals.SpellStatusEffect;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class SpellPowerMechanics {
    public final static float PERCENT_ATTRIBUTE_BASELINE = 100F;
    public static String translationPrefix() {
        return "attribute.name." + SpellPowerMod.ID + ".";
    }

    public static class Entry {
        public final String name;
        public final Identifier id;
        public final float defaultValue, min, max;
        public final CustomEntityAttribute attribute;
        public final StatusEffect boostEffect;
        public @Nullable EntityAttributeModifier innateModifier;

        @Nullable
        public RegistryEntry<EntityAttribute> attributeEntry;

        @Nullable
        public RegistryEntry<StatusEffect> effectEntry;

        public Entry(String name, float defaultValue, float min, float max, int color) {
            this.name = name;
            this.id = Identifier.of(SpellPowerMod.ID, name);
            this.defaultValue = defaultValue;
            this.min = min;
            this.max = max;
            this.attribute = new CustomEntityAttribute(translationPrefix() + name, defaultValue, min, max, id);
            this.attribute.setTracked(true);
            this.boostEffect = new SpellStatusEffect(StatusEffectCategory.BENEFICIAL, color);
        }

        public void registerAttribute() {
            attributeEntry = Registry.registerReference(Registries.ATTRIBUTE, id, attribute);
        }

        public void registerEffect() {
            effectEntry = Registry.registerReference(Registries.STATUS_EFFECT, id, boostEffect);
        }

        public Entry innateModifier(EntityAttributeModifier.Operation operation, float value) {
            innateModifier = new EntityAttributeModifier(ModifierDefinitions.INNATE_BONUS, value, operation);
            return this;
        }
    }

    public static final HashMap<String, Entry> all = new HashMap<>();

    public static Entry entry(String name, float defaultValue, float min, float max, int color) {
        var entry = new Entry(name, defaultValue, min, max, color);
        all.put(name, entry);
        return entry;
    }

    public static final Entry CRITICAL_CHANCE = entry("critical_chance", PERCENT_ATTRIBUTE_BASELINE, PERCENT_ATTRIBUTE_BASELINE, PERCENT_ATTRIBUTE_BASELINE * 10, 0x66ccff)
            .innateModifier(EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE, ((float) SpellPowerMod.attributesConfig.safeValue().base_spell_critical_chance_percentage) / 100F);
    public static final Entry CRITICAL_DAMAGE = entry("critical_damage", PERCENT_ATTRIBUTE_BASELINE, PERCENT_ATTRIBUTE_BASELINE, PERCENT_ATTRIBUTE_BASELINE * 10, 0x66ffcc)
            .innateModifier(EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE, ((float) SpellPowerMod.attributesConfig.safeValue().base_spell_critical_damage_percentage) / 100F);
    public static final Entry HASTE = entry("haste", PERCENT_ATTRIBUTE_BASELINE, PERCENT_ATTRIBUTE_BASELINE, PERCENT_ATTRIBUTE_BASELINE * 10, 0xcc99ff);
}
