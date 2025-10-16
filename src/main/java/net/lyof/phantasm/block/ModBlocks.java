package net.lyof.phantasm.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.custom.FacingBlock;
import net.lyof.phantasm.block.custom.*;
import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.item.ModItems;
import net.lyof.phantasm.setup.ModRegistry;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.feature.custom.tree.PreamSaplingGenerator;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.data.client.Models;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.Map;

public class ModBlocks {
    public static void register() {
        ModRegistry.addDrop(PREAM_SIGN, ModItems.PREAM_SIGN);
        ModRegistry.addDrop(PREAM_WALL_SIGN, ModItems.PREAM_SIGN);
        ModRegistry.addDrop(PREAM_HANGING_SIGN, ModItems.PREAM_HANGING_SIGN);
        ModRegistry.addDrop(PREAM_WALL_HANGING_SIGN, ModItems.PREAM_HANGING_SIGN);

        ModRegistry.registerStairsAndSlab(CRYSTAL_TILES, CRYSTAL_TILE_STAIRS, CRYSTAL_TILE_SLAB);
        ModRegistry.registerStairsAndSlab(VOID_CRYSTAL_TILES, VOID_CRYSTAL_TILE_STAIRS, VOID_CRYSTAL_TILE_SLAB);
        ModRegistry.registerStairsAndSlab(POLISHED_OBSIDIAN_BRICKS, POLISHED_OBSIDIAN_BRICK_STAIRS, POLISHED_OBSIDIAN_BRICK_SLAB);
        ModRegistry.registerSet(RAW_PURPUR_BRICKS, Map.of(
                ModRegistry.Models.STAIRS, RAW_PURPUR_BRICK_STAIRS,
                ModRegistry.Models.SLAB, RAW_PURPUR_BRICK_SLAB,
                ModRegistry.Models.WALL, RAW_PURPUR_BRICK_WALL
        ));
        ModRegistry.registerSet(CIRITE_BRICKS, Map.of(
                ModRegistry.Models.STAIRS, CIRITE_BRICK_STAIRS,
                ModRegistry.Models.SLAB, CIRITE_BRICK_SLAB,
                ModRegistry.Models.WALL, CIRITE_BRICK_WALL
        ));

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


    private static final FabricBlockSettings crystalMaterial =
            copy(Blocks.DIAMOND_ORE).luminance(4).emissiveLighting((a, b, c) -> true)
                    .nonOpaque();
    private static final FabricBlockSettings crystalGlassMaterial =
            FabricBlockSettings.create().emissiveLighting((a, b, c) -> true).hardness(0.75f).luminance(4)
            .nonOpaque().mapColor(MapColor.LIGHT_BLUE).sounds(BlockSoundGroup.GLASS)
            .allowsSpawning(Blocks::never).solidBlock(Blocks::never).suffocates(Blocks::never).blockVision(Blocks::never);

    private static final FabricBlockSettings polishedObsidianMaterial =
            copy(Blocks.OBSIDIAN).hardness(7);

    private static final FabricBlockSettings preamWoodMaterial =
            copy(Blocks.OAK_LOG).mapColor(MapColor.BROWN);
    private static final FabricBlockSettings preamPlankMaterial =
            copy(Blocks.OAK_PLANKS).mapColor(MapColor.TERRACOTTA_YELLOW);
    private static final FabricBlockSettings preamPassableMaterial =
            copy(Blocks.OAK_SIGN).mapColor(MapColor.TERRACOTTA_YELLOW);
    private static final FabricBlockSettings preamLeafMaterial =
            copy(Blocks.OAK_LEAVES).mapColor(MapColor.PURPLE);

    private static final FabricBlockSettings rawPurpurMaterial =
            copy(Blocks.BLACKSTONE).mapColor(MapColor.TERRACOTTA_PURPLE);

    private static final FabricBlockSettings oblivionMaterial =
            copy(Blocks.MOSS_BLOCK).mapColor(MapColor.BLACK);

    public static final WoodType PREAM = new WoodTypeBuilder().register(Phantasm.makeID("pream"), BlockSetType.OAK);
    //


    public static final Block FALLEN_STAR = ModRegistry.ofBlock("fallen_star",
            new Block(copy(Blocks.DIAMOND_BLOCK).mapColor(MapColor.LIGHT_BLUE_GRAY).luminance(15)))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE).tool("stone_pickaxe")
            .drop().model().build();

