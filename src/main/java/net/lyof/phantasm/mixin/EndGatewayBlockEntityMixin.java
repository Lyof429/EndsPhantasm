package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EndGatewayBlockEntity.class)
public class EndGatewayBlockEntityMixin {
    @WrapOperation(method = "findExitPortalPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    private static boolean invalidStar(BlockState instance, Block block, Operation<Boolean> original) {
        return original.call(instance, block) || instance.isOf(ModBlocks.FALLEN_STAR);
    }
}
