package net.lyof.phantasm;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;

public class ModClientSetup implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        for (Block block : ModRegistry.BLOCK_CUTOUT)
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
    }
}
