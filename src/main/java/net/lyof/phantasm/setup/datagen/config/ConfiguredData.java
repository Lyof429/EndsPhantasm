package net.lyof.phantasm.setup.datagen.config;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.world.ModWorldGeneration;
import net.lyof.phantasm.world.biome.EndDataCompat;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.ReloadCommand;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import org.jetbrains.annotations.Nullable;

import java.security.Provider;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

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
        return INSTANCES.stream().filter(data -> data.target.equals(id)).findAny().orElse(null);
    }

    protected static void register(Identifier target, Supplier<Boolean> enabled, Function<JsonElement, String> provider) {
        INSTANCES.add(new ConfiguredData(target, enabled, provider));
    }


    public static void register() {
        register(Identifier.of("minecraft", "dimension/the_end.json"), () -> true,
                Instances::changeBiomeSource);

        // Huge thanks to Ice (https://linktr.ee/icycrystal) for these noise values
        register(Identifier.of("minecraft", "worldgen/noise_settings/end.json"), () -> true,
                Instances::changeNoiseRouter);

        register(Identifier.of("minecraft", "worldgen/density_function/end/base_3d_noise.json"),
                () -> EndDataCompat.getCompatibilityMode().equals("custom"),
                json -> "{ \"type\": \"minecraft:old_blended_noise\", \"xz_scale\": 0.7, \"y_scale\": 1.2, \"xz_factor\": 90, \"y_factor\": 145, \"smear_scale_multiplier\": 8 }");
    }

    private static class Instances {
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
                    .getAsString().equals("minecraft:the_end") && EndDataCompat.getCompatibilityMode().equals("custom")) {

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
                             "temperature": [-1, 1],
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
                             "depth": 0,
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
                    if (e.getAsJsonObject().get("biome").getAsString().equals(BiomeKeys.END_HIGHLANDS.getValue().toString()))
                        highlands = e.getAsJsonObject().get("parameters").getAsJsonObject();
                }

                int customCount = EndDataCompat.getEnabledBiomes().size();

                if (highlands != null && customCount > 0) {
                    String noise = EndDataCompat.getCompatibilityMode().equals("nullscape") ? "weirdness" : "temperature";
                    JsonElement point = highlands.get(noise);
                    double pMin = point.isJsonArray() ? point.getAsJsonArray().get(0).getAsDouble() : -1;
                    double pMax = point.isJsonArray() ? point.getAsJsonArray().get(1).getAsDouble() : 1;

                    double highlandsRange = (pMax - pMin) * (1 - ConfigEntries.customBiomesWeight);
                    double highlandsStep = customCount == 1 ? highlandsRange : highlandsRange / (customCount - 1);
                    double customRange = (pMax - pMin) * ConfigEntries.customBiomesWeight;
                    double customStep = 0;
                    for (Pair<Identifier, Double> entry : EndDataCompat.getEnabledBiomes())
                        customStep += entry.getSecond();

                    double min = pMin;
                    double max = pMin;

                    // hook the custom biomes in
                    int j = 1;
                    for (Pair<Identifier, Double> entry : EndDataCompat.getEnabledBiomes()) {
                        Identifier biome = entry.getFirst();
                        Phantasm.log("Adding " + biome + " to the End biome source at slice " + j + " out of " + customCount);

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

            return Phantasm.log(gson.toJson(json));
        }

        public static String changeNoiseRouter(JsonElement json) {
            if (json.getAsJsonObject().get("noise_router")
                    .getAsJsonObject().get("temperature").isJsonPrimitive()) {

                JsonElement temperature = getJson("""
                                { "type": "minecraft:noise", "noise": "minecraft:temperature", "xz_scale": 1, "y_scale": 1 }""");
                int biomes = EndDataCompat.getEnabledBiomes().size();
                temperature.getAsJsonObject().addProperty("xz_scale",
                        biomes == 0 ? 1 : 1f / biomes);

                json.getAsJsonObject().get("noise_router")
                        .getAsJsonObject().asMap().replace("temperature", temperature);
            }
            if (EndDataCompat.getCompatibilityMode().equals("custom")) {
                json.getAsJsonObject().asMap().replace("noise", getJson("""
                        { "min_y": 0, "height": 256, "size_horizontal": 2, "size_vertical": 1 }"""));

                json.getAsJsonObject().get("noise_router")
                        .getAsJsonObject().asMap().replace("initial_density_without_jaggedness", getJson("""
                            {
                              "type": "minecraft:add",
                              "argument1": -0.234375,
                              "argument2": {
                                "type": "minecraft:mul",
                                "argument1": {
                                  "type": "minecraft:y_clamped_gradient",
                                  "from_y": 4,
                                  "to_y": 32,
                                  "from_value": 0,
                                  "to_value": 1
                                },
                                "argument2": {
                                  "type": "minecraft:add",
                                  "argument1": 0.234375,
                                  "argument2": {
                                    "type": "minecraft:add",
                                    "argument1": -23.4375,
                                    "argument2": {
                                      "type": "minecraft:mul",
                                      "argument1": {
                                        "type": "minecraft:y_clamped_gradient",
                                        "from_y": 8,
                                        "to_y": 64,
                                        "from_value": 1,
                                        "to_value": 0
                                      },
                                      "argument2": {
                                        "type": "minecraft:add",
                                        "argument1": 23.4375,
                                        "argument2": {
                                          "type": "minecraft:add",
                                          "argument1": -0.703125,
                                          "argument2": {
                                            "type": "minecraft:cache_2d",
                                            "argument": {
                                              "type": "minecraft:end_islands"
                                            }
                                          }
                                        }
                                      }
                                    }
                                  }
                                }
                              }
                            }"""));
                json.getAsJsonObject().get("noise_router")
                        .getAsJsonObject().asMap().replace("final_density", getJson("""
                            {
                              "type": "minecraft:squeeze",
                              "argument": {
                                "type": "minecraft:mul",
                                "argument1": 0.64,
                                "argument2": {
                                  "type": "minecraft:interpolated",
                                  "argument": {
                                    "type": "minecraft:blend_density",
                                    "argument": {
                                      "type": "minecraft:add",
                                      "argument1": -0.234375,
                                      "argument2": {
                                        "type": "minecraft:mul",
                                        "argument1": {
                                          "type": "minecraft:y_clamped_gradient",
                                          "from_y": 12,
                                          "to_y": 52,
                                          "from_value": 0.01,
                                          "to_value": 0.9875
                                        },
                                        "argument2": {
                                          "type": "minecraft:add",
                                          "argument1": 0.234375,
                                          "argument2": {
                                            "type": "minecraft:add",
                                            "argument1": -23.4375,
                                            "argument2": {
                                              "type": "minecraft:mul",
                                              "argument1": {
                                                "type": "minecraft:y_clamped_gradient",
                                                "from_y": 58,
                                                "to_y": 160,
                                                "from_value": 1,
                                                "to_value": 0.9
                                              },
                                              "argument2": {
                                                "type": "minecraft:add",
                                                "argument1": 23.4375,
                                                "argument2": "minecraft:end/sloped_cheese"
                                              }
                                            }
                                          }
                                        }
                                      }
                                    }
                                  }
                                }
                              }
                            }"""));
            }

            return json.toString();/*
            return """
                {
                  "sea_level": 0,
                  "disable_mob_generation": true,
                  "aquifers_enabled": false,
                  "ore_veins_enabled": false,
                  "legacy_random_source": false,
                  "default_block": {
                    "Name": "minecraft:end_stone"
                  },
                  "default_fluid": {
                    "Name": "minecraft:air"
                  },
                  "noise": {
                    "min_y": 0,
                    "height": 256,
                    "size_horizontal": 2,
                    "size_vertical": 1
                  },
                  "noise_router": {
                    "barrier": 0,
                    "fluid_level_floodedness": 0,
                    "fluid_level_spread": 0,
                    "lava": 0,
                    "temperature": 0,
                    "vegetation": 0,
                    "continents": {
                      "type": "minecraft:noise",
                      "noise": "minecraft:temperature",
                      "xz_scale": 6,
                      "y_scale": 1
                    },
                    "erosion": {
                      "type": "minecraft:cache_2d",
                      "argument": {
                        "type": "minecraft:end_islands"
                      }
                    },
                    "depth": 0,
                    "ridges": 0,
                    "initial_density_without_jaggedness": {
                      "type": "minecraft:add",
                      "argument1": -0.234375,
                      "argument2": {
                        "type": "minecraft:mul",
                        "argument1": {
                          "type": "minecraft:y_clamped_gradient",
                          "from_y": 4,
                          "to_y": 32,
                          "from_value": 0,
                          "to_value": 1
                        },
                        "argument2": {
                          "type": "minecraft:add",
                          "argument1": 0.234375,
                          "argument2": {
                            "type": "minecraft:add",
                            "argument1": -23.4375,
                            "argument2": {
                              "type": "minecraft:mul",
                              "argument1": {
                                "type": "minecraft:y_clamped_gradient",
                                "from_y": 8,
                                "to_y": 64,
                                "from_value": 1,
                                "to_value": 0
                              },
                              "argument2": {
                                "type": "minecraft:add",
                                "argument1": 23.4375,
                                "argument2": {
                                  "type": "minecraft:add",
                                  "argument1": -0.703125,
                                  "argument2": {
                                    "type": "minecraft:cache_2d",
                                    "argument": {
                                      "type": "minecraft:end_islands"
                                    }
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    },
                    "final_density": {
                      "type": "minecraft:squeeze",
                      "argument": {
                        "type": "minecraft:mul",
                        "argument1": 0.64,
                        "argument2": {
                          "type": "minecraft:interpolated",
                          "argument": {
                            "type": "minecraft:blend_density",
                            "argument": {
                              "type": "minecraft:add",
                              "argument1": -0.234375,
                              "argument2": {
                                "type": "minecraft:mul",
                                "argument1": {
                                  "type": "minecraft:y_clamped_gradient",
                                  "from_y": 12,
                                  "to_y": 52,
                                  "from_value": 0.01,
                                  "to_value": 0.9875
                                },
                                "argument2": {
                                  "type": "minecraft:add",
                                  "argument1": 0.234375,
                                  "argument2": {
                                    "type": "minecraft:add",
                                    "argument1": -23.4375,
                                    "argument2": {
                                      "type": "minecraft:mul",
                                      "argument1": {
                                        "type": "minecraft:y_clamped_gradient",
                                        "from_y": 58,
                                        "to_y": 160,
                                        "from_value": 1,
                                        "to_value": 0.9
                                      },
                                      "argument2": {
                                        "type": "minecraft:add",
                                        "argument1": 23.4375,
                                        "argument2": "minecraft:end/sloped_cheese"
                                      }
                                    }
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    },
                    "vein_toggle": 0,
                    "vein_ridged": 0,
                    "vein_gap": 0
                  },
                  "spawn_target": [],
                  "surface_rule": {
                    "type": "minecraft:block",
                    "result_state": {
                      "Name": "minecraft:end_stone"
                    }
                  }
                }
                """;*/
        }
    }
}
