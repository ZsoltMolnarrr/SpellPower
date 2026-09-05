package net.spell_power.internals;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.util.Identifier;
import net.spell_power.api.ModifierDefinitions;

import java.util.UUID;

public class CustomEntityAttribute extends ClampedEntityAttribute {
    public final Identifier id;
    /// Deterministic UUID derived from the attribute id (legacy convenience for consumers that want
    /// one well-known modifier UUID per attribute). See {@link ModifierDefinitions#uuid(Identifier)}.
    public final UUID nameUUID;

    public CustomEntityAttribute(String translationKey, double fallback, double min, double max, Identifier id) {
        super(translationKey, fallback, min, max);
        this.id = id;
        this.nameUUID = ModifierDefinitions.uuid(id);
    }
}
