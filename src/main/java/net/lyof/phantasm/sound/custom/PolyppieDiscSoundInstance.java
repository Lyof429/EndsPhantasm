package net.lyof.phantasm.sound.custom;

import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;

public class PolyppieDiscSoundInstance extends AbstractSoundInstance implements PolyppieSoundInstance {
    protected Entity entity;
    protected boolean done;

    public PolyppieDiscSoundInstance(SoundEvent sound, float pitch, Entity entity, long seed) {
        super(sound, SoundCategory.RECORDS, Random.create(seed));
        this.volume = 4;
        this.pitch = pitch;
        this.entity = entity;
        this.x = this.entity.getX();
        this.y = this.entity.getY();
        this.z = this.entity.getZ();
    }

    @Override
    public boolean canPlay() {
        return !this.entity.isSilent();
    }

    @Override
    public boolean isDone() {
        return this.done;
    }

    @Override
    public void tick() {
        if (!this.entity.isRemoved()) {
            this.x = this.entity.getX();
            this.y = this.entity.getY();
            this.z = this.entity.getZ();
        }
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
