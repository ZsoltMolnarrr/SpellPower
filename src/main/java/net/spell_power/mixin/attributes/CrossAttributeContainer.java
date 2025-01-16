package net.spell_power.mixin.attributes;

import com.google.common.collect.Multimap;
import net.minecraft.entity.attribute.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.spell_power.internals.CrossFunctionalAttributes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(AttributeContainer.class)
public class CrossAttributeContainer
        implements CrossFunctionalAttributes.Proxy {

    // Inject to init tail

    @Shadow @Final private Map<RegistryEntry<EntityAttribute>, EntityAttributeInstance> custom;

    @Shadow @Final private DefaultAttributeContainer fallback;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init_Tail(DefaultAttributeContainer defaultAttributes, CallbackInfo ci) {
        connect();
    }

    @Inject(method = "getCustomInstance", at = @At("RETURN"))
    private void getCustomInstance_Return(RegistryEntry<EntityAttribute> attribute, CallbackInfoReturnable<EntityAttributeInstance> cir) {
        var instance = cir.getReturnValue();
        if (instance != null) {
            ((CrossFunctionalAttributes.Provider)instance).setProxy(this);
        }
    }

    @Inject(method = "addTemporaryModifiers", at = @At("TAIL"))
    private void addTemporaryModifiers_Head(Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> modifiersMap, CallbackInfo ci) {
        for (var entry: modifiersMap.entries()) {
            var instance = custom.get(entry.getKey());
            if (instance != null) {
                var attribute = instance.getAttribute();
                this.updateProvidersPoweredBy(attribute);
            }
        }
    }

    // @Override
    private void connect() {
        for(var entry: custom.entrySet()) {
            var instance = entry.getValue();
            ((CrossFunctionalAttributes.Provider)instance).setProxy(this);
        }
        ((CrossFunctionalAttributes.Fallback)fallback).setProxy(this);
    }

    @Override
    public void updateProvidersPowering(RegistryEntry<EntityAttribute> attribute) {
        for (var provider: getCrossProvidersPowering(attribute)) {
            provider.updateIfNecessaryForCross();
        }
    }

    @Override
    public void updateProvidersPoweredBy(RegistryEntry<EntityAttribute> attribute) {
        for (var provider: getCrossProvidersPoweredBy(attribute)) {
            provider.setDirtyForCross();
            provider.updateIfNecessaryForCross();
        }
    }

    @Override
    public List<CrossFunctionalAttributes.Provider> getCrossProvidersPowering(RegistryEntry<EntityAttribute> attribute) {
        var crossAttributes = CrossFunctionalAttributes.getPoweringOf(attribute);
        return crossAttributes.stream()
                .map(crossAttribute -> {
                    var instance = custom.get(crossAttribute);
                    if (instance == null) {
                        instance = ((CrossFunctionalAttributes.Fallback)fallback).getAttributeInstances().get(crossAttribute);
                    }
                    return instance;
                })
                .map(instance -> (CrossFunctionalAttributes.Provider)instance)
                .toList();
    }

    @Override
    public List<CrossFunctionalAttributes.Provider> getCrossProvidersPoweredBy(RegistryEntry<EntityAttribute> attribute) {
        var crossAttributes = CrossFunctionalAttributes.getPoweredBy(attribute);
        return crossAttributes.stream()
                .map(crossAttribute -> {
                    var instance = custom.get(crossAttribute);
                    if (instance == null) {
                        instance = ((CrossFunctionalAttributes.Fallback)fallback).getAttributeInstances().get(crossAttribute);
                    }
                    return instance;
                })
                .map(instance -> (CrossFunctionalAttributes.Provider)instance)
                .toList();
    }
}
