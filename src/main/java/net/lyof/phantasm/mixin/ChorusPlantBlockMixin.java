package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusPlantBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ChorusPlantBlock.class, priority = 990)
public class ChorusPlantBlockMixin {
    @WrapOperation(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    public boolean canPlaceAtNihilium(BlockState instance, Block block, Operation<Boolean> original) {
        if (block == Blocks.END_STONE)
            return instance.isIn(ModTags.Blocks.END_PLANTS_GROWABLE_ON);
        return original.call(instance, block);
    }

    @WrapOperation(method = "getStateForNeighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    public boolean getStateForNeighborUpdateNihilium(BlockState instance, Block block, Operation<Boolean> original) {
        return this.canPlaceAtNihilium(instance, block, original);
    }

    @WrapOperation(method = "withConnectionProperties", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    public boolean withConnectionPropertiesNihilium(BlockState instance, Block block, Operation<Boolean> original) {
        return this.canPlaceAtNihilium(instance, block, original);
    }
}
