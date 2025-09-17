package net.spell_power.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.spell_power.api.statuseffects.VulnerabilityEffect;

import java.util.*;
import java.util.function.Function;

public class SpellPower {
    public record Result(SpellSchool school, double baseValue, double criticalChance, double criticalDamage) {
        public static Result empty(SpellSchool school) {
            return new Result(school, 0, 0, 0);
        }
        private static Random rng = new Random();
        private enum CriticalStrikeMode {
            DISABLED, ALLOWED, FORCED
        }
        public record Value(double amount, boolean isCritical) { }

        public Value random() {
            return value(CriticalStrikeMode.ALLOWED, Vulnerability.none);
        }
        public double randomValue() {
            return random().amount();
        }

        public Value random(Vulnerability vulnerability) {
            return value(CriticalStrikeMode.ALLOWED, vulnerability);
        }
        public double randomValue(Vulnerability vulnerability) {
            return random(vulnerability).amount();
        }

        public Value nonCritical() {
            return value(CriticalStrikeMode.DISABLED, Vulnerability.none);
        }
        public double nonCriticalValue() {
            return nonCritical().amount();
        }

        public Value forcedCritical() {
            return value(CriticalStrikeMode.FORCED, Vulnerability.none);
        }
        public double forcedCriticalValue() {
            return forcedCritical().amount();
        }

        private Value value(CriticalStrikeMode mode, Vulnerability vulnerability) {
            var value = baseValue * (1F + vulnerability.powerBaseMultiplier);
            boolean isCritical = false;
            if (mode != CriticalStrikeMode.DISABLED) {
                isCritical = (mode == CriticalStrikeMode.FORCED) || (rng.nextFloat() < (criticalChance + vulnerability.criticalChanceBonus));
                if (isCritical) {
                    value *= (criticalDamage + vulnerability.criticalDamageBonus);
                }
            }
            return new Value(value, isCritical);
        }
    }

    public record VulnerabilityQuery(LivingEntity entity, SpellSchool school) { }
    public static final ArrayList<Function<VulnerabilityQuery, List<Vulnerability>>> vulnerabilitySources = new ArrayList<Function<VulnerabilityQuery, List<Vulnerability>>>(
            Arrays.asList(
                    (query -> {
                        var vulnerabilities = new ArrayList<Vulnerability>();
                        for(var effect: query.entity.getStatusEffects()) {
                            if (effect.getEffectType().value() instanceof VulnerabilityEffect vulnerabilityEffect) {
                                vulnerabilities.add(vulnerabilityEffect.getVulnerability(query.school, effect.getAmplifier()));
                            }
                        }
                        return vulnerabilities;
                    })
            ));

    public static Vulnerability getVulnerability(LivingEntity livingEntity, SpellSchool school) {
        var query = new VulnerabilityQuery(livingEntity, school);
        var elements = new ArrayList<Vulnerability>();
        for(var source: vulnerabilitySources) {
            elements.addAll(source.apply(query));
        }
        return Vulnerability.sum(elements);
    }

    public record Vulnerability(float powerBaseMultiplier, float criticalChanceBonus, float criticalDamageBonus) {
        public static final Vulnerability none = new Vulnerability(0, 0, 0);
        public static Vulnerability sum(List<Vulnerability> elements) {
            var value = none;
            for(var element: elements) {
                value = new Vulnerability(
                        value.powerBaseMultiplier + element.powerBaseMultiplier,
                        value.criticalChanceBonus + element.criticalChanceBonus,
                        value.criticalDamageBonus + element.criticalDamageBonus
                );
            }
            return value;
        }

        public Vulnerability multiply(float value) {
            return new Vulnerability(powerBaseMultiplier * value, criticalChanceBonus * value, criticalDamageBonus * value);
        }
    }

    public static Result getSpellPower(SpellSchool school, LivingEntity entity) {
        var args = new SpellSchool.QueryArgs(entity);
        var power = school.getValue(SpellSchool.Trait.POWER, args);
        if (school.archetype == SpellSchool.Archetype.MAGIC) {
            var instance = entity.getAttributes().getCustomInstance(school.attributeEntry);
            if (instance != null) {
                var flatPower = getAttributeFlatValue(instance);
                var genericSpellPower = entity.getAttributeValue(SpellSchools.GENERIC.attributeEntry);
                var multiplier = genericSpellPower / SpellSchools.GENERIC.attributeBaseValue();
                power += flatPower * (multiplier - 1);
            }
        }
        return new Result(
                school,
                power,
                school.getValue(SpellSchool.Trait.CRIT_CHANCE, args),
                school.getValue(SpellSchool.Trait.CRIT_DAMAGE, args));
    }

    private static double getAttributeFlatValue(EntityAttributeInstance instance) {
        double result = 0;
        for (var modifier: instance.getModifiers()) {
            if (modifier.operation() == EntityAttributeModifier.Operation.ADD_VALUE) {
                result += modifier.value();
            }
        }
        return result;
    }

    public static float getHaste(LivingEntity entity, SpellSchool school) {
        var args = new SpellSchool.QueryArgs(entity);
        return (float) school.getValue(SpellSchool.Trait.HASTE, args);
    }
}