package net.lyof.phantasm.world;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;

import java.util.ArrayList;
import java.util.List;

public class ModPlacedFeatures {
    public static RegistryKey<PlacedFeature> register(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Phantasm.makeID(name));
    }

    private static void register(Registerable<PlacedFeature> context,
                                 RegistryKey<PlacedFeature> key,
                                 RegistryEntry<ConfiguredFeature<?, ?>> config,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(config, List.copyOf(modifiers)));
    }

    private static void register(Registerable<PlacedFeature> context,
                                 RegistryKey<PlacedFeature> key,
                                 RegistryEntry<ConfiguredFeature<?, ?>> config,
                                 PlacementModifier... modifiers) {
        register(context, key, config, List.of(modifiers));
    }

    public static void bootstrap(Registerable<PlacedFeature> context) {
        var configLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        List<PlacementModifier> modifiers = new ArrayList<>();
        modifiers.add(RarityFilterPlacementModifier.of(2));
        modifiers.addAll(VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                PlacedFeatures.createCountExtraModifier(1, 0.5f, 2),
                ModBlocks.PREAM_SAPLING));


        register(context, PREAM_PLACED_KEY, configLookup.getOrThrow(ModConfiguredFeatures.PREAM_KEY),
                modifiers);

        //register(context, CRYSTAL_SPIKE_PLACED_KEY, configLookup.getOrThrow(ModConfiguredFeatures.CRYSTAL_SPIKE_KEY),
        //        modifiers);
    }



    public static final RegistryKey<PlacedFeature> PREAM_PLACED_KEY = register("pream_placed");
    //public static final RegistryKey<PlacedFeature> CRYSTAL_SPIKE_PLACED_KEY = register("crystal_spike_placed");
}
