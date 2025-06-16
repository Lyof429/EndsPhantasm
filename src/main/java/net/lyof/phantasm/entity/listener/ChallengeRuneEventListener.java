package net.lyof.phantasm.entity.listener;

import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;

public class ChallengeRuneEventListener implements GameEventListener {
    private final ChallengeRuneBlockEntity entity;

    public ChallengeRuneEventListener(ChallengeRuneBlockEntity entity) {
        this.entity = entity;
    }

    @Override
    public PositionSource getPositionSource() {
        return new BlockPositionSource(this.entity.getPos());
    }

    @Override
    public int getRange() {
        return 8;
    }

    @Override
    public boolean listen(ServerWorld world, GameEvent event, GameEvent.Emitter emitter, Vec3d emitterPos) {
        if (event.getId().equals("entity_place") && BlockPos.ofFloored(emitterPos).equals(this.entity.getPos().up())
                && emitter.sourceEntity() instanceof PlayerEntity player
                && !this.entity.hasCompleted(player)) {

            this.entity.startChallenge(player);
            return true;
        }
        return false;
    }
}
