package net.lyof.phantasm.setup.datagen.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.security.Provider;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ConfiguredData {
    private static final Gson gson = new Gson();

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
        register(Identifier.of("minecraft", "dimension/the_end.json"), () -> ConfigEntries.chorusSaladTp, json -> """
                {
                   "type": "minecraft:the_end",
                   "generator": {
                     "type": "minecraft:noise",
                     "biome_source": {
                       "biomes": [
                         {
                           "biome": "minecraft:end_highlands",
                           "parameters": {
                             "continentalness": [-1, 1],
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
                     },
                     "settings": "minecraft:end"
                   }
                 }""");

        // Huge thanks to Ice (https://linktr.ee/icycrystal) for these noise values
        register(Identifier.of("minecraft", "worldgen/noise_settings/end.json"), () -> true, json -> """
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
                """);

        register(Identifier.of("minecraft", "worldgen/density_function/end/base_3d_noise.json"), () -> true, json -> """
                {
                  "type": "minecraft:old_blended_noise",
                  "xz_scale": 0.7,
                  "y_scale": 1.2,
                  "xz_factor": 90,
                  "y_factor": 145,
                  "smear_scale_multiplier": 8
                }""");
    }
}
