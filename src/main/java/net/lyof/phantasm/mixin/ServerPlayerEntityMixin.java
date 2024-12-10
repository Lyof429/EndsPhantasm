package net.lyof.phantasm.mixin;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ServerPlayerEntity.class, priority = 1001)
public abstract class ServerPlayerEntityMixin extends Entity {
    public ServerPlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract ServerWorld getServerWorld();

    @Shadow public abstract PlayerAdvancementTracker getAdvancementTracker();

    @Shadow private boolean seenCredits;

    @Redirect(method = "moveToWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;createEndSpawnPlatform(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V"))
    public void noPlatform(ServerPlayerEntity instance, ServerWorld world, BlockPos centerPos) {
        BlockPos.Mutable mutable = ServerWorld.END_SPAWN_POS.mutableCopy();
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                for (int k = -1; k < 3; ++k) {
                    BlockState blockState = k == -1 ? Blocks.OBSIDIAN.getDefaultState() : Blocks.AIR.getDefaultState();
                    world.setBlockState(mutable.set(ServerWorld.END_SPAWN_POS).move(j, k, i), blockState);
                }
            }
        }
    }

    @Inject(method = "moveToWorld", at = @At("HEAD"))
    public void cancelCreditsHead(ServerWorld destination, CallbackInfoReturnable<Entity> cir) {
        Advancement advancement = this.getServerWorld().getServer().getAdvancementLoader().get(new Identifier("end/kill_dragon"));
        if (this.getServerWorld().getRegistryKey() == World.END && destination.getRegistryKey() == World.OVERWORLD
                && advancement != null && !this.getAdvancementTracker().getProgress(advancement).isDone()) {
            this.seenCredits = true;
        }
    }

    @Inject(method = "moveToWorld", at = @At("RETURN"))
    public void cancelCreditsTail(ServerWorld destination, CallbackInfoReturnable<Entity> cir) {
        Advancement advancement = this.getServerWorld().getServer().getAdvancementLoader().get(new Identifier("end/kill_dragon"));
        if (this.getServerWorld().getRegistryKey() == World.END && destination.getRegistryKey() == World.OVERWORLD
                && advancement != null && !this.getAdvancementTracker().getProgress(advancement).isDone()) {
            this.seenCredits = false;
        }
    }
}
