package net.lyof.phantasm.setup;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.entity.client.ModModelLayers;
import net.lyof.phantasm.entity.client.model.CrystieModel;
import net.lyof.phantasm.entity.client.renderer.CrystieRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;

public class ModClientSetup implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        for (Block block : ModRegistry.BLOCK_CUTOUT)
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());


        EntityRendererRegistry.register(ModEntities.CRYSTIE, CrystieRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.CRYSTIE, CrystieModel::getTexturedModelData);
    }
}
