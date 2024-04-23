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
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.ArrayList;
import java.util.List;

public class ModPlacedFeatures {
    public static RegistryKey<PlacedFeature> create(String name) {
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
        //modifiers.add(RarityFilterPlacementModifier.of(2));
        modifiers.addAll(VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                PlacedFeatures.createCountExtraModifier(1, 0.5f, 2),
                ModBlocks.PREAM_SAPLING));


        register(context, PREAM, configLookup.getOrThrow(ModConfiguredFeatures.PREAM),
                modifiers);
        modifiers.add(RarityFilterPlacementModifier.of(6));
        register(context, TALL_PREAM, configLookup.getOrThrow(ModConfiguredFeatures.TALL_PREAM),
                modifiers);

        register(context, CRYSTAL_SPIKE, configLookup.getOrThrow(ModConfiguredFeatures.CRYSTAL_SPIKE),
                SquarePlacementModifier.of(),
                RarityFilterPlacementModifier.of(2));

        register(context, FALLEN_STAR, configLookup.getOrThrow(ModConfiguredFeatures.FALLEN_STAR),
                SquarePlacementModifier.of(),
                RarityFilterPlacementModifier.of(3));

        register(context, VIVID_NIHILIUM_PATCH, configLookup.getOrThrow(ModConfiguredFeatures.VIVID_NIHILIUM),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of());

        register(context, TALL_VIVID_NIHILIUM_PATCH, configLookup.getOrThrow(ModConfiguredFeatures.TALL_VIVID_NIHILIUM),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of());

    }



    public static final RegistryKey<PlacedFeature> PREAM = create("pream");
    public static final RegistryKey<PlacedFeature> TALL_PREAM = create("tall_pream");

    public static final RegistryKey<PlacedFeature> CRYSTAL_SPIKE = create("crystal_spike");
    public static final RegistryKey<PlacedFeature> FALLEN_STAR = create("fallen_star");

    public static final RegistryKey<PlacedFeature> VIVID_NIHILIUM_PATCH = create("patch_vivid_nihilis");
    public static final RegistryKey<PlacedFeature> TALL_VIVID_NIHILIUM_PATCH = create("patch_tall_vivid_nihilis");

    //public static final RegistryKey<PlacedFeature> RAW_PURPUR_CABIN = create("raw_purpur_maze");
}
