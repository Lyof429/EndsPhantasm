package net.lyof.phantasm.entity.custom;

import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public class HarmonicArrowEntity extends ArrowEntity {
    public static HarmonicArrowEntity create(EntityType<? extends ArrowEntity> type, World world) {
        return new HarmonicArrowEntity(type, world);
    }

    private HarmonicArrowEntity(EntityType<? extends ArrowEntity> type, World world) {
        super(type, world);
    }

    public HarmonicArrowEntity(World world, LivingEntity shooter) {
        this(ModEntities.HARMONIC_ARROW, world);
        this.setOwner(shooter);
        this.setPosition(shooter.getX(), shooter.getEyeY() - 0.10000000149011612D, shooter.getZ());
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);
        target.addStatusEffect(new StatusEffectInstance(ModEffects.CHARM, 40, 0));
    }

    @Override
    public ItemStack asItemStack() {
        return ModItems.HARMONIC_ARROW.getDefaultStack();
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
    public void tick() {
        super.tick();
        if (this.getWorld().isClient() && !this.inGround) {
            this.getWorld().addParticle(ParticleTypes.NOTE,
                    this.getParticleX(0.5D), this.getRandomBodyY(), this.getParticleZ(0.5D),
                    0, 0.1, 0);
        }
    }
}
