package net.lyof.phantasm.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class DirectionalBlock extends Block {
    public static final DirectionProperty FACING = DirectionProperty.of("facing");
    public static final Map<Direction, VoxelShape> SHAPES = Map.of(
            Direction.DOWN, Block.createCuboidShape(3, 0, 3, 13, 10, 13),
            Direction.UP, Block.createCuboidShape(3, 6, 3, 13, 16, 13),
            Direction.NORTH, Block.createCuboidShape(3, 3, 0, 13, 13, 10),
            Direction.SOUTH, Block.createCuboidShape(3, 3, 6, 13, 13, 16),
            Direction.EAST, Block.createCuboidShape(6, 3, 3, 16, 13, 13),
            Direction.WEST, Block.createCuboidShape(0, 3, 3, 10, 13, 13)
    );

    public DirectionalBlock(Settings properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.DOWN));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.offset(state.get(FACING));
        BlockState support = world.getBlockState(blockPos);
        return support.isSideSolidFullSquare(world, blockPos, state.get(FACING));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState other, WorldAccess world,
                                  BlockPos pos, BlockPos otherPos) {
        BlockState result = super.getStateForNeighborUpdate(state, direction, other, world, pos, otherPos);
        if (!result.isOf(this)) return result;

        return this.canPlaceAt(state, world, pos) ? result : Blocks.AIR.getDefaultState();
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultState().with(FACING, context.getSide().getOpposite());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state.get(FACING));
    }
}