package net.spelldamage.forge;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.spelldamage.SpellDamage;
import net.minecraftforge.fml.common.Mod;
import net.spelldamage.api.Enchantments_SpellDamage;
import net.spelldamage.api.EntityAttributes_SpellDamage;

@Mod(SpellDamage.MOD_ID)
public class ForgeMod {
    public ForgeMod() {
        // Submit our event bus to let architectury register our content on the right time
        // EventBuses.registerModEventBus(SpellDamage.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        SpellDamage.init();
        SpellDamage.configureEnchantments();
    }

    @SubscribeEvent
    public void register(RegisterEvent event) {
        // These don't seem to do anything :D
        event.register(ForgeRegistries.Keys.ATTRIBUTES,
                helper -> {
                    for(var entry: EntityAttributes_SpellDamage.types.entrySet()) {
                        helper.register(entry.getKey().attributeId(), entry.getValue());
                    }
                }
        );
        event.register(ForgeRegistries.Keys.ENCHANTMENTS,
                helper -> {
                    helper.register(Enchantments_SpellDamage.spellPowerId, Enchantments_SpellDamage.SPELL_POWER);
                }
        );
    }
}