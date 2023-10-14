package net.lyof.phantasm.datagen;

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
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        List<ItemConvertible> polished_obsidian = List.of(ModBlocks.POLISHED_OBSIDIAN, ModBlocks.POLISHED_OBSIDIAN_BRICKS,
                ModBlocks.POLISHED_OBSIDIAN_STAIRS, ModBlocks.POLISHED_OBSIDIAN_SLAB);

        result.add(crystal);
        result.add(void_crystal);
        result.add(polished_obsidian);
        return result;
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        for (List<ItemConvertible> pool : getStoneCuttingRecipes()) {
            for (ItemConvertible out : pool) {
                for (ItemConvertible in : pool) {
                    if (out != in && !(in instanceof SlabBlock))
                        offerStonecuttingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, out, in,
                                out instanceof SlabBlock ? 2 : 1);
                }
            }
        }

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

                if (entry.getKey() == ModRegistry.Models.HANGING_SIGN)
                    offerHangingSignRecipe(exporter, entry.getValue(), parent);
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
                .group("polished_obsidian_bricks").offerTo(exporter, Phantasm.makeID("polished_obsidian_bricks"));

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

        // Pream Planks
        offerPlanksRecipe(exporter, ModBlocks.PREAM_PLANKS, ModTags.Items.PREAM_LOGS, 4);
    }
}
