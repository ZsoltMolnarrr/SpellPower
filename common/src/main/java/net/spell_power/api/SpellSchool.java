package net.spell_power.api;

import com.google.common.base.Suppliers;
import com.google.gson.annotations.JsonAdapter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.misc.SpellSchoolJSONAdapter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

@JsonAdapter(SpellSchoolJSONAdapter.class)
public class SpellSchool {
    public enum Archetype { ARCHERY, MAGIC, MELEE }
    public final Archetype archetype;
    /**
     * ID of the:
     * - Spell School itself
     * - Powering Entity Attribute if managed internally
     * - Powering Status Effect if managed internally
     */
    public final Identifier id;

    /**
     * Theme color of the spell school.
     * Format: 0xRRGGBB. For example, 0xff0000 is red, 0x00ff00 is green, 0x0000ff is blue.
     * Used for:
     * - Cast bar tinting
     * - Boosting status effect color
     */
    public final int color;

    /**
     * Internally managed entity attribute that boosts this spell school.
     */
    @Nullable private final EntityAttribute ownedAttribute;

    /**
     * Status effect that boosts this spell school.
     * Maybe left null, if status effect that boosts the respective attribute already exists.
     * (Like how vanilla Strength boosts attack damage)
     */
    @Nullable public final StatusEffect ownedBoostEffect;

    /**
     * Spells of this school deal this type of damage
     */
    public final RegistryKey<DamageType> damageType;

    @Nullable public RegistryEntry<EntityAttribute> attributeEntry;
    @Nullable public RegistryEntry<Potion> potionEntry;

    public SpellSchool(Archetype archetype, Identifier id, int color, RegistryKey<DamageType> damageType, RegistryEntry<EntityAttribute> attributeEntry) {
        this(archetype, id, color, damageType, null, null);
        this.attributeEntry = attributeEntry;
    }

    public SpellSchool(Archetype archetype, Identifier id, int color, RegistryKey<DamageType> damageType, EntityAttribute attribute, @Nullable StatusEffect boostEffect) {
        this.archetype = archetype;
        this.id = id;
        this.color = color;
        this.damageType = damageType;
        this.ownedAttribute = attribute;
        this.ownedBoostEffect = boostEffect;
    }

    public float attributeBaseValue() {
        return ownedAttribute != null ? (float) ownedAttribute.getDefaultValue() : 0;
    }

    public void registerAttribute() {
        if (ownedAttribute != null) {
            attributeEntry = Registry.registerReference(Registries.ATTRIBUTE, id, ownedAttribute);
        }
    }

    public void registerPotion() {
        if (ownedBoostEffect != null) {
            var entry = Registries.STATUS_EFFECT.getEntry(ownedBoostEffect);
            if (entry != null) {
                var potion = new Potion(new StatusEffectInstance(entry, 3600));
                var potionId = SpellPowerMod.potionIdFrom(id);
                Registry.register(Registries.POTION, potionId, potion);
            }
        }
    }

    public RegistryEntry<EntityAttribute> getAttributeEntry() {
        return attributeEntry;
    }

    public boolean ownsAttribute() {
        return ownedAttribute != null;
    }

    public boolean isMagicArchetype() {
        return archetype == Archetype.MAGIC;
    }

    // Sources
    public enum Apply { ADD, MULTIPLY }
    public record QueryArgs(LivingEntity entity) { }
    public record Source(Apply apply, Function<QueryArgs, Double> function) { }
    public enum Trait { POWER, HASTE, CRIT_CHANCE, CRIT_DAMAGE }
    private static HashMap<Trait, ArrayList<Source>> emptyTraits() {
        var map = new HashMap<Trait, ArrayList<Source>>();
        for (var trait: Trait.values()) {
            map.put(trait, new ArrayList<>());
        }
        return map;
    }
    private HashMap<Trait, ArrayList<Source>> sources = emptyTraits();

    public void addSource(Trait trait, Apply apply, Function<QueryArgs, Double> function) {
        addSource(trait, new Source(apply, function));
    }

    public void addSource(Trait trait, Source source) {
        sources.get(trait).add(source);
        sources.get(trait).sort(Comparator.comparingInt(a -> a.apply.ordinal()));
    }

    public double getValue(Trait trait, QueryArgs query) {
        var traitSources = sources.get(trait);
        var value = 0F;
        switch (trait) {
            // Base value
            case POWER, CRIT_CHANCE -> { value = 0; }
            case HASTE, CRIT_DAMAGE -> { value = 1; }
        }
        var multiplier = 1F;
        for (var source: traitSources) {
            switch (source.apply) {
                case ADD -> value += source.function.apply(query);
                case MULTIPLY -> multiplier += source.function.apply(query);
            };
        }
        value *= multiplier;
        return value;
    }
}