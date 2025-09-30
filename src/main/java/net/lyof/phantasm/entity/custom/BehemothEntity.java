package net.lyof.phantasm.entity.custom;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.lyof.phantasm.entity.client.animation.BehemothAnimation;
import net.lyof.phantasm.entity.goal.BehemothAttackGoal;
import net.lyof.phantasm.entity.goal.SleepGoal;
import net.lyof.phantasm.entity.listener.BehemothEventListener;
import net.lyof.phantasm.particle.ModParticles;
import net.lyof.phantasm.setup.ModPackets;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.EntityGameEventHandler;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class BehemothEntity extends HostileEntity implements Monster {
    public static final EntityDimensions STANDARD_DIMENSIONS = EntityDimensions.changing(0.95f, 1.95f);
    public static final EntityDimensions SLEEPING_DIMENSIONS = EntityDimensions.changing(1.5f, 1f);
    public int angryTicks = 0;
    public int animTicks = 0;
    public BehemothAnimation animation = BehemothAnimation.SLEEPING;
    public static int MAX_ANGRY_TICKS = 600;

    public final EntityGameEventHandler<BehemothEventListener> listener = new EntityGameEventHandler<>(new BehemothEventListener(this));

    public BehemothEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void updateEventHandler(BiConsumer<EntityGameEventHandler<?>, ServerWorld> callback) {
        if (this.getWorld() instanceof ServerWorld serverWorld)
            callback.accept(this.listener, serverWorld);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SleepGoal(this));
        this.goalSelector.add(1, new BehemothAttackGoal(this, 1, true));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 60)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 10);
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return this.animation == BehemothAnimation.SLEEPING ? SLEEPING_DIMENSIONS : STANDARD_DIMENSIONS;
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
        if (source.getAttacker() instanceof PlayerEntity player && !player.isCreative() && !player.isSpectator()) {
            this.setTarget(player);
            this.angryTicks = MAX_ANGRY_TICKS;
        }
        return super.damage(source, amount);
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        // Client syncing
        if (!this.getWorld().isClient()) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(this.getId());
            buf.writeInt(target == null ? 0 : target.getId());
            for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) this.getWorld(), this.getBlockPos())) {
                ServerPlayNetworking.send(player, ModPackets.BEHEMOTH_WAKES_UP, buf);
            }
        }

        if (target == null && this.isAngry()) this.setAnimation(BehemothAnimation.WAKING_DOWN);
        else if (target != null && !this.isAngry()) {
            this.setAnimation(BehemothAnimation.WAKING_UP);
            this.playSound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
        }

        super.setTarget(target);

        if (target == null) this.angryTicks = 0;
        else this.angryTicks = MAX_ANGRY_TICKS;
    }

    public void setAnimation(BehemothAnimation anim) {
        if (anim != this.animation) this.animTicks = 0;
        this.animation = anim;
        this.calculateDimensions();
    }

    @Override
    public void tick() {
        if (this.firstUpdate) this.calculateDimensions();
        super.tick();

        this.animTicks++;
        if (this.animation.maxTime > 0 && this.animTicks > this.animation.maxTime) {
            if (this.isAngry())
                this.setAnimation(BehemothAnimation.WALKING);
            else
                this.setAnimation(BehemothAnimation.SLEEPING);
            //else this.setAnimation(BehemothAnimation.IDLE);
        }

        if (this.angryTicks > 0) this.angryTicks--;
        else if (this.isAngry()) this.setTarget(null);
        if (this.getTarget() != null && (this.getTarget().distanceTo(this) > 16 || !this.getTarget().isAlive()))
            this.setTarget(null);

        if (!this.isAngry() && this.age % 20 == 0) {
            this.playSound(SoundEvents.ENTITY_SNIFFER_SNIFFING, 2, 1);
            if (this.getWorld().isClient() && this.getRandom().nextInt(2) == 0)
                this.getWorld().addParticle(ModParticles.ZZZ,
                        this.getX() - Math.sin(-this.getYaw() * Math.PI / 180), this.getY() + 0.3,
                        this.getZ() - Math.cos(this.getYaw() * Math.PI / 180), 0, 0.05, 0);
        }
    }
}
