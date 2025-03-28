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

public class ConfiguredData {
    public final Identifier target;
    public Function<JsonElement, String> provider;
    public final Supplier<Boolean> enabled;

    public ConfiguredData(Identifier target, Supplier<Boolean> enabled, Function<JsonElement, String> provider) {
        this.target = target;
        this.provider = provider;
        this.enabled = enabled;
    }

    public JsonElement apply(@Nullable JsonElement original, Gson gson) {
        return gson.fromJson(this.provider.apply(original), JsonElement.class);
    }


    public static List<ConfiguredData> INSTANCES = new LinkedList<>();

    protected static void register(String target, Supplier<Boolean> enabled, Function<JsonElement, String> provider) {
        INSTANCES.add(new ConfiguredData(Phantasm.makeID(target), enabled, provider));
    }

    public static void register() {
        register("recipes/awesomeeee", () -> ConfigEntries.chorusSaladTp, json -> """
            {
              "type": "minecraft:crafting_shaped",
              "category": "equipment",
              "key": {
                "#": {
                  "item": "minecraft:rabbit_hide"
                },
                "-": {
                  "item": "minecraft:string"
                }
              },
              "pattern": [
                "-#-",
                "# #",
                "###"
              ],
              "result": {
                "item": "minecraft:diamond_sword"
              },
              "show_notification": true
            }""");
    }
}
