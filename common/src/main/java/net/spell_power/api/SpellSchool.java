package net.spell_power.api;

import com.google.common.base.Suppliers;
import com.google.gson.annotations.JsonAdapter;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.alchemy.Potion;
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
    @Nullable private final Attribute ownedAttribute;

    /**
     * Status effect that boosts this spell school.
     * Maybe left null, if status effect that boosts the respective attribute already exists.
     * (Like how vanilla Strength boosts attack damage)
     */
    @Nullable public final MobEffect ownedBoostEffect;

    /**
     * Spells of this school deal this type of damage
     */
    public final ResourceKey<DamageType> damageType;

    @Nullable public Holder<Attribute> attributeEntry;
    @Nullable public Holder<Potion> potionEntry;

    public SpellSchool(Archetype archetype, Identifier id, int color, ResourceKey<DamageType> damageType, Holder<Attribute> attributeEntry) {
        this(archetype, id, color, damageType, null, null);
        this.attributeEntry = attributeEntry;
    }

    public SpellSchool(Archetype archetype, Identifier id, int color, ResourceKey<DamageType> damageType, Attribute attribute, @Nullable MobEffect boostEffect) {
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
            attributeEntry = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, id, ownedAttribute);
        }
    }

    public void registerPotion() {
        if (ownedBoostEffect != null) {
            var entry = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ownedBoostEffect);
            if (entry != null) {
                var potionId = SpellPowerMod.potionIdFrom(id);
                var potion = new Potion(potionId.getPath(), new MobEffectInstance(entry, 3600));
                Registry.register(BuiltInRegistries.POTION, potionId, potion);
            }
        }
    }

    public Holder<Attribute> getAttributeEntry() {
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