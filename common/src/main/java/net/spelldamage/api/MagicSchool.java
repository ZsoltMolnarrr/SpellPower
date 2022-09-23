package net.spelldamage.api;

import net.minecraft.util.Identifier;
import net.spelldamage.SpellDamage;

public enum MagicSchool {
    FIRE, FROST, SHADOW;

    public String spellName() {
        return this.toString().toLowerCase();
    }

    public Identifier attributeId() {
        return new Identifier(SpellDamage.MOD_ID, spellName());
    }
}
