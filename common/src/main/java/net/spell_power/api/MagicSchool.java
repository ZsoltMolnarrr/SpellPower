package net.spell_power.api;

import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.Consumer;

public enum MagicSchool {
    ARCANE, FIRE, FROST, HEALING, LIGHTNING, SOUL,
    PHYSICAL_MELEE(false, new Identifier("generic.attack_damage"), new Identifier(SpellPowerMod.ID, "spell_physical")),
    PHYSICAL_RANGED(false, new Identifier("projectile_damage:generic"), DamageTypes.ARROW.getValue());

    @Nullable private final Identifier externalAttributeId;
    public final boolean isMagical;
    private Identifier damageTypeId;

    public boolean isExternalAttribute() {
        return externalAttributeId != null;
    }

    MagicSchool() {
        this(true, null, new Identifier(SpellPowerMod.ID, "spell"));
        this.damageTypeId = new Identifier(SpellPowerMod.ID, "spell_" + spellName());
    }

    MagicSchool(boolean isMagical, @Nullable Identifier externalAttributeId, Identifier damageTypeId) {
        this.isMagical = isMagical;
        this.externalAttributeId = externalAttributeId;
        this.damageTypeId = damageTypeId;
    }

    public static MagicSchool fromAttributeId(Identifier id) {
        return valueOf(id.getPath().toUpperCase());
    }

    public String spellName() {
        return this.toString().toLowerCase(Locale.ENGLISH);
    }

    public Identifier attributeId() {
        if (externalAttributeId != null) {
            return externalAttributeId;
        }
        return new Identifier(SpellPowerMod.ID, spellName());
    }

    public Identifier damageTypeId() {
        return damageTypeId;
    }

    public int color() {
        switch (this) {
            case ARCANE -> {
                return 0xff66ff;
            }
            case FIRE -> {
                return 0xff3300;
            }
            case FROST -> {
                return 0xccffff;
            }
            case HEALING -> {
                return 0x66ff66;
            }
            case LIGHTNING -> {
                return 0xffff99;
            }
            case SOUL -> {
                return 0x2dd4da;
            }
            case PHYSICAL_MELEE -> {
                return 0xb3b3b3;
            }
        }
        assert true;
        return 0xffffff;
    }
}