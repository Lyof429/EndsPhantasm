package net.lyof.phantasm.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.lyof.phantasm.ModRegistry;
import net.lyof.phantasm.Phantasm;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;

public class ModBlocks {
    public static void register() {
        Phantasm.log("Registering Blocks for modid : " + Phantasm.MOD_ID);
    }


    public static final Block FALLEN_STAR = ModRegistry.ofBlock("fallen_star",
            new Block(FabricBlockSettings.copyOf(Blocks.DIAMOND_BLOCK).luminance(15)))
            .tag(BlockTags.DRAGON_IMMUNE).tag(BlockTags.WITHER_IMMUNE).tag(BlockTags.PICKAXE_MINEABLE).tag(BlockTags.NEEDS_STONE_TOOL)
            .drop().model().build();
}
