package net.spell_power.internals;

import com.google.common.collect.HashMultimap;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CrossFunctionalAttributes {
    /**
     * Register a cross-functional attributes.
     * - key: what is powered
     * - value: by what other attributes
     */
    private static final HashMultimap<RegistryEntry<EntityAttribute>, RegistryEntry<EntityAttribute>> cross = HashMultimap.create();
    /**
     * Register of reverse cross-functional attributes.
     * - key: what is powering
     * - value: what is powered
     */
    private static final HashMultimap<RegistryEntry<EntityAttribute>, RegistryEntry<EntityAttribute>> reverse = HashMultimap.create();

    public static void power(RegistryEntry<EntityAttribute> powered, RegistryEntry<EntityAttribute> by) {
        cross.put(powered, by);
        reverse.put(by, powered);
    }

    /**
     * Returns what attributes power the given attribute.
     */
    public static Set<RegistryEntry<EntityAttribute>> getPoweringOf(RegistryEntry<EntityAttribute> attribute) {
        return cross.get(attribute);
    }

    /**
     * Returns what attributes are powered by the given attribute.
     */
    public static Set<RegistryEntry<EntityAttribute>> getPoweredBy(RegistryEntry<EntityAttribute> attribute) {
        return reverse.get(attribute);
    }

    public interface Proxy {
        void updateProvidersPowering(RegistryEntry<EntityAttribute> attribute);
        void updateProvidersPoweredBy(RegistryEntry<EntityAttribute> attribute);
        List<Provider> getCrossProvidersPowering(RegistryEntry<EntityAttribute> attribute);
        List<Provider> getCrossProvidersPoweredBy(RegistryEntry<EntityAttribute> attribute);
    }

    public interface Fallback {
        void setProxy(Proxy proxy);
        Map<RegistryEntry<EntityAttribute>, EntityAttributeInstance> getAttributeInstances();
    }

    public interface Provider {
        void setProxy(Proxy proxy);
        void setDirtyForCross();
        void updateIfNecessaryForCross();
        Collection<EntityAttributeModifier> getModifiersByOperation_Cross(EntityAttributeModifier.Operation operation);
    }
}
