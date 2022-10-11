package net.spelldamage.api;

import net.minecraft.util.Identifier;
import net.spelldamage.SpellDamage;

public enum MagicSchool {
    FIRE, FROST, SHADOW;

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
            case FIRE -> {
                return 0xff3300;
            }
            case FROST -> {
                return 0x66c2ff;
            }
            case SHADOW -> {
                return 0x660066;
            }
        }
        assert true;
        return 0xffffff;
    }
}
