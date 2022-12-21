package net.spell_power.api.attributes;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class CustomEntityAttribute extends ClampedEntityAttribute {
    public UUID weaponUUID;
    public CustomEntityAttribute(String translationKey, double fallback, double min, double max, Identifier id) {
        super(translationKey, fallback, min, max);
        this.weaponUUID = UUID.nameUUIDFromBytes(id.toString().getBytes());
    }
}