    // Polished Obsidian Blockset
    public static final Block POLISHED_OBSIDIAN = ModRegistry.ofBlock("polished_obsidian",
            new Block(polishedObsidianMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, ModTags.Blocks.END_CRYSTAL_PLACEABLE_ON).tool("iron_pickaxe").drop().model().build();
    public static final Block POLISHED_OBSIDIAN_BRICKS = ModRegistry.ofBlock("polished_obsidian_bricks",
            new Block(polishedObsidianMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, ModTags.Blocks.END_CRYSTAL_PLACEABLE_ON).tool("iron_pickaxe").drop().build();
    public static final Block POLISHED_OBSIDIAN_BRICK_STAIRS = ModRegistry.ofBlock("polished_obsidian_brick_stairs",
            new StairsBlock(POLISHED_OBSIDIAN_BRICKS.getDefaultState(), polishedObsidianMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, ModTags.Blocks.END_CRYSTAL_PLACEABLE_ON, BlockTags.STAIRS).tagitem(ItemTags.STAIRS)
            .tool("iron_pickaxe").drop().cutout().build();
    public static final Block POLISHED_OBSIDIAN_BRICK_SLAB = ModRegistry.ofBlock("polished_obsidian_brick_slab",
            new SlabBlock(polishedObsidianMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, ModTags.Blocks.END_CRYSTAL_PLACEABLE_ON, BlockTags.SLABS).tagitem(ItemTags.SLABS)
            .tool("iron_pickaxe").drop().cutout().build();

    public static final Block POLISHED_OBSIDIAN_PILLAR = ModRegistry.ofBlock("polished_obsidian_pillar",
                    new PillarBlock(polishedObsidianMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, ModTags.Blocks.END_CRYSTAL_PLACEABLE_ON).tool("iron_pickaxe")
            .model(ModRegistry.Models.PILLAR).drop().build();
    public static final Block CHISELED_OBSIDIAN = ModRegistry.ofBlock("chiseled_obsidian",
                    new FacingBlock(polishedObsidianMaterial))
            .tag(BlockTags.DRAGON_IMMUNE, ModTags.Blocks.END_CRYSTAL_PLACEABLE_ON).tool("iron_pickaxe")
            .drop().build();

    public static final Block CHALLENGE_RUNE = ModRegistry.ofBlock("challenge_rune",
            new ChallengeRuneBlock(FabricBlockSettings.create().mapColor(DyeColor.GRAY)
                    .pistonBehavior(PistonBehavior.BLOCK)
                    .strength(100, 1200)))
            .tag(BlockTags.DRAGON_IMMUNE).tool("_pickaxe")
            .build();

    public static final Block CRYSTAL_SHARD = ModRegistry.ofBlock("crystal_shard",
            new CrystalShardBlock(copy(crystalMaterial).luminance(7).sounds(BlockSoundGroup.GLASS)))
            .model(Models.GENERATED).tool("_pickaxe").drop().cutout().build();
    public static final Block VOID_CRYSTAL_SHARD = ModRegistry.ofBlock("void_crystal_shard",
            new CrystalShardBlock(copy(crystalMaterial).sounds(BlockSoundGroup.GLASS)))
            .model(Models.GENERATED).tool("_pickaxe").drop().cutout().build();


    // Crystal Blockset
    public static final Block CRYSTAL_BLOCK = ModRegistry.ofBlock("crystal_block",
            new Block(crystalMaterial))
            .model().tool("_pickaxe").drop().build();
    public static final Block CRYSTAL_TILES = ModRegistry.ofBlock("crystal_tiles",
            new Block(crystalMaterial)).tool("_pickaxe").drop().build();
    public static final Block CRYSTAL_TILE_STAIRS = ModRegistry.ofBlock("crystal_tile_stairs",
            new StairsBlock(CRYSTAL_TILES.getDefaultState(), crystalMaterial))
            .tool("_pickaxe").drop()
            .tag(BlockTags.STAIRS).tagitem(ItemTags.STAIRS).cutout().build();
    public static final Block CRYSTAL_TILE_SLAB = ModRegistry.ofBlock("crystal_tile_slab",
            new SlabBlock(crystalMaterial))
            .tool("_pickaxe").drop()
            .tag(BlockTags.SLABS).tagitem(ItemTags.SLABS).cutout().build();
    public static final Block CRYSTAL_PILLAR = ModRegistry.ofBlock("crystal_pillar",
            new PillarBlock(crystalMaterial))
            .tool("_pickaxe").drop().build();

    public static final Block CRYSTAL_GLASS = ModRegistry.ofBlock("crystal_glass",
            new GlassBlock(crystalGlassMaterial))
            .tag(ConventionalBlockTags.GLASS_BLOCKS)
            .cutout().drop().build();
    public static final Block CRYSTAL_GLASS_PANE = ModRegistry.ofBlock("crystal_glass_pane",
            new PaneBlock(crystalGlassMaterial))
            .tag(ConventionalBlockTags.GLASS_PANES)
            .cutout().model(ModRegistry.Models.PANE).drop().build();

    // Void Crystal Blockset
    public static final Block VOID_CRYSTAL_BLOCK = ModRegistry.ofBlock("void_crystal_block",
            new Block(crystalMaterial))
            .model().tool("_pickaxe").drop().build();
    public static final Block VOID_CRYSTAL_TILES = ModRegistry.ofBlock("void_crystal_tiles",
            new Block(crystalMaterial)).tool("_pickaxe").drop().build();
    public static final Block VOID_CRYSTAL_TILE_STAIRS = ModRegistry.ofBlock("void_crystal_tile_stairs",
            new StairsBlock(VOID_CRYSTAL_TILES.getDefaultState(), crystalMaterial))
            .tool("_pickaxe").drop()
            .tag(BlockTags.STAIRS).tagitem(ItemTags.STAIRS).cutout().build();
    public static final Block VOID_CRYSTAL_TILE_SLAB = ModRegistry.ofBlock("void_crystal_tile_slab",
            new SlabBlock(crystalMaterial))
            .tool("_pickaxe").drop()
            .tag(BlockTags.SLABS).tagitem(ItemTags.SLABS).cutout().build();
    public static final Block VOID_CRYSTAL_PILLAR = ModRegistry.ofBlock("void_crystal_pillar",
            new PillarBlock(crystalMaterial))
            .tool("_pickaxe").drop().build();

    public static final Block VOID_CRYSTAL_GLASS = ModRegistry.ofBlock("void_crystal_glass",
            new GlassBlock(crystalGlassMaterial))
            .tag(ConventionalBlockTags.GLASS_BLOCKS)
            .cutout().drop().build();
    public static final Block VOID_CRYSTAL_GLASS_PANE = ModRegistry.ofBlock("void_crystal_glass_pane",
            new PaneBlock(crystalGlassMaterial))
            .tag(ConventionalBlockTags.GLASS_PANES)
            .cutout().model(ModRegistry.Models.PANE).drop().build();

    // Crystal Redstone Components
    public static final Block DELAYER = ModRegistry.ofBlock("delayer",
            new DelayerBlock(copy(Blocks.REPEATER)))
            .cutout().drop().build();

    public static final Block SPLITTER = ModRegistry.ofBlock("splitter",
            new SplitterBlock(copy(Blocks.REPEATER)))
            .cutout().drop().build();


    // Pream Blockset
    public static final Block STRIPPED_PREAM_LOG = ModRegistry.ofBlock("stripped_pream_log",
            new PillarBlock(preamWoodMaterial))
            .tool("_axe")
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.PREAM_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.PREAM_LOGS)
            .flammable(5, 5).fuel(300).drop().build();
    public static final Block PREAM_LOG = ModRegistry.ofBlock("pream_log",
            new PillarBlock(preamWoodMaterial))
            .tool("_axe").strip(STRIPPED_PREAM_LOG)
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.PREAM_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.PREAM_LOGS)
            .flammable(5, 5).fuel(300).drop().build();

    public static final Block STRIPPED_PREAM_WOOD = ModRegistry.ofBlock("stripped_pream_wood",
            new PillarBlock(preamWoodMaterial))
            .tool("_axe")
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.PREAM_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.PREAM_LOGS)
            .flammable(5, 5).fuel(300).drop().build();
    public static final Block PREAM_WOOD = ModRegistry.ofBlock("pream_wood",
            new PillarBlock(preamWoodMaterial))
            .tool("_axe").strip(STRIPPED_PREAM_WOOD)
            .tag(BlockTags.LOGS_THAT_BURN, ModTags.Blocks.PREAM_BLOCKS).tagitem(ItemTags.LOGS_THAT_BURN, ModTags.Items.PREAM_LOGS)
            .flammable(5, 5).fuel(300).drop().build();

    public static final Block PREAM_LEAVES = ModRegistry.ofBlock("pream_leaves",
            new LeavesBlock(preamLeafMaterial))
            .tag(BlockTags.LEAVES, ModTags.Blocks.PREAM_BLOCKS, ModTags.Blocks.HANGING_PREAM_LEAVES_GROWABLE_ON)
            .tool("_hoe").flammable(5, 30)
            .model().cutout().build();
    public static final Block HANGING_PREAM_LEAVES = ModRegistry.ofBlock("hanging_pream_leaves",
            new HangingFruitBlock(copy(preamLeafMaterial).collidable(false).breakInstantly(),
                    () -> ModItems.PREAM_BERRY,
                    ModTags.Blocks.HANGING_PREAM_LEAVES_GROWABLE_ON,
                    Block.createCuboidShape(0, 8, 0, 16, 16, 16)))
            .tag(BlockTags.LEAVES, ModTags.Blocks.PREAM_BLOCKS)
            .tool("_hoe").flammable(5, 30)
            .cutout().build();

    public static final Block PREAM_PLANKS = ModRegistry.ofBlock("pream_planks",
            new Block(preamPlankMaterial))
            .tool("_axe")
            .tag(BlockTags.PLANKS, ModTags.Blocks.PREAM_BLOCKS).tagitem(ItemTags.PLANKS)
            .flammable(5, 20).fuel(300).drop().build();
    public static final Block PREAM_STAIRS = ModRegistry.ofBlock("pream_stairs",
            new StairsBlock(PREAM_PLANKS.getDefaultState(), preamPlankMaterial))
            .tool("_axe")
            .tag(BlockTags.WOODEN_STAIRS, ModTags.Blocks.PREAM_BLOCKS).tagitem(ItemTags.WOODEN_STAIRS)
            .flammable(5, 20).fuel(300).drop().build();
    public static final Block PREAM_SLAB = ModRegistry.ofBlock("pream_slab",
            new SlabBlock(preamPlankMaterial))
            .tool("_axe")
            .tag(BlockTags.WOODEN_SLABS, ModTags.Blocks.PREAM_BLOCKS).tagitem(ItemTags.WOODEN_SLABS)
            .flammable(5, 20).fuel(300).drop().build();

    public static final Block PREAM_PRESSURE_PLATE = ModRegistry.ofBlock("pream_pressure_plate",
            new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, preamPassableMaterial, BlockSetType.OAK))
            .tool("_axe")
            .tag(BlockTags.WOODEN_PRESSURE_PLATES, BlockTags.WALL_POST_OVERRIDE, ModTags.Blocks.PREAM_BLOCKS)
            .tagitem(ItemTags.WOODEN_PRESSURE_PLATES)
            .fuel(300).drop().build();
    public static final Block PREAM_BUTTON = ModRegistry.ofBlock("pream_button",
            new ButtonBlock(preamPassableMaterial, BlockSetType.OAK, 10, true))
            .tool("_axe")
            .tag(BlockTags.WOODEN_BUTTONS, ModTags.Blocks.PREAM_BLOCKS).tagitem(ItemTags.WOODEN_BUTTONS)
            .fuel(100).drop().build();

    public static final Block PREAM_FENCE = ModRegistry.ofBlock("pream_fence",
            new FenceBlock(preamPlankMaterial))
            .tool("_axe")
            .tag(BlockTags.WOODEN_FENCES, ModTags.Blocks.PREAM_BLOCKS).tagitem(ItemTags.WOODEN_FENCES)
            .flammable(5, 5).fuel(300).drop().build();
    public static final Block PREAM_FENCE_GATE = ModRegistry.ofBlock("pream_fence_gate",
            new FenceGateBlock(preamPlankMaterial, PREAM))
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
            new SignBlock(preamPassableMaterial, PREAM), null)
            .tag(BlockTags.SIGNS, BlockTags.STANDING_SIGNS, BlockTags.WALL_POST_OVERRIDE, ModTags.Blocks.PREAM_BLOCKS)
            .tool("_axe").drop(ModItems.PREAM_SIGN).build();
    public static final Block PREAM_WALL_SIGN = ModRegistry.ofBlock("pream_wall_sign",
            new WallSignBlock(preamPassableMaterial, PREAM), null)
            .tag(BlockTags.SIGNS, BlockTags.WALL_SIGNS, BlockTags.WALL_POST_OVERRIDE)
            .tool("_axe").drop(ModItems.PREAM_SIGN).build();

