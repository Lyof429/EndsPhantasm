package net.lyof.phantasm.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.setup.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;

import java.util.Map;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        for (Map.Entry<Block, ItemConvertible> entry : ModRegistry.BLOCK_DROPS.entrySet()) {
            Phantasm.log(entry);
            if (ModRegistry.BLOCK_MODELS.get(ModRegistry.Models.SLAB).contains(entry.getKey()))
                addDrop(entry.getKey(), slabDrops(entry.getKey()));
            else
                addDrop(entry.getKey(), entry.getValue());
        }
    }
}
