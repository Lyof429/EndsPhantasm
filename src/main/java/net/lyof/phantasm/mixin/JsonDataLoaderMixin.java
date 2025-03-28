package net.lyof.phantasm.mixin;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.netty.handler.codec.json.JsonObjectDecoder;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.setup.datagen.config.ConfiguredData;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(JsonDataLoader.class)
public class JsonDataLoaderMixin {
    @Inject(method = "load", at = @At("TAIL"))
    private static void addPhantasmData(ResourceManager manager, String dataType, Gson gson,
                                        Map<Identifier, JsonElement> results, CallbackInfo ci) {

        for (ConfiguredData data : ConfiguredData.INSTANCES) {
            if (!data.enabled.get()) continue;

            JsonElement original = results.get(data.target);

            if (original == null)
                results.put(data.target, data.apply(null, gson));
            else
                results.replace(data.target, data.apply(original, gson));
        }
        //JsonElement x = gson.fromJson(recipe, JsonElement.class);
        //results.putIfAbsent(Phantasm.makeID("the_awesome_stuff"), x);
    }

    private static String recipe = """
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
            }""";
}
