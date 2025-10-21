package net.lyof.phantasm.setup.compat;

import com.vinurl.client.AudioHandler;
import com.vinurl.client.FileSound;
import com.vinurl.client.KeyListener;
import com.vinurl.client.VinURLClient;
import com.vinurl.exe.Executable;
import com.vinurl.util.Constants;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.lyof.phantasm.sound.SongHandler;
import net.lyof.phantasm.sound.custom.PolyppieSoundInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class VinURLCompat {
    public static void playSound(int id) {
        TickableSoundInstance soundInstance = SongHandler.instance.get(id);
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
        String url = item.getOrCreateNbt().getString(Constants.URL_KEY);
        String fileName = AudioHandler.hashURL(url);

        if (client.player != null && url != null && !url.isEmpty()) {
            SongHandler.instance.add(soundKey, new PolyppieUrlSoundInstance(fileName, polyppie));

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
    public static class PolyppieUrlSoundInstance extends FileSound implements PolyppieSoundInstance {
        protected Entity entity;
        protected boolean done;

        public PolyppieUrlSoundInstance(String fileName, Entity entity) {
            super(fileName, entity.getPos(), false);
            this.entity = entity;
            this.done = false;
        }

        @Override
        public Entity getEntity() {
            return this.entity;
        }

        @Override
        public void setPos(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean isDone() {
            return this.done;
        }

        @Override
        public void update(Entity entity) {
            this.entity = entity;
        }

        @Override
        public void setFinished() {
            this.done = true;
            this.repeat = false;
        }
    }
}
