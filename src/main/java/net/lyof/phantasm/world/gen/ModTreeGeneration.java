package net.lyof.phantasm.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.ModPlacedFeatures;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;

public class ModTreeGeneration {
    public static void generateTrees() {
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.DREAMING_DEN)
                        .or(BiomeSelectors.includeByKey(BiomeKeys.END_HIGHLANDS)),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.PREAM_PLACED_KEY);
    }
}
