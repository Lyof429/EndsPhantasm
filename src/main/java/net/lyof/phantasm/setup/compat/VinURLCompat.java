package net.lyof.phantasm.setup.compat;

import com.vinurl.client.AudioHandler;
import com.vinurl.client.FileSound;
import com.vinurl.client.KeyListener;
import com.vinurl.client.VinURLClient;
import com.vinurl.exe.Executable;
import com.vinurl.util.Constants;
import net.lyof.phantasm.PhantasmClient;
import net.lyof.phantasm.entity.client.SongHandler;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class VinURLCompat {
    public static void playSound(int id) {
        TickableSoundInstance soundInstance = PhantasmClient.SONG_HANDLER.get(id);
        if (soundInstance instanceof FileSound fileSound) {
            VinURLClient.CLIENT.getSoundManager().play(soundInstance);
            VinURLClient.CLIENT.inGameHud.setRecordPlayingOverlay(Text.literal(AudioHandler.getDescription(fileSound.fileName)));
        }
    }

    public static void queueSound(String fileName, int id) {
        Executable.YT_DLP.getProcessStream(fileName + "/download").subscribe(String.valueOf(id)).onComplete(() -> {
            playSound(id);
        }).start();
    }


    public static void playSound(PolyppieEntity polyppie, int soundKey, ItemStack item, MinecraftClient client) {
        SongHandler songHandler = PhantasmClient.SONG_HANDLER;

        String url = item.getOrCreateNbt().getString(Constants.URL_KEY);
        String fileName = AudioHandler.hashURL(url);

        if (client.player != null && url != null && !url.isEmpty()) {
            songHandler.add(soundKey, new EntityTrackingFileSound(fileName, polyppie));

            if (Executable.YT_DLP.isProcessRunning(fileName + "/download"))
                queueSound(fileName, soundKey);
            else if (AudioHandler.getAudioFile(fileName).exists())
                playSound(soundKey);
            else {
                if (VinURLClient.CONFIG.downloadEnabled()) {
                    List<String> whitelist = VinURLClient.CONFIG.urlWhitelist();
                    String baseURL = AudioHandler.getBaseURL(url);
                    if (whitelist.stream().anyMatch(url::startsWith)) {
                        AudioHandler.downloadSound(url, fileName);
                        queueSound(fileName, soundKey);
                        return;
                    }

                    client.player.sendMessage(Text.literal("Press ")
                            .append(Text.literal(KeyListener.getHotKey()).formatted(Formatting.YELLOW))
                            .append(" to whitelist ").append(Text.literal(baseURL).formatted(Formatting.YELLOW)), true);
                    KeyListener.waitForKeyPress().thenAccept((confirmed) -> {
                        if (confirmed) {
                            whitelist.add(baseURL);
                            VinURLClient.CONFIG.save();
                            AudioHandler.downloadSound(url, fileName);
                            queueSound(fileName, soundKey);
                        }
                    });
                }
            }
        }
    }


    // Reimplementation of EntityTrackingSoundInstance using a VinURL FileSound
    public static class EntityTrackingFileSound extends FileSound implements TickableSoundInstance {
        protected final Entity entity;
        protected boolean done;

        public EntityTrackingFileSound(String fileName, Entity entity) {
            super(fileName, entity.getPos(), false);
            this.entity = entity;
            this.done = false;
        }

        @Override
        public boolean canPlay() {
            return !this.entity.isSilent();
        }

        @Override
        public boolean isDone() {
            return this.done;
        }

        public void setDone() {
            this.done = true;
            this.repeat = false;
        }

        @Override
        public void tick() {
            if (this.entity.isRemoved())
                this.setDone();
            else {
                this.x = this.entity.getX();
                this.y = this.entity.getY();
                this.z = this.entity.getZ();
            }
        }
    }
}
