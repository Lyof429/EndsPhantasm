package net.lyof.phantasm.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.lyof.phantasm.ModRegistry;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModBlocks {
    public static void register() {
        Phantasm.log("Registering Blocks for modid : " + Phantasm.MOD_ID);
    }

    public static Block register(String id, Block block) {
        return register(id, block, new ArrayList<>());
    }

    public static Block register(String id, Block block, List<TagKey<Block>> tags) {
        BLOCKS.put(block, tags);

        ModItems.register(id, new BlockItem(block, new FabricItemSettings()));
        return Registry.register(Registries.BLOCK, Phantasm.makeID(id), block);
    }

    public static final Map<Block, List<TagKey<Block>>> BLOCKS = new HashMap<>();


/*
    public static final Block FALLEN_STAR = register("fallen_star",
            new Block(FabricBlockSettings.copyOf(Blocks.DIAMOND_BLOCK).luminance(15)),
            List.of(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.PICKAXE_MINEABLE, BlockTags.NEEDS_STONE_TOOL));*/
    public static final Block FALLEN_STAR = ModRegistry.ofBlock("fallen_star",
            new Block(FabricBlockSettings.copyOf(Blocks.DIAMOND_BLOCK).luminance(15)))
            .tag(BlockTags.DRAGON_IMMUNE).tag(BlockTags.WITHER_IMMUNE).tag(BlockTags.PICKAXE_MINEABLE).tag(BlockTags.NEEDS_STONE_TOOL)
            .drop().build();
}
