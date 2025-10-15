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
    protected final Map<UUID, TickableSoundInstance> playing = new HashMap<>();

    public void add(UUID id, TickableSoundInstance soundInstance) {
        this.remove(id);
        this.playing.put(id, soundInstance);
    }

    public void remove(UUID id) {
        this.playing.remove(id);
    }

    public TickableSoundInstance get(UUID id) {
        return this.playing.get(id);
    }
}
