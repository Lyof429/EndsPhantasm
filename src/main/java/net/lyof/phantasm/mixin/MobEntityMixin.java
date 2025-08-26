package net.lyof.phantasm.mixin;

import net.lyof.phantasm.effect.ModEffects;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tickNewAi", at = @At("HEAD"), cancellable = true)
    private void charmAi(CallbackInfo ci) {
        if (this.hasStatusEffect(ModEffects.CHARM)) ci.cancel();
    }

    @Inject(method = "initialize", at = @At("HEAD"))
    private void initializeBounds(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
                                  EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {

        if ((MobEntity) (Object) this instanceof VexEntity vex && spawnReason == SpawnReason.SPAWNER) {
            vex.setBounds(vex.getBlockPos());
        }
    }
}
