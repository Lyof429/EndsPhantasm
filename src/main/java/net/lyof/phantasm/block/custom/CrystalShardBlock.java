package net.lyof.phantasm.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Waterloggable;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public class CrystalShardBlock extends Block implements Waterloggable {

    public static final DirectionProperty VERTICAL_DIRECTION = DirectionProperty.of("direction");
    public static final BooleanProperty IS_TIP = BooleanProperty.of("is_tip");
    public static final BooleanProperty WATERLOGGED = BooleanProperty.of("waterlogged");

    public CrystalShardBlock(Settings settings) {
        super(settings.emissiveLighting((a, b, c) -> true).nonOpaque());
        this.setDefaultState(this.getDefaultState()
                .with(IS_TIP, true)
                .with(VERTICAL_DIRECTION, Direction.UP)
                .with(WATERLOGGED, false)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(IS_TIP).add(VERTICAL_DIRECTION).add(WATERLOGGED);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world,
                                                BlockPos pos, BlockPos neighborPos) {
        if (neighborState.isAir() && neighborPos.equals(pos.down()))
            return Blocks.AIR.getDefaultState();
        return state;
    }
}
