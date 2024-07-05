package net.lyof.phantasm.particle.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.Nullable;

public class ZzzParticle extends SpriteBillboardParticle {
    public ZzzParticle(ClientWorld clientWorld, double x, double y, double z) {
        super(clientWorld, x, y, z);
    }

    public ZzzParticle(ClientWorld clientWorld, double x, double y, double z, SpriteProvider sprite, float vx, float vy, float vz) {
        super(clientWorld, x, y, z);

        this.setSprite(sprite);

        this.setPos(x, y, z);
        this.setVelocity(vx, vy, vz);

        this.gravityStrength = 0;
        this.maxAge = 100;
    }

    @Override
    public void tick() {
        float ratio = (float) (this.maxAge - this.age) / this.maxAge;

        //this.scale = ;
        super.tick();
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteProvider) {
            this.sprites = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ZzzParticle(world, x, y, z, this.sprites, (float) velocityX, (float) velocityY, (float) velocityZ);
        }
    }
}
