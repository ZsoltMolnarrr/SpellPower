package net.spell_power.config;

import net.spell_power.api.DamageCurve;

import java.util.Map;

public class AttributesConfig {
    public enum AttributeScope {
        LIVING_ENTITY, PLAYER_ENTITY
    }
    // public AttributeScope attributes_container_injection_scope = AttributeScope.LIVING_ENTITY;
    public boolean migrate_attributes_base = true;
    public float base_spell_power = 1F;
    public double base_spell_critical_chance_percentage = 5;
    public double base_spell_critical_damage_percentage = 50;
    public EffectConfig spell_power_effect = new EffectConfig(0.1F);
    public Map<String, EffectConfig> secondary_effects;
    public boolean enchantments_require_matching_attribute = true;

    public DamageCurve resistance_curve = DamageCurve.HYPERBOLIC;
    public float resistance_multiplier = 1F;
    public float resistance_tuning_constant = 20F;
    public float resistance_reduction_cap = 0.9F;
    public boolean register_potions = false;

    public static AttributesConfig defaults() {
        var config = new AttributesConfig();
        config.secondary_effects = Map.of(
                "critical_chance", new EffectConfig(0.05F),
                "critical_damage", new EffectConfig(0.1F),
                "haste", new EffectConfig(0.05F)
// Disabled due to static init circularity
//                SpellPowerMechanics.CRITICAL_CHANCE.name, new SpellStatusEffect.Config(0.05F),
//                SpellPowerMechanics.CRITICAL_DAMAGE.name, new SpellStatusEffect.Config(0.1F),
//                SpellPowerMechanics.HASTE.name, new SpellStatusEffect.Config(0.05F)
        );
        return config;
    }

    public boolean isValid() {
        var defaults = defaults();

//        if (attributes_container_injection_scope == null) {
//            return false;
//        }
        if (secondary_effects == null) {
            return false;
        }
        for(var entry: defaults.secondary_effects.entrySet()) {
            if (!secondary_effects.containsKey(entry.getKey())) {
                return false;
            }
        }
        if (spell_power_effect == null) {
            return false;
        }

        return true;
    }

    public static class EffectConfig {
        public float bonus_per_stack = 0.1F;

        public EffectConfig() { }

        public EffectConfig(float bonus_per_stack) {
            this.bonus_per_stack = bonus_per_stack;
        }
    }
}
