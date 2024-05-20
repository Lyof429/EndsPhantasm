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

        Phantasm.log("Loading Configs for Phantasm");

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

                Phantasm.log("Phantasm Config file created");
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

        if (!force && (RELOAD.get() || VERSION.get() < getVersion())) {
            register(true);
        }
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
    "VERSION_DO_NOT_EDIT": 1.3,
    "FORCE_RESET": false
  },

  // This config file uses a custom defined parser. That's why there are comments here, they wouldn't be valid in any other .json file.
  //    To add a comment yourself, just start a line with // like I did here
  //    (although their main use is explaining you what the entries do)

  // CATEGORY: WORLD GEN
  "world_gen": {
    "biomes": {
      // DEAMING DEN
      "dreaming_den": {
        "generate": true,
        "generation_weight": 2.5,
        // Should Pream Trees appear in the biome
        "do_pream_trees": true,
        // Should Tall Pream Trees appear in the biome
        "do_tall_pream_trees": true,
        // Should Crystal Spikes appear in the biome
        "do_crystal_spikes": true
      }
    },
    // Should Fallen Stars appear in the End's sky
    "do_fallen_stars": true,
    // Should Raw Purpur stripes appear on the islands' sides
    "do_raw_purpur": true,
    // Should the main island's obsidian spires be prettified
    "improve_end_spires": true
  }
}""";
}
