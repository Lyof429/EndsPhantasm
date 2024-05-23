package net.lyof.phantasm.mixin.access;

import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EndGatewayBlockEntity.class)
public interface EndGatewayBlockEntityAccessor {
    @Invoker(value = "findBestPortalExitPos")
    static BlockPos getExitPos(World world, BlockPos origin) {
        throw new AssertionError();
    }
}
