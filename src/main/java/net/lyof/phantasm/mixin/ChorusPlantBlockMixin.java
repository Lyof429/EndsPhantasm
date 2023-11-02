package net.lyof.phantasm.mixin;

import net.lyof.phantasm.setup.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChorusPlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChorusPlantBlock.class)
public class ChorusPlantBlockMixin {/*
    @Inject(method = "canPlaceAt", at = @At("RETURN"), cancellable = true)
    public void canPlaceOnNihilium(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState down = world.getBlockState(pos.down());
        boolean bl = !world.getBlockState(pos.up()).isAir() && !down.isAir();

        if (bl) return;
        if (world.getBlockState(pos.down()).isIn(ModTags.Blocks.END_PLANTS_GROWABLE_ON))
            cir.setReturnValue(true);
    }*/
}
