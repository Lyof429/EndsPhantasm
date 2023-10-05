package net.lyof.phantasm.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.lyof.phantasm.setup.ModRegistry;
import net.minecraft.block.Block;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        for (Block block : ModRegistry.BLOCK_AUTODROPS) {
            if (ModRegistry.BLOCK_MODELS.get(ModRegistry.Models.SLAB).contains(block))
                addDrop(block, slabDrops(block));
            else
                addDrop(block);
        }
    }
}
