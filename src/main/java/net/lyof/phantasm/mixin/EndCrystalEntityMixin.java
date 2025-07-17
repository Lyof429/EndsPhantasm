package net.lyof.phantasm.mixin;

import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndCrystalEntity.class)
public abstract class EndCrystalEntityMixin extends Entity {
    public EndCrystalEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "canHit", at = @At("HEAD"), cancellable = true)
    public void preventHit(CallbackInfoReturnable<Boolean> cir) {
        if (this.getWorld().getBlockEntity(this.getBlockPos().down()) instanceof ChallengeRuneBlockEntity challengeRune
                && challengeRune.isChallengeRunning())
            cir.setReturnValue(false);
    }
}
