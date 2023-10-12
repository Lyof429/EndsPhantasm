package net.lyof.phantasm.mixin;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {
    @Inject(method = "supports", at = @At("HEAD"), cancellable = true)
    private void supports(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        BlockEntityType self = (BlockEntityType) (Object) this;
        if ((BlockEntityType.SIGN.equals(self) || BlockEntityType.HANGING_SIGN.equals(self)) &&
                (state.getBlock() instanceof SignBlock || state.getBlock() instanceof WallSignBlock
                 || state.getBlock() instanceof HangingSignBlock || state.getBlock() instanceof WallHangingSignBlock)) {
            cir.setReturnValue(true);
        }
    }
}