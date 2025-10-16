package net.lyof.phantasm.sound.custom;

import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.entity.Entity;

public interface PolyppieSoundInstance extends TickableSoundInstance {
    void update(Entity entity);
    void setFinished();
}
