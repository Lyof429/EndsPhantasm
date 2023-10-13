package net.lyof.phantasm.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.setup.ModRegistry;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.data.client.Models;
import net.minecraft.item.*;

public class ModItems {
    public static void register() {
        Phantasm.log("Registering Items for modid : " + Phantasm.MOD_ID);
    }


    public static final Item PREAM_BERRY = ModRegistry.ofItem("pream_berry",
            new Item(new FabricItemSettings().food(ModRegistry.Foods.PREAM_BERRY))).model(Models.GENERATED).build();

    public static final Item CRYSTALLINE_SHOVEL = ModRegistry.ofItem("crystalline_shovel",
                    new ShovelItem(ModTiers.CRYSTALLINE, 2, -3f, new FabricItemSettings()))
            .model(Models.HANDHELD).tag(ModTags.Items.XP_BOOSTED).build();
    public static final Item CRYSTALLINE_PICKAXE = ModRegistry.ofItem("crystalline_pickaxe",
            new PickaxeItem(ModTiers.CRYSTALLINE, 2, -2.8f, new FabricItemSettings()))
            .model(Models.HANDHELD).tag(ModTags.Items.XP_BOOSTED).build();
    public static final Item CRYSTALLINE_AXE = ModRegistry.ofItem("crystalline_axe",
                    new AxeItem(ModTiers.CRYSTALLINE, 7, -3f, new FabricItemSettings()))
            .model(Models.HANDHELD).tag(ModTags.Items.XP_BOOSTED).build();
    public static final Item CRYSTALLINE_HOE = ModRegistry.ofItem("crystalline_hoe",
                    new HoeItem(ModTiers.CRYSTALLINE, 0, -3f, new FabricItemSettings()))
            .model(Models.HANDHELD).tag(ModTags.Items.XP_BOOSTED).build();
    public static final Item CRYSTALLINE_SWORD = ModRegistry.ofItem("crystalline_sword",
                    new SwordItem(ModTiers.CRYSTALLINE, 4, -2.4f, new FabricItemSettings()))
            .model(Models.HANDHELD).tag(ModTags.Items.XP_BOOSTED).build();

    public static final Item PREAM_SIGN = ModRegistry.ofItem("pream_sign",
                    new SignItem(new FabricItemSettings(), ModBlocks.PREAM_SIGN, ModBlocks.PREAM_WALL_SIGN))
            .fuel(200)
            .build();
    public static final Item PREAM_HANGING_SIGN = ModRegistry.ofItem("pream_hanging_sign",
                    new HangingSignItem(ModBlocks.PREAM_HANGING_SIGN, ModBlocks.PREAM_WALL_HANGING_SIGN, new FabricItemSettings()))
            .fuel(200)
            .build();
}
