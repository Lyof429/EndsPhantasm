package net.lyof.phantasm.entity.client;

import net.lyof.phantasm.sound.custom.PolyppieSoundInstance;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SongHandler {
    public static final SongHandler instance = new SongHandler();

    protected final Map<Integer, PolyppieSoundInstance> playing = new HashMap<>();
    protected int offset = 0;

    public void add(int id, PolyppieSoundInstance soundInstance) {
        this.remove(id);
        this.playing.put(id, soundInstance);
    }

    public void remove(int id) {
        if (this.playing.containsKey(id)) this.get(id).setFinished();
        this.playing.remove(id);
    }

    @Nullable
    public PolyppieSoundInstance get(int id) {
        return this.playing.get(id);
    }
}
