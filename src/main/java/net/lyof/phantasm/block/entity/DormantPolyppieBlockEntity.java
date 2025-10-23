package net.lyof.phantasm.block.entity;

import net.lyof.phantasm.block.ModBlockEntities;
import net.lyof.phantasm.entity.listener.DormantPolyppieEventListener;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SculkSensorBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.listener.GameEventListener;

public class DormantPolyppieBlockEntity extends BlockEntity implements GameEventListener.Holder<DormantPolyppieEventListener> {
    protected final DormantPolyppieEventListener listener;

    public DormantPolyppieBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DORMANT_POLYPPIE, pos, state);
        this.listener = new DormantPolyppieEventListener(this);
    }

    @Override
    public DormantPolyppieEventListener getEventListener() {
        return this.listener;
    }
}
