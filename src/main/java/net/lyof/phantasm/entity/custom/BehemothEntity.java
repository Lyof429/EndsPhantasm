package net.lyof.phantasm.entity.custom;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.goal.SleepGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class BehemothEntity extends HostileEntity implements Monster {
    public int angryTicks = 0;

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
            this.angryTicks = 600;
        }
        return super.damage(source, amount);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.angryTicks > 0) this.angryTicks--;
        else if (this.isAngry()) this.setTarget(null);
        else if (this.age % 20 == 0) {
            PlayerEntity player = this.getWorld().getClosestPlayer(this, 4);
            if (player != null && !player.isCreative() && !player.isSpectator()) {
                this.setTarget(player);
                this.angryTicks = 600;
            }
        }
        if (this.getTarget() != null && (this.getTarget().distanceTo(this) > 16 || !this.getTarget().isAlive())) {
            this.setTarget(null);
            this.angryTicks = 0;
        }
    }
}
