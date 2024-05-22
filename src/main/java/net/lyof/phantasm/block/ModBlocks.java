package net.lyof.phantasm.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.custom.CrystalShardBlock;
import net.lyof.phantasm.block.custom.HangingFruitBlock;
import net.lyof.phantasm.block.custom.HangingPlantBlock;
import net.lyof.phantasm.block.custom.NihiliumBlock;
import net.lyof.phantasm.item.ModItems;
import net.lyof.phantasm.setup.ModRegistry;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.feature.custom.tree.PreamSaplingGenerator;
import net.minecraft.block.*;
import net.minecraft.data.client.Models;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.BlockSoundGroup;

import java.util.Map;

public class ModBlocks {
    public static void register() {
        Phantasm.log("Registering Blocks for modid : " + Phantasm.MOD_ID);


        ModRegistry.addDrop(PREAM_SIGN, ModItems.PREAM_SIGN);
        ModRegistry.addDrop(PREAM_WALL_SIGN, ModItems.PREAM_SIGN);
        ModRegistry.addDrop(PREAM_HANGING_SIGN, ModItems.PREAM_HANGING_SIGN);
        ModRegistry.addDrop(PREAM_WALL_HANGING_SIGN, ModItems.PREAM_HANGING_SIGN);

        ModRegistry.registerStairsAndSlab(CRYSTAL_TILES, CRYSTAL_TILES_STAIRS, CRYSTAL_TILES_SLAB);
        ModRegistry.registerStairsAndSlab(VOID_CRYSTAL_TILES, VOID_CRYSTAL_TILES_STAIRS, VOID_CRYSTAL_TILES_SLAB);
        ModRegistry.registerStairsAndSlab(POLISHED_OBSIDIAN_BRICKS, POLISHED_OBSIDIAN_BRICKS_STAIRS, POLISHED_OBSIDIAN_BRICKS_SLAB);
        ModRegistry.registerStairsAndSlab(RAW_PURPUR_BRICKS, RAW_PURPUR_BRICKS_STAIRS, RAW_PURPUR_BRICKS_SLAB);

        ModRegistry.registerGlass(CRYSTAL_GLASS, CRYSTAL_GLASS_PANE);
        ModRegistry.registerGlass(VOID_CRYSTAL_GLASS, VOID_CRYSTAL_GLASS_PANE);

        ModRegistry.registerSet(PREAM_LOG, Map.of(ModRegistry.Models.WOOD, PREAM_WOOD));
        ModRegistry.registerSet(STRIPPED_PREAM_LOG, Map.of(ModRegistry.Models.WOOD, STRIPPED_PREAM_WOOD));

        ModRegistry.registerSet(PREAM_PLANKS, Map.of(
                ModRegistry.Models.STAIRS, PREAM_STAIRS,
                ModRegistry.Models.SLAB, PREAM_SLAB,
                ModRegistry.Models.BUTTON, PREAM_BUTTON,
                ModRegistry.Models.PRESSURE_PLATE, PREAM_PRESSURE_PLATE,
                ModRegistry.Models.FENCE, PREAM_FENCE,
                ModRegistry.Models.FENCE_GATE, PREAM_FENCE_GATE,
                ModRegistry.Models.SIGN, PREAM_SIGN,
                ModRegistry.Models.WALL_SIGN, PREAM_WALL_SIGN,
                ModRegistry.Models.HANGING_SIGN, PREAM_HANGING_SIGN,
                ModRegistry.Models.WALL_HANGING_SIGN, PREAM_WALL_HANGING_SIGN
        ));
    }

    // Block Settings
    private static FabricBlockSettings copy(FabricBlockSettings original) {
        return FabricBlockSettings.copyOf(original);
    }

    private static FabricBlockSettings copy(Block original) {
        return FabricBlockSettings.copyOf(original);
    }


    private static final FabricBlockSettings CrystalMaterial = 
            copy(Blocks.DIAMOND_ORE).luminance(4).emissiveLighting((a, b, c) -> true)
                    .nonOpaque();
    private static final FabricBlockSettings CrystalGlassMaterial =
            FabricBlockSettings.create().emissiveLighting((a, b, c) -> true).hardness(0.75f).luminance(4)
            .nonOpaque().mapColor(MapColor.LIGHT_BLUE).sounds(BlockSoundGroup.GLASS);

