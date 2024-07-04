package net.lyof.phantasm.entity.custom;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.animation.BehemothAnimation;
import net.lyof.phantasm.entity.goal.SleepGoal;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
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
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BehemothEntity extends HostileEntity implements Monster {
    public int angryTicks = 0;
    public int animTime = 0;
    public BehemothAnimation animation = BehemothAnimation.SLEEPING;
    public static int ANGRY_TIME = 600;

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
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 10);
    }

    @Override
    public float getStepHeight() {
        return 1f;
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return this.animation == BehemothAnimation.SLEEPING ?
                EntityDimensions.changing(1.5f, 1) : super.getDimensions(pose);
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
        else if (target != null && !this.isAngry()) this.setAnimation(BehemothAnimation.WAKING_UP);

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
            PlayerEntity player = this.getWorld().getClosestPlayer(this, 4);
            if (player != null && !player.isCreative() && !player.isSpectator())
                this.setTarget(player);
        }
        if (this.getTarget() != null && (this.getTarget().distanceTo(this) > 16 || !this.getTarget().isAlive()))
            this.setTarget(null);
    }
}
