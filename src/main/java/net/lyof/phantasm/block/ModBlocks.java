package net.lyof.phantasm.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.lyof.phantasm.setup.ModRegistry;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.custom.CrystalShardBlock;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.block.*;
import net.minecraft.data.client.Models;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.BlockSoundGroup;

import java.util.Map;

public class ModBlocks {
    public static void register() {
        Phantasm.log("Registering Blocks for modid : " + Phantasm.MOD_ID);

        ModRegistry.registerStairsAndSlab(CRYSTAL_TILES, CRYSTAL_TILES_STAIRS, CRYSTAL_TILES_SLAB);
        ModRegistry.registerStairsAndSlab(VOID_CRYSTAL_TILES, VOID_CRYSTAL_TILES_STAIRS, VOID_CRYSTAL_TILES_SLAB);
        ModRegistry.registerStairsAndSlab(POLISHED_OBSIDIAN_BRICKS, POLISHED_OBSIDIAN_STAIRS, POLISHED_OBSIDIAN_SLAB);

        ModRegistry.registerSet(PREAM_LOG, Map.of(ModRegistry.Models.WOOD, PREAM_WOOD));
        ModRegistry.registerSet(STRIPPED_PREAM_LOG, Map.of(ModRegistry.Models.WOOD, STRIPPED_PREAM_WOOD));
        ModRegistry.registerSet(PREAM_PLANKS, Map.of(
                ModRegistry.Models.STAIRS, PREAM_STAIRS,
                ModRegistry.Models.SLAB, PREAM_SLAB
        ));
    }

    // Block Settings
    private static final FabricBlockSettings CrystalMaterial = 
            FabricBlockSettings.copyOf(Blocks.DIAMOND_ORE).luminance(4).emissiveLighting((a, b, c) -> true);

    private static final FabricBlockSettings PolishedObsidianMaterial =
            FabricBlockSettings.copyOf(Blocks.OBSIDIAN).hardness(5);

