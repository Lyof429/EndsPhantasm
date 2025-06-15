package net.lyof.phantasm.world.biome;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.loader.api.FabricLoader;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EndDataCompat {
    public static void register() {
        add(ModBiomes.DREAMING_DEN.getValue(),
                () -> ConfigEntries.doDreamingDenBiome ? ConfigEntries.dreamingDenWeight : 0);
        add(ModBiomes.ACIDBURNT_ABYSSES.getValue(),
                () -> ConfigEntries.doAcidburntAbyssesBiome ? ConfigEntries.acidburntAbyssesWeight : 0);
    }

    public static String getCompatibilityMode() {
        if (ConfigEntries.dataCompatMode.equals("automatic")) {
            if (FabricLoader.getInstance().isModLoaded("nullscape"))
                return "nullscape";
            else return "custom";
        }
        return ConfigEntries.dataCompatMode;
    }


    private static final List<Pair<Identifier, Supplier<Double>>> BIOMES_WEIGHT = new ArrayList<>();
    private static final List<Pair<Identifier, JsonObject>> BIOMES_NOISE = new ArrayList<>();
    private static final List<Pair<Identifier, JsonObject>> BIOMES_RULES = new ArrayList<>();

    public static void add(Identifier biome, Supplier<Double> weight) {
        if (BIOMES_WEIGHT.stream().noneMatch(pair -> pair.getFirst().equals(biome)))
            BIOMES_WEIGHT.add(new Pair<>(biome, weight));
    }

    public static void add(Identifier biome, JsonObject noise) {
        BIOMES_NOISE.add(new Pair<>(biome, noise));
    }

    public static void addRules(Identifier biome, JsonObject rules) {
        BIOMES_RULES.add(new Pair<>(biome, rules));
    }

    public static void clear() {
        BIOMES_WEIGHT.removeAll(BIOMES_WEIGHT.stream().filter(p -> p.getSecond().get() < 0).toList());
        BIOMES_NOISE.clear();
        BIOMES_RULES.clear();
    }

    public static boolean contains(Identifier biome) {
        return BIOMES_WEIGHT.stream().anyMatch(pair -> pair.getFirst().equals(biome))
                || BIOMES_NOISE.stream().anyMatch(pair -> pair.getFirst().equals(biome));
    }

    public static List<Pair<Identifier, Double>> getEnabledWeightedBiomes() {
        List<Pair<Identifier, Double>> result = new ArrayList<>();
        for (Pair<Identifier, Supplier<Double>> pair : BIOMES_WEIGHT) {
            if (BIOMES_NOISE.stream().noneMatch(p -> p.getFirst().equals(pair.getFirst())) && Math.abs(pair.getSecond().get()) > 0)
                result.add(new Pair<>(pair.getFirst(), Math.abs(pair.getSecond().get())));
        }
        return result;
    }

    public static List<Pair<Identifier, JsonObject>> getNoiseBiomes() {
        return BIOMES_NOISE;
    }

    public static List<Pair<Identifier, JsonObject>> getCustomRules() {
        return BIOMES_RULES;
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

    public static void read(JsonObject json) {
        if (!json.has("biome")) return;

        if (json.has("weight")) {
            double weight = json.get("weight").getAsDouble();
            add(new Identifier(json.get("biome").getAsString()), () -> -weight);
        }
        else if (json.has("parameters")) {
            JsonObject noise = new JsonObject();
            noise.add("biome", json.get("biome"));
            noise.add("parameters", json.get("parameters"));
            add(new Identifier(json.get("biome").getAsString()), noise);
        }

        if (json.has("surface_rule")) {
            addRules(new Identifier(json.get("biome").getAsString()), json.get("surface_rule").getAsJsonObject());
        }
    }
}
