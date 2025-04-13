package net.lyof.phantasm.entity.custom;

import net.lyof.phantasm.block.custom.SubwooferBlock;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChoralArrowEntity extends ArrowEntity {
    public ChoralArrowEntity(EntityType<? extends ArrowEntity> type, World world) {
        super(type, world);
    }

    public int lifetime = 0;
    public boolean shotByCrossbow = false;

    public static ChoralArrowEntity create(World world, LivingEntity shooter) {
        ChoralArrowEntity arrow = create(world, shooter.getX(), shooter.getEyeY() - 0.10000000149011612D, shooter.getZ());
        arrow.setOwner(shooter);
        return arrow;
    }

    public static ChoralArrowEntity create(World world, double x, double y, double z) {
        ChoralArrowEntity arrow = new ChoralArrowEntity(ModEntities.CHORAL_ARROW, world);
        arrow.setPosition(x, y, z);
        return arrow;
    }

    @Override
    protected void onHit(LivingEntity target) {
        if (!this.shotByCrossbow) {
            super.onHit(target);
            target.addStatusEffect(new StatusEffectInstance(ModEffects.CHARM, 40, 0));
        }
    }

    @Override
    public ItemStack asItemStack() {
        return ModItems.CHORAL_ARROW.getDefaultStack();
    }

    @Override
    protected float getDragInWater() {
        return 0.2f;
    }

    @Override
    public void setVelocity(double x, double y, double z, float speed, float divergence) {
        super.setVelocity(x, y, z, speed * 0.75f, divergence * 4f);
    }

    @Override
    public boolean isCritical() {
        return false;
    }

    public static boolean isUsingCrossbow(LivingEntity shooter) {
        return shooter.getMainHandStack().getItem() instanceof CrossbowItem
                || (shooter.getOffHandStack().getItem() instanceof CrossbowItem && !(shooter.getMainHandStack().getItem() instanceof BowItem));
    }

    @Override
    public void tick() {
        if (this.lifetime == 0)
            this.shotByCrossbow = this.getOwner() != null && this.getOwner() instanceof LivingEntity living &&
                    isUsingCrossbow(living);
        if (this.shotByCrossbow && this.lifetime > 0)
            this.discard();

        if (this.shotByCrossbow && this.getOwner() != null && this.lifetime == 0) {
            World world = this.getWorld();
            Entity shooter = this.getOwner();

            if (this.distanceTo(shooter) > 4) return;

            Vec3d direction = this.getVelocity().normalize();
            Vec3d position = this.getPos();
            int range = ConfigEntries.subwooferRange * 2;

            List<UUID> affected = new ArrayList<>();

            if (!shooter.isSneaking()) {
                shooter.setVelocity(direction.multiply(-1.5).add(0, 0.1, 0));
                shooter.velocityModified = true;
                shooter.fallDistance = 0;
                affected.add(shooter.getUuid());
            }

            BlockPos pos;
            for (int i = 0; i < range; i++) {
                pos = new BlockPos((int) Math.round(position.x - 0.5), (int) Math.round(position.y - 0.5), (int) Math.round(position.z - 0.5));
                List<Entity> entities = world.getOtherEntities(shooter, new Box(pos).expand(1), SubwooferBlock::canPush);

                // TODO: packet mess
                world.addImportantParticle(ParticleTypes.SONIC_BOOM,
                        position.x, position.y, position.z,
                        0, 0, 0);
                world.playSound(null, pos, SoundEvents.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.PLAYERS, 0.2f, 1.5f);

                for (Entity e : entities) {
                    if (affected.contains(e.getUuid())) continue;

                    affected.add(e.getUuid());
                    e.damage(shooter.getDamageSources().arrow(this, shooter), 6);
                    e.setVelocity(direction.multiply(2.5).add(0, 0.4, 0));
                    e.velocityModified = true;
                }

                if (world.getBlockState(pos).isIn(BlockTags.OCCLUDES_VIBRATION_SIGNALS)) break;

                position = position.add(direction);
            }
        }

        if (!this.shotByCrossbow)
            super.tick();

        if (!this.shotByCrossbow && this.getWorld().isClient() && !this.inGround) {
            this.getWorld().addParticle(ParticleTypes.NOTE,
                    this.getParticleX(0.5D), this.getRandomBodyY(), this.getParticleZ(0.5D),
                    this.lifetime / 20f, 0, 0);
        }
        this.lifetime++;
    }
}