    private static final FabricBlockSettings PreamWoodMaterial =
            FabricBlockSettings.copyOf(Blocks.OAK_LOG).mapColor(MapColor.BROWN);
    private static final FabricBlockSettings PreamPlankMaterial =
            FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).mapColor(MapColor.TERRACOTTA_YELLOW);
    //


    public static final Block FALLEN_STAR = ModRegistry.ofBlock("fallen_star",
            new Block(FabricBlockSettings.copyOf(Blocks.DIAMOND_BLOCK).mapColor(MapColor.LIGHT_BLUE_GRAY).luminance(15)))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().model().build();

    // Polished Obsidian Blockset
    public static final Block POLISHED_OBSIDIAN = ModRegistry.ofBlock("polished_obsidian",
            new Block(PolishedObsidianMaterial))
            .tag(BlockTags.DRAGON_IMMUNE).tool("iron_pickaxe").drop().model().build();
    public static final Block POLISHED_OBSIDIAN_BRICKS = ModRegistry.ofBlock("polished_obsidian_bricks",
                    new Block(PolishedObsidianMaterial))
            .tag(BlockTags.DRAGON_IMMUNE).tool("iron_pickaxe").drop().build();
    public static final Block POLISHED_OBSIDIAN_STAIRS = ModRegistry.ofBlock("polished_obsidian_stairs",
                    new StairsBlock(POLISHED_OBSIDIAN_BRICKS.getDefaultState(), PolishedObsidianMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.STAIRS).tagitem(ItemTags.STAIRS)
            .tool("iron_pickaxe").drop().cutout().build();
    public static final Block POLISHED_OBSIDIAN_SLAB = ModRegistry.ofBlock("polished_obsidian_slab",
                    new SlabBlock(PolishedObsidianMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.SLABS).tagitem(ItemTags.SLABS)
            .tool("iron_pickaxe").drop().cutout().build();

    public static final Block CRYSTAL_SHARD = ModRegistry.ofBlock("crystal_shard",
                    new CrystalShardBlock(FabricBlockSettings.copyOf(CrystalMaterial).luminance(7).sounds(BlockSoundGroup.GLASS)))
            .model(Models.GENERATED).tool("_pickaxe").drop().cutout().build();
    public static final Block VOID_CRYSTAL_SHARD = ModRegistry.ofBlock("void_crystal_shard",
                    new CrystalShardBlock(FabricBlockSettings.copyOf(CrystalMaterial).sounds(BlockSoundGroup.GLASS)))
            .model(Models.GENERATED).tool("_pickaxe").drop().cutout().build();

    // Crystal Blockset
    public static final Block CRYSTAL_BLOCK = ModRegistry.ofBlock("crystal_block",
                    new Block(CrystalMaterial))
            .model().tool("_pickaxe").drop().build();
    public static final Block CRYSTAL_TILES = ModRegistry.ofBlock("crystal_tiles",
                    new Block(CrystalMaterial)).tool("_pickaxe").drop().build();
    public static final Block CRYSTAL_TILES_STAIRS = ModRegistry.ofBlock("crystal_tiles_stairs",
                    new StairsBlock(CRYSTAL_TILES.getDefaultState(), CrystalMaterial))
            .tool("_pickaxe").drop()
            .tag(BlockTags.STAIRS).tagitem(ItemTags.STAIRS).cutout().build();
    public static final Block CRYSTAL_TILES_SLAB = ModRegistry.ofBlock("crystal_tiles_slab",
                    new SlabBlock(CrystalMaterial))
            .tool("_pickaxe").drop()
            .tag(BlockTags.SLABS).tagitem(ItemTags.SLABS).cutout().build();
    public static final Block CRYSTAL_PILLAR = ModRegistry.ofBlock("crystal_pillar",
                    new PillarBlock(CrystalMaterial))
            .model(ModRegistry.Models.PILLAR).tool("_pickaxe").drop().build();

    // Void Crystal Blockset
    public static final Block VOID_CRYSTAL_BLOCK = ModRegistry.ofBlock("void_crystal_block",
                    new Block(CrystalMaterial))
            .model().tool("_pickaxe").drop().build();
    public static final Block VOID_CRYSTAL_TILES = ModRegistry.ofBlock("void_crystal_tiles",
                    new Block(CrystalMaterial)).tool("_pickaxe").drop().build();
    public static final Block VOID_CRYSTAL_TILES_STAIRS = ModRegistry.ofBlock("void_crystal_tiles_stairs",
                    new StairsBlock(VOID_CRYSTAL_TILES.getDefaultState(), CrystalMaterial))
            .tool("_pickaxe").drop()
            .tag(BlockTags.STAIRS).tagitem(ItemTags.STAIRS).cutout().build();
    public static final Block VOID_CRYSTAL_TILES_SLAB = ModRegistry.ofBlock("void_crystal_tiles_slab",
                    new SlabBlock(CrystalMaterial))
            .tool("_pickaxe").drop()
            .tag(BlockTags.SLABS).tagitem(ItemTags.SLABS).cutout().build();
    public static final Block VOID_CRYSTAL_PILLAR = ModRegistry.ofBlock("void_crystal_pillar",
                    new PillarBlock(CrystalMaterial))
            .model(ModRegistry.Models.PILLAR).tool("_pickaxe").drop().build();

    // Pream Blockset
    public static final Block STRIPPED_PREAM_LOG = ModRegistry.ofBlock("stripped_pream_log",
                    new PillarBlock(PreamWoodMaterial))
            .tool("_axe")
            .tag(BlockTags.LOGS_THAT_BURN).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.PREAM_LOGS)
            .flammable(5, 5).fuel(300).drop().build();
    public static final Block PREAM_LOG = ModRegistry.ofBlock("pream_log",
                    new PillarBlock(PreamWoodMaterial))
            .tool("_axe").strip(STRIPPED_PREAM_LOG)
            .tag(BlockTags.LOGS_THAT_BURN).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.PREAM_LOGS)
            .flammable(5, 5).fuel(300).drop().build();

    public static final Block STRIPPED_PREAM_WOOD = ModRegistry.ofBlock("stripped_pream_wood",
                    new PillarBlock(PreamWoodMaterial))
            .tool("_axe")
            .tag(BlockTags.LOGS_THAT_BURN).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.PREAM_LOGS)
            .flammable(5, 5).fuel(300).drop().build();
    public static final Block PREAM_WOOD = ModRegistry.ofBlock("pream_wood",
                    new PillarBlock(PreamWoodMaterial))
            .tool("_axe").strip(STRIPPED_PREAM_WOOD)
            .tag(BlockTags.LOGS_THAT_BURN).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.PREAM_LOGS)
            .flammable(5, 5).fuel(300).drop().build();

    public static final Block PREAM_PLANKS = ModRegistry.ofBlock("pream_planks",
                    new Block(PreamPlankMaterial))
            .tool("_axe")
            .tag(BlockTags.PLANKS).tagitem(ItemTags.PLANKS)
            .flammable(5, 20).fuel(300).drop().build();
    public static final Block PREAM_STAIRS = ModRegistry.ofBlock("pream_stairs",
                    new StairsBlock(PREAM_PLANKS.getDefaultState(), PreamPlankMaterial))
            .tool("_axe")
            .tag(BlockTags.WOODEN_STAIRS).tagitem(ItemTags.WOODEN_STAIRS)
            .flammable(5, 20).fuel(300).drop().build();
    public static final Block PREAM_SLAB = ModRegistry.ofBlock("pream_slab",
                    new SlabBlock(PreamPlankMaterial))
            .tool("_axe")
            .tag(BlockTags.WOODEN_SLABS).tagitem(ItemTags.WOODEN_SLABS)
            .flammable(5, 20).fuel(300).drop().build();
/*
    public static final Block PREAM_WOOD = ModRegistry.ofBlock("pream_wood",
                    new PillarBlock(PreamWoodMaterial))
            .tool("_axe").strip(STRIPPED_PREAM_WOOD)
            .tag(BlockTags.LOGS_THAT_BURN).tagitem(ItemTags.LOGS_THAT_BURN)
            .flammable(5, 5).fuel(300).drop().build();
    public static final Block PREAM_WOOD = ModRegistry.ofBlock("pream_wood",
                    new PillarBlock(PreamWoodMaterial))
            .tool("_axe").strip(STRIPPED_PREAM_WOOD)
            .tag(BlockTags.LOGS_THAT_BURN).tagitem(ItemTags.LOGS_THAT_BURN)
            .flammable(5, 5).fuel(300).drop().build();*/
}
