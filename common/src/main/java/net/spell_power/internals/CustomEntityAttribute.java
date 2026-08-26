package net.spell_power.internals;

import java.util.UUID;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class CustomEntityAttribute extends RangedAttribute {
    public CustomEntityAttribute(String translationKey, double fallback, double min, double max, Identifier id) {
        super(translationKey, fallback, min, max);
    }
}
