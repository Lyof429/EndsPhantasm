package net.lyof.phantasm.entity.client;

import net.lyof.phantasm.Phantasm;
import net.minecraft.client.sound.EntityTrackingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

import java.util.HashMap;
import java.util.Map;

public class SongHandler {
    protected final Map<Integer, TickableSoundInstance> playing = new HashMap<>();

    public void tick(int id) {
        TickableSoundInstance sound = this.playing.get(id);
        if (sound == null) return;

        sound.tick();
    }

    public void add(int id, TickableSoundInstance soundInstance) {
        this.remove(id);
        this.playing.put(id, soundInstance);
    }

    public void remove(int id) {
        this.playing.remove(id);
    }

    public TickableSoundInstance get(int id) {
        return this.playing.get(id);
    }
}
