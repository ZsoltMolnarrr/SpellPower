package net.spell_power.api;

import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public enum MagicSchool {
    ARCANE, FIRE, FROST, HEALING, LIGHTNING, SOUL,
    PHYSICAL_MELEE(new Identifier("generic.attack_speed"), SpellDamageSource.Configurator.MELEE);

    @Nullable private final Identifier externalAttributeId;
    private final Consumer<SpellDamageSource> damageSourceConfigurator;
    public boolean isExternalAttribute() {
        return externalAttributeId != null;
    }

    MagicSchool() {
        this(null, SpellDamageSource.Configurator.MAGIC);
    }

    MagicSchool(@Nullable Identifier externalAttributeId, Consumer<SpellDamageSource> damageSourceConfigurator) {
        this.externalAttributeId = externalAttributeId;
        this.damageSourceConfigurator = damageSourceConfigurator;
    }

    public static MagicSchool fromAttributeId(Identifier id) {
        return valueOf(id.getPath().toUpperCase());
    }

    public String spellName() {
        return this.toString().toLowerCase();
    }

    public Identifier attributeId() {
        if (externalAttributeId != null) {
            return externalAttributeId;
        }
        return new Identifier(SpellPowerMod.ID, spellName());
    }

    public Consumer<SpellDamageSource> damageSourceConfigurator() {
        return damageSourceConfigurator;
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
