package net.lyof.phantasm.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class BehemothEntity extends HostileEntity implements Monster {
    public boolean isAngry = false;

    public BehemothEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new WanderAroundGoal(this, 1));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 100)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 10);
    }

    @Override
    public int getXpToDrop() {
        return Monster.STRONG_MONSTER_XP;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.isIndirect()) return false;
        if (source.getAttacker() instanceof PlayerEntity)
            this.isAngry = true;
        return super.damage(source, amount);
    }
}
