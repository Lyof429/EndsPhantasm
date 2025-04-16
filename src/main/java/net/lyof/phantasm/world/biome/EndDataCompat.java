package net.lyof.phantasm.world.biome;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.loader.api.FabricLoader;
import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EndDataCompat {
    public static void register() {
        add(ModBiomes.DREAMING_DEN, () -> ConfigEntries.doDreamingDenBiome ? ConfigEntries.dreamingDenWeight : 0);
        add(ModBiomes.ACIDBURNT_ABYSSES, () -> ConfigEntries.doAcidburntAbyssesBiome ? ConfigEntries.acidburntAbyssesWeight : 0);
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

    public static JsonElement splitHypercube(Identifier biome, JsonObject highlands, String noise, double min, double max) {
        JsonArray array = new JsonArray();
        array.add(min);
        array.add(max);
        JsonObject parameters = highlands.deepCopy();
        parameters.asMap().replace(noise, array);

        JsonObject result = new JsonObject();
        result.addProperty("biome", biome.toString());
        result.add("parameters", parameters);
        return result;
    }
}
