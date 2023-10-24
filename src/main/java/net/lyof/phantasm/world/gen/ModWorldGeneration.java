package net.lyof.phantasm.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.TheEndBiomes;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.ModPlacedFeatures;
import net.lyof.phantasm.world.biome.ModBiomes;
import net.minecraft.world.gen.GenerationStep;

public class ModWorldGeneration {
    public static void register() {
        generateTrees();
        generateFeatures();
        generateBiomes();
    }


    public static void generateTrees() {
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.DREAMING_DEN),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.PREAM_PLACED_KEY);
    }

    public static void generateFeatures() {
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.DREAMING_DEN),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.CRYSTAL_SPIKE_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(),
                GenerationStep.Feature.SURFACE_STRUCTURES,
                ModPlacedFeatures.FALLEN_STAR_PLACED_KEY);
    }

    public static void generateBiomes() {
        TheEndBiomes.addHighlandsBiome(ModBiomes.DREAMING_DEN, 2.5);
    }
}
