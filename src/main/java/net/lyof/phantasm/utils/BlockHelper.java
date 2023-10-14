package net.lyof.phantasm.utils;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import java.util.function.Predicate;

public class BlockHelper {
    public static boolean hasNeighbor(WorldView world, BlockPos pos, Predicate<BlockState> check) {
        return check.test(world.getBlockState(pos.up()))
                || check.test(world.getBlockState(pos.down()))
                || check.test(world.getBlockState(pos.north()))
                || check.test(world.getBlockState(pos.south()))
                || check.test(world.getBlockState(pos.east()))
                || check.test(world.getBlockState(pos.west()));
    }
}
