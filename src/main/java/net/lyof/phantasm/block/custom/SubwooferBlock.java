package net.lyof.phantasm.block.custom;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SubwooferBlock extends FacingBlock {
    public static final BooleanProperty POWERED = BooleanProperty.of("powered");

    public SubwooferBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(POWERED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(POWERED);
    }

    public static boolean canPush(Entity e) {
        return e.isPushable() || e instanceof ItemEntity || e instanceof ProjectileEntity || e instanceof ArmorStandEntity;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        boolean b = world.isReceivingRedstonePower(pos);
        Direction dir = state.get(FACING);
        BlockPos p;

        if (b != state.get(POWERED)) {
            world.setBlockState(pos, state.with(POWERED, b));

            if (b) {
                List<UUID> affected = new ArrayList<>();

                for (int i = 1; i < ConfigEntries.subwooferRange; i++) {
                    p = pos.mutableCopy().offset(dir, i);
                    if (world.getBlockState(p).isIn(BlockTags.OCCLUDES_VIBRATION_SIGNALS)) return;

                    world.addSyncedBlockEvent(pos, this, dir.getId(), i);
                    world.emitGameEvent(null, GameEvent.NOTE_BLOCK_PLAY, pos);

                    if (i == 1) continue;

                    List<Entity> entities = world.getOtherEntities(null, new Box(p).expand(1.2), SubwooferBlock::canPush);
                    for (Entity e : entities) {
                        if (affected.contains(e.getUuid())) continue;

                        affected.add(e.getUuid());
                        e.addVelocity(new Vec3d(dir.getOffsetX(), dir.getOffsetY(), dir.getOffsetZ())
                                .multiply(world.getReceivedRedstonePower(pos) / 5f)
                                .add(0, 0.1, 0));
                        if (e.getVelocity().y < 0.1 && dir.getAxis() != Direction.Axis.Y)
                            e.addVelocity(0, 0.1, 0);
                        e.velocityModified = true;
                        if (dir == Direction.UP) e.fallDistance = 0;
                    }
                }
            }
        }
    }

    @Override
    public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
        if (data == 1 && !world.getBlockState(pos.offset(state.get(FACING).getOpposite())).isIn(BlockTags.OCCLUDES_VIBRATION_SIGNALS))
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.BLOCKS,
                    0.2f, 1.5f, true);

        BlockPos p = pos.mutableCopy().offset(Direction.byId(type), data);
        world.addImportantParticle(ParticleTypes.SONIC_BOOM, p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5,
                0, 0, 0);
        return true;
    }
}
