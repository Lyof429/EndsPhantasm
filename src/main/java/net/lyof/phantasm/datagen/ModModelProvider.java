package net.lyof.phantasm.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.lyof.phantasm.ModRegistry;
import net.lyof.phantasm.Phantasm;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
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

        Phantasm.log(ModRegistry.BLOCK_MODELS);
        for (Block block : ModRegistry.BLOCK_MODELS.get(ModRegistry.Models.CUBE))
            generator.registerSimpleCubeAll(block);
        for (Block block : ModRegistry.BLOCK_MODELS.get(ModRegistry.Models.PILLAR))
            generator.registerLog(block).log(block);

        for (Block block : ModRegistry.BLOCK_STAIRS_SLABS.keySet()) {
            pool = generator.registerCubeAllModelTexturePool(block);
            for (Pair<Block, ModRegistry.Models> pair : ModRegistry.BLOCK_STAIRS_SLABS.get(block)) {
                if (pair.getRight() == ModRegistry.Models.STAIRS)
                    pool.stairs(pair.getLeft());
                if (pair.getRight() == ModRegistry.Models.SLAB)
                    pool.slab(pair.getLeft());
            }
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        for (Map.Entry<Item, Model> entry : ModRegistry.ITEM_MODELS.entrySet())
            generator.register(entry.getKey(), entry.getValue());
    }
}
