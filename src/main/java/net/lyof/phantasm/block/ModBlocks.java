package net.lyof.phantasm.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.lyof.phantasm.ModRegistry;
import net.lyof.phantasm.Phantasm;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.registry.tag.BlockTags;

public class ModBlocks {
    public static void register() {
        Phantasm.log("Registering Blocks for modid : " + Phantasm.MOD_ID);
    }


    public static final Block FALLEN_STAR = ModRegistry.ofBlock("fallen_star",
            new Block(FabricBlockSettings.copyOf(Blocks.DIAMOND_BLOCK).mapColor(MapColor.LIGHT_BLUE_GRAY).luminance(15)))
            .tag(BlockTags.DRAGON_IMMUNE).tag(BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().model().build();

    public static final Block POLISHED_OBSIDIAN = ModRegistry.ofBlock("polished_obsidian",
            new Block(FabricBlockSettings.copyOf(Blocks.OBSIDIAN).hardness(5)))
            .tag(BlockTags.DRAGON_IMMUNE).tool("iron_pickaxe").drop().model().build();
    public static final Block POLISHED_OBSIDIAN_BRICKS = ModRegistry.ofBlock("polished_obsidian_bricks",
            new Block(FabricBlockSettings.copyOf(Blocks.OBSIDIAN).hardness(5)))
            .tag(BlockTags.DRAGON_IMMUNE).tool("iron_pickaxe").drop().model().build();
}
