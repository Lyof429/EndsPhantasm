package net.lyof.phantasm.entity.custom;

import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.entity.goal.DiveBombGoal;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CrystieEntity extends AnimalEntity {
    public boolean isAngry = false;

    public CrystieEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 10, true);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new DiveBombGoal(this));

        this.goalSelector.add(1, new FlyGoal(this, 1));
        this.goalSelector.add(2, new TemptGoal(this, 1, Ingredient.fromTag(ModTags.Items.CRYSTAL_FLOWERS), false));
        this.goalSelector.add(3, new LookAroundGoal(this));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 12)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.1f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.CRYSTIE.create(world);
    }

    public void explode() {
        this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 2, World.ExplosionSourceType.MOB);
        this.kill();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getAttacker() instanceof PlayerEntity)
            this.isAngry = true;
        return super.damage(source, amount);
    }
}
