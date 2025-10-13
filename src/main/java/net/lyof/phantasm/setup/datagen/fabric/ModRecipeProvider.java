package net.lyof.phantasm.setup.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.item.ModItems;
import net.lyof.phantasm.setup.ModRegistry;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.ItemTags;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {

    private static void offerTilesRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible result,
                                         ItemConvertible a, ItemConvertible b) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, result)
                .pattern("AB").pattern("BA")
                .input('A', a).input('B', b)
                .criterion(hasItem(a), conditionsFromItem(a))
                .criterion(hasItem(b), conditionsFromItem(b))
                .group(Registries.ITEM.getId(result.asItem()).getPath())
                .offerTo(exporter, Registries.ITEM.getId(result.asItem()));
    }


    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    private List<List<ItemConvertible>> getStoneCuttingRecipes() {
        List<List<ItemConvertible>> result = new ArrayList<>();

        List<ItemConvertible> crystal = List.of(ModBlocks.CRYSTAL_TILES, ModBlocks.CRYSTAL_TILE_STAIRS,
                ModBlocks.CRYSTAL_TILE_SLAB, ModBlocks.CRYSTAL_PILLAR);
        List<ItemConvertible> void_crystal = List.of(ModBlocks.VOID_CRYSTAL_TILES, ModBlocks.VOID_CRYSTAL_TILE_STAIRS,
                ModBlocks.VOID_CRYSTAL_TILE_SLAB, ModBlocks.VOID_CRYSTAL_PILLAR);
        List<ItemConvertible> polished_obsidian = List.of(ModBlocks.POLISHED_OBSIDIAN, ModBlocks.POLISHED_OBSIDIAN_BRICKS,
                ModBlocks.POLISHED_OBSIDIAN_BRICK_STAIRS, ModBlocks.POLISHED_OBSIDIAN_BRICK_SLAB,
                ModBlocks.POLISHED_OBSIDIAN_PILLAR, ModBlocks.CHISELED_OBSIDIAN);
        List<ItemConvertible> raw_purpur = List.of(ModBlocks.RAW_PURPUR, ModBlocks.RAW_PURPUR_BRICKS,
                ModBlocks.RAW_PURPUR_BRICK_STAIRS, ModBlocks.RAW_PURPUR_BRICK_SLAB, ModBlocks.RAW_PURPUR_TILES,
                ModBlocks.RAW_PURPUR_PILLAR, ModBlocks.RAW_PURPUR_BRICK_WALL);
        List<ItemConvertible> cirite = List.of(ModBlocks.CIRITE, ModBlocks.CIRITE_BRICKS,
                ModBlocks.CIRITE_BRICK_STAIRS, ModBlocks.CIRITE_BRICK_SLAB, ModBlocks.CIRITE_PILLAR,
                ModBlocks.CHISELED_CIRITE, ModBlocks.CIRITE_BRICK_WALL);

        result.add(crystal);
        result.add(void_crystal);
        result.add(polished_obsidian);
        result.add(raw_purpur);
        result.add(cirite);
        return result;
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        for (List<ItemConvertible> pool : getStoneCuttingRecipes()) {
            for (ItemConvertible out : pool) {
                for (ItemConvertible in : pool) {
                    if (out != in && !(in instanceof SlabBlock) && !(in instanceof StairsBlock))
                        offerStonecuttingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, out, in,
                                out instanceof SlabBlock ? 2 : 1);
                }
            }
        }

        offerStonecuttingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PURPUR_WALL, Blocks.PURPUR_BLOCK);

        for (Block parent : ModRegistry.BLOCK_SETS.keySet()) {
            for (Map.Entry<ModRegistry.Models, Block> entry : ModRegistry.BLOCK_SETS.get(parent).entrySet()) {
                if (entry.getKey() == ModRegistry.Models.STAIRS)
                    createStairsRecipe(entry.getValue(), Ingredient.ofItems(parent))
                            .criterion(hasItem(parent), conditionsFromItem(parent))
                            .offerTo(exporter);

                if (entry.getKey() == ModRegistry.Models.SLAB)
                    offerSlabRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, entry.getValue(), parent);

                if (entry.getKey() == ModRegistry.Models.PRESSURE_PLATE)
                    offerPressurePlateRecipe(exporter, entry.getValue(), parent);

                if (entry.getKey() == ModRegistry.Models.BUTTON)
                    offerSingleOutputShapelessRecipe(exporter, entry.getValue(), parent, "pream_button");

                if (entry.getKey() == ModRegistry.Models.FENCE)
                    createFenceRecipe(entry.getValue(), Ingredient.ofItems(parent))
                            .criterion(hasItem(parent), conditionsFromItem(parent))
                            .offerTo(exporter);

                if (entry.getKey() == ModRegistry.Models.FENCE_GATE)
                    createFenceGateRecipe(entry.getValue(), Ingredient.ofItems(parent))
                            .criterion(hasItem(parent), conditionsFromItem(parent))
                            .offerTo(exporter);

                if (entry.getKey() == ModRegistry.Models.SIGN)
                    createSignRecipe(entry.getValue(), Ingredient.ofItems(parent))
                            .criterion(hasItem(parent), conditionsFromItem(parent))
                            .offerTo(exporter);

                if (entry.getKey() == ModRegistry.Models.PANE)
                    ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, entry.getValue(), 16)
                            .input('#', parent)
                            .pattern("###").pattern("###")
                            .group(Registries.ITEM.getId(entry.getValue().asItem()).getPath())
                            .criterion(hasItem(parent),conditionsFromItem(parent))
                            .offerTo(exporter);

                if (entry.getKey() == ModRegistry.Models.WALL)
                    ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, entry.getValue(), 6)
                            .input('#', parent)
                            .pattern("###").pattern("###")
                            .group(Registries.ITEM.getId(entry.getValue().asItem()).getPath())
                            .criterion(hasItem(parent),conditionsFromItem(parent))
                            .offerTo(exporter);
            }
        }


        // Polished Obisidian
        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_OBSIDIAN, 8)
                .input(Blocks.OBSIDIAN, 4).input(Items.DRAGON_BREATH)
                .criterion(hasItem(Blocks.OBSIDIAN), conditionsFromItem(Blocks.OBSIDIAN))
                .criterion(hasItem(Items.DRAGON_BREATH), conditionsFromItem(Items.DRAGON_BREATH))
                .group("polished_obsidian").offerTo(exporter, Phantasm.makeID("polished_obsidian"));
        // Polished Obsidian Bricks
        createCondensingRecipe(RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_OBSIDIAN_BRICKS,
                Ingredient.ofItems(ModBlocks.POLISHED_OBSIDIAN))
                .criterion(hasItem(ModBlocks.POLISHED_OBSIDIAN), conditionsFromItem(ModBlocks.POLISHED_OBSIDIAN))
                .group("polished_obsidian_bricks").offerTo(exporter, Phantasm.makeID("polished_obsidian_bricks"));
        // Polished Obisidian Pillar
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_OBSIDIAN_PILLAR, 2)
                .pattern("O")
                .pattern("O")
                .input('O', ModBlocks.POLISHED_OBSIDIAN_BRICKS)
                .criterion(hasItem(ModBlocks.POLISHED_OBSIDIAN_BRICKS), conditionsFromItem(ModBlocks.POLISHED_OBSIDIAN_BRICKS))
                .group("polished_obsidian_pillar").offerTo(exporter, Phantasm.makeID("polished_obsidian_pillar"));
        // Chiseled Obsidian
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_OBSIDIAN)
                .pattern("O")
                .pattern("O")
                .input('O', ModBlocks.POLISHED_OBSIDIAN_BRICK_SLAB)
                .criterion(hasItem(ModBlocks.POLISHED_OBSIDIAN_BRICK_SLAB), conditionsFromItem(ModBlocks.POLISHED_OBSIDIAN_BRICK_SLAB))
                .group("chiseled_obsidian").offerTo(exporter, Phantasm.makeID("chiseled_obsidian"));


        // Crystal Tiles
        offerTilesRecipe(exporter, ModBlocks.CRYSTAL_TILES, ModBlocks.CRYSTAL_SHARD, Blocks.END_STONE);
        // Crystal Block
        offer2x2CompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CRYSTAL_BLOCK, ModBlocks.CRYSTAL_SHARD);
        // Crystal Pillar
        offerMosaicRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CRYSTAL_PILLAR, ModBlocks.CRYSTAL_TILE_SLAB);
        // Crystal Glass
        offerTilesRecipe(exporter, ModBlocks.CRYSTAL_GLASS, ModBlocks.CRYSTAL_SHARD, Blocks.GLASS);

        // Void Crystal Tiles
        offerTilesRecipe(exporter, ModBlocks.VOID_CRYSTAL_TILES, ModBlocks.VOID_CRYSTAL_SHARD, Blocks.END_STONE);
        // Void Crystal Block
        offer2x2CompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.VOID_CRYSTAL_BLOCK, ModBlocks.VOID_CRYSTAL_SHARD);
        // Void Crystal Pillar
        offerMosaicRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.VOID_CRYSTAL_PILLAR, ModBlocks.VOID_CRYSTAL_TILE_SLAB);
        // Void Crystal Glass
        offerTilesRecipe(exporter, ModBlocks.VOID_CRYSTAL_GLASS, ModBlocks.VOID_CRYSTAL_SHARD, Blocks.GLASS);


        // Crystalline Sword
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.CRYSTALLINE_SWORD)
                .pattern("C")
                .pattern("V")
                .pattern("S")
                .input('C', ModBlocks.CRYSTAL_BLOCK).input('V', ModBlocks.VOID_CRYSTAL_BLOCK)
                .input('S', Items.STICK)
                .criterion(hasItem(ModBlocks.CRYSTAL_BLOCK), conditionsFromItem(ModBlocks.CRYSTAL_BLOCK))
                .criterion(hasItem(ModBlocks.VOID_CRYSTAL_BLOCK), conditionsFromItem(ModBlocks.VOID_CRYSTAL_BLOCK))
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .group("crystalline_sword").offerTo(exporter, Phantasm.makeID("crystalline_sword"));
        // Crystalline Shovel
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.CRYSTALLINE_SHOVEL)
                .pattern("V")
                .pattern("S")
                .pattern("S")
                .input('V', ModBlocks.VOID_CRYSTAL_BLOCK)
                .input('S', Items.STICK)
                .criterion(hasItem(ModBlocks.CRYSTAL_BLOCK), conditionsFromItem(ModBlocks.CRYSTAL_BLOCK))
                .criterion(hasItem(ModBlocks.VOID_CRYSTAL_BLOCK), conditionsFromItem(ModBlocks.VOID_CRYSTAL_BLOCK))
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .group("crystalline_shovel").offerTo(exporter, Phantasm.makeID("crystalline_shovel"));
        // Crystalline Pickaxe
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.CRYSTALLINE_PICKAXE)
                .pattern("CVC")
                .pattern(" S ")
                .pattern(" S ")
                .input('C', ModBlocks.CRYSTAL_BLOCK).input('V', ModBlocks.VOID_CRYSTAL_BLOCK)
                .input('S', Items.STICK)
                .criterion(hasItem(ModBlocks.CRYSTAL_BLOCK), conditionsFromItem(ModBlocks.CRYSTAL_BLOCK))
                .criterion(hasItem(ModBlocks.VOID_CRYSTAL_BLOCK), conditionsFromItem(ModBlocks.VOID_CRYSTAL_BLOCK))
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .group("crystalline_pickaxe").offerTo(exporter, Phantasm.makeID("crystalline_pickaxe"));
        // Crystalline Axe
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.CRYSTALLINE_AXE)
                .pattern("CV")
                .pattern("CS")
                .pattern(" S")
                .input('C', ModBlocks.CRYSTAL_BLOCK).input('V', ModBlocks.VOID_CRYSTAL_BLOCK)
                .input('S', Items.STICK)
                .criterion(hasItem(ModBlocks.CRYSTAL_BLOCK), conditionsFromItem(ModBlocks.CRYSTAL_BLOCK))
                .criterion(hasItem(ModBlocks.VOID_CRYSTAL_BLOCK), conditionsFromItem(ModBlocks.VOID_CRYSTAL_BLOCK))
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .group("crystalline_axe").offerTo(exporter, Phantasm.makeID("crystalline_axe"));
        // Crystalline Hoe
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.CRYSTALLINE_HOE)
                .pattern("CV")
                .pattern(" S")
                .pattern(" S")
                .input('C', ModBlocks.CRYSTAL_BLOCK).input('V', ModBlocks.VOID_CRYSTAL_BLOCK)
                .input('S', Items.STICK)
                .criterion(hasItem(ModBlocks.CRYSTAL_BLOCK), conditionsFromItem(ModBlocks.CRYSTAL_BLOCK))
                .criterion(hasItem(ModBlocks.VOID_CRYSTAL_BLOCK), conditionsFromItem(ModBlocks.VOID_CRYSTAL_BLOCK))
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .group("crystalline_hoe").offerTo(exporter, Phantasm.makeID("crystalline_hoe"));

        // Delayer
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.DELAYER)
                .pattern("RCT")
                .pattern("SSS")
                .input('C', ModBlocks.CRYSTAL_BLOCK)
                .input('T', Items.REDSTONE_TORCH)
                .input('R', Items.REDSTONE)
                .input('S', Items.STONE)
                .criterion(hasItem(ModBlocks.CRYSTAL_BLOCK), conditionsFromItem(ModBlocks.CRYSTAL_BLOCK))
                .group("delayer").offerTo(exporter, Phantasm.makeID("delayer"));
        // Splitter
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.SPLITTER)
                .pattern(" T ")
                .pattern("TCT")
                .pattern("SSS")
                .input('C', ModBlocks.CRYSTAL_BLOCK)
                .input('T', Items.REDSTONE_TORCH)
                .input('S', Items.STONE)
                .criterion(hasItem(ModBlocks.CRYSTAL_BLOCK), conditionsFromItem(ModBlocks.CRYSTAL_BLOCK))
                .group("splitter").offerTo(exporter, Phantasm.makeID("splitter"));

        // Pream Planks
        offerPlanksRecipe(exporter, ModBlocks.PREAM_PLANKS, ModTags.Items.PREAM_LOGS, 4);
        createCondensingRecipe(RecipeCategory.BUILDING_BLOCKS, ModBlocks.PREAM_WOOD,
                Ingredient.ofItems(ModBlocks.PREAM_LOG))
                .criterion(hasItem(ModBlocks.PREAM_LOG), conditionsFromItem(ModBlocks.PREAM_LOG))
                .group("pream_wood").offerTo(exporter, Phantasm.makeID("pream_wood"));
        createCondensingRecipe(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STRIPPED_PREAM_WOOD,
                Ingredient.ofItems(ModBlocks.STRIPPED_PREAM_LOG))
                .criterion(hasItem(ModBlocks.STRIPPED_PREAM_LOG), conditionsFromItem(ModBlocks.STRIPPED_PREAM_LOG))
                .group("stripped_pream_wood").offerTo(exporter, Phantasm.makeID("stripped_pream_wood"));

        // Pream Hanging Sign
        offerHangingSignRecipe(exporter, ModItems.PREAM_HANGING_SIGN, ModBlocks.STRIPPED_PREAM_LOG);
        // Pream Door
        createDoorRecipe(ModBlocks.PREAM_DOOR, Ingredient.ofItems(ModBlocks.PREAM_PLANKS))
                .criterion(hasItem(ModBlocks.PREAM_PLANKS), conditionsFromItem(ModBlocks.PREAM_PLANKS))
                .offerTo(exporter);
        //Pream Trapdoor
        createTrapdoorRecipe(ModBlocks.PREAM_TRAPDOOR, Ingredient.ofItems(ModBlocks.PREAM_PLANKS))
                .criterion(hasItem(ModBlocks.PREAM_PLANKS), conditionsFromItem(ModBlocks.PREAM_PLANKS))
                .offerTo(exporter);

        // Raw Purpur Bricks
        createCondensingRecipe(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RAW_PURPUR_BRICKS,
                Ingredient.ofItems(ModBlocks.RAW_PURPUR))
                .criterion(hasItem(ModBlocks.RAW_PURPUR), conditionsFromItem(ModBlocks.RAW_PURPUR))
                .group("raw_purpur_bricks").offerTo(exporter, Phantasm.makeID("raw_purpur_bricks"));
        // Raw Purpur Tiles
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RAW_PURPUR_TILES, 4)
                .pattern("AB").pattern("BA")
                .input('A', ModBlocks.RAW_PURPUR).input('B', Blocks.END_STONE)
                .criterion(hasItem(ModBlocks.RAW_PURPUR), conditionsFromItem(ModBlocks.RAW_PURPUR))
                .criterion(hasItem(Blocks.END_STONE), conditionsFromItem(Blocks.END_STONE))
                .group("raw_purpur_tiles").offerTo(exporter, Phantasm.makeID("raw_purpur_tiles"));
        // Raw Purpur Pillar
        offerMosaicRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RAW_PURPUR_PILLAR, ModBlocks.RAW_PURPUR_BRICK_SLAB);

        // Purpur Wall
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.PURPUR_WALL, 6)
                .input('#', Blocks.PURPUR_BLOCK)
                .pattern("###").pattern("###")
                .group("purpur_wall")
                .criterion(hasItem(Blocks.PURPUR_BLOCK),conditionsFromItem(Blocks.PURPUR_BLOCK))
                .offerTo(exporter);
        // Purpur Lamp
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.PURPUR_LAMP, 3)
                .pattern("SSS")
                .pattern("GRG")
                .pattern("SSS")
                .input('S', Blocks.PURPUR_SLAB)
                .input('G', Blocks.PURPLE_STAINED_GLASS_PANE)
                .input('R', Blocks.END_ROD)
                .criterion(hasItem(Blocks.PURPUR_SLAB), conditionsFromItem(Blocks.PURPUR_SLAB))
                .criterion(hasItem(Blocks.PURPLE_STAINED_GLASS_PANE), conditionsFromItem(Blocks.PURPLE_STAINED_GLASS_PANE))
                .criterion(hasItem(Blocks.END_ROD), conditionsFromItem(Blocks.END_ROD))
                .group("purpur_lamp").offerTo(exporter, Phantasm.makeID("purpur_lamp"));

        // Oblivine to Oblivion
        offer2x2CompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.OBLIVION, ModBlocks.OBLIVINE);

        // Chorus Fruit Salad
        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, ModItems.CHORUS_FRUIT_SALAD)
                .input(ModItems.PREAM_BERRY)
                .input(ModItems.OBLIFRUIT)
                .input(Items.CHORUS_FRUIT, 2)
                .input(Items.BOWL)
                .criterion(hasItem(ModItems.PREAM_BERRY), conditionsFromItem(ModItems.PREAM_BERRY))
                .criterion(hasItem(ModItems.OBLIFRUIT), conditionsFromItem(ModItems.OBLIFRUIT))
                .group("chorus_fruit_salad").offerTo(exporter, Phantasm.makeID("chorus_fruit_salad"));

        // Crystal Flowers to Shards
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.CRYSTAL_SHARD)
                .input(Ingredient.fromTag(ModTags.Items.CRYSTAL_FLOWERS))
                .criterion("has_crystal_flower", conditionsFromTag(ModTags.Items.CRYSTAL_FLOWERS))
                .group("crystal_flower").offerTo(exporter, Phantasm.makeID("crystal_flower"));

        // Behemoth Meat Cooking
        offerFoodCookingRecipe(exporter, "furnace", CookingRecipeSerializer.SMELTING, 200,
                ModItems.BEHEMOTH_MEAT, ModItems.BEHEMOTH_STEAK, 5);
        offerFoodCookingRecipe(exporter, "smoker", CookingRecipeSerializer.SMOKING, 100,
                ModItems.BEHEMOTH_MEAT, ModItems.BEHEMOTH_STEAK, 5);
        offerFoodCookingRecipe(exporter, "campfire", CookingRecipeSerializer.CAMPFIRE_COOKING, 500,
                ModItems.BEHEMOTH_MEAT, ModItems.BEHEMOTH_STEAK, 5);

        // Cirite Bricks
        createCondensingRecipe(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CIRITE_BRICKS,
                Ingredient.ofItems(ModBlocks.CIRITE))
                .criterion(hasItem(ModBlocks.CIRITE), conditionsFromItem(ModBlocks.CIRITE))
                .group("cirite_bricks").offerTo(exporter, Phantasm.makeID("cirite_bricks"));
        // Cirite Pillar
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CIRITE_PILLAR, 2)
                .pattern("O")
                .pattern("O")
                .input('O', ModBlocks.CIRITE_BRICKS)
                .criterion(hasItem(ModBlocks.CIRITE_BRICKS), conditionsFromItem(ModBlocks.CIRITE_BRICKS))
                .group("cirite_pillar").offerTo(exporter, Phantasm.makeID("cirite_pillar"));
        // Chiseled Cirite
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_CIRITE)
                .pattern("O")
                .pattern("O")
                .input('O', ModBlocks.CIRITE_BRICK_SLAB)
                .criterion(hasItem(ModBlocks.CIRITE_BRICK_SLAB), conditionsFromItem(ModBlocks.CIRITE_BRICK_SLAB))
                .group("chiseled_cirite").offerTo(exporter, Phantasm.makeID("chiseled_cirite"));

        // Cirite Iron Ore
        offerFoodCookingRecipe(exporter, "furnace", CookingRecipeSerializer.SMELTING, 200,
                ModBlocks.CIRITE_IRON_ORE, Items.IRON_INGOT, 5);
        offerFoodCookingRecipe(exporter, "blast_furnace", CookingRecipeSerializer.BLASTING, 100,
                ModBlocks.CIRITE_IRON_ORE, Items.IRON_INGOT, 5);

        // Dralgae to Acidic Mass or Obsidian
        offer2x2CompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.ACIDIC_MASS, ModBlocks.DRALGAE);
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.OBSIDIAN, 2)
                .pattern("###")
                .pattern("#O#")
                .pattern("###")
                .input('#', ModBlocks.DRALGAE)
                .input('O', Blocks.OBSIDIAN)
                .criterion(hasItem(ModBlocks.DRALGAE), conditionsFromItem(ModBlocks.DRALGAE))
                .criterion(hasItem(Blocks.OBSIDIAN), conditionsFromItem(Blocks.OBSIDIAN))
                .group("obsidian").offerTo(exporter, Phantasm.makeID("obsidian"));

        // Choral Blocks
        offer2x2CompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHORAL_BLOCK, ModBlocks.CHORAL_FAN);
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.SUBWOOFER_BLOCK)
                .pattern("F##")
                .pattern("FO#")
                .pattern("F##")
                .input('#', ModBlocks.CHORAL_BLOCK)
                .input('F', ModBlocks.CHORAL_FAN)
                .input('O', Blocks.NOTE_BLOCK)
                .criterion(hasItem(ModBlocks.CHORAL_BLOCK), conditionsFromItem(ModBlocks.CHORAL_BLOCK))
                .criterion(hasItem(ModBlocks.CHORAL_FAN), conditionsFromItem(ModBlocks.CHORAL_FAN))
                .group("subwoofer_block").offerTo(exporter, Phantasm.makeID("subwoofer_block"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, Blocks.JUKEBOX)
                .pattern("###")
                .pattern("#C#")
                .pattern("###")
                .input('C', ModBlocks.CHORAL_BLOCK)
                .input('#', ItemTags.PLANKS)
                .criterion(hasItem(ModBlocks.CHORAL_BLOCK), conditionsFromItem(ModBlocks.CHORAL_BLOCK))
                .group("jukebox").offerTo(exporter, Phantasm.makeID("jukebox"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, Blocks.NOTE_BLOCK)
                .pattern("###")
                .pattern("#C#")
                .pattern("###")
                .input('C', ModBlocks.CHORAL_FAN)
                .input('#', ItemTags.PLANKS)
                .criterion(hasItem(ModBlocks.CHORAL_FAN), conditionsFromItem(ModBlocks.CHORAL_FAN))
                .group("note_block").offerTo(exporter, Phantasm.makeID("note_block"));

        // Choral Arrow
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.CHORAL_ARROW, 6)
                .pattern(" I ")
                .pattern(" F ")
                .pattern(" F ")
                .input('I', Items.IRON_INGOT)
                .input('F', ModBlocks.CHORAL_FAN)
                .criterion(hasItem(ModBlocks.CHORAL_FAN), conditionsFromItem(ModBlocks.CHORAL_FAN))
                .group("choral_arrow").offerTo(exporter, Phantasm.makeID("choral_arrow"));

        // Eggs Nihilo
        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, ModItems.EGGS_NIHILO)
                .input(ModItems.OBLIFRUIT, 5)
                .input(Items.DRAGON_EGG)
                .input(ModBlocks.FALLEN_STAR)
                .input(Items.BOWL)
                .criterion(hasItem(Items.DRAGON_EGG), conditionsFromItem(Items.DRAGON_EGG))
                .group("eggs_nihilo").offerTo(exporter, Phantasm.makeID("eggs_nihilo"));
    }
}
