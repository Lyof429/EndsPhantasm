package net.lyof.phantasm.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.setup.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.GroupEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;

import java.util.Map;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    public void addDropWithSilkTouchOr(Block self, ItemConvertible loot) {
        addDropWithSilkTouchOr(self, loot, ConstantLootNumberProvider.create(1));
    }

    public void addDropWithSilkTouchOr(Block self, ItemConvertible loot, LootNumberProvider amount) {
        addDrop(self, dropsWithSilkTouch(self,
                new GroupEntry.Builder())
                .pool(LootPool.builder().conditionally(WITHOUT_SILK_TOUCH)
                        .rolls(amount)
                        .with(ItemEntry.builder(loot))));
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

        addDropWithSilkTouchOr(ModBlocks.VIVID_NIHILIUM_BLOCK, Items.END_STONE);

        addDrop(ModBlocks.VIVID_NIHILIUM, dropsWithShears(ModBlocks.VIVID_NIHILIUM));
    }
}
