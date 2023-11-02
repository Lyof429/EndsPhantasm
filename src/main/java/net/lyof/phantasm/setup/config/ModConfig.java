package net.lyof.phantasm.setup.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.lyof.phantasm.Phantasm;

@Config(name = Phantasm.MOD_ID)
public class ModConfig implements ConfigData {
    // The Gui generator works with boolean, int, long, double, float, String, and enum types

    @Comment(value = "Whether this exists")
    public boolean config = true;



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
