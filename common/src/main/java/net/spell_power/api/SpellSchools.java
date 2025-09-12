package net.spell_power.api;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;
import net.spell_power.internals.CustomEntityAttribute;
import net.spell_power.internals.SpellStatusEffect;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import static net.spell_power.api.SpellPowerMechanics.PERCENT_ATTRIBUTE_BASELINE;

public class SpellSchools {

    // Registration

    /**
     * Default namespace for spell schools
     */
    public static final String DEFAULT_NAMESPACE = SpellPowerMod.ID;
    private static final LinkedHashMap<Identifier, SpellSchool> REGISTRY = new LinkedHashMap<>();

    public static SpellSchool register(SpellSchool school) {
        REGISTRY.put(school.id, school);
        return school;
    }

    public static Set<SpellSchool> all() {
        // Using linked hash set to preserve order
        return new LinkedHashSet<SpellSchool>(REGISTRY.values());
    }


    // Predefined Spell Schools

    public static final SpellSchool GENERIC = register(createMagic("generic", 0x9999BB));
    public static final SpellSchool ARCANE = register(createMagic("arcane", 0xff66ff));
    public static final SpellSchool FIRE = register(createMagic("fire", 0xff3300));
    public static final SpellSchool FROST = register(createMagic("frost", 0xccffff));
    public static final SpellSchool HEALING = register(createMagic("healing", 0x66ff66));
    public static final SpellSchool LIGHTNING = register(createMagic("lightning", 0xffff99));
    public static final SpellSchool SOUL = register(createMagic("soul", 0x2dd4da));

    // School Creation

    public static SpellSchool createMagic(String name, int color) {
        return createMagic(Identifier.of(DEFAULT_NAMESPACE, name.toLowerCase()), color);
    }

    public static SpellSchool createMagic(Identifier id, int color) {
        var powerEffect = new SpellStatusEffect(StatusEffectCategory.BENEFICIAL, color);

        var translationPrefix = "attribute.name." + id.getNamespace() + ".";
        var attribute = new CustomEntityAttribute(translationPrefix + id.getPath(), 0, 0, 2048, id).setTracked(true);

        return createMagic(id, color, true, attribute, powerEffect);
    }

    public static SpellSchool createMagic(Identifier id, int color, boolean customDamageType, EntityAttribute powerAttribute, StatusEffect powerEffect) {
        var school = new SpellSchool(
                SpellSchool.Archetype.MAGIC,
                id,
                color,
                customDamageType ? RegistryKey.of(RegistryKeys.DAMAGE_TYPE, id) : DamageTypes.MAGIC,
                powerAttribute,
                powerEffect);
        return configureAsMagic(school);
    }


    public static SpellSchool configureAsMagic(SpellSchool school) {
        school.addSource(SpellSchool.Trait.POWER, new SpellSchool.Source(SpellSchool.Apply.ADD, query ->
            query.entity().getAttributeValue(school.getAttributeEntry()))
        );
        // Spell Power Enchantments added by Enchantments_SpellDamage.attach
        configureSpellHaste(school);
        configureSpellCritChance(school);
        configureSpellCritDamage(school);
        return school;
    }

    public static SpellSchool configureSpellHaste(SpellSchool school) {
        school.addSource(SpellSchool.Trait.HASTE, new SpellSchool.Source(SpellSchool.Apply.ADD, query -> {
            var value = query.entity().getAttributeValue(SpellPowerMechanics.HASTE.attributeEntry); // 110
            var rate = (value / PERCENT_ATTRIBUTE_BASELINE);    // For example: 110/100 = 1.1
            return rate - 1;  // 0.1
        }));
        return school;
    }

    public static SpellSchool configureSpellCritChance(SpellSchool school) {
        school.addSource(SpellSchool.Trait.CRIT_CHANCE, new SpellSchool.Source(SpellSchool.Apply.ADD, query ->  {
            var value = SpellPowerMod.attributesConfig.value.base_spell_critical_chance_percentage  // 5
                    + query.entity().getAttributeValue(SpellPowerMechanics.CRITICAL_CHANCE.attributeEntry);    // 20
            return (value / PERCENT_ATTRIBUTE_BASELINE) - 1;    // For example: (125/100) - 1 = 0.25
        }));
        return school;
    }

    public static SpellSchool configureSpellCritDamage(SpellSchool school) {
        school.addSource(SpellSchool.Trait.CRIT_DAMAGE, new SpellSchool.Source(SpellSchool.Apply.ADD, query -> {
            var value = SpellPowerMod.attributesConfig.value.base_spell_critical_damage_percentage          // 50
                    + query.entity().getAttributeValue(SpellPowerMechanics.CRITICAL_DAMAGE.attributeEntry);    // 110
            var rate = (value / PERCENT_ATTRIBUTE_BASELINE);    // For example: 160/100 = 1.6
            return rate - 1;    // 0.6
        }));
        return school;
    }

    // Utility

    @Nullable public static SpellSchool getSchool(String idString) {
        var string = idString.toLowerCase(Locale.US);
        var id = Identifier.of(string);
        // Replacing default namespace
        if (id.getNamespace().equals(Identifier.DEFAULT_NAMESPACE)) {
            id = Identifier.of(DEFAULT_NAMESPACE, id.getPath());
        }
        return REGISTRY.get(id);
    }
}
