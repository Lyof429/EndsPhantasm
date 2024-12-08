package net.lyof.phantasm.world;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.TheEndBiomes;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.biome.ModBiomes;
import net.lyof.phantasm.world.feature.ModPlacedFeatures;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;

public class ModWorldGeneration {
    public static void register() {
        generateTrees();
        generateFeatures();
        generateBiomes();

        generateSpawns();
        generateSpawnRestrictions();
    }


    public static RegistryEntryLookup<Biome> LOOKUP;

    public static void register(RegistryEntryLookup<Biome> lookup) {
        LOOKUP = lookup;

        //DREAMING_DEN = lookup.getOrThrow(ModBiomes.DREAMING_DEN);
        //ACIDBURNT_ABYSSES = lookup.getOrThrow(ModBiomes.ACIDBURNT_ABYSSES);
    }


    private static void generateTrees() {
        if (ConfigEntries.doPreamTrees)
            BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.DREAMING_DEN),
                    GenerationStep.Feature.VEGETAL_DECORATION,
                    ModPlacedFeatures.PREAM);
    }

    private static void generateFeatures() {
        if (ConfigEntries.doCrystalSpikes)
            BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.DREAMING_DEN),
                    GenerationStep.Feature.VEGETAL_DECORATION,
                    ModPlacedFeatures.CRYSTAL_SPIKE);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.DREAMING_DEN),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.VIVID_NIHILIS);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.DREAMING_DEN),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.TALL_VIVID_NIHILIS);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.DREAMING_DEN),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.STARFLOWER_PATCH);

        if (ConfigEntries.doFallenStars)
            BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(),
                    GenerationStep.Feature.SURFACE_STRUCTURES,
                    ModPlacedFeatures.FALLEN_STAR);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.DREAMING_DEN),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.OBLIVINE_PATCH);


        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.ACIDBURNT_ABYSSES),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.ACIDIC_NIHILIS);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.ACIDBURNT_ABYSSES),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.TALL_ACIDIC_NIHILIS);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.ACIDBURNT_ABYSSES),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.DRAGON_MINT);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.ACIDBURNT_ABYSSES),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.DRALGAE);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.ACIDBURNT_ABYSSES),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.TALL_DRALGAE);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.ACIDBURNT_ABYSSES),
                GenerationStep.Feature.TOP_LAYER_MODIFICATION,
                ModPlacedFeatures.HUGE_DRALGAE);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.ACIDBURNT_ABYSSES),
                GenerationStep.Feature.SURFACE_STRUCTURES,
                ModPlacedFeatures.CIRITE_BOULDER);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.ACIDBURNT_ABYSSES),
                GenerationStep.Feature.SURFACE_STRUCTURES,
                ModPlacedFeatures.CIRITE_SPIKE);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.ACIDBURNT_ABYSSES),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.CHORAL_RIFF);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.ACIDBURNT_ABYSSES),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.CHORAL_FAN);
    }

    private static void generateBiomes() {
        if (ConfigEntries.doDreamingDenBiome)
            TheEndBiomes.addHighlandsBiome(ModBiomes.DREAMING_DEN, ConfigEntries.dreamingDenWeight);

        if (ConfigEntries.doAcidburntAbyssesBiome)
            TheEndBiomes.addHighlandsBiome(ModBiomes.ACIDBURNT_ABYSSES, ConfigEntries.acidburntAbyssesWeight);
    }

    private static void generateSpawns() {
        BiomeModifications.addSpawn(BiomeSelectors.tag(ModTags.Biomes.DREAMING_DEN),
                SpawnGroup.MONSTER,
                ModEntities.CRYSTIE,
                10, 2, 4);

        BiomeModifications.addSpawn(BiomeSelectors.tag(ModTags.Biomes.DREAMING_DEN),
                SpawnGroup.MONSTER,
                ModEntities.BEHEMOTH,
                7, 1, 1);
    }

    private static void generateSpawnRestrictions() {
        SpawnRestriction.register(ModEntities.BEHEMOTH, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canMobSpawn);
    }
}
