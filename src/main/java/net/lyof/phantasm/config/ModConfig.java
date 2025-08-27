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

        // Create config file if it doesn't exist already
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
    "VERSION_DO_NOT_EDIT": 1.7,
    "FORCE_RESET": false
  },

  // This config file uses a custom defined parser. That's why there are comments here, they wouldn't be valid in any other .json file.
  //    To add a comment yourself, just start a line with // like I did here
  //    (although their main use is explaining you what the entries do)

  // CATEGORY: WORLD GEN
  "world_gen": {
    "biomes": {
      // Compatibility mode for datapacks modifying the End generation, such as Nullscape or Endercon
      // Value must be one of:
        // "custom": The whole terrain generation will be tweaked to have more variated islands and elevation.
          // To disable this, use "default" without datapacks or "vanilla"
        // "default": Other datapacks/vanilla generation will be used, with Phantasm's biomes on top
          // Phantasm will replace an even proportion of the End Highlands with its custom biomes, mimicking its noise values
          // Theoretically works with any datapack
          // If no end datapack is installed, will improve the biome distribution without changing the terrain
        // "nullscape": Optimizes noise values to work best with Nullscape
        // "vanilla": The terrain will be left untouched, and Fabric's biome distribution will be used
        // "automatic": Same as "custom", but will work like "nullscape" if it is loaded as a mod
          // if using the datapack version, set this manually
      "datapack_compatibility": "automatic",
      // How much of the End Highlands should be replaced with custom biomes. Must be a number between 0 and 1 (0: none, 1: all of it, 0.5: 50%)
      "custom_biomes_weight": 0.6,
      // Value to scale the End's biome generation with. Higher values mean bigger biomes. Must be greater than 0
      "noise_scale": 2,
      // Should the End's biome source be partially overrode so that the main island biome always generate at the center
      "force_main_island": true,
    
      // DEAMING DEN
      "dreaming_den": {
        "enabled": true,
        "generation_weight": 1.5
      },
      
      // ACIDBURNT ABYSSES
      "acidburnt_abysses": {
        "enabled": true,
        "generation_weight": 1.2
      }
    },
    // Should Fallen Stars appear in the End's sky
    "do_fallen_stars": true,
    // Should Raw Purpur stripes appear on the islands' sides
    "do_raw_purpur": true,
    // y offset to be added to the Raw Purpur stripes generation
    "raw_purpur_offset": 10,
    
    "end_spires": {
      // Should the main island's obsidian spires be prettified with crying obsidian
      "beautify": true,
      // Should the main island's obsidian spires never have iron bars around the End Crystal
      "no_crystal_cages": false,
      // Height to be added to the spires, mostly useful in case of datapacks raising the main island level
        // Set to 0 to have them be the same height as they are in vanilla
      "extra_height": 0
    }
  },
  
  // CATEGORY: GAMEPLAY
  "gameplay": {
    // Should End portals teleport to the outer islands instead of the Dragon's
    "outer_end_in_gameloop": true,
    // Should the End be rendered dark (like if it were actually night)
    "dark_end": false,
    
    "dragon": {
      // Should the Dragon's Fireballs create a firey explosion on impact
      "explosive_fireballs": true,
      // Should Endermen not be aggroed when looked at during the fight
      "no_pesky_endermen": false
    },
    
    "behemoth": {
      // Distance (in blocks) at which walking close to a Behemoth will aggro it
      "aggro_range": 6,
      // Distance (in blocks) at which walking close to a Behemoth while sneaking will aggro it
      "aggro_range_sneaking": 0
    },
    
    "challenge": {
      // Use a see through, dynamic runic barrier texture for challenge boundaries
      //   If false, the End portal's static texture will be used. Looks better to me, but might be confusing
      "accessibility_barrier": false
    }
  },
  
  // CATEGORY: EQUIPMENT
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
    
    // Crystalline tools boost ratio
    "crystal_xp_boost": 3,
    
    "chorus_fruit_salad": {
      // Should eating a Chorus Fruit Salad teleport in/out of the End
      "teleportation": true,
      // Max Chorus Fruit Salad stack size
      "stack_size": 4
    },
    
    // Max uses for Shattered Pendants
    "shattered_pendant_durability": 6,
    
    // Range (in blocks) for Crossbow Choral Arrows and Subwoofer Blocks
    "subwoofer_range": 6
  }
}""";
}
