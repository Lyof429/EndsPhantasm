package net.lyof.phantasm.setup.datagen.config;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.loader.api.FabricLoader;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.world.biome.EndDataCompat;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConfiguredData {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public final Identifier target;
    public Function<JsonElement, String> provider;
    public final Supplier<Boolean> enabled;

    public ConfiguredData(Identifier target, Supplier<Boolean> enabled, Function<JsonElement, String> provider) {
        this.target = target;
        this.provider = provider;
        this.enabled = enabled;
    }

    public String apply(@Nullable String original) {
        return gson.fromJson(this.provider.apply(gson.fromJson(original == null ? "" : original, JsonElement.class)),
                JsonElement.class).toString();
    }


    public static List<ConfiguredData> INSTANCES = new LinkedList<>();

    public static @Nullable ConfiguredData get(Identifier id) {
        for (ConfiguredData data : INSTANCES) {
            if (data.target.equals(id)) return data;
        }
        return null;
    }

    protected static void register(Identifier target, Supplier<Boolean> enabled, Function<JsonElement, String> provider) {
        INSTANCES.add(new ConfiguredData(target, enabled, provider));
    }


    public static void register() {
        register(Identifier.of("minecraft", "dimension/the_end.json"), () -> true,
                Common::changeBiomeSource);

        // Huge thanks to Ice (https://linktr.ee/icycrystal) for these noise values
        register(Identifier.of("minecraft", "worldgen/noise_settings/end.json"), () -> true,
                Common::changeNoiseRouter);

        register(Phantasm.makeID("loot_tables/chests/challenges/elytra.json"),
                () -> FabricLoader.getInstance().isModLoaded("grindy-elytras"),
                Common::changeElytraChallengeLoot);

        register(Identifier.of("minecraft", "advancements/end/elytra.json"),
                () -> ConfigEntries.elytraChallenge, Common::changeElytraParent);
    }

    private static class Common {
        private static JsonElement getJson(String string) {
            return gson.fromJson(string, JsonElement.class);
        }

        public static String changeBiomeSource(JsonElement json) {
            if (json == null) {
                json = getJson("""
                        { "type": "minecraft:the_end", "generator": { "type": "minecraft:noise", "biome_source":
                        { "type": "minecraft:the_end" }, "settings": "minecraft:end" } }""");
            }

            // Replaces the End's hardcoded biome source with a multinoise
            if (json.getAsJsonObject().get("generator")
                    .getAsJsonObject().get("biome_source")
                    .getAsJsonObject().get("type")
                    .getAsString().equals("minecraft:the_end") && !EndDataCompat.getCompatibilityMode().equals("vanilla")) {

                json.getAsJsonObject().get("generator")
                        .getAsJsonObject().asMap().replace("biome_source", getJson("""
                     {
                       "biomes": [
                         {
                           "biome": "minecraft:end_highlands",
                           "parameters": {
                             "continentalness": 0,
                             "depth": 0,
                             "erosion": [0.25, 1],
                             "humidity": 0,
                             "offset": 0,
                             "temperature": 0,
                             "weirdness": 0
                           }
                         },
                         {
                           "biome": "minecraft:end_midlands",
                           "parameters": {
                             "continentalness": 0,
                             "depth": 0,
                             "erosion": [-0.0625, 0.25],
                             "humidity": 0,
                             "offset": 0,
                             "temperature": 0,
                             "weirdness": 0
                           }
                         },
                         {
                           "biome": "minecraft:end_barrens",
                           "parameters": {
                             "continentalness": 0,
                             "depth": 0,
                             "erosion": [-0.21875, -0.0625],
                             "humidity": 0,
                             "offset": 0,
                             "temperature": 0,
                             "weirdness": 0
                           }
                         },
                         {
                           "biome": "minecraft:small_end_islands",
                           "parameters": {
                             "continentalness": 0,
                             "depth": 0,
                             "erosion": [-1, -0.21875],
                             "humidity": 0,
                             "offset": 0,
                             "temperature": 0,
                             "weirdness": 0
                           }
                         },
                         {
                           "biome": "minecraft:the_end",
                           "parameters": {
                             "continentalness": 0,
                             "depth": 1,
                             "erosion": 0,
                             "humidity": 0,
                             "offset": 0,
                             "temperature": 0,
                             "weirdness": 0
                           }
                         }
                       ],
                       "type": "minecraft:multi_noise"
                     }"""));
            }

            if (json.getAsJsonObject().get("generator")
                    .getAsJsonObject().get("biome_source")
                    .getAsJsonObject().get("type")
                    .getAsString().equals("minecraft:multi_noise")) {

                // Adds custom biomes to the biome source
                List<JsonElement> entries = json.getAsJsonObject().get("generator")
                        .getAsJsonObject().get("biome_source").getAsJsonObject().get("biomes").getAsJsonArray().asList();
                JsonArray endEntries = new JsonArray();
                JsonObject highlands = null;

                for (JsonElement e : entries) {
                    if (e.getAsJsonObject().get("biome").getAsString().equals(BiomeKeys.END_HIGHLANDS.getValue().toString())) {
                        highlands = e.getAsJsonObject().get("parameters").getAsJsonObject();
                        break;
                    }
                }

                // Adds biomes which already have noise parameters
                for (JsonObject biome : EndDataCompat.getNoiseBiomes().stream().map(Pair::getSecond).toList()) {
                    endEntries.add(biome);
                }

                // Calculate the other biomes
                int customCount = EndDataCompat.getEnabledWeightedBiomes().size();

                if (highlands != null && customCount > 0) {
                    String noise = EndDataCompat.getCompatibilityMode().equals("nullscape") ? "weirdness" : "temperature";
                    JsonElement point = highlands.get(noise);
                    double pMin = point.isJsonArray() ? point.getAsJsonArray().get(0).getAsDouble() : -1;
                    double pMax = point.isJsonArray() ? point.getAsJsonArray().get(1).getAsDouble() : 1;

                    double highlandsRange = (pMax - pMin) * (1 - ConfigEntries.customBiomesWeight);
                    double highlandsStep = customCount == 1 ? highlandsRange : highlandsRange / (customCount - 1);
                    double customRange = (pMax - pMin) * ConfigEntries.customBiomesWeight;
                    double customStep = 0;
                    for (Pair<Identifier, Double> entry : EndDataCompat.getEnabledWeightedBiomes())
                        customStep += entry.getSecond();

                    double min = pMin;
                    double max = pMin;

                    // hook the custom biomes in
                    int j = 1;
                    for (Pair<Identifier, Double> entry : EndDataCompat.getEnabledWeightedBiomes()) {
                        Identifier biome = entry.getFirst();
                        Phantasm.log("Adding " + biome + " to the End biome source at slice " + j + " out of " + customCount, 0);

                        max += customRange * entry.getSecond() / customStep;
                        endEntries.add(EndDataCompat.splitHypercube(biome, highlands, noise, min, max));
                        min = max;

                        if (j != customCount || customCount == 1) {
                            max += highlandsStep;
                            endEntries.add(EndDataCompat.splitHypercube(BiomeKeys.END_HIGHLANDS.getValue(), highlands, noise, min, max));
                            min = max;
                        }

                        j++;
                    }

                    // add all the default biomes
                    endEntries.asList().addAll(entries.stream()
                            .filter(p -> !p.getAsJsonObject().get("biome").getAsString().equals(BiomeKeys.END_HIGHLANDS.getValue().toString())
                                    && !EndDataCompat.contains(new Identifier(p.getAsJsonObject().get("biome").getAsString()))).toList());

                    json.getAsJsonObject().get("generator")
                            .getAsJsonObject().get("biome_source")
                            .getAsJsonObject().asMap().replace("biomes", endEntries);
                }
            }

            return gson.toJson(json);
        }

        public static String changeNoiseRouter(JsonElement json) {
            if (json.getAsJsonObject().get("noise_router")
                    .getAsJsonObject().get("temperature").isJsonPrimitive()) {

                JsonElement temperature = getJson("""
                        {
                          "type": "minecraft:cache_2d",
                          "argument": {
                            "type": "minecraft:noise",
                            "noise": "minecraft:temperature",
                            "y_scale": 0
                          }
                        }""");
                temperature.getAsJsonObject().get("argument").getAsJsonObject()
                        .addProperty("xz_scale", ConfigEntries.noiseScale > 0 ? 1 / ConfigEntries.noiseScale : 1);

                json.getAsJsonObject().get("noise_router")
                        .getAsJsonObject().asMap().replace("temperature", temperature);
            }


            if (EndDataCompat.getCompatibilityMode().equals("custom")) {
                json.getAsJsonObject().asMap().replace("noise", getJson("""
                        { "min_y": 0, "height": 256, "size_horizontal": 2, "size_vertical": 1 }"""));

                json.getAsJsonObject().get("noise_router")
                        .getAsJsonObject().asMap().replace("depth", new JsonPrimitive("phantasm:is_center"));

                json.getAsJsonObject().get("noise_router")
                        .getAsJsonObject().asMap().replace("initial_density_without_jaggedness", new JsonPrimitive("phantasm:initial_density_without_jaggedness"));
                json.getAsJsonObject().get("noise_router")
                        .getAsJsonObject().asMap().replace("final_density", new JsonPrimitive("phantasm:final_density"));
            }

            JsonObject baseRules = json.getAsJsonObject().get("surface_rule").getAsJsonObject();
            JsonArray sequence = new JsonArray();
            for (Pair<Identifier, JsonObject> biome : EndDataCompat.getCustomRules()) {
                JsonObject condition = getJson("""
                        {
                          "type": "minecraft:condition",
                          "if_true": {
                            "type": "minecraft:biome",
                            "biome_is": []
                          }
                        }""").getAsJsonObject();
                condition.get("if_true").getAsJsonObject().get("biome_is").getAsJsonArray().add(biome.getFirst().toString());
                condition.add("then_run", biome.getSecond());

                sequence.add(condition);
            }
            sequence.add(baseRules);

            JsonObject rules = getJson("""
                    {
                      "type": "minecraft:sequence"
                    }""").getAsJsonObject();
            rules.add("sequence", sequence);
            json.getAsJsonObject().asMap().replace("surface_rule", rules);

            return gson.toJson(json);
        }

        public static String changeElytraChallengeLoot(JsonElement json) {
            return gson.toJson(json).replace("minecraft:elytra", "grindy-elytras:elytra_fragment");
        }

        public static String changeElytraParent(JsonElement json) {
            json.getAsJsonObject().asMap().replace("parent", new JsonPrimitive(Phantasm.makeID("beat_challenge").toString()));
            return json.toString();
        }
    }
}
