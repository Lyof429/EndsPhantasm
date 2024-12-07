package net.lyof.phantasm.block.custom;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SubwooferBlock extends Block {
    public static final BooleanProperty POWERED = BooleanProperty.of("powered");
    public static final DirectionProperty FACING = DirectionProperty.of("facing");

    public SubwooferBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(POWERED, false).with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(POWERED).add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        boolean b = world.isReceivingRedstonePower(pos);
        Direction dir = state.get(FACING);
        BlockPos p;

        if (b != state.get(POWERED)) {
            if (b) {
                world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.BLOCKS,
                        1, 1, true);

                for (int i = 2; i < ConfigEntries.subwooferRange; i++) {
                    p = pos.mutableCopy().offset(dir, i);

                    world.addSyncedBlockEvent(pos, this, dir.getId(), i);

                    List<Entity> entities = world.getOtherEntities(null, new Box(p).expand(1.2), Entity::isAlive);
                    for (Entity e : entities) {
                        e.setVelocity(new Vec3d(dir.getOffsetX(), dir.getOffsetY() + 0.1, dir.getOffsetZ()));
                        if (e instanceof PlayerEntity pl)
                            pl.velocityModified = true;
                    }
                }
            }

            world.setBlockState(pos, state.with(POWERED, b));
        }
    }

    @Override
    public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
        BlockPos p = pos.mutableCopy().offset(Direction.byId(type), data);
        world.addImportantParticle(ParticleTypes.SONIC_BOOM, p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5,
                0, 0, 0);
        return true;
    }
}
