package net.spelldamage;

import net.spelldamage.api.EntityAttributes_SpellDamage;

public class SpellDamage {
    public static final String MOD_ID = "spelldamage";

    public static void init() {
        var register = EntityAttributes_SpellDamage.all;
    }
}