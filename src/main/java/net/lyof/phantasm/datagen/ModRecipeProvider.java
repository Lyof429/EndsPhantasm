package net.lyof.phantasm.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    private List<List<ItemConvertible>> getStoneCuttingRecipes() {
        List<List<ItemConvertible>> result = new ArrayList<>();

        List<ItemConvertible> crystal = List.of(ModBlocks.CRYSTAL_TILES, ModBlocks.CRYSTAL_TILES_STAIRS,
                ModBlocks.CRYSTAL_TILES_SLAB, ModBlocks.CRYSTAL_PILLAR);
        List<ItemConvertible> void_crystal = List.of(ModBlocks.VOID_CRYSTAL_TILES, ModBlocks.VOID_CRYSTAL_TILES_STAIRS,
                ModBlocks.VOID_CRYSTAL_TILES_SLAB, ModBlocks.VOID_CRYSTAL_PILLAR);

        result.add(crystal);
        result.add(void_crystal);
        return result;
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        List<List<ItemConvertible>> STONE_CUTTABLE = getStoneCuttingRecipes();
        for (List<ItemConvertible> pool : STONE_CUTTABLE) {
            for (ItemConvertible a : pool) {
                for (ItemConvertible b : pool) {
                    if (a != b)
                        offerStonecuttingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, a, b,
                                a instanceof SlabBlock ? 2 : 1);
                }
            }
        }


        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_OBSIDIAN, 8)
                .input(Blocks.OBSIDIAN, 4).input(Items.DRAGON_BREATH)
                .criterion(hasItem(Blocks.OBSIDIAN), conditionsFromItem(Blocks.OBSIDIAN))
                .criterion(hasItem(Items.DRAGON_BREATH), conditionsFromItem(Items.DRAGON_BREATH))
                .group("polished_obsidian").offerTo(exporter, Phantasm.makeID("polished_obsidian"));
        createCondensingRecipe(RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_OBSIDIAN_BRICKS,
                    Ingredient.ofItems(ModBlocks.POLISHED_OBSIDIAN))
                .criterion(hasItem(ModBlocks.POLISHED_OBSIDIAN), conditionsFromItem(ModBlocks.POLISHED_OBSIDIAN))
                .group("polished_obsidian").offerTo(exporter, Phantasm.makeID("polished_obsidian_bricks"));

        // Crystal Tiles
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CRYSTAL_TILES)
                .pattern("AB").pattern("BA")
                .input('A', ModBlocks.CRYSTAL_SHARD).input('B', Blocks.END_STONE)
                .criterion(hasItem(ModBlocks.CRYSTAL_SHARD), conditionsFromItem(ModBlocks.CRYSTAL_SHARD))
                .criterion(hasItem(Blocks.END_STONE), conditionsFromItem(Blocks.END_STONE))
                .group("crystal_tiles").offerTo(exporter, Phantasm.makeID("crystal_tiles"));
        // Crystal Block
        offer2x2CompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CRYSTAL_BLOCK, ModBlocks.CRYSTAL_SHARD);
        // Crystal Pillar
        offerMosaicRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CRYSTAL_PILLAR, ModBlocks.CRYSTAL_TILES_SLAB);
        // Crystal Tiles Slab
        offerSlabRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CRYSTAL_TILES_SLAB, ModBlocks.CRYSTAL_TILES);
        // Crystal Tiles Stairs
        createStairsRecipe(ModBlocks.CRYSTAL_TILES_STAIRS, Ingredient.ofItems(ModBlocks.CRYSTAL_TILES))
                .criterion(hasItem(ModBlocks.CRYSTAL_TILES), conditionsFromItem(ModBlocks.CRYSTAL_TILES))
                .offerTo(exporter);


        // Void Crystal Tiles
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.VOID_CRYSTAL_TILES)
                .pattern("AB").pattern("BA")
                .input('A', ModBlocks.VOID_CRYSTAL_SHARD).input('B', Blocks.END_STONE)
                .criterion(hasItem(ModBlocks.VOID_CRYSTAL_SHARD), conditionsFromItem(ModBlocks.VOID_CRYSTAL_SHARD))
                .criterion(hasItem(Blocks.END_STONE), conditionsFromItem(Blocks.END_STONE))
                .group("void_crystal_tiles").offerTo(exporter, Phantasm.makeID("void_crystal_tiles"));
        // Void Crystal Block
        offer2x2CompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.VOID_CRYSTAL_BLOCK, ModBlocks.VOID_CRYSTAL_SHARD);
        // Void Crystal Pillar
        offerMosaicRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.VOID_CRYSTAL_PILLAR, ModBlocks.VOID_CRYSTAL_TILES_SLAB);
        // Void Crystal Tiles Slab
        offerSlabRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.VOID_CRYSTAL_TILES_SLAB, ModBlocks.VOID_CRYSTAL_TILES);
        // Void Crystal Tiles Stairs
        createStairsRecipe(ModBlocks.VOID_CRYSTAL_TILES_STAIRS, Ingredient.ofItems(ModBlocks.VOID_CRYSTAL_TILES))
                .criterion(hasItem(ModBlocks.VOID_CRYSTAL_TILES), conditionsFromItem(ModBlocks.VOID_CRYSTAL_TILES))
                .offerTo(exporter);
    }
}
