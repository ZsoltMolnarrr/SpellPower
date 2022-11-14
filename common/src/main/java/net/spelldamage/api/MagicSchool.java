package net.spelldamage.api;

import net.minecraft.util.Identifier;
import net.spelldamage.SpellDamage;

public enum MagicSchool {
    ARCANE, FIRE, FROST, HEALING, LIGHTNING, SOUL;

    public static MagicSchool fromAttributeId(Identifier id) {
        return valueOf(id.getPath().toUpperCase());
    }

    public String spellName() {
        return this.toString().toLowerCase();
    }

    public Identifier attributeId() {
        return new Identifier(SpellDamage.MOD_ID, spellName());
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
        }
        assert true;
        return 0xffffff;
    }
}
