package net.lyof.phantasm;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.entity.animation.BehemothAnimation;
import net.lyof.phantasm.entity.client.ModModelLayers;
import net.lyof.phantasm.entity.client.model.BehemothModel;
import net.lyof.phantasm.entity.client.model.CrystieModel;
import net.lyof.phantasm.entity.client.renderer.BehemothRenderer;
import net.lyof.phantasm.entity.client.renderer.ChoralArrowRenderer;
import net.lyof.phantasm.entity.client.renderer.CrystieRenderer;
import net.lyof.phantasm.entity.custom.BehemothEntity;
import net.lyof.phantasm.particle.ModParticles;
import net.lyof.phantasm.particle.custom.ZzzParticle;
import net.lyof.phantasm.setup.ModPackets;
import net.lyof.phantasm.setup.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class PhantasmClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        for (Block block : ModRegistry.BLOCK_CUTOUT)
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());

        EntityRendererRegistry.register(ModEntities.CRYSTIE, CrystieRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.CRYSTIE, CrystieModel::getTexturedModelData);

        EntityRendererRegistry.register(ModEntities.BEHEMOTH, BehemothRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.BEHEMOTH, BehemothModel::getTexturedModelData);

        EntityRendererRegistry.register(ModEntities.CHORAL_ARROW, ChoralArrowRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ModParticles.ZZZ, ZzzParticle.Factory::new);

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.BEHEMOTH_WAKE_UP, (client, handler, buf, responseSender) -> {
            int selfId = buf.readInt();
            int targetId = buf.readInt();
            client.execute(() -> {
                Entity self = client.world.getEntityById(selfId);
                Entity target = client.world.getEntityById(targetId);
                if (self instanceof BehemothEntity behemoth) {
                    Phantasm.log("Found it! " + behemoth.getBlockPos());
                    behemoth.setTarget((LivingEntity) target);
                }
            });
        });
    }
}
