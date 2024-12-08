package net.lyof.phantasm.mixin;

import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin extends HostileEntity {
    protected EndermanEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "isPlayerStaring", at = @At("HEAD"), cancellable = true)
    public void cancelDragonFightStare(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        List<EnderDragonEntity> list = player.getWorld().getEntitiesByType(TypeFilter.instanceOf(EnderDragonEntity.class),
                new Box(player.getBlockPos()).expand(100),
                a -> true);
        if (ConfigEntries.noEndermenFight && !list.isEmpty()) cir.setReturnValue(false);
    }
}