    private static final FabricBlockSettings PolishedObsidianMaterial =
            copy(Blocks.OBSIDIAN).hardness(7);

    private static final FabricBlockSettings PreamWoodMaterial =
            copy(Blocks.OAK_LOG).mapColor(MapColor.BROWN);
    private static final FabricBlockSettings PreamPlankMaterial =
            copy(Blocks.OAK_PLANKS).mapColor(MapColor.TERRACOTTA_YELLOW);
    private static final FabricBlockSettings PreamPassableMaterial =
            copy(Blocks.OAK_SIGN).mapColor(MapColor.TERRACOTTA_YELLOW);
    private static final FabricBlockSettings PreamLeafMaterial =
            copy(Blocks.OAK_LEAVES).mapColor(MapColor.PURPLE);

    private static final FabricBlockSettings RawPurpurMaterial =
            copy(Blocks.BLACKSTONE).mapColor(MapColor.TERRACOTTA_PURPLE);

    private static final FabricBlockSettings OblivionMaterial =
            copy(Blocks.MOSS_BLOCK).mapColor(MapColor.BLACK);

    public static final WoodType PREAM = new WoodTypeBuilder().register(Phantasm.makeID("pream"), BlockSetType.OAK);
    //


    public static final Block FALLEN_STAR = ModRegistry.ofBlock("fallen_star",
            new Block(copy(Blocks.DIAMOND_BLOCK).mapColor(MapColor.LIGHT_BLUE_GRAY).luminance(15)))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().model().build();

    // Polished Obsidian Blockset
    public static final Block POLISHED_OBSIDIAN = ModRegistry.ofBlock("polished_obsidian",
            new Block(PolishedObsidianMaterial))
            .tag(BlockTags.DRAGON_IMMUNE).tool("iron_pickaxe").drop().model().build();
    public static final Block POLISHED_OBSIDIAN_BRICKS = ModRegistry.ofBlock("polished_obsidian_bricks",
            new Block(PolishedObsidianMaterial))
            .tag(BlockTags.DRAGON_IMMUNE).tool("iron_pickaxe").drop().build();
    public static final Block POLISHED_OBSIDIAN_BRICKS_STAIRS = ModRegistry.ofBlock("polished_obsidian_bricks_stairs",
            new StairsBlock(POLISHED_OBSIDIAN_BRICKS.getDefaultState(), PolishedObsidianMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.STAIRS).tagitem(ItemTags.STAIRS)
            .tool("iron_pickaxe").drop().cutout().build();
    public static final Block POLISHED_OBSIDIAN_BRICKS_SLAB = ModRegistry.ofBlock("polished_obsidian_bricks_slab",
            new SlabBlock(PolishedObsidianMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.SLABS).tagitem(ItemTags.SLABS)
            .tool("iron_pickaxe").drop().cutout().build();

    public static final Block CRYSTAL_SHARD = ModRegistry.ofBlock("crystal_shard",
            new CrystalShardBlock(copy(CrystalMaterial).luminance(7).sounds(BlockSoundGroup.GLASS)))
            .model(Models.GENERATED).tool("_pickaxe").drop().cutout().build();
    public static final Block VOID_CRYSTAL_SHARD = ModRegistry.ofBlock("void_crystal_shard",
            new CrystalShardBlock(copy(CrystalMaterial).sounds(BlockSoundGroup.GLASS)))
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

    public static final Block CRYSTAL_GLASS = ModRegistry.ofBlock("crystal_glass",
            new Block(CrystalGlassMaterial))
            .cutout().drop().build();
    public static final Block CRYSTAL_GLASS_PANE = ModRegistry.ofBlock("crystal_glass_pane",
            new PaneBlock(CrystalGlassMaterial))
            .cutout().model(ModRegistry.Models.PANE).drop().build();

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

    public static final Block VOID_CRYSTAL_GLASS = ModRegistry.ofBlock("void_crystal_glass",
                    new Block(CrystalGlassMaterial))
            .cutout().drop().build();
    public static final Block VOID_CRYSTAL_GLASS_PANE = ModRegistry.ofBlock("void_crystal_glass_pane",
                    new PaneBlock(CrystalGlassMaterial))
            .cutout().model(ModRegistry.Models.PANE).drop().build();


    // Pream Blockset
    public static final Block STRIPPED_PREAM_LOG = ModRegistry.ofBlock("stripped_pream_log",
            new PillarBlock(PreamWoodMaterial))
            .tool("_axe")
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.PREAM_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.PREAM_LOGS)
            .flammable(5, 5).fuel(300).drop().build();
    public static final Block PREAM_LOG = ModRegistry.ofBlock("pream_log",
            new PillarBlock(PreamWoodMaterial))
            .tool("_axe").strip(STRIPPED_PREAM_LOG)
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.PREAM_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.PREAM_LOGS)
            .flammable(5, 5).fuel(300).drop().build();

