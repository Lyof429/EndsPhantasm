package net.lyof.phantasm.setup;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.lyof.phantasm.entity.access.Challenger;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.entity.custom.BehemothEntity;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.lyof.phantasm.setup.compat.VinURLCompat;
import net.lyof.phantasm.sound.ModSounds;
import net.lyof.phantasm.sound.SongHandler;
import net.lyof.phantasm.sound.custom.PolyppieDiscSoundInstance;
import net.lyof.phantasm.sound.custom.PolyppieSoundInstance;
import net.lyof.phantasm.util.MixinAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ModPackets {
    public static final Identifier INITIALIZE = Phantasm.makeID("initialize");

    public static final Identifier BEHEMOTH_WAKES_UP = Phantasm.makeID("behemoth_wakes_up");

    public static final Identifier CHALLENGE_STARTS = Phantasm.makeID("challenge_starts");
    public static final Identifier CHALLENGE_ENDS = Phantasm.makeID("challenge_ends");

    public static final Identifier BEGIN_CUTSCENE_STARTS = Phantasm.makeID("begin_cutscene_starts");
    public static final Identifier BEGIN_CUTSCENE_ENDS = Phantasm.makeID("begin_cutscene_ends");

    public static final Identifier POLYPPIE_UPDATES = Phantasm.makeID("polyppie_updates");
    public static final Identifier POLYPPIE_STARTS_BEING_CARRIED = Phantasm.makeID("polyppie_starts_being_carried");
    public static final Identifier POLYPPIE_STOPS_BEING_CARRIED = Phantasm.makeID("polyppie_stops_being_carried");
    public static final Identifier POLYPPIE_SETS_VARIANT = Phantasm.makeID("polyppie_sets_variant");


    public static class Server {
        public static void beginCutsceneEnds(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                                             PacketByteBuf buf, PacketSender responseSender) {
            ((MixinAccess<Boolean>) player).setMixinValue(true);
            player.moveToWorld(server.getWorld(World.END));
        }
    }


    public static class Client {
        public static void initialize(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            int eventType = buf.readInt();

            if (eventType == 0)
                ReloadListener.INSTANCE.reloadClient();
            else if (eventType == 1)
                PolyppieEntity.Variant.read(buf);
        }

        public static void behemothWakesUp(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            int selfId = buf.readInt();
            int targetId = buf.readInt();
            client.execute(() -> {
                Entity self = client.world.getEntityById(selfId);
                Entity target = client.world.getEntityById(targetId);
                if (self instanceof BehemothEntity behemoth)
                    behemoth.setTarget((LivingEntity) target);
            });
        }

        public static void challengeStarts(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
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
        }

        public static void challengeEnds(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
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
        }

        public static void beginCutsceneStarts(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            client.execute(() -> {
                CreditsScreen creditsScreen = new CreditsScreen(true, () -> {
                    ClientPlayNetworking.send(ModPackets.BEGIN_CUTSCENE_ENDS, PacketByteBufs.empty());
                    client.setScreen(null);
                });
                ((MixinAccess<Boolean>) creditsScreen).setMixinValue(true);

                client.setScreen(creditsScreen);
            });
        }

        public static void polyppieUpdates(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
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
        }

        public static void polyppieStartsBeingCarried(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            int polyppieid = buf.readInt();
            int playerid = buf.readInt();

            client.execute(() -> {
                if (Phantasm.log(client.world.getEntityById(polyppieid)) instanceof PolyppieEntity polyppie
                        && Phantasm.log(client.world.getEntityById(playerid)) instanceof PlayerEntity player) {
                    polyppie.setCarriedBy(player, null);
                }
            });
        }

        public static void polyppieStopsBeingCarried(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
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
        }

        public static void polyppieSetsVariant(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            int id = buf.readInt();
            Identifier variant = buf.readIdentifier();

            client.execute(() -> {
                if (client.world.getEntityById(id) instanceof PolyppieEntity polyppie)
                    polyppie.setVariant(PolyppieEntity.Variant.get(variant));
            });
        }
    }
}
