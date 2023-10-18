package net.lyof.phantasm.world;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.world.feature.CrystalSpikeFeature;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.feature.Feature;

public class ModFeatures {
    public static RegistryKey<Feature<?>> register(String name) {
        return RegistryKey.of(RegistryKeys.FEATURE, Phantasm.makeID(name));
    }

    private static void register(Registerable<Feature<?>> context,
                                 RegistryKey<Feature<?>> key,
                                 Feature<?> feature) {
        context.register(key, feature);
    }

    public static void bootstrap(Registerable<Feature<?>> context) {
        register(context, CRYSTAL_SPIKE, new CrystalSpikeFeature());
    }


    public static final RegistryKey<Feature<?>> CRYSTAL_SPIKE = register("crystal_spike");
}
