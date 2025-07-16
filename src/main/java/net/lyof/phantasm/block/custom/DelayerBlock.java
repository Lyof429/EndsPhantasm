package net.lyof.phantasm.block.custom;

import net.lyof.phantasm.Phantasm;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RedstoneView;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;

public class DelayerBlock extends AbstractRedstoneGateBlock {
    public static BooleanProperty DELAYING = BooleanProperty.of("delaying");
    private int receivedPower = 0;

    public DelayerBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(POWERED, false).with(FACING, Direction.NORTH)
                .with(DELAYING, false));
    }

    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, DELAYING);
    }

    @Override
    protected int getUpdateDelayInternal(BlockState state) {
        return 2 + this.receivedPower * 20;
    }

    @Override
    protected void updatePowered(World world, BlockPos pos, BlockState state) {
        Direction facing = state.get(FACING);
        Direction right = facing.rotateYClockwise();
        Direction left = facing.rotateYCounterclockwise();
        boolean gatesOnly = this.getSideInputFromGatesOnly();
        this.receivedPower = world.getEmittedRedstonePower(pos.offset(right), right, gatesOnly)
                + world.getEmittedRedstonePower(pos.offset(left), left, gatesOnly);

        boolean had = state.get(POWERED);
        boolean has = this.hasPower(world, pos, state);
        if (had != has) world.setBlockState(pos, state.with(DELAYING, has), Block.NOTIFY_LISTENERS);

        super.updatePowered(world, pos, world.getBlockState(pos));
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean had = state.get(POWERED);
        boolean has = this.hasPower(world, pos, state);
        if (had != has) world.setBlockState(pos, state.with(DELAYING, has), Block.NOTIFY_LISTENERS);

        state = world.getBlockState(pos);

        int save = this.receivedPower;
        this.receivedPower = 1;
        super.scheduledTick(state, world, pos, random);
        this.receivedPower = save;
    }
}
