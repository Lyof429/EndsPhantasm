package net.lyof.phantasm.block.custom;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class CrystalShardBlock extends Block implements Waterloggable {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(2, 0, 2, 14, 16, 14);

    public static final DirectionProperty DIRECTION = DirectionProperty.of("direction");
    public static final BooleanProperty IS_TIP = BooleanProperty.of("is_tip");
    public static final BooleanProperty WATERLOGGED = BooleanProperty.of("waterlogged");

    public CrystalShardBlock(Settings settings) {
        super(settings.nonOpaque());
        this.setDefaultState(this.getDefaultState()
                .with(IS_TIP, true)
                .with(DIRECTION, Direction.UP)
                .with(WATERLOGGED, false)
        );
    }


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(IS_TIP).add(DIRECTION).add(WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return isValidPos(world, pos.up(), Direction.DOWN) || isValidPos(world, pos.down(), Direction.UP);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        BlockState state = this.getDefaultState()
                .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).isOf(Fluids.WATER));;

        boolean down = world.getBlockState(pos.up()).isSideSolidFullSquare(world, pos, Direction.DOWN)
                || world.getBlockState(pos.up()).getBlock() == this.asBlock();
        boolean up = world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos, Direction.UP)
                || world.getBlockState(pos.down()).getBlock() == this.asBlock();

        if (down && up) {
            if (ctx.getSide() == Direction.DOWN) return state.with(DIRECTION, Direction.DOWN);
        }
        else if (down)
            return state.with(DIRECTION, Direction.DOWN);

        return state;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world,
                                                BlockPos pos, BlockPos neighborPos) {

        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        if (isValidPos(world, neighborPos, direction.getOpposite())) {
            if (direction == state.get(DIRECTION))
                return state.with(IS_TIP, false);
        }

        else if (direction == state.get(DIRECTION).getOpposite()) {
            return Blocks.AIR.getDefaultState();
        }

        if (direction == state.get(DIRECTION))
            return state.with(IS_TIP, true);

        return state;
    }

    public boolean isValidPos(BlockView world, BlockPos pos, Direction direction) {
        BlockState state = world.getBlockState(pos);
        return state.isSideSolidFullSquare(world, pos, direction) || state.getBlock() == this.asBlock();
    }
}
