package net.lyof.phantasm.entity.custom;

import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
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
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue((3+size)*size);
        if (heal) this.setHealth(this.getMaxHealth());
    }

    @Override
    protected void updateStretch() {
        this.targetStretch *= 0.8f;
    }

    @Override
    protected float getJumpVelocity() {
        return super.getJumpVelocity() + 0.07f*this.getSize();
    }

    @Override
    public boolean hurtByWater() {
        return !this.isSmall();
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        Vec3d v = this.getVelocity();
        this.setVelocity(v.x, fallDistance*this.getSize(), v.z);
        this.velocityDirty = true;
        return false;
    }
}
