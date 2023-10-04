package net.lyof.phantasm.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.lyof.phantasm.ModRegistry;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.custom.CrystalShardBlock;
import net.minecraft.block.*;
import net.minecraft.data.client.Models;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.BlockSoundGroup;

public class ModBlocks {
    public static void register() {
        Phantasm.log("Registering Blocks for modid : " + Phantasm.MOD_ID);
    }

    private static final FabricBlockSettings CrystalMaterial = 
            FabricBlockSettings.copyOf(Blocks.DIAMOND_ORE).luminance(4).emissiveLighting((a, b, c) -> true);


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

    public static final Block CRYSTAL_SHARD = ModRegistry.ofBlock("crystal_shard",
                    new CrystalShardBlock(CrystalMaterial.luminance(7).sounds(BlockSoundGroup.GLASS)))
            .model(Models.GENERATED).tool("_pickaxe").drop().cutout().build();
    public static final Block VOID_CRYSTAL_SHARD = ModRegistry.ofBlock("void_crystal_shard",
                    new CrystalShardBlock(CrystalMaterial.sounds(BlockSoundGroup.GLASS)))
            .model(Models.GENERATED).tool("_pickaxe").drop().cutout().build();

    public static final Block CRYSTAL_BLOCK = ModRegistry.ofBlock("crystal_block",
                    new Block(CrystalMaterial))
            .model().tool("_pickaxe").drop().build();
    public static final Block CRYSTAL_TILES = ModRegistry.ofBlock("crystal_tiles",
                    new Block(CrystalMaterial))
            .tool("_pickaxe").drop().build();
    public static final Block CRYSTAL_TILES_STAIRS = ModRegistry.ofBlock("crystal_tiles_stairs",
                    new StairsBlock(CRYSTAL_BLOCK.getDefaultState(), CrystalMaterial))
            .model(ModRegistry.Models.STAIRS, CRYSTAL_TILES).tool("_pickaxe").drop().cutout().build();
    public static final Block CRYSTAL_TILES_SLAB = ModRegistry.ofBlock("crystal_tiles_slab",
                    new SlabBlock(CrystalMaterial))
            .model(ModRegistry.Models.SLAB, CRYSTAL_TILES).tool("_pickaxe").drop().cutout().build();
    public static final Block CRYSTAL_PILLAR = ModRegistry.ofBlock("crystal_pillar",
                    new PillarBlock(CrystalMaterial))
            .model(ModRegistry.Models.PILLAR).tool("_pickaxe").drop().build();

    public static final Block VOID_CRYSTAL_BLOCK = ModRegistry.ofBlock("void_crystal_block",
                    new Block(CrystalMaterial))
            .model().tool("_pickaxe").drop().build();
    public static final Block VOID_CRYSTAL_TILES = ModRegistry.ofBlock("void_crystal_tiles",
                    new Block(CrystalMaterial))
            .tool("_pickaxe").drop().build();
    public static final Block VOID_CRYSTAL_TILES_STAIRS = ModRegistry.ofBlock("void_crystal_tiles_stairs",
                    new StairsBlock(CRYSTAL_BLOCK.getDefaultState(), CrystalMaterial))
            .model(ModRegistry.Models.STAIRS, VOID_CRYSTAL_TILES).tool("_pickaxe").drop().cutout().build();
    public static final Block VOID_CRYSTAL_TILES_SLAB = ModRegistry.ofBlock("void_crystal_tiles_slab",
                    new SlabBlock(CrystalMaterial))
            .model(ModRegistry.Models.SLAB, VOID_CRYSTAL_TILES).tool("_pickaxe").drop().cutout().build();
    public static final Block VOID_CRYSTAL_PILLAR = ModRegistry.ofBlock("void_crystal_pillar",
                    new PillarBlock(CrystalMaterial))
            .model(ModRegistry.Models.PILLAR).tool("_pickaxe").drop().build();
}
