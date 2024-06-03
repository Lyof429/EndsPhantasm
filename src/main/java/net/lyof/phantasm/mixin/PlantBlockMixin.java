package net.lyof.phantasm.mixin;

import net.lyof.phantasm.setup.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.SideShapeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlantBlock.class)
public class PlantBlockMixin {
    @Inject(method = "canPlantOnTop", at = @At("HEAD"), cancellable = true)
    private void customPlantTypes(BlockState floor, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        Block self = (Block) (Object) this;
        if (self.getDefaultState().isIn(ModTags.Blocks.END_PLANTS)) {
            cir.setReturnValue(floor.isSideSolid(world, pos.down(), Direction.UP, SideShapeType.FULL)
                    && floor.isIn(ModTags.Blocks.END_PLANTS_GROWABLE_ON));
            cir.cancel();
        }
    }
}
