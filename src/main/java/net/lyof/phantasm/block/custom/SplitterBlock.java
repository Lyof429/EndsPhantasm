package net.lyof.phantasm.block.custom;

import net.lyof.phantasm.Phantasm;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class SplitterBlock extends AbstractRedstoneGateBlock {
    public static DirectionProperty OUTPUT = DirectionProperty.of("output", Direction.Type.HORIZONTAL);
    public static BooleanProperty MODE2 = BooleanProperty.of("mode");

    public SplitterBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(POWERED, false)
                .with(OUTPUT, Direction.NORTH).with(MODE2, false));
    }

    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, OUTPUT, MODE2);
    }

    @Override
    protected int getUpdateDelayInternal(BlockState state) {
        return 2;
    }

    protected Direction chooseOutput(Random random, BlockState state) {
        Direction dir = state.get(FACING);
        if (state.get(MODE2) || random.nextInt(3) != 0) {
            if (random.nextBoolean()) dir = dir.rotateYClockwise();
            else dir = dir.rotateYCounterclockwise();
        }
        return dir;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!player.getAbilities().allowModifyWorld) {
            return ActionResult.PASS;
        } else {
            world.setBlockState(pos, state.with(MODE2, !state.get(MODE2)), 3);
            return ActionResult.success(world.isClient);
        }
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return super.getWeakRedstonePower(state, world, pos, direction == state.get(OUTPUT) ? state.get(FACING) : state.get(FACING).getOpposite());
    }

    @Override
    protected void updatePowered(World world, BlockPos pos, BlockState state) {
        boolean had = state.get(POWERED);
        boolean has = this.hasPower(world, pos, state);
        if (has && !had)
            world.setBlockState(pos, state.with(OUTPUT, this.chooseOutput(world.random, state)), Block.NOTIFY_LISTENERS);

        super.updatePowered(world, pos, state);
    }

    @Override
    protected void updateTarget(World world, BlockPos pos, BlockState state) {
        super.updateTarget(world, pos, state.with(FACING, state.get(OUTPUT)));
    }
}
