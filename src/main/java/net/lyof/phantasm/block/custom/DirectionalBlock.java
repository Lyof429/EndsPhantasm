package net.lyof.phantasm.block.custom;

import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DirectionalBlock extends Block {
    public static final DirectionProperty FACING = DirectionProperty.of("facing");
    protected static final Map<Direction, VoxelShape> SHAPES = Map.of(
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

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultState().with(FACING, context.getSide().getOpposite());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state.get(FACING));
    }

    public BlockState getPlacementState(WorldView world, BlockPos pos) {
        List<Direction> possible = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            if (this.canPlaceAt(this.getDefaultState().with(FACING, dir), world, pos))
                possible.add(dir);
        }
        if (possible.isEmpty()) return Blocks.AIR.getDefaultState();
        return this.getDefaultState().with(FACING, possible.get((int) pos.asLong() % possible.size()));
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (this == ModBlocks.CHORAL_FAN && random.nextInt(20) == 0) {
            world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.BLOCKS, 1, 1, true);
        }
        super.randomDisplayTick(state, world, pos, random);
    }
}