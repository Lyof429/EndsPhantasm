package net.lyof.phantasm.mixin;

import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DragonFireballEntity.class)
public abstract class DragonFireballEntityMixin extends ExplosiveProjectileEntity {
    protected DragonFireballEntityMixin(EntityType<? extends ExplosiveProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "onCollision", at = @At("TAIL"))
    public void explode(HitResult hitResult, CallbackInfo ci) {
        if (!(hitResult instanceof EntityHitResult entityHitResult) || !this.isOwner(entityHitResult.getEntity()) && ConfigEntries.explosiveDragonFireballs)
            this.getWorld().createExplosion((DragonFireballEntity) (Object) this, this.getX(), this.getY() + 0.5, this.getZ(),
                    3, true, World.ExplosionSourceType.MOB);
    }
}
