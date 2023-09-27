package net.lyof.phantasm.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlocks {
    public static Block register(String id, Block block) {
        ModItems.register(id, new BlockItem(block, new FabricItemSettings()));
        return Registry.register(Registries.BLOCK, Phantasm.makeID(id), block);
    }

    public static void register() {
        Phantasm.log("Registering Blocks for modid : " + Phantasm.MOD_ID);
    }



    public static final Block FALLEN_STAR = register("fallen_star",
            new Block(FabricBlockSettings.copyOf(Blocks.GLOWSTONE)));
}
