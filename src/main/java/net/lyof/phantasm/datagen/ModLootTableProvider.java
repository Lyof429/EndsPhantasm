package net.lyof.phantasm.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.setup.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.entry.GroupEntry;

import java.util.Map;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        for (Map.Entry<Block, ItemConvertible> entry : ModRegistry.BLOCK_DROPS.entrySet()) {
            if (ModRegistry.BLOCK_MODELS.get(ModRegistry.Models.SLAB).contains(entry.getKey()))
                addDrop(entry.getKey(), slabDrops(entry.getKey()));
            else if (ModRegistry.BLOCK_MODELS.get(ModRegistry.Models.DOOR).contains(entry.getKey()))
                addDrop(entry.getKey(), doorDrops(entry.getKey()));
            else
                addDrop(entry.getKey(), entry.getValue());
        }
        addDrop(ModBlocks.PREAM_LEAVES, leavesDrops(ModBlocks.PREAM_LEAVES, ModBlocks.PREAM_SAPLING, 0.1f));
        addDrop(ModBlocks.HANGING_PREAM_LEAVES, dropsWithSilkTouchOrShears(ModBlocks.HANGING_PREAM_LEAVES,
                new GroupEntry.Builder()));
        /*addDrop(ModBlocks.HANGING_PREAM_BERRY, dropsWithSilkTouchOrShears(ModBlocks.HANGING_PREAM_BERRY,
                ItemEntry.builder(ModItems.PREAM_BERRY).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1)))));*/
    }
}
