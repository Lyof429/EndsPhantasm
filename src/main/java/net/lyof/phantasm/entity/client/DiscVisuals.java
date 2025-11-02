package net.lyof.phantasm.entity.client;

import com.google.gson.JsonObject;
import net.lyof.phantasm.Phantasm;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class DiscVisuals {
    public final Identifier progressBar;
    public final Identifier notes;

    protected DiscVisuals(Identifier progressBar, Identifier notes) {
        this.progressBar = progressBar;
        this.notes = notes;
    }


    private static final DiscVisuals DEFAULT = new DiscVisuals(Identifier.of("minecraft", "textures/gui/polyppie/progress_bar.png"),
            Identifier.of("minecraft", "textures/gui/polyppie/notes.png"));
    private static final Map<Identifier, DiscVisuals> toLoad = new HashMap<>();
    private static final Map<Item, DiscVisuals> instances = new HashMap<>();

    public static void clear() {
        toLoad.clear();
        instances.clear();
    }

    public static void load() {
        instances.clear();
        for (Map.Entry<Identifier, DiscVisuals> entry : toLoad.entrySet()) {
            Item item = Registries.ITEM.get(entry.getKey());
            if (item == Items.AIR) continue;

            instances.put(item, entry.getValue());
        }
    }

    public static void read(JsonObject json) {
        if (!json.has("disc")) return;

        Identifier disc = new Identifier(json.get("disc").getAsString());

        Identifier progressBar = DEFAULT.progressBar;
        Identifier notes = DEFAULT.notes;
        if (json.has("progress_bar"))
            progressBar = new Identifier(json.get("progress_bar").getAsString());
        if (json.has("notes"))
            notes = new Identifier(json.get("notes").getAsString());

        toLoad.putIfAbsent(disc, new DiscVisuals(progressBar, notes));
    }

    public static DiscVisuals get(ItemStack stack) {
        return instances.getOrDefault(stack.getItem(), DEFAULT);
    }
}
