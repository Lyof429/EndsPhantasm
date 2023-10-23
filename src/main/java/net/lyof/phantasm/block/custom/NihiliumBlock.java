package net.lyof.phantasm.block.custom;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.block.*;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;

public class NihiliumBlock extends Block implements Fertilizable {
    public NihiliumBlock(Settings settings) {
        super(settings);
    }

    public static boolean canSurvive(BlockState state, WorldView world, BlockPos pos) {
        BlockPos up = pos.up();
        BlockState state_up = world.getBlockState(up);

        if (state_up.getFluidState().getLevel() == 8)
            return false;

        int i = ChunkLightProvider
                .getRealisticOpacity(world, state, pos, state_up, up, Direction.UP, state_up.getOpacity(world, up));
        return i < world.getMaxLightLevel();
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!canSurvive(state, world, pos))
            world.setBlockState(pos, Blocks.END_STONE.getDefaultState());
    }


    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return world.getBlockState(pos.up()).isAir();
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        BlockPos target;
        for (int i = 0; i < 6; i++) {
            target = pos.mutableCopy().add(random.nextBetween(-1, 1), random.nextBetween(-1, 1), random.nextBetween(-1, 1));

            if (canSurvive(world.getBlockState(target), world, target) && world.getBlockState(target).getBlock() == Blocks.END_STONE)
                world.setBlockState(target, this.getDefaultState());
            // TODO: Add plants
        }
    }
}
