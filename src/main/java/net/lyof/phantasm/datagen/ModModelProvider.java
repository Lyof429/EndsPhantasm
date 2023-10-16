package net.lyof.phantasm.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.setup.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.item.Item;

import java.util.ArrayList;
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
        for (Block block : ModRegistry.BLOCK_MODELS.getOrDefault(ModRegistry.Models.CUBE, new ArrayList<>()))
            generator.registerSimpleCubeAll(block);
        for (Block block : ModRegistry.BLOCK_MODELS.getOrDefault(ModRegistry.Models.PILLAR, new ArrayList<>()))
            generator.registerLog(block).log(block);
        for (Block block : ModRegistry.BLOCK_MODELS.getOrDefault(ModRegistry.Models.CROSS, new ArrayList<>()))
            generator.registerTintableCross(block, BlockStateModelGenerator.TintType.NOT_TINTED);

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
                    if (entry.getKey() == ModRegistry.Models.BUTTON)
                        pool.button(entry.getValue());
                    if (entry.getKey() == ModRegistry.Models.PRESSURE_PLATE)
                        pool.pressurePlate(entry.getValue());
                    if (entry.getKey() == ModRegistry.Models.FENCE)
                        pool.fence(entry.getValue());
                    if (entry.getKey() == ModRegistry.Models.FENCE_GATE)
                        pool.fenceGate(entry.getValue());
                    if (entry.getKey() == ModRegistry.Models.SIGN)
                        pool.family(new BlockFamily.Builder(block)
                                .sign(entry.getValue(), ModRegistry.BLOCK_SETS.get(block).get(ModRegistry.Models.WALL_SIGN))
                                .build());
                    if (entry.getKey() == ModRegistry.Models.HANGING_SIGN)
                        generator.registerHangingSign(ModBlocks.STRIPPED_PREAM_LOG,
                                entry.getValue(), ModRegistry.BLOCK_SETS.get(block).get(ModRegistry.Models.WALL_HANGING_SIGN));
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
