package net.lyof.phantasm.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.function.BiPredicate;
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

    public static void placeColumn(WorldAccess world, BlockPos start, int size, BlockState block) {
        placeColumn(world, start, size, block, (a, b) -> true);
    }

    public static void placeColumn(WorldAccess world, BlockPos start, int size, BlockState block,
                                   BiPredicate<WorldView, BlockPos> condition) {
        for (int sy = 0; sy < size; sy++) {
            if (condition.test(world, start.up(sy)))
                world.setBlockState(start.up(sy), block, 0);
        }
    }
}
