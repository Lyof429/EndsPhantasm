package net.lyof.phantasm.mixin;

import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.mixin.access.EndGatewayBlockEntityAccessor;
import net.minecraft.block.EndGatewayBlock;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.recipe.FireworkRocketRecipe;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
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
        if (destination.getRegistryKey() == World.END && ConfigEntries.outerEndIntegration) {
            TeleportTarget result = cir.getReturnValue();
            BlockPos p = new BlockPos(1280, 60, 0);

            BlockPos pos = EndGatewayBlockEntityAccessor.getExitPos(destination, p).up(2);
            Phantasm.log(pos);

            result = new TeleportTarget(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), result.velocity, result.yaw, result.pitch);


            cir.setReturnValue(result);
        }
    }
}
