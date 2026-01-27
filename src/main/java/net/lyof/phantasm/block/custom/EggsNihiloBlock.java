package net.lyof.phantasm.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class EggsNihiloBlock extends Block {
    public static final IntProperty SERVINGS = IntProperty.of("servings", 0, 4);

    protected static final VoxelShape SHAPE =
            Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 9.0D, 13.0D);

    public EggsNihiloBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(SERVINGS, 4));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(SERVINGS);
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(SERVINGS);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return direction == Direction.DOWN && !state.canPlaceAt(world, pos)
                ? Blocks.AIR.getDefaultState()
                : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.down()).isSolid();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            if (this.tryEat(world, pos, state, player).isAccepted())
                return ActionResult.SUCCESS;

            if (player.getStackInHand(hand).isEmpty())
                return ActionResult.CONSUME;
        }

        return this.tryEat(world, pos, state, player);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int i = state.get(SERVINGS);
        if (i < 4 && random.nextDouble() < 0.05)
            world.setBlockState(pos, state.with(SERVINGS, i+1));
    }

    public ActionResult tryEat(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!player.canConsume(false) || state.get(SERVINGS) == 0)
            return ActionResult.PASS;

        player.eatFood(world, this.asItem().getDefaultStack());
        world.addBlockBreakParticles(pos, state);
        world.setBlockState(pos, state.with(SERVINGS, state.get(SERVINGS) - 1), 3);

        return ActionResult.SUCCESS;
    }
}
