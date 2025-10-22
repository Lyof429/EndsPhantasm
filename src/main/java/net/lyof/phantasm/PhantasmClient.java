package net.lyof.phantasm;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.lyof.phantasm.block.ModBlockEntities;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.entity.client.ModModelLayers;
import net.lyof.phantasm.entity.client.model.BehemothModel;
import net.lyof.phantasm.entity.client.model.CrystieModel;
import net.lyof.phantasm.entity.client.renderer.*;
import net.lyof.phantasm.particle.ModParticles;
import net.lyof.phantasm.particle.custom.ZzzParticle;
import net.lyof.phantasm.setup.ModPackets;
import net.lyof.phantasm.setup.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class PhantasmClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        for (Block block : ModRegistry.BLOCK_CUTOUT)
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());

        EntityRendererRegistry.register(ModEntities.CRYSTIE, CrystieRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.CRYSTIE, CrystieModel::getTexturedModelData);

        EntityRendererRegistry.register(ModEntities.BEHEMOTH, BehemothRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.BEHEMOTH, BehemothModel::getTexturedModelData);

        EntityRendererRegistry.register(ModEntities.POLYPPIE, PolyppieRenderer::new);
        //EntityModelLayerRegistry.registerModelLayer(ModModelLayers.BEHEMOTH, BehemothModel::getTexturedModelData);

        EntityRendererRegistry.register(ModEntities.CHORAL_ARROW, ChoralArrowRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.CHALLENGE_RUNE, ChallengeRuneBlockEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ModParticles.ZZZ, ZzzParticle.Factory::new);

        registerPackets();
    }

    public static void registerPackets() {
        ClientPlayNetworking.registerGlobalReceiver(ModPackets.INITIALIZE, ModPackets.Client::initialize);

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.BEHEMOTH_WAKES_UP, ModPackets.Client::behemothWakesUp);

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.CHALLENGE_STARTS, ModPackets.Client::challengeStarts);
        ClientPlayNetworking.registerGlobalReceiver(ModPackets.CHALLENGE_ENDS, ModPackets.Client::challengeEnds);

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.BEGIN_CUTSCENE_STARTS, ModPackets.Client::beginCutsceneStarts);

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.POLYPPIE_UPDATES, ModPackets.Client::polyppieUpdates);
        ClientPlayNetworking.registerGlobalReceiver(ModPackets.POLYPPIE_STARTS_BEING_CARRIED, ModPackets.Client::polyppieStartsBeingCarried);
        ClientPlayNetworking.registerGlobalReceiver(ModPackets.POLYPPIE_STOPS_BEING_CARRIED, ModPackets.Client::polyppieStopsBeingCarried);
        ClientPlayNetworking.registerGlobalReceiver(ModPackets.POLYPPIE_SETS_VARIANT, ModPackets.Client::polyppieSetsVariant);
    }
}
