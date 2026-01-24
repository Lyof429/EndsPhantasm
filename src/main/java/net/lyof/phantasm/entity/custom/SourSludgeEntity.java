package net.lyof.phantasm.entity.custom;

import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.effect.ModEffects;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class SourSludgeEntity extends SlimeEntity {
    private static ParticleEffect particles = null;

    protected float bounceDistance;

    public SourSludgeEntity(EntityType<? extends SlimeEntity> entityType, World world) {
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
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(2+size);
        if (heal) this.setHealth(this.getMaxHealth());
    }

    @Override
    protected boolean canAttack() {
        return this.canMoveVoluntarily();
    }

    @Override
    protected void updateStretch() {
        this.targetStretch *= 0.95f;
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
    protected float modifyAppliedDamage(DamageSource source, float amount) {
        if (source.isOf(DamageTypes.DROWN)) amount *= 5;
        return super.modifyAppliedDamage(source, amount);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        boolean b = super.damage(source, amount);
        Random random = this.getRandom();
        if (b && source.isOf(DamageTypes.DROWN) && random.nextFloat() < 0.1) {
            AreaEffectCloudEntity cloud = EntityType.AREA_EFFECT_CLOUD.create(this.getWorld());
            cloud.addEffect(new StatusEffectInstance(ModEffects.CORROSION, random.nextBetween(100, 400), random.nextInt(3)));
            cloud.setOwner(this);
            cloud.setPosition(this.getPos());
            this.getWorld().spawnEntity(cloud);
        }
        return b;
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        return super.canHaveStatusEffect(effect) && effect.getEffectType() != ModEffects.CORROSION;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        this.bounceDistance = fallDistance;
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.bounceDistance > 3) {
            Vec3d v = this.getVelocity();
            this.setVelocity(v.x, Math.log(this.bounceDistance - 2) * 0.5, v.z);
            this.velocityDirty = true;
            this.bounceDistance = 0;
        }
    }
}
