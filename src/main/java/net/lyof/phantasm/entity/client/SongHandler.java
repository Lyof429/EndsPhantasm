package net.lyof.phantasm.entity.client;

import net.lyof.phantasm.Phantasm;
import net.minecraft.client.sound.EntityTrackingSoundInstance;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SongHandler {
    protected final Map<Integer, TickableSoundInstance> playing = new HashMap<>();
    protected int offset = 0;

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
