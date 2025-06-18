package net.lyof.phantasm.world.feature;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.world.feature.custom.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModFeatures {
    public static void register() {
        Registry.register(Registries.FEATURE, Phantasm.makeID("crystal_spike"), CrystalSpikeFeature.INSTANCE);
        Registry.register(Registries.FEATURE, Phantasm.makeID("single_block"), SingleBlockFeature.INSTANCE);
        Registry.register(Registries.FEATURE, Phantasm.makeID("shattered_tower"), ShatteredTowerStructure.INSTANCE);
        Registry.register(Registries.FEATURE, Phantasm.makeID("oblivine"), OblivineFeature.INSTANCE);
        Registry.register(Registries.FEATURE, Phantasm.makeID("dralgae"), DralgaeFeature.INSTANCE);
        Registry.register(Registries.FEATURE, Phantasm.makeID("huge_dralgae"), HugeDralgaeFeature.INSTANCE);
        Registry.register(Registries.FEATURE, Phantasm.makeID("boulder"), BoulderFeature.INSTANCE);
        Registry.register(Registries.FEATURE, Phantasm.makeID("ceiling_boulder"), CeilingBoulderFeature.INSTANCE);
        Registry.register(Registries.FEATURE, Phantasm.makeID("ceiling_spike"), CeilingSpikeFeature.INSTANCE);
    }
}
