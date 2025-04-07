package net.lyof.phantasm.world.biome;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.loader.api.FabricLoader;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.setup.datagen.config.ConfiguredData;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EndDataCompat {
    public static void register() {
        add(ModBiomes.ACIDBURNT_ABYSSES, () -> ConfigEntries.doAcidburntAbyssesBiome ? ConfigEntries.acidburntAbyssesWeight : 0);
        add(ModBiomes.DREAMING_DEN, () -> ConfigEntries.doDreamingDenBiome ? ConfigEntries.dreamingDenWeight : 0);
    }

    public static String getCompatibilityMode() {
        if (ConfigEntries.dataCompatMode.equals("automatic")) {
            if (FabricLoader.getInstance().isModLoaded("nullscape"))
                return "nullscape";
            else return "custom";
        }
        return ConfigEntries.dataCompatMode;
    }


    private static final List<Pair<Identifier, Supplier<Double>>> BIOMES = new ArrayList<>();

    public static void add(RegistryKey<Biome> biome, Supplier<Double> weight) {
        if (!contains(biome.getValue()))
            BIOMES.add(new Pair<>(biome.getValue(), weight));
    }

    public static boolean contains(Identifier biome) {
        return BIOMES.stream().anyMatch(pair -> pair.getFirst() == biome);
    }

    public static List<Pair<Identifier, Double>> getEnabledBiomes() {
        List<Pair<Identifier, Double>> result = new ArrayList<>();
        for (Pair<Identifier, Supplier<Double>> pair : BIOMES) {
            if (pair.getSecond().get() > 0)
                result.add(new Pair<>(pair.getFirst(), pair.getSecond().get()));
        }
        return result;
    }


    public static JsonElement splitHypercube(JsonObject highlands, int count, int i) {
        String noise = getCompatibilityMode().equals("nullscape") ? "weirdness" : "temperature";
        JsonObject result = highlands.deepCopy();
        result.asMap().replace(noise, splitRange(highlands.get(noise), count, i));
        return result;
    }

    public static JsonArray splitRange(JsonElement point, int count, int i) {
        JsonArray result = new JsonArray();
        double pMin = point.isJsonArray() ? point.getAsJsonArray().get(0).getAsDouble() : -1;
        double pMax = point.isJsonArray() ? point.getAsJsonArray().get(1).getAsDouble() : 1;

        result.add((pMax - pMin) / count * i + pMin);
        result.add((pMax - pMin) / count * (i+1) + pMin);
        return result;
    }
}
