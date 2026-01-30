package net.lyof.phantasm.config;

import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;
import net.lyof.phantasm.Phantasm;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ModConfig {
    static final ConfigEntry<Double> VERSION = new ConfigEntry<>("TECHNICAL.VERSION_DO_NOT_EDIT", 0d);
    static final ConfigEntry<Boolean> RELOAD = new ConfigEntry<>("TECHNICAL.FORCE_RESET", false);

    static Map CONFIG = new TreeMap<>();


    public static void register() {
        register(false);
    }

    public static void register(boolean force) {
        String path = FabricLoader.getInstance().getConfigDir().resolve(Phantasm.MOD_ID + ".json").toString();

        Phantasm.log("Loading Configs for Phantasm", 0);

        // Create the config file if it doesn't exist already
        File config = new File(path);
        boolean create = !config.isFile();

        if (create || force) {
            try {
                config.delete();
                config.createNewFile();

                FileWriter writer = new FileWriter(path);
                writer.write(DEFAULT_CONFIG);
                writer.close();

                Phantasm.log("Phantasm Config file created", 3);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }


        String configContent = DEFAULT_CONFIG;
        try {
            configContent = FileUtils.readFileToString(config, StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        CONFIG = new Gson().fromJson(parseJson(configContent), Map.class);
        ConfigEntries.reload();

        if (!force && RELOAD.get()) {
            register(true);
            return;
        }

        if (getVersion() > VERSION.get())
            Phantasm.log("Your Phantasm configs are outdated! Consider deleting them so they can refresh", 1);
    }

    static String parseJson(String text) {
        StringBuilder result = new StringBuilder();

        for (String line : text.split("\n")) {
            if (!line.strip().startsWith("//"))
                result.append("\n").append(line);
        }

        return result.toString();
    }

    static double getVersion() {
        String text = DEFAULT_CONFIG;
        int start = 0;

        while (!List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.').contains(text.charAt(start))) {
            start++;
        }
        int end = start + 1;
        while (List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.').contains(text.charAt(end))) {
            end++;
        }

        return Double.parseDouble(text.substring(start, end));
    }


    static final String DEFAULT_CONFIG = """
{
   "TECHNICAL": {
     "VERSION_DO_NOT_EDIT": 2.0,
     "FORCE_RESET": false
   },

   // This config file uses a custom defined parser. That's why there are comments here, they wouldn't be valid in any other .json file.
   //    To add a comment yourself, just start a line with // like I did here
   //    (although their main use is explaining you what the entries do)

   // CATEGORY: WORLD GENERATION
   "generation": {
     "raw_purpur": {
       // Should Raw Purpur stripes generate on the side of islands
       "enable": true,
       // y level for the lowest stripe
       "first_stripe": 29
     },
     "fallen_stars": {
       "enable": true
     },

     "biomes": {
       // Compatibility mode for datapacks modifying the End generation, such as Nullscape or Endercon
       // Value must be one of:
       //   "custom": The whole terrain generation will be tweaked to have more variated islands and elevation.
       //     To disable this, use "default" without datapacks or "vanilla"
       //   "default": Other datapacks/vanilla generation will be used, with Phantasm's biomes on top
       //     Phantasm will replace an even proportion of the End Highlands with its custom biomes, mimicking its noise values
       //     Theoretically works with any datapack
       //     If no end datapack is installed, will improve the biome distribution without changing the terrain
       //   "nullscape": Optimizes noise values to work best with Nullscape
       //   "vanilla": The terrain will be left untouched, and Fabric's biome distribution will be used
       //   "automatic": Same as "custom", but will work like "nullscape" if it is loaded as a mod
       //     if using the datapack version, set this manually
       "datapack_compatibility": "automatic",
       // Value to scale the End's biome generation with. Higher values mean bigger biomes. Must be greater than 0
       "noise_scale": 2,

       // Generation weights for the biomes. Please note this can't add new biomes alone, you need a datapack for that
       // Set to 0 to disable the corresponding biome
       "weights": {
         "end_highlands": 1.2,

         "dreaming_den": 1.5,
         "acidburnt_abysses": 1.2
       }
     }
   },

   // CATEGORY: GENERAL
   "general": {
     // If true, End Portals will teleport you to the outer end instead of the Dragon's island
     // Use a gateway to fight her
     "outer_end_before_dragon": true,
     // If true, a credits-like cutscene will be played the first time a player gets to the End
     //   Has no effect if the above is false
     // The text is stored at phantasm:assets/texts/begin.txt and the background texture at phantasm:assets/textures/gui/credits_background.png
     "show_cutscene": true,

     // Should the End be rendered dark (like if it were actually night)
     "dark_end": false,

     "challenge": {
       // Use a see through, dynamic runic barrier texture for challenge boundaries
       //   If false, the End portal's static texture will be used. Looks better, but might be confusing
       "alternate_barrier": false,
       // Radius (in blocks) for challenges. Leaving the area will be considered a defeat
       "radius": 16
     },

     "dragon_fight": {
       // Should the Dragon's Fireballs create a firey explosion on impact
       "explosive_fireballs": true,
       // Should Endermen not be aggroed when looked at during the fight
       "passive_endermen": false,

       "obsidian_tower": {
         // Should the main island's obsidian towers be prettified with crying obsidian
         "beautify": true,
         // Should the main island's obsidian towers never have iron bars around the End Crystal
         "no_crystal_cages": false,
         // Height to be added to the towers, mostly useful in case of datapacks raising the main island level
         // Set to 0 to have them be the same height as they are in vanilla
         "extra_height": 0
       }
     },

     "equipment": {
       "elytra": {
         // Advancement needed to enable elytra boosting. Leave blank "" to disable the need for one
         "boost_advancement": "minecraft:end/kill_dragon",
         // Should the item frame in End City Ships be replaced by a Challenge Rune
         "has_challenge": true,
         // Offset to place the Challenge Rune in End City Ships. Doeesn't need to be modified unless you're using
         //   a mod or datapack that alters this structure
         "challenge_rune_offset": [0, 2, 8]
       },

       "shattered_pendant": {
         "durability": 6
       }
     }
   },

   // CATEGORY: DREAMING DEN
   "dreaming_den": {
     "entities": {
       "behemoth": {
         // Distance (in blocks) at which walking close to a Behemoth will aggro it
         "aggro_range": 6,
         // Distance (in blocks) at which walking close to a Behemoth while sneaking will aggro it
         "sneaking_aggro_range": 0
       }
     },

     "equipment": {
       "chorus_fruit_salad": {
         // Should eating a Chorus Fruit Salad teleport in/out of the End
         "teleportation": true,
         // Max Chorus Fruit Salad stack size
         "stack_size": 4
       },

       "crystalline_tools": {
         "durability": 712,
         // Strength of the xp efficiency boost. Set to 0 to disable the feature
         "efficiency_boost": 3
       }
     }
   },

   // CATEGORY: ACIDBURNT ABYSSES
   "acidburnt_abysses": {
     "entities": {

     },

     "equipment": {
       "choral_arrow": {
         // Range (in blocks) for the beam of Choral Arrows shot from a Crossbow
         "crossbow_range": 10,
         // Duration (in ticks) for the Charm effect from Choral Arrows shot from a Bow
         "charm_duration": 20,
         // Duration (in ticks) for the Charm effect on players from Choral Arrows shot from a Bow
         "player_charm_duration": 40
       },

       "subwoofer": {
         // Range (in blocks) for the beam of Subwoofer Blocks
         "range": 8
       },

       "corrosion": {
         // Extra damage multiplier per level. 0.2 means +20% per level
         "damage_multiplier": 0.2,

         "corrosive_food": {
           // Duration (in ticks) for which hitting entities after eating a Pomb Slice/Pop Rock Candy will inflict them with Corrosion
           "pomb_slice": 60,
           "pop_rock_candy": 120,

           // Duration (in ticks) for the inflicted Corrosion
           "duration": 80,
           // Amplifier for the inflicted Corrosion
           "amplifier": 0
         },

         "falling_pomb": {
           // Damage taken when caught in the explosion of a falling Pomb
           "damage": 5,
           // Duration (in ticks) for the inflicted Corrosion
           "duration": 200,
           // Amplifier for the inflicted Corrosion
           "amplifier": 0
         }
       }
     }
   }
 }""";
}
