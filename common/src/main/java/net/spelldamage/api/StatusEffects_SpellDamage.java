package net.spelldamage.api;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.spelldamage.SpellDamage;
import net.spelldamage.config.StatusEffectConfig;
import net.spelldamage.internals.SpellStatusEffect;

import java.util.HashMap;
import java.util.Map;

public class StatusEffects_SpellDamage {
    private static StatusEffectConfig config() {
        return SpellDamage.effectsConfig.currentConfig;
    }

    private static SpellStatusEffect createDamageEffect(MagicSchool school) {
        var config = config().damage_effects.get(school.spellName());
        var statusEffect = new SpellStatusEffect(StatusEffectCategory.BENEFICIAL, config.color);
        statusEffect.addAttributeModifier(
                EntityAttributes_SpellDamage.DAMAGE.get(school),
                config.udid,
                config.bonus_per_stack,
                EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        return statusEffect;
    }

    public static final Map<MagicSchool, SpellStatusEffect> DAMAGE;
    public static final Map<Identifier, SpellStatusEffect> all;
    static {
        all = new HashMap<>();
        DAMAGE = new HashMap<>();
        for(var school: MagicSchool.values()) {
            var id = new Identifier(SpellDamage.MOD_ID, school.spellName());
            var effect = createDamageEffect(school);
            DAMAGE.put(school, effect);
            all.put(id, effect);
        }
    }

    public static class AttributeModifierProperties {
        public int color = 0xFFFFFF;
        public String udid;
        public float bonus_per_stack = 0.1F;

        public AttributeModifierProperties() { }

        public AttributeModifierProperties(int color, String udid, float bonus_per_stack) {
            this.color = color;
            this.udid = udid;
            this.bonus_per_stack = bonus_per_stack;
        }
    }
}
