package net.lyof.phantasm.entity.custom;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.entity.goal.AvoidGroundGoal;
import net.lyof.phantasm.entity.goal.DiveBombGoal;
import net.lyof.phantasm.entity.goal.FlyAroundGoal;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.EnumSet;

public class CrystieEntity extends AnimalEntity {
    public boolean isAngry = false;

    public CrystieEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 100, true);
        this.setNoGravity(true);
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new Goal() {
            {
                this.setControls(EnumSet.of(Goal.Control.MOVE));
            }

            @Override
            public boolean shouldContinue() {
                return CrystieEntity.this.getMoveControl().isMoving() && CrystieEntity.this.getTarget() != null && CrystieEntity.this.getTarget().isAlive();
            }

            @Override
            public boolean canStart() {
                return CrystieEntity.this.getTarget() != null && !CrystieEntity.this.getMoveControl().isMoving();
            }

            @Override
            public void start() {
                LivingEntity livingentity = CrystieEntity.this.getTarget();
                Vec3d vec3d = livingentity.getEyePos();
                CrystieEntity.this.moveControl.moveTo(vec3d.x, vec3d.y, vec3d.z, 3);
            }
            @Override
            public void tick() {
                LivingEntity livingentity = CrystieEntity.this.getTarget();
                if (CrystieEntity.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
                    CrystieEntity.this.tryAttack(livingentity);
                } else {
                    double d0 = CrystieEntity.this.distanceTo(livingentity);
                    if (d0 < 32) {
                        Vec3d vec3d = livingentity.getEyePos();
                        CrystieEntity.this.moveControl.moveTo(vec3d.x, vec3d.y, vec3d.z, 3);
                    }
                }
            }
        });
        this.goalSelector.add(1, new FlyGoal(this, 20));
        this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, false, true){
            @Override
            public boolean canStart() {
                return super.canStart() && CrystieEntity.this.isAngry;
            }
        });
        this.goalSelector.add(3, new TemptGoal(this, 1, Ingredient.fromTag(ModTags.Items.CRYSTAL_FLOWERS), false));
        //this.goalSelector.add(4, new AvoidGroundGoal(this));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 12)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3);
    }

    @Override
    public AnimalEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.CRYSTIE.create(world);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new BirdNavigation(this,world);
    }

    @Override
    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        if (pos == this.getBlockPos()) return -10;
        if (world.getBlockState(pos).isAir())
            return 10;
        return 0;
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

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isIn(ModTags.Items.CRYSTAL_FLOWERS);
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean result = super.tryAttack(target);
        if (result) this.explode();
        return result;
    }

    @Override
    public void tick() {
        if (this.age % 40 == 0 && !this.getWorld().isClient())
            Phantasm.log(this.getNavigation().getTargetPos());
        super.tick();
    }
}
