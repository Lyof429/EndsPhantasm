package net.lyof.phantasm.entity.custom;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.animation.BehemothAnimation;
import net.lyof.phantasm.entity.goal.SleepGoal;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BehemothEntity extends HostileEntity implements Monster {
    public int angryTicks = 0;
    public int animTime = 0;
    public BehemothAnimation animation = BehemothAnimation.SLEEPING;
    public static int ANGRY_TIME = 600;

    public AnimationState sleepingAnimationState = new AnimationState();

    public BehemothEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SleepGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1, true));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 60)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 10);
    }

    @Override
    public float getStepHeight() {
        return 1f;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return super.getHurtSound(source);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return super.getDeathSound();
    }

    @Override
    public int getXpToDrop() {
        return Monster.STRONG_MONSTER_XP;
    }

    public boolean isAngry() {
        return this.getTarget() != null;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.isIndirect()) return false;
        if (source.getAttacker() instanceof PlayerEntity player) {
            this.setTarget(player);
            this.angryTicks = ANGRY_TIME;
        }
        return super.damage(source, amount);
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (target == null && this.isAngry()) this.setAnimation(BehemothAnimation.WAKING_DOWN);
        else if (target != null && !this.isAngry()) {
            this.setAnimation(BehemothAnimation.WAKING_UP);
            this.playSound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
        }

        super.setTarget(target);

        if (target == null) this.angryTicks = 0;
        else this.angryTicks = ANGRY_TIME;
    }

    public void setAnimation(BehemothAnimation anim) {
        if (anim != this.animation) this.animTime = 0;
        this.animation = anim;
    }

    @Override
    public void tick() {
        super.tick();

        this.animTime++;
        if (this.animation.maxTime > 0 && this.animTime > this.animation.maxTime) {
            if (this.animation == BehemothAnimation.WAKING_UP)
                this.setAnimation(BehemothAnimation.WALKING);
            else if (this.animation == BehemothAnimation.WAKING_DOWN)
                this.setAnimation(BehemothAnimation.SLEEPING);
            else this.setAnimation(BehemothAnimation.IDLE);
        }

        if (this.angryTicks > 0) this.angryTicks--;
        else if (this.isAngry()) this.setTarget(null);
        else if (this.age % 20 == 0) {
            PlayerEntity player = this.getWorld().getClosestPlayer(this, 8);
            if (player != null && !player.isCreative() && !player.isSpectator())
                this.setTarget(player);
        }
        if (this.getTarget() != null && (this.getTarget().distanceTo(this) > 16 || !this.getTarget().isAlive()))
            this.setTarget(null);

        if (this.getWorld().isClient() && this.animation == BehemothAnimation.SLEEPING && this.age % 20 == 0) {
            this.getWorld().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    this.getX() - Math.sin(-this.getYaw() * Math.PI / 180), this.getY() + 0.1,
                    this.getZ() - Math.cos(this.getYaw() * Math.PI / 180), 0, 0.05, 0);
            this.playSound(SoundEvents.ENTITY_SNIFFER_SNIFFING, 10, 1);
        }
    }
}
