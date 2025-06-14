package net.lyof.phantasm.setup;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.config.ModConfig;
import net.lyof.phantasm.world.biome.EndDataCompat;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.Map;

public class ReloadListener {
    public static final ReloadListener INSTANCE = new ReloadListener();

    public void preload(ResourceManager manager) {
        ModConfig.register();

        for (Map.Entry<Identifier, Resource> entry : manager.findResources("worldgen/end_biomes",
                path -> path.toString().endsWith(".json")).entrySet()) {

            try {
                Resource resource = entry.getValue();

                String content = new String(resource.getInputStream().readAllBytes());
                JsonElement json = new Gson().fromJson(content, JsonElement.class);

                if (json == null || !json.isJsonObject()) continue;

                EndDataCompat.read(json.getAsJsonObject());
            }
            catch (Throwable e) {
                Phantasm.log("Could not read data file " + entry.getKey(), 2);
            }
        }
    }
}
