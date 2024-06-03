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
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.world.gen.GenerationStep;

public class ModWorldGeneration {
    public static void register() {
        generateTrees();
        generateFeatures();
        generateBiomes();

        generateSpawns();
    }


    public static void generateTrees() {
        if (ConfigEntries.doPreamTrees)
            BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.DREAMING_DEN),
                    GenerationStep.Feature.VEGETAL_DECORATION,
                    ModPlacedFeatures.PREAM);
    }

    public static void generateFeatures() {
        if (ConfigEntries.doCrystalSpikes)
            BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.DREAMING_DEN),
                    GenerationStep.Feature.VEGETAL_DECORATION,
                    ModPlacedFeatures.CRYSTAL_SPIKE);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.DREAMING_DEN),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.VIVID_NIHILIUM_PATCH);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.DREAMING_DEN),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.TALL_VIVID_NIHILIUM_PATCH);

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
    }

    public static void generateBiomes() {
        if (ConfigEntries.doDreamingDenBiome)
            TheEndBiomes.addHighlandsBiome(ModBiomes.DREAMING_DEN, ConfigEntries.dreamingDenWeight);
    }

    public static void generateSpawns() {
        BiomeModifications.addSpawn(BiomeSelectors.tag(ModTags.Biomes.DREAMING_DEN),
                SpawnGroup.MONSTER,
                ModEntities.CRYSTIE,
                6, 4, 4);
    }
}