    public static final Block STRIPPED_PREAM_WOOD = ModRegistry.ofBlock("stripped_pream_wood",
            new PillarBlock(PreamWoodMaterial))
            .tool("_axe")
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.PREAM_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.PREAM_LOGS)
            .flammable(5, 5).fuel(300).drop().build();
    public static final Block PREAM_WOOD = ModRegistry.ofBlock("pream_wood",
            new PillarBlock(PreamWoodMaterial))
            .tool("_axe").strip(STRIPPED_PREAM_WOOD)
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.PREAM_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.PREAM_LOGS)
            .flammable(5, 5).fuel(300).drop().build();

    public static final Block PREAM_LEAVES = ModRegistry.ofBlock("pream_leaves",
            new LeavesBlock(PreamLeafMaterial))
            .tag(BlockTags.LEAVES, ModTags.Blocks.PREAM_BLOCKS, ModTags.Blocks.HANGING_PREAM_LEAVES_GROWABLE_ON)
            .tool("_hoe").flammable(5, 30)
            .model().cutout().build();
    public static final Block HANGING_PREAM_LEAVES = ModRegistry.ofBlock("hanging_pream_leaves",
            new HangingFruitBlock(copy(PreamLeafMaterial).collidable(false).breakInstantly(),
                    () -> ModItems.PREAM_BERRY,
                    ModTags.Blocks.HANGING_PREAM_LEAVES_GROWABLE_ON,
                    Block.createCuboidShape(0, 8, 0, 16, 16, 16)))
            .tag(BlockTags.LEAVES, ModTags.Blocks.PREAM_BLOCKS)
            .tool("_hoe").flammable(5, 30)
            .cutout().build();

    public static final Block PREAM_PLANKS = ModRegistry.ofBlock("pream_planks",
            new Block(PreamPlankMaterial))
            .tool("_axe")
            .tag(BlockTags.PLANKS, ModTags.Blocks.PREAM_BLOCKS).tagitem(ItemTags.PLANKS)
            .flammable(5, 20).fuel(300).drop().build();
    public static final Block PREAM_STAIRS = ModRegistry.ofBlock("pream_stairs",
            new StairsBlock(PREAM_PLANKS.getDefaultState(), PreamPlankMaterial))
            .tool("_axe")
            .tag(BlockTags.WOODEN_STAIRS, ModTags.Blocks.PREAM_BLOCKS).tagitem(ItemTags.WOODEN_STAIRS)
            .flammable(5, 20).fuel(300).drop().build();
    public static final Block PREAM_SLAB = ModRegistry.ofBlock("pream_slab",
            new SlabBlock(PreamPlankMaterial))
            .tool("_axe")
            .tag(BlockTags.WOODEN_SLABS, ModTags.Blocks.PREAM_BLOCKS).tagitem(ItemTags.WOODEN_SLABS)
            .flammable(5, 20).fuel(300).drop().build();

    public static final Block PREAM_PRESSURE_PLATE = ModRegistry.ofBlock("pream_pressure_plate",
            new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, PreamPassableMaterial, BlockSetType.OAK))
            .tool("_axe")
            .tag(BlockTags.WOODEN_PRESSURE_PLATES, BlockTags.WALL_POST_OVERRIDE, ModTags.Blocks.PREAM_BLOCKS)
            .tagitem(ItemTags.WOODEN_PRESSURE_PLATES)
            .fuel(300).drop().build();
    public static final Block PREAM_BUTTON = ModRegistry.ofBlock("pream_button",
            new ButtonBlock(PreamPassableMaterial, BlockSetType.OAK, 10, true))
            .tool("_axe")
            .tag(BlockTags.WOODEN_BUTTONS, ModTags.Blocks.PREAM_BLOCKS).tagitem(ItemTags.WOODEN_BUTTONS)
            .fuel(100).drop().build();

    public static final Block PREAM_FENCE = ModRegistry.ofBlock("pream_fence",
            new FenceBlock(PreamPlankMaterial))
            .tool("_axe")
            .tag(BlockTags.WOODEN_FENCES, ModTags.Blocks.PREAM_BLOCKS).tagitem(ItemTags.WOODEN_FENCES)
            .flammable(5, 5).fuel(300).drop().build();
    public static final Block PREAM_FENCE_GATE = ModRegistry.ofBlock("pream_fence_gate",
            new FenceGateBlock(PreamPlankMaterial, PREAM))
            .tool("_axe")
            .tag(BlockTags.WOODEN_FENCES, BlockTags.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER, ModTags.Blocks.PREAM_BLOCKS)
            .tagitem(ItemTags.WOODEN_FENCES, ItemTags.FENCE_GATES)
            .flammable(5, 5).fuel(300).drop().build();

    public static final Block PREAM_DOOR = ModRegistry.ofBlock("pream_door",
            new DoorBlock(copy(Blocks.OAK_DOOR).mapColor(MapColor.TERRACOTTA_YELLOW), BlockSetType.OAK))
            .tool("_axe")
            .tag(BlockTags.WOODEN_DOORS, ModTags.Blocks.PREAM_BLOCKS)
            .tagitem(ItemTags.WOODEN_DOORS)
            .model(ModRegistry.Models.DOOR).cutout()
            .drop().build();
    public static final Block PREAM_TRAPDOOR = ModRegistry.ofBlock("pream_trapdoor",
            new TrapdoorBlock(copy(Blocks.OAK_TRAPDOOR).mapColor(MapColor.TERRACOTTA_YELLOW), BlockSetType.OAK))
            .tool("_axe")
            .tag(BlockTags.WOODEN_TRAPDOORS, ModTags.Blocks.PREAM_BLOCKS)
            .tagitem(ItemTags.WOODEN_TRAPDOORS)
            .model(ModRegistry.Models.TRAPDOOR).cutout()
            .drop().build();

    public static final Block PREAM_SIGN = ModRegistry.ofBlock("pream_sign",
            new SignBlock(PreamPassableMaterial, PREAM), false)
            .tag(BlockTags.SIGNS, BlockTags.STANDING_SIGNS, BlockTags.WALL_POST_OVERRIDE, ModTags.Blocks.PREAM_BLOCKS)
            .tool("_axe").drop(ModItems.PREAM_SIGN).build();
    public static final Block PREAM_WALL_SIGN = ModRegistry.ofBlock("pream_wall_sign",
            new WallSignBlock(PreamPassableMaterial, PREAM), false)
            .tag(BlockTags.SIGNS, BlockTags.WALL_SIGNS, BlockTags.WALL_POST_OVERRIDE)
            .tool("_axe").drop(ModItems.PREAM_SIGN).build();

    public static final Block PREAM_HANGING_SIGN = ModRegistry.ofBlock("pream_hanging_sign",
            new HangingSignBlock(PreamPassableMaterial, PREAM), false)
            .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.CEILING_HANGING_SIGNS, ModTags.Blocks.PREAM_BLOCKS)
            .tool("_axe").build();
    public static final Block PREAM_WALL_HANGING_SIGN = ModRegistry.ofBlock("pream_wall_hanging_sign",
            new WallHangingSignBlock(copy(PreamPassableMaterial), PREAM), false)
            .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.WALL_HANGING_SIGNS, ModTags.Blocks.PREAM_BLOCKS)
            .tool("_axe").build();

    public static final Block PREAM_SAPLING = ModRegistry.ofBlock("pream_sapling",
            new SaplingBlock(new PreamSaplingGenerator(), copy(Blocks.OAK_SAPLING).mapColor(MapColor.PURPLE).hardness(0)))
            .model(ModRegistry.Models.CROSS).cutout()
            .tag(BlockTags.SAPLINGS, ModTags.Blocks.PREAM_BLOCKS).tagitem(ItemTags.SAPLINGS)
            .end_plant().fuel(100).drop().build();

    // Vivid Nihilium
    public static final Block VIVID_NIHILIUM = ModRegistry.ofBlock("vivid_nihilium",
            new NihiliumBlock(copy(Blocks.END_STONE).mapColor(MapColor.TEAL).ticksRandomly()))
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE).end_soil()
            .cutout().build();

    public static final Block VIVID_NIHILIS = ModRegistry.ofBlock("vivid_nihilis",
            new PlantBlock(copy(Blocks.WARPED_ROOTS).mapColor(MapColor.TEAL).replaceable()))
            .tagitem(ItemTags.FLOWERS)
            .model(ModRegistry.Models.CROSS).end_plant()
            .cutout().build();

    public static final Block TALL_VIVID_NIHILIS = ModRegistry.ofBlock("tall_vivid_nihilis",
                    new TallPlantBlock(copy(Blocks.WARPED_ROOTS).mapColor(MapColor.TEAL).replaceable()))
            .tagitem(ItemTags.FLOWERS).end_plant()
            .cutout().build();


    // Raw Purpur
    public static final Block RAW_PURPUR = ModRegistry.ofBlock("raw_purpur",
                    new Block(RawPurpurMaterial))
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE)
            .tagitem(ItemTags.STONE_CRAFTING_MATERIALS, ItemTags.STONE_TOOL_MATERIALS)
            .drop()
            .model().build();

    public static final Block RAW_PURPUR_BRICKS = ModRegistry.ofBlock("raw_purpur_bricks",
                    new Block(RawPurpurMaterial))
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE)
            .drop().build();
    public static final Block RAW_PURPUR_BRICKS_STAIRS = ModRegistry.ofBlock("raw_purpur_bricks_stairs",
                    new StairsBlock(RAW_PURPUR_BRICKS.getDefaultState(), RawPurpurMaterial))
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE, BlockTags.STAIRS)
            .tagitem(ItemTags.STAIRS)
            .drop().build();
    public static final Block RAW_PURPUR_BRICKS_SLAB = ModRegistry.ofBlock("raw_purpur_bricks_slab",
                    new SlabBlock(RawPurpurMaterial))
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE, BlockTags.SLABS)
            .tagitem(ItemTags.SLABS)
            .drop().build();

    public static final Block RAW_PURPUR_TILES = ModRegistry.ofBlock("raw_purpur_tiles",
                    new Block(RawPurpurMaterial))
            .model()
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE)
            .drop().build();
    public static final Block RAW_PURPUR_PILLAR = ModRegistry.ofBlock("raw_purpur_pillar",
                    new PillarBlock(RawPurpurMaterial))
            .model(ModRegistry.Models.PILLAR)
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE)
            .drop().build();

    public static final Block PURPUR_LAMP = ModRegistry.ofBlock("purpur_lamp",
                    new Block(copy(Blocks.PURPUR_BLOCK).luminance(15)))
            .tool("_pickaxe")
            .drop().build();


    // Oblivion
    public static final Block OBLIVION = ModRegistry.ofBlock("oblivion",
                    new Block(OblivionMaterial))
            .tag(ModTags.Blocks.OBLIVINE_GROWABLE_ON)
            .tool("_hoe")
            .drop()
            .model().build();
    public static final Block OBLIVINE = ModRegistry.ofBlock("oblivine",
                    new HangingFruitBlock(copy(OblivionMaterial).breakInstantly().collidable(false),
                            () -> ModItems.OBLIFRUIT,
                            ModTags.Blocks.OBLIVINE_GROWABLE_ON,
                            Block.createCuboidShape(2, 0, 2, 14, 16, 14)))
            .model(ModRegistry.Models.CROSS).tag(ModTags.Blocks.OBLIVINE_GROWABLE_ON)
            .drop().cutout()
            .tag(BlockTags.CLIMBABLE)
            .build();
}
