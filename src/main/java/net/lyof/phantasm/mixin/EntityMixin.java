package net.lyof.phantasm.mixin;

import net.lyof.phantasm.Phantasm;
import net.minecraft.block.EndGatewayBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.EndGatewayFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "getTeleportTarget", at = @At("RETURN"), cancellable = true)
    public void spawnInOuterEnd(ServerWorld destination, CallbackInfoReturnable<TeleportTarget> cir) {
        if (destination.getRegistryKey() == World.END) {
            TeleportTarget result = cir.getReturnValue();
            result = new TeleportTarget(new Vec3d(1280, 60, 0), result.velocity, result.yaw, result.pitch);
            Phantasm.log(destination.getEnderDragonFight().hasPreviouslyKilled());
            cir.setReturnValue(result);
        }
    }
}
