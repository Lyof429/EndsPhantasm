package net.lyof.phantasm.world.feature;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.minecraft.world.gen.placementmodifier.*;

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


        register(context, PREAM, configLookup.getOrThrow(ModConfiguredFeatures.PREAM),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        PlacedFeatures.createCountExtraModifier(1, 0.5f, 2),
                        ModBlocks.PREAM_SAPLING));

        register(context, CRYSTAL_SPIKE, configLookup.getOrThrow(ModConfiguredFeatures.CRYSTAL_SPIKE),
                SquarePlacementModifier.of(),
                RarityFilterPlacementModifier.of(4),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of(),
                PlacedFeatures.createCountExtraModifier(2, 0.5f, 2));

        register(context, FALLEN_STAR, configLookup.getOrThrow(ModConfiguredFeatures.FALLEN_STAR),
                SquarePlacementModifier.of(),
                RarityFilterPlacementModifier.of(3));

        register(context, VIVID_NIHILIS, configLookup.getOrThrow(ModConfiguredFeatures.VIVID_NIHILIS),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of());

        register(context, TALL_VIVID_NIHILIS, configLookup.getOrThrow(ModConfiguredFeatures.TALL_VIVID_NIHILIS),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of());

        register(context, STARFLOWER_PATCH, configLookup.getOrThrow(ModConfiguredFeatures.STARFLOWER),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of());

        register(context, SHATTERED_TOWER, configLookup.getOrThrow(ModConfiguredFeatures.SHATTERED_TOWER),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP);

        register(context, RAW_PURPUR_COAL_ORE, configLookup.getOrThrow(ModConfiguredFeatures.RAW_PURPUR_COAL_ORE),
                CountPlacementModifier.of(32),
                SquarePlacementModifier.of(),
                HeightRangePlacementModifier.uniform(YOffset.BOTTOM, YOffset.TOP));

        register(context, OBLIVINE_PATCH, configLookup.getOrThrow(ModConfiguredFeatures.OBLIVINE),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of(),
                PlacedFeatures.createCountExtraModifier(10, 1, 3));

        register(context, ACIDIC_NIHILIS, configLookup.getOrThrow(ModConfiguredFeatures.ACIDIC_NIHILIS),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of());

        register(context, TALL_ACIDIC_NIHILIS, configLookup.getOrThrow(ModConfiguredFeatures.TALL_ACIDIC_NIHILIS),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of());

        register(context, DRAGON_MINT, configLookup.getOrThrow(ModConfiguredFeatures.DRAGON_MINT),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of());

        register(context, DRALGAE, configLookup.getOrThrow(ModConfiguredFeatures.DRALGAE),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of(),
                PlacedFeatures.createCountExtraModifier(10, 1, 5));

        register(context, TALL_DRALGAE, configLookup.getOrThrow(ModConfiguredFeatures.TALL_DRALGAE),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of(),
                PlacedFeatures.createCountExtraModifier(10, 1, 5));

        register(context, HUGE_DRALGAE, configLookup.getOrThrow(ModConfiguredFeatures.HUGE_DRALGAE),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of(),
                RarityFilterPlacementModifier.of(2));

        register(context, CIRITE_SPIKE, configLookup.getOrThrow(ModConfiguredFeatures.CIRITE_SPIKE),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of(),
                RarityFilterPlacementModifier.of(4));

        register(context, CIRITE_CEILING_SPIKE, configLookup.getOrThrow(ModConfiguredFeatures.CIRITE_CEILING_SPIKE),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of(),
                RarityFilterPlacementModifier.of(2));

        register(context, CHORAL_RIFF, configLookup.getOrThrow(ModConfiguredFeatures.CHORAL_RIFF),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of());

        register(context, CHORAL_FAN, configLookup.getOrThrow(ModConfiguredFeatures.CHORAL_FAN),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of(),
                PlacedFeatures.createCountExtraModifier(10, 1, 5));
    }



    public static final RegistryKey<PlacedFeature> PREAM = create("pream");

    public static final RegistryKey<PlacedFeature> CRYSTAL_SPIKE = create("crystal_spike");
    public static final RegistryKey<PlacedFeature> FALLEN_STAR = create("fallen_star");

    public static final RegistryKey<PlacedFeature> VIVID_NIHILIS = create("patch_vivid_nihilis");
    public static final RegistryKey<PlacedFeature> TALL_VIVID_NIHILIS = create("patch_tall_vivid_nihilis");
    public static final RegistryKey<PlacedFeature> STARFLOWER_PATCH = create("patch_starflower");

    public static final RegistryKey<PlacedFeature> SHATTERED_TOWER = create("shattered_tower");

    public static final RegistryKey<PlacedFeature> RAW_PURPUR_COAL_ORE = create("raw_purpur_coal_ore");
    public static final RegistryKey<PlacedFeature> OBLIVINE_PATCH = create("patch_oblivine");

    public static final RegistryKey<PlacedFeature> ACIDIC_NIHILIS = create("patch_acidic_nihilis");
    public static final RegistryKey<PlacedFeature> TALL_ACIDIC_NIHILIS = create("patch_tall_acidic_nihilis");

    public static final RegistryKey<PlacedFeature> DRAGON_MINT = create("patch_dragon_mint");

    public static final RegistryKey<PlacedFeature> DRALGAE = create("dralgae");
    public static final RegistryKey<PlacedFeature> TALL_DRALGAE = create("tall_dralgae");
    public static final RegistryKey<PlacedFeature> HUGE_DRALGAE = create("huge_dralgae");

    public static final RegistryKey<PlacedFeature> CIRITE_SPIKE = create("cirite_spike");
    public static final RegistryKey<PlacedFeature> CIRITE_CEILING_SPIKE = create("cirite_ceiling_spike");

    public static final RegistryKey<PlacedFeature> CHORAL_RIFF = create("choral_riff");
    public static final RegistryKey<PlacedFeature> CHORAL_FAN = create("patch_choral_fan");
}
