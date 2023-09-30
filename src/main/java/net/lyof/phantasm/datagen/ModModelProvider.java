package net.lyof.phantasm.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.lyof.phantasm.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;

import java.util.Map;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        for (Block block : ModRegistry.BLOCK_MODELS_CUBES)
            generator.registerSimpleCubeAll(block);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        for (Map.Entry<Item, Model> entry : ModRegistry.ITEM_MODELS.entrySet())
            generator.register(entry.getKey(), entry.getValue());
    }
}
