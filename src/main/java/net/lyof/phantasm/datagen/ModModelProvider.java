package net.lyof.phantasm.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.setup.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;
import net.minecraft.util.Pair;

import java.util.Map;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        BlockStateModelGenerator.BlockTexturePool pool;
        BlockStateModelGenerator.LogTexturePool poollog;

        Phantasm.log(ModRegistry.BLOCK_SETS);
        for (Block block : ModRegistry.BLOCK_MODELS.get(ModRegistry.Models.CUBE))
            generator.registerSimpleCubeAll(block);
        for (Block block : ModRegistry.BLOCK_MODELS.get(ModRegistry.Models.PILLAR))
            generator.registerLog(block).log(block);

        for (Block block : ModRegistry.BLOCK_SETS.keySet()) {
            if (ModRegistry.BLOCK_SETS.get(block).containsKey(ModRegistry.Models.WOOD)) {
                poollog = generator.registerLog(block);
                poollog.log(block);
                for (Map.Entry<ModRegistry.Models, Block> entry : ModRegistry.BLOCK_SETS.get(block).entrySet()) {
                    if (entry.getKey() == ModRegistry.Models.WOOD)
                        poollog.wood(entry.getValue());
                }
            }
            else {
                pool = generator.registerCubeAllModelTexturePool(block);
                for (Map.Entry<ModRegistry.Models, Block> entry : ModRegistry.BLOCK_SETS.get(block).entrySet()) {
                    if (entry.getKey() == ModRegistry.Models.STAIRS)
                        pool.stairs(entry.getValue());
                    if (entry.getKey() == ModRegistry.Models.SLAB)
                        pool.slab(entry.getValue());
                }
            }
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        for (Map.Entry<Item, Model> entry : ModRegistry.ITEM_MODELS.entrySet())
            generator.register(entry.getKey(), entry.getValue());
    }
}