    public static final Block PREAM_HANGING_SIGN = ModRegistry.ofBlock("pream_hanging_sign",
            new HangingSignBlock(preamPassableMaterial, PREAM), null)
            .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.CEILING_HANGING_SIGNS, ModTags.Blocks.PREAM_BLOCKS)
            .tool("_axe").build();
    public static final Block PREAM_WALL_HANGING_SIGN = ModRegistry.ofBlock("pream_wall_hanging_sign",
            new WallHangingSignBlock(copy(preamPassableMaterial), PREAM), null)
            .tag(BlockTags.ALL_HANGING_SIGNS, BlockTags.WALL_HANGING_SIGNS, ModTags.Blocks.PREAM_BLOCKS)
            .tool("_axe").build();

    public static final Block PREAM_SAPLING = ModRegistry.ofBlock("pream_sapling",
            new SaplingBlock(new PreamSaplingGenerator(), copy(Blocks.OAK_SAPLING).mapColor(MapColor.PURPLE).hardness(0)))
            .model(ModRegistry.Models.CROSS).cutout()
            .tag(BlockTags.SAPLINGS, ModTags.Blocks.PREAM_BLOCKS).tagitem(ItemTags.SAPLINGS)
            .end_plant().fuel(100).drop().build();

    public static Block PREAM_CABINET = null;

    // Vivid Nihilium
    public static final Block VIVID_NIHILIUM = ModRegistry.ofBlock("vivid_nihilium",
            new NihiliumBlock(copy(Blocks.END_STONE).mapColor(MapColor.TEAL).ticksRandomly()))
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE).end_soil()
            .cutout().build();

    public static final Block TALL_VIVID_NIHILIS = ModRegistry.ofBlock("tall_vivid_nihilis",
                    new TallPlantBlock(copy(Blocks.WARPED_ROOTS).mapColor(MapColor.TEAL).replaceable()))
            .tagitem(ItemTags.FLOWERS).end_plant()
            .cutout().build();

    public static final Block VIVID_NIHILIS = ModRegistry.ofBlock("vivid_nihilis",
            new FernBlock(copy(Blocks.WARPED_ROOTS).mapColor(MapColor.TEAL).replaceable()) {
                @Override
                public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
                    if (TALL_VIVID_NIHILIS.getDefaultState().canPlaceAt(world, pos) && world.isAir(pos.up()))
                        TallPlantBlock.placeAt(world, TALL_VIVID_NIHILIS.getDefaultState(), pos, 2);
                }
            })
            .tagitem(ItemTags.FLOWERS)
            .model(ModRegistry.Models.CROSS).end_plant()
            .cutout().build();


    public static final Block STARFLOWER = ModRegistry.ofBlock("starflower",
                    new FlowerBlock(StatusEffects.NIGHT_VISION, 100, copy(Blocks.WARPED_ROOTS).mapColor(MapColor.LIGHT_BLUE).breakInstantly().luminance(5).emissiveLighting((a, b, c) -> true)))
            .drop().cutout()
            .tagitem(ModTags.Items.CRYSTAL_FLOWERS, ItemTags.SMALL_FLOWERS)
            .end_plant()
            .build();


    // Raw Purpur
    public static final Block RAW_PURPUR = ModRegistry.ofBlock("raw_purpur",
                    new Block(rawPurpurMaterial))
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE)
            .tagitem(ItemTags.STONE_CRAFTING_MATERIALS, ItemTags.STONE_TOOL_MATERIALS)
            .drop().model().build();
    public static final Block RAW_PURPUR_COAL_ORE = ModRegistry.ofBlock("raw_purpur_coal_ore",
                    new Block(copy(rawPurpurMaterial).mapColor(MapColor.DEEPSLATE_GRAY)))
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE, BlockTags.COAL_ORES)
            .model().build();

    public static final Block RAW_PURPUR_BRICKS = ModRegistry.ofBlock("raw_purpur_bricks",
                    new Block(rawPurpurMaterial))
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE)
            .drop().build();
    public static final Block RAW_PURPUR_BRICK_STAIRS = ModRegistry.ofBlock("raw_purpur_brick_stairs",
                    new StairsBlock(RAW_PURPUR_BRICKS.getDefaultState(), rawPurpurMaterial))
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE, BlockTags.STAIRS)
            .tagitem(ItemTags.STAIRS)
            .drop().build();
    public static final Block RAW_PURPUR_BRICK_SLAB = ModRegistry.ofBlock("raw_purpur_brick_slab",
                    new SlabBlock(rawPurpurMaterial))
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE, BlockTags.SLABS)
            .tagitem(ItemTags.SLABS)
            .drop().build();
    public static final Block RAW_PURPUR_BRICK_WALL = ModRegistry.ofBlock("raw_purpur_brick_wall",
                    new WallBlock(rawPurpurMaterial))
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE, BlockTags.WALLS)
            .tagitem(ItemTags.WALLS)
            .drop().build();

    public static final Block RAW_PURPUR_TILES = ModRegistry.ofBlock("raw_purpur_tiles",
                    new Block(rawPurpurMaterial))
            .model()
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE)
            .drop().build();
    public static final Block RAW_PURPUR_PILLAR = ModRegistry.ofBlock("raw_purpur_pillar",
                    new PillarBlock(rawPurpurMaterial))
            .model(ModRegistry.Models.PILLAR)
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE)
            .drop().build();

    public static final Block PURPUR_WALL = ModRegistry.ofBlock("purpur_wall",
                    new WallBlock(copy(Blocks.PURPUR_BLOCK)))
            .tool("_pickaxe").tag(BlockTags.WALLS)
            .tagitem(ItemTags.WALLS)
            .drop().build();
    public static final Block PURPUR_LAMP = ModRegistry.ofBlock("purpur_lamp",
                    new Block(copy(Blocks.PURPUR_BLOCK).luminance(15)))
            .tool("_pickaxe")
            .drop().build();


    // Oblivion
    public static final Block OBLIVION = ModRegistry.ofBlock("oblivion",
                    new Block(oblivionMaterial))
            .tag(ModTags.Blocks.OBLIVINE_GROWABLE_ON, ModTags.Blocks.END_PLANTS_GROWABLE_ON)
            .tool("_hoe")
            .drop()
            .model().build();

    public static final Block OBLIVINE = ModRegistry.ofBlock("oblivine",
                    new HangingFruitBlock(copy(oblivionMaterial).breakInstantly().collidable(false),
                            () -> ModItems.OBLIFRUIT,
                            ModTags.Blocks.OBLIVINE_GROWABLE_ON,
                            Block.createCuboidShape(2, 0, 2, 14, 16, 14)))
            .tag(ModTags.Blocks.OBLIVINE_GROWABLE_ON)
            .drop().cutout()
            .tag(BlockTags.CLIMBABLE)
            .build();

    public static final Block CRYSTALILY = ModRegistry.ofBlock("crystalily",
                    new HangingPlantBlock(copy(oblivionMaterial).mapColor(MapColor.LIGHT_BLUE).breakInstantly().luminance(7).emissiveLighting((a, b, c) -> true),
                            ModTags.Blocks.OBLIVINE_GROWABLE_ON,
                            Block.createCuboidShape(0, 8, 0, 16, 16, 16)))
            .drop().cutout()
            .tagitem(ModTags.Items.CRYSTAL_FLOWERS)
            .build();


    private static final FabricBlockSettings acidicMassMaterial =
            copy(Blocks.MOSS_BLOCK).mapColor(MapColor.DARK_DULL_PINK).strength(1.8f);

    private static final FabricBlockSettings pomeMaterial =
            copy(Blocks.MELON).mapColor(MapColor.DARK_DULL_PINK).strength(1.5f);

    private static final FabricBlockSettings ciriteMaterial =
            copy(Blocks.SANDSTONE).strength(2f).resistance(0.2f).slipperiness(1);

    // Acidic Nihilium
    public static final Block ACIDIC_NIHILIUM = ModRegistry.ofBlock("acidic_nihilium",
                    new NihiliumBlock(copy(Blocks.END_STONE).mapColor(MapColor.DARK_DULL_PINK).ticksRandomly()))
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE).end_soil()
            .cutout().build();

    public static final Block ACIDIC_NIHILIS = ModRegistry.ofBlock("acidic_nihilis",
                    new FernBlock(copy(Blocks.WARPED_ROOTS).mapColor(MapColor.DARK_DULL_PINK).replaceable()) {
                        @Override
                        public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
                            if (TALL_ACIDIC_NIHILIS.getDefaultState().canPlaceAt(world, pos) && world.isAir(pos.up()))
                                TallPlantBlock.placeAt(world, TALL_ACIDIC_NIHILIS.getDefaultState(), pos, 2);
                        }
                    })
            .tagitem(ItemTags.FLOWERS)
            .model(ModRegistry.Models.CROSS).end_plant()
            .cutout().build();

    public static final Block TALL_ACIDIC_NIHILIS = ModRegistry.ofBlock("tall_acidic_nihilis",
                    new TallPlantBlock(copy(Blocks.WARPED_ROOTS).mapColor(MapColor.DARK_DULL_PINK).replaceable()))
            .tagitem(ItemTags.FLOWERS).end_plant()
            .cutout().build();

    public static final Block DRAGON_MINT = ModRegistry.ofBlock("dragon_mint",
                    new DragonMintBlock(copy(Blocks.WARPED_ROOTS).mapColor(MapColor.DULL_PINK).sounds(BlockSoundGroup.CHERRY_LEAVES)
                            .offset(AbstractBlock.OffsetType.NONE)
                            .luminance(state -> state.get(HangingFruitBlock.HAS_FRUIT) ? 9 : 0).ticksRandomly()))
            .tagitem(ItemTags.FLOWERS).end_plant()
            .cutout().build();

    public static final Block ACIDIC_MASS = ModRegistry.ofBlock("acidic_mass",
                    new Block(acidicMassMaterial) {
                        @Override
                        public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
                            if (!entity.bypassesSteppingEffects()) {
                                entity.setVelocity(entity.getVelocity().multiply(0.4, 1, 0.4));
                                if (entity instanceof LivingEntity living)
                                    living.addStatusEffect(new StatusEffectInstance(ModEffects.CORROSION, 100, 0));
                            }
                            super.onSteppedOn(world, pos, state, entity);
                        }
                    })
            .tool("_hoe").tag(BlockTags.DRAGON_IMMUNE).end_soil()
            .model(ModRegistry.Models.ROTATABLE).drop().build();


    public static final Block DRALGAE = ModRegistry.ofBlock("dralgae",
                    new PillaringPlantBlock(FabricBlockSettings.create().noCollision().breakInstantly().sounds(BlockSoundGroup.ROOTS).pistonBehavior(PistonBehavior.DESTROY).mapColor(MapColor.DARK_CRIMSON),
                            ModTags.Blocks.DRALGAE_GROWABLE_ON,
                            Block.createCuboidShape(5, 0, 5, 11, 16, 11)))
            .model(ModRegistry.Models.CROSS).drop()
            .tag(ModTags.Blocks.DRALGAE_GROWABLE_ON, BlockTags.CLIMBABLE)
            .cutout().build();

    public static final Block POME = ModRegistry.ofBlock("pome",
                    new PomeBlock(pomeMaterial))
            .tool("_axe")
            .build();


    public static final Block CIRITE = ModRegistry.ofBlock("cirite",
                    new Block(ciriteMaterial))
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE)
            .tagitem(ItemTags.STONE_CRAFTING_MATERIALS, ItemTags.STONE_TOOL_MATERIALS)
            .drop()
            .model().build();

    public static final Block CIRITE_IRON_ORE = ModRegistry.ofBlock("cirite_iron_ore",
                    new ExperienceDroppingBlock(ciriteMaterial, UniformIntProvider.create(1, 4)) )
            .tool("stone_pickaxe").tag(BlockTags.DRAGON_IMMUNE, BlockTags.IRON_ORES)
            .tagitem(ItemTags.IRON_ORES)
            .model().build();

    public static final Block CIRITE_BRICKS = ModRegistry.ofBlock("cirite_bricks",
                    new Block(ciriteMaterial))
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE)
            .drop().build();
    public static final Block CIRITE_BRICK_STAIRS = ModRegistry.ofBlock("cirite_brick_stairs",
                    new StairsBlock(CIRITE_BRICKS.getDefaultState(), ciriteMaterial))
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE, BlockTags.STAIRS)
            .tagitem(ItemTags.STAIRS)
            .drop().build();
    public static final Block CIRITE_BRICK_SLAB = ModRegistry.ofBlock("cirite_brick_slab",
                    new SlabBlock(ciriteMaterial))
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE, BlockTags.SLABS)
            .tagitem(ItemTags.SLABS)
            .drop().build();
    public static final Block CIRITE_BRICK_WALL = ModRegistry.ofBlock("cirite_brick_wall",
                    new WallBlock(ciriteMaterial))
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE, BlockTags.WALLS)
            .tagitem(ItemTags.WALLS)
            .drop().build();

    public static final Block CIRITE_PILLAR = ModRegistry.ofBlock("cirite_pillar",
                    new PillarBlock(ciriteMaterial))
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE)
            .model(ModRegistry.Models.PILLAR)
            .drop().build();
    public static final Block CHISELED_CIRITE = ModRegistry.ofBlock("chiseled_cirite",
                    new Block(ciriteMaterial))
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE)
            .model().drop().build();


    // Choral
    public static final Block CHORAL_BLOCK = ModRegistry.ofBlock("choral_block",
                    new Block(copy(Blocks.BRAIN_CORAL_BLOCK).mapColor(MapColor.WHITE)))
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE)
            .model().drop().build();

    public static final DirectionalBlock CHORAL_FAN = (DirectionalBlock) ModRegistry.ofBlock("choral_fan",
                    new DirectionalBlock(copy(Blocks.BRAIN_CORAL_FAN).mapColor(MapColor.WHITE)))
            .tool("_pickaxe").tag(BlockTags.DRAGON_IMMUNE)
            .drop().cutout().build();

    public static final Block SUBWOOFER_BLOCK = ModRegistry.ofBlock("subwoofer_block",
                    new SubwooferBlock(copy(Blocks.NOTE_BLOCK).mapColor(MapColor.WHITE)))
            .tool("_axe").drop().build();


    public static final Block EGGS_NIHILO = ModRegistry.ofBlock("eggs_nihilo",
                    new EggsNihiloBlock(copy(Blocks.CAKE).mapColor(MapColor.BLACK).ticksRandomly()), null)
            .build();
}
