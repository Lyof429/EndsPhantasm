package net.lyof.phantasm.world.biome;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.world.feature.ModPlacedFeatures;
import net.minecraft.client.sound.MusicType;
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
        context.register(ACIDBURNT_ABYSSES, acidburntAbysses(context));
    }

    public static void endBiome(SpawnSettings.Builder spawns, GenerationSettings.LookupBackedBuilder generation, boolean chorus) {
        DefaultBiomeFeatures.addEndMobs(spawns);

        if (chorus) generation.feature(GenerationStep.Feature.VEGETAL_DECORATION, EndPlacedFeatures.CHORUS_PLANT);
        generation.feature(GenerationStep.Feature.SURFACE_STRUCTURES, EndPlacedFeatures.END_GATEWAY_RETURN);
    }


    public static final RegistryKey<Biome> DREAMING_DEN = register("dreaming_den");
    public static final RegistryKey<Biome> ACIDBURNT_ABYSSES = register("acidburnt_abysses");

    public static Biome dreamingDen(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();
        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));

        endBiome(spawnBuilder, biomeBuilder, true);

        return new Biome.Builder()
                .precipitation(false)
                .downfall(0.5f)
                .temperature(0.5f)
                .generationSettings(biomeBuilder
                        .feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.PREAM)
                        .feature(GenerationStep.Feature.SURFACE_STRUCTURES, ModPlacedFeatures.CRYSTAL_SPIKE)
                        .feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.VIVID_NIHILIS)
                        .feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.TALL_VIVID_NIHILIS)
                        .feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.STARFLOWER_PATCH)
                        .feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.OBLIVINE_PATCH).build())
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

        endBiome(spawnBuilder, biomeBuilder, false);

        return new Biome.Builder()
                .precipitation(false)
                .downfall(0.5f)
                .temperature(0.5f)
                .generationSettings(biomeBuilder
                        .feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.ACIDIC_NIHILIS)
                        .feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.TALL_ACIDIC_NIHILIS)
                        .feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.DRAGON_MINT)
                        .feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.DRALGAE)
                        .feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.TALL_DRALGAE)
                        .feature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, ModPlacedFeatures.HUGE_DRALGAE)
                        .feature(GenerationStep.Feature.SURFACE_STRUCTURES, ModPlacedFeatures.CIRITE_BOULDER)
                        .feature(GenerationStep.Feature.SURFACE_STRUCTURES, ModPlacedFeatures.CIRITE_SPIKE)
                        .feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.CHORAL_RIFF)
                        .feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.CHORAL_FAN).build())
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
