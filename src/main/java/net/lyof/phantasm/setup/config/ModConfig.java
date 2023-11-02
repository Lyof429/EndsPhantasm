package net.lyof.phantasm.setup.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.lyof.phantasm.Phantasm;

@Config(name = Phantasm.MOD_ID)
public class ModConfig implements ConfigData {
    public static class GenerationCategory {
        @Comment(value = "Should Fallen Stars generate in the End")
        public boolean do_fallen_stars = true;
        @Comment(value = "Should Raw Purpur stripes generate in the End")
        public boolean do_raw_purpur = true;

        @ConfigEntry.Gui.CollapsibleObject
        @Comment(value = "Values for the Dreaming Den biome")
        public BiomeConfig.DreamingDenConfig dreaming_den = new BiomeConfig.DreamingDenConfig();
    }



    // The Gui generator works with boolean, int, long, double, float, String, and enum types

    @ConfigEntry.Gui.CollapsibleObject
    public GenerationCategory world_gen = new GenerationCategory();



    //      -----
    // Management methods
    private static ModConfig INSTANCE;

    public static void register() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        INSTANCE = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public static ModConfig get() {
        return INSTANCE;
    }
}
