package net.lyof.phantasm;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.lyof.phantasm.block.ModBlockEntities;
import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.entity.access.Challenger;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.entity.client.ModModelLayers;
import net.lyof.phantasm.entity.client.model.BehemothModel;
import net.lyof.phantasm.entity.client.model.CrystieModel;
import net.lyof.phantasm.entity.client.renderer.*;
import net.lyof.phantasm.entity.custom.BehemothEntity;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.lyof.phantasm.particle.ModParticles;
import net.lyof.phantasm.particle.custom.ZzzParticle;
import net.lyof.phantasm.setup.ModPackets;
import net.lyof.phantasm.setup.ModRegistry;
import net.lyof.phantasm.setup.ReloadListener;
import net.lyof.phantasm.setup.compat.VinURLCompat;
import net.lyof.phantasm.sound.ModSounds;
import net.lyof.phantasm.sound.SongHandler;
import net.lyof.phantasm.sound.custom.PolyppieDiscSoundInstance;
import net.lyof.phantasm.sound.custom.PolyppieSoundInstance;
import net.lyof.phantasm.util.MixinAccess;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

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
        ClientPlayNetworking.registerGlobalReceiver(ModPackets.INITIALIZE, (client, handler, buf, responseSender) -> {
            int eventType = buf.readInt();

            if (eventType == 0)
                ReloadListener.INSTANCE.reloadClient();
            else if (eventType == 1)
                PolyppieEntity.Variant.read(buf);
        });

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

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.BEGIN_CUTSCENE_STARTS, (client, handler, buf, responseSender) -> {
            client.execute(() -> {
                CreditsScreen creditsScreen = new CreditsScreen(true, () -> {
                    ClientPlayNetworking.send(ModPackets.BEGIN_CUTSCENE_ENDS, PacketByteBufs.empty());
                    client.setScreen(null);
                });
                ((MixinAccess<Boolean>) creditsScreen).setMixinValue(true);

                client.setScreen(creditsScreen);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.POLYPPIE_UPDATES, (client, handler, buf, responseSender) -> {
            NbtCompound nbt = buf.readNbt();
            int id = buf.readInt();
            int soundKey = buf.readInt();

            client.execute(() -> {
                Entity self = client.world.getEntityById(id);
                ItemStack item = ItemStack.EMPTY;
                if (!nbt.isEmpty()) item = ItemStack.fromNbt(nbt);

                PolyppieEntity polyppie = null;
                if (self instanceof PolyppieCarrier carrier && carrier.getCarriedPolyppie() != null)
                    polyppie = carrier.getCarriedPolyppie();
                else if (self instanceof PolyppieEntity)
                    polyppie = (PolyppieEntity) self;

                if (polyppie != null) {
                    polyppie.setSoundKey(soundKey);
                    PolyppieSoundInstance soundInstance = SongHandler.instance.get(soundKey);

                    if (soundInstance != null) {
                        SongHandler.instance.remove(soundKey);
                        client.getSoundManager().stop(soundInstance);
                    }

                    if (polyppie.isDead()) return;

                    if (Phantasm.isVinURLLoaded() && item.getTranslationKey().equals("item.vinurl.custom_record")) {
                        VinURLCompat.playSound(polyppie, soundKey, item, client);
                    } else if (item.getItem() instanceof MusicDiscItem musicDisc) {
                        client.inGameHud.setRecordPlayingOverlay(musicDisc.getDescription());

                        soundInstance = new PolyppieDiscSoundInstance(musicDisc.getSound(), 1, polyppie, 0);
                        SongHandler.instance.add(soundKey, soundInstance);
                        client.getSoundManager().play(soundInstance);
                    }
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.POLYPPIE_STARTS_BEING_CARRIED, (client, handler, buf, responseSender) -> {
            int polyppieid = buf.readInt();
            int playerid = buf.readInt();

            client.execute(() -> {
                if (client.world.getEntityById(polyppieid) instanceof PolyppieEntity polyppie
                        && client.world.getEntityById(playerid) instanceof PlayerEntity player) {
                    polyppie.setCarriedBy(player, null);
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.POLYPPIE_STOPS_BEING_CARRIED, (client, handler, buf, responseSender) -> {
            int polyppieid = buf.readInt();
            int playerid = buf.readInt();
            double x = buf.readDouble(), y = buf.readDouble(), z = buf.readDouble();

            client.execute(() -> {
                 if (client.world.getEntityById(polyppieid) instanceof PolyppieEntity polyppie
                         && client.world.getEntityById(playerid) instanceof PlayerEntity player) {
                     PolyppieSoundInstance soundInstance = SongHandler.instance.get(polyppie.getSoundKey());
                     if (soundInstance != null) soundInstance.update(polyppie);

                     polyppie.setCarriedBy(player, new Vec3d(x, y, z));
                 }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.POLYPPIE_SETS_VARIANT, (client, handler, buf, responseSender) -> {
            int id = buf.readInt();
            int variant = buf.readInt();

            client.execute(() -> {
                if (client.world.getEntityById(id) instanceof PolyppieEntity polyppie) {
                    Phantasm.log(client.player.getEntityName() + " set variant " + variant);
                    polyppie.setVariant(PolyppieEntity.Variant.get(variant));
                }
            });
        });
    }
}
