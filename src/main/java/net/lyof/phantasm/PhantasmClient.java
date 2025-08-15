package net.lyof.phantasm;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.lyof.phantasm.block.ModBlockEntities;
import net.lyof.phantasm.block.challenge.Challenger;
import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.entity.client.ModModelLayers;
import net.lyof.phantasm.entity.client.model.BehemothModel;
import net.lyof.phantasm.entity.client.model.CrystieModel;
import net.lyof.phantasm.entity.client.renderer.BehemothRenderer;
import net.lyof.phantasm.entity.client.renderer.ChallengeRuneBlockEntityRenderer;
import net.lyof.phantasm.entity.client.renderer.ChoralArrowRenderer;
import net.lyof.phantasm.entity.client.renderer.CrystieRenderer;
import net.lyof.phantasm.entity.custom.BehemothEntity;
import net.lyof.phantasm.particle.ModParticles;
import net.lyof.phantasm.particle.custom.ZzzParticle;
import net.lyof.phantasm.setup.ModPackets;
import net.lyof.phantasm.setup.ModRegistry;
import net.lyof.phantasm.sound.ModSounds;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

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
        BlockEntityRendererFactories.register(ModBlockEntities.CHALLENGE_RUNE, ChallengeRuneBlockEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ModParticles.ZZZ, ZzzParticle.Factory::new);

        registerPackets();
    }

    public static void registerPackets() {
        ClientPlayNetworking.registerGlobalReceiver(ModPackets.BEHEMOTH_WAKES_UP, (client, handler, buf, responseSender) -> {
            int selfId = buf.readInt();
            int targetId = buf.readInt();
            client.execute(() -> {
                Entity self = client.world.getEntityById(selfId);
                Entity target = client.world.getEntityById(targetId);
                if (self instanceof BehemothEntity behemoth)
                    behemoth.setTarget((LivingEntity) target);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.CHALLENGE_STARTS, (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            client.execute(() -> {
                if (client.world.getBlockEntity(pos) instanceof ChallengeRuneBlockEntity rune) {
                    rune.startChallenge();
                    rune.addChallenger(client.player);

                    client.worldRenderer.playSong(ModSounds.CHALLENGE, pos);

                    client.inGameHud.setTitle(Text.empty());
                    client.inGameHud.setSubtitle(Text.translatable("block.phantasm.challenge_rune.start")
                            .formatted(Formatting.LIGHT_PURPLE));
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.CHALLENGE_ENDS, (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            boolean success = buf.readBoolean();
            client.execute(() -> {
                if (client.world.getBlockEntity(pos) instanceof ChallengeRuneBlockEntity rune) {
                    if (((Challenger) client.player).getChallengeRune() == rune) {
                        client.inGameHud.setTitle(Text.empty());
                        client.inGameHud.setSubtitle(Text.translatable(success ?
                                        "block.phantasm.challenge_rune.success" :
                                        "block.phantasm.challenge_rune.fail")
                                .formatted(Formatting.LIGHT_PURPLE));
                    }

                    rune.stopChallenge(success);

                    client.worldRenderer.playSong(null, pos);
                }
            });
        });
    }
}
