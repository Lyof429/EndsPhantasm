package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.lyof.phantasm.item.custom.RealityBreakerItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(Block.class)
public class BlockMixin {
    @WrapMethod(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;")
    private static List<ItemStack> skipRealityBreakDrops(BlockState state, ServerWorld world, BlockPos pos, BlockEntity blockEntity,
                                                         Entity entity, ItemStack stack, Operation<List<ItemStack>> original) {
        if (stack.getItem() instanceof RealityBreakerItem)
            return List.of();
        return original.call(state, world, pos, blockEntity, entity, stack);
    }
}
