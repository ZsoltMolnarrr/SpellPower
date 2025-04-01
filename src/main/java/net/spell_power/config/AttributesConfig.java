package net.spell_power.config;

import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.internals.SpellStatusEffect;

import java.util.Map;

public class AttributesConfig {
    public enum AttributeScope {
        LIVING_ENTITY, PLAYER_ENTITY
    }
    public AttributeScope attributes_container_injection_scope = AttributeScope.LIVING_ENTITY;
    public double base_spell_critical_chance_percentage = 5;
    public double base_spell_critical_damage_percentage = 50;
    public SpellStatusEffect.Config spell_power_effect = new SpellStatusEffect.Config(0.1F);
    public Map<String, SpellStatusEffect.Config> secondary_effects;
    public float resistance_multiplier = 1F;
    public float resistance_reduction_cap = 0.75F;
    public boolean register_potions = false;

    public static AttributesConfig defaults() {
        var config = new AttributesConfig();
        config.secondary_effects = Map.of(
                SpellPowerMechanics.CRITICAL_CHANCE.name, new SpellStatusEffect.Config(0.05F),
                SpellPowerMechanics.CRITICAL_DAMAGE.name, new SpellStatusEffect.Config(0.1F),
                SpellPowerMechanics.HASTE.name, new SpellStatusEffect.Config(0.05F)
        );
        return config;
    }

    public boolean isValid() {
        var defaults = defaults();

        if (attributes_container_injection_scope == null) {
            return false;
        }
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
}
