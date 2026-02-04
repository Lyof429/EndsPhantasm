package net.lyof.phantasm.world;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.TheEndBiomes;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.entity.custom.SourSludgeEntity;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.biome.ModBiomes;
import net.lyof.phantasm.world.feature.ModPlacedFeatures;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.GenerationStep;

public class ModWorldGeneration {
    public static void register() {
        generateFeatures();
        generateBiomes();

        generateSpawns();
        generateSpawnRestrictions();
    }


    private static void generateFeatures() {
        if (ConfigEntries.doFallenStars)
            BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(),
                    GenerationStep.Feature.SURFACE_STRUCTURES,
                    ModPlacedFeatures.FALLEN_STAR);

        if (ConfigEntries.doRawPurpur)
            BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(),
                    GenerationStep.Feature.UNDERGROUND_ORES,
                    ModPlacedFeatures.RAW_PURPUR_COAL_ORE);
    }

    private static void generateBiomes() {
        if (ConfigEntries.dreamingDenWeight > 0)
            TheEndBiomes.addHighlandsBiome(ModBiomes.DREAMING_DEN, ConfigEntries.dreamingDenWeight);

        if (ConfigEntries.acidburntAbyssesWeight > 0)
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

        BiomeModifications.addSpawn(BiomeSelectors.tag(ModTags.Biomes.ACIDBURNT_ABYSSES),
                SpawnGroup.MONSTER,
                ModEntities.SOUR_SLUDGE,
                10, 4, 6);
    }

    private static void generateSpawnRestrictions() {
        SpawnRestriction.register(ModEntities.BEHEMOTH, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canMobSpawn);
        SpawnRestriction.register(ModEntities.SOUR_SLUDGE, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SourSludgeEntity::canMobSpawn);
    }
}
