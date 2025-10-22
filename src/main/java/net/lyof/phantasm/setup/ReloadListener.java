package net.lyof.phantasm.setup;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.challenge.Challenge;
import net.lyof.phantasm.block.challenge.ChallengeRegistry;
import net.lyof.phantasm.config.ModConfig;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.lyof.phantasm.world.biome.EndDataCompat;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.Map;

public class ReloadListener {
    public static final ReloadListener INSTANCE = new ReloadListener();

    public void preload(ResourceManager manager) {
        ModConfig.register();

        EndDataCompat.clear();
        ChallengeRegistry.clear();
        PolyppieEntity.Variant.clear();

        for (Map.Entry<Identifier, Resource> entry : manager.findResources("worldgen/end_biomes",
                path -> path.toString().endsWith(".json")).entrySet()) {

            try {
                String content = new String(entry.getValue().getInputStream().readAllBytes());
                JsonElement json = new Gson().fromJson(content, JsonElement.class);

                if (json == null || !json.isJsonObject()) continue;

                EndDataCompat.read(json.getAsJsonObject());
            }
            catch (Throwable e) {
                Phantasm.log("Could not read data file " + entry.getKey(), 2);
            }
        }

        ResourceFinder finder = ResourceFinder.json("challenges");
        for (Map.Entry<Identifier, Resource> entry : finder.findResources(manager).entrySet()) {
            try {
                String content = new String(entry.getValue().getInputStream().readAllBytes());
                JsonElement json = new Gson().fromJson(content, JsonElement.class);

                if (json == null || !json.isJsonObject()) continue;

                Challenge.read(finder.toResourceId(entry.getKey()), json.getAsJsonObject());
            }
            catch (Throwable e) {
                Phantasm.log("Could not read data file " + entry.getKey(), 2);
            }
        }

        finder = ResourceFinder.json("entities/polyppie_variants");
        for (Map.Entry<Identifier, Resource> entry : finder.findResources(manager).entrySet()) {
            try {
                String content = new String(entry.getValue().getInputStream().readAllBytes());
                JsonElement json = new Gson().fromJson(content, JsonElement.class);

                if (json == null || !json.isJsonObject()) continue;

                PolyppieEntity.Variant.read(finder.toResourceId(entry.getKey()), json.getAsJsonObject());
            }
            catch (Throwable e) {
                Phantasm.log("Could not read data file " + entry.getKey(), 2);
            }
        }
    }

    public void reloadClient() {
        PolyppieEntity.Variant.clear();
    }
}
