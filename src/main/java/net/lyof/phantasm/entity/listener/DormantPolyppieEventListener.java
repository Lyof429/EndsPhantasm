package net.lyof.phantasm.entity.listener;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.entity.DormantPolyppieBlockEntity;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;

public class DormantPolyppieEventListener implements GameEventListener {
    private final DormantPolyppieBlockEntity source;

    public DormantPolyppieEventListener(DormantPolyppieBlockEntity polyppie) {
        this.source = polyppie;
    }

    @Override
    public PositionSource getPositionSource() {
        return new BlockPositionSource(this.source.getPos());
    }

    @Override
    public int getRange() {
        return 8;
    }

    @Override
    public boolean listen(ServerWorld world, GameEvent event, GameEvent.Emitter emitter, Vec3d emitterPos) {
        if (event.isIn(ModTags.GameEvents.DORMANT_POLYPPIE_CAN_LISTEN)) {
            world.addSyncedBlockEvent(this.source.getPos(), this.source.getCachedState().getBlock(), 0, 0);
            return true;
        }
        return false;
    }
}
