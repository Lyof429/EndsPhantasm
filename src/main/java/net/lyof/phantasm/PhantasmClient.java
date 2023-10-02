package net.lyof.phantasm;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.client.render.RenderLayer;

public class PhantasmClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CRYSTAL_SHARD, RenderLayer.getCutout());
    }
}
