package net.lyof.phantasm.mixin;

import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.mixin.access.EndGatewayBlockEntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.EndConfiguredFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract boolean canMoveVoluntarily();

    @Shadow public abstract World getWorld();

    @Inject(method = "getTeleportTarget", at = @At("RETURN"), cancellable = true)
    public void spawnInOuterEnd(ServerWorld destination, CallbackInfoReturnable<TeleportTarget> cir) {
        if (destination.getRegistryKey() == World.END && ConfigEntries.outerEndIntegration) {
            TeleportTarget result = cir.getReturnValue();
            BlockPos p = new BlockPos(1280, 60, 0);

            BlockPos pos = EndGatewayBlockEntityAccessor.getExitPos(destination, p).up(2);
            if (destination.getBlockState(pos.down()).isAir()) {
                destination.getRegistryManager().getOptional(RegistryKeys.CONFIGURED_FEATURE).flatMap(registry ->
                        registry.getEntry(EndConfiguredFeatures.END_ISLAND)).ifPresent(reference ->
                    reference.value().generate(destination, destination.getChunkManager().getChunkGenerator(),
                            Random.create(pos.asLong()), pos.down(2)));
            }

            result = new TeleportTarget(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), result.velocity, result.yaw, result.pitch);

            cir.setReturnValue(result);
        }
    }

    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    public void charmCursor(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        if (((Entity) (Object) this) instanceof LivingEntity living && living.hasStatusEffect(ModEffects.CHARM))
            ci.cancel();
    }
}
