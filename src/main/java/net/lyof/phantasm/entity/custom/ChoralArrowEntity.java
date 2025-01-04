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

public class ChoralArrowEntity extends ArrowEntity {
    public static ChoralArrowEntity create(EntityType<? extends ArrowEntity> type, World world) {
        return new ChoralArrowEntity(type, world);
    }

    private ChoralArrowEntity(EntityType<? extends ArrowEntity> type, World world) {
        super(type, world);
    }

    public int lifetime = 0;

    public ChoralArrowEntity(World world, LivingEntity shooter) {
        this(world, shooter.getX(), shooter.getEyeY() - 0.10000000149011612D, shooter.getZ());
        this.setOwner(shooter);
    }

    public ChoralArrowEntity(World world, double x, double y, double z) {
        super(ModEntities.CHORAL_ARROW, world);
        this.setPosition(x, y, z);
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);
        target.addStatusEffect(new StatusEffectInstance(ModEffects.CHARM, 40, 0));
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

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient() && !this.inGround) {
            this.getWorld().addParticle(ParticleTypes.NOTE,
                    this.getParticleX(0.5D), this.getRandomBodyY(), this.getParticleZ(0.5D),
                    this.lifetime / 20f, 0, 0);
        }
        this.lifetime++;
    }
}
