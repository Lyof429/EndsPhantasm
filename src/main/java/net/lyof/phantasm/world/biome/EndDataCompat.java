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
        add(ModBiomes.ACIDBURNT_ABYSSES, () -> ConfigEntries.doAcidburntAbyssesBiome);
        add(ModBiomes.DREAMING_DEN, () -> ConfigEntries.doDreamingDenBiome);
    }

    public static String getCompatibilityMode() {
        if (ConfigEntries.dataCompatMode.equals("automatic")) {
            if (FabricLoader.getInstance().isModLoaded("nullscape"))
                return "nullscape";
            else return "default";
        }
        return ConfigEntries.dataCompatMode;
    }


    private static final List<Pair<Identifier, Supplier<Boolean>>> BIOMES = new ArrayList<>();

    public static void add(RegistryKey<Biome> biome, Supplier<Boolean> condition) {
        BIOMES.add(new Pair<>(biome.getValue(), condition));
    }

    public static boolean contains(Identifier biome) {
        return BIOMES.stream().anyMatch(pair -> pair.getFirst() == biome);
    }

    public static List<Identifier> getEnabledBiomes() {
        List<Identifier> result = new ArrayList<>();
        for (Pair<Identifier, Supplier<Boolean>> pair : BIOMES) {
            if (pair.getSecond().get())
                result.add(pair.getFirst());
        }
        return result;
    }


    public static JsonElement splitHypercube(JsonObject highlands, int count, int i) {
        count = count * 2 - 1;
        String noise = getCompatibilityMode().equals("nullscape") ? "weirdness" : "temperature";
        JsonObject result = highlands.deepCopy();
        result.asMap().replace(noise, splitRange(highlands.get(noise), count, i));
        return result;
    }

    public static JsonArray splitRange(JsonElement point, int count, int i) {
        JsonArray result = new JsonArray();
        double pMin = point.isJsonArray() ? point.getAsJsonArray().get(0).getAsDouble() : -1;
        double pMax = point.isJsonArray() ? point.getAsJsonArray().get(1).getAsDouble() : 1;
        Phantasm.log(pMin + " " + pMax + " " + point);

        result.add((pMax - pMin) / count * i + pMin);
        result.add((pMax - pMin) / count * (i+1) + pMin);
        return result;
    }
}
