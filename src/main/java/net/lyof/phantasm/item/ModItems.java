package net.lyof.phantasm.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.lyof.phantasm.ModRegistry;
import net.lyof.phantasm.Phantasm;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;

public class ModItems {
    public static void register() {
        Phantasm.log("Registering Items for modid : " + Phantasm.MOD_ID);
    }


    public static final Item PREAM_BERRY = ModRegistry.ofItem("pream_berry",
            new Item(new FabricItemSettings().food(ModRegistry.Foods.PREAM_BERRY))).model(Models.GENERATED).build();
}
