package net.lyof.phantasm.entity.client;

import net.lyof.phantasm.Phantasm;
import net.minecraft.client.sound.EntityTrackingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

import java.util.HashMap;
import java.util.Map;

public class SongHandler {
    protected final Map<Integer, EntityTrackingSoundInstance> playing = new HashMap<>();

    public void tick(int id) {
        EntityTrackingSoundInstance sound = this.playing.get(id);
        if (sound == null) return;

        sound.tick();
    }

    public void add(int id, EntityTrackingSoundInstance soundInstance) {
        this.remove(id);
        this.playing.put(id, soundInstance);
    }

    public void remove(int id) {
        this.playing.remove(id);
    }

    public EntityTrackingSoundInstance get(int id) {
        return this.playing.get(id);
    }
}
