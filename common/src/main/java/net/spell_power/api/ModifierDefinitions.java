package net.spell_power.api;

import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/// Attribute-modifier identities.
///
/// 1.20.1 attribute modifiers are keyed by `UUID` (+ a free-form name) rather than by `Identifier`. To keep the
/// modern, namespaced ids as the source of truth, every modifier UUID is derived deterministically from its
/// `Identifier` via {@link #uuid(Identifier)} — the same recipe the legacy `CustomEntityAttribute.nameUUID` used —
/// so ids stay stable across worlds and cannot collide across namespaces. Consumer mods should use the same bridge
/// for their own `Identifier`-keyed modifiers.
public class ModifierDefinitions {
    public static Identifier INNATE_BONUS = new Identifier(SpellPowerMod.ID, "innate_bonus");
    public static Identifier POTION_EFFECT = new Identifier(SpellPowerMod.ID, "potion_effect");

    public static final UUID INNATE_BONUS_UUID = uuid(INNATE_BONUS);
    public static final UUID POTION_EFFECT_UUID = uuid(POTION_EFFECT);

    /// Deterministic modifier UUID for a namespaced modifier id.
    public static UUID uuid(Identifier id) {
        return UUID.nameUUIDFromBytes(id.toString().getBytes(StandardCharsets.UTF_8));
    }

    /// Modifier display name for a namespaced modifier id (the `String name` ctor argument of `EntityAttributeModifier`).
    public static String name(Identifier id) {
        return id.toString();
    }
}
