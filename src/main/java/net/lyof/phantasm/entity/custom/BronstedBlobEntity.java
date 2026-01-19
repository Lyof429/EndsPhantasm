package net.lyof.phantasm.entity.custom;

import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class BronstedBlobEntity extends SlimeEntity {
    private static ParticleEffect particles = null;

    public BronstedBlobEntity(EntityType<? extends SlimeEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return true;
    }

    @Override
    protected ParticleEffect getParticles() {
        if (particles == null)
            particles = new BlockStateParticleEffect(ParticleTypes.BLOCK, ModBlocks.ACIDIC_MASS.getDefaultState());
        return particles;
    }

    @Override
    public boolean canSpawn(WorldView world) {
        return world.doesNotIntersectEntities(this) && !world.containsFluid(this.getBoundingBox());
    }

    @Override
    public void setSize(int size, boolean heal) {
        super.setSize(size, heal);
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(6 + size*size);
        if (heal) this.setHealth(this.getMaxHealth());
    }

    @Override
    public void tick() {
        super.tick();

        if (this.age % 20 == 0 && !this.isSmall() && this.isTouchingWaterOrRain()) {
            this.getWorld().addParticle(this.getParticles(), this.getParticleX(1), this.getY(), this.getParticleZ(1), 0.0, 0.0, 0.0);
            this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            this.remove(RemovalReason.KILLED);
        }
    }
}
