package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ServerPlayerEntity.class, priority = 1001)
public abstract class ServerPlayerEntityMixin extends Entity {
    public ServerPlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract ServerWorld getServerWorld();

    @Shadow public abstract PlayerAdvancementTracker getAdvancementTracker();

    @Shadow private boolean seenCredits;

    @WrapOperation(method = "moveToWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;createEndSpawnPlatform(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V"))
    public void noPlatform(ServerPlayerEntity instance, ServerWorld world, BlockPos centerPos, Operation<Void> original) {
        original.call(instance, world, ServerWorld.END_SPAWN_POS.mutableCopy());
    }

    @WrapMethod(method = "moveToWorld")
    public Entity cancelCredits(ServerWorld destination, Operation<Entity> original) {
        Advancement advancement = this.getServerWorld().getServer().getAdvancementLoader().get(new Identifier("end/kill_dragon"));
        if (this.getServerWorld().getRegistryKey() == World.END && destination.getRegistryKey() == World.OVERWORLD
                && advancement != null && !this.getAdvancementTracker().getProgress(advancement).isDone()) {

            this.seenCredits = true;
            Entity result = original.call(destination);
            this.seenCredits = false;
            return result;
        }
        return original.call(destination);
    }
}
