package net.lyof.phantasm.world.biome;

import net.lyof.phantasm.Phantasm;
import net.minecraft.client.sound.MusicType;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.EndPlacedFeatures;

public class ModBiomes {
    public static RegistryKey<Biome> register(String name) {
        return RegistryKey.of(RegistryKeys.BIOME, Phantasm.makeID(name));
    }

    public static void bootstrap(Registerable<Biome> context) {
        context.register(DREAMING_DEN, dreamingDen(context));

    }

    public static void endBiome(SpawnSettings.Builder spawns, GenerationSettings.LookupBackedBuilder generation) {
        DefaultBiomeFeatures.addEndMobs(spawns);

        generation.feature(GenerationStep.Feature.VEGETAL_DECORATION, EndPlacedFeatures.CHORUS_PLANT);
        generation.feature(GenerationStep.Feature.SURFACE_STRUCTURES, EndPlacedFeatures.END_GATEWAY_RETURN);
    }


    public static final RegistryKey<Biome> DREAMING_DEN = register("dreaming_den");

    public static Biome dreamingDen(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();
        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));

        endBiome(spawnBuilder, biomeBuilder);

        return new Biome.Builder()
                .precipitation(false)
                .downfall(0.5f)
                .temperature(0.5f)
                .generationSettings(biomeBuilder.build())
                .spawnSettings(spawnBuilder.build())
                .effects(new BiomeEffects.Builder()
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .skyColor(0x30c918)
                        .fogColor(0x22a1e6)
                        .moodSound(BiomeMoodSound.CAVE)
                        .music(MusicType.END).build()
                ).build();
    }

    public static Biome acidburntAbysses(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();
        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));

        endBiome(spawnBuilder, biomeBuilder);

        return new Biome.Builder()
                .precipitation(false)
                .downfall(0.5f)
                .temperature(0.5f)
                .generationSettings(biomeBuilder.build())
                .spawnSettings(spawnBuilder.build())
                .effects(new BiomeEffects.Builder()
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .skyColor(0xb21d49)
                        .fogColor(0xca2656)
                        .moodSound(BiomeMoodSound.CAVE)
                        .music(MusicType.END).build()
                ).build();
    }
}
