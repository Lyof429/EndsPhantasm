package net.lyof.phantasm.world;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.world.feature.CrystalSpikeFeature;
import net.lyof.phantasm.world.feature.SingleBlockFeature;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModFeatures {
    public static void register() {
        Registry.register(Registries.FEATURE, Phantasm.makeID("crystal_spike"), CrystalSpikeFeature.INSTANCE);
        Registry.register(Registries.FEATURE, Phantasm.makeID("single_block"), SingleBlockFeature.INSTANCE);
    }
}
