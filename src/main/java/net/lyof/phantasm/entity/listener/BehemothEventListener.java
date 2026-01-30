package net.lyof.phantasm.entity.listener;

import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.entity.custom.BehemothEntity;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.EntityPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;

public class BehemothEventListener implements GameEventListener {
    private final BehemothEntity behemoth;

    public BehemothEventListener(BehemothEntity behemoth) {
        this.behemoth = behemoth;
    }

    @Override
    public PositionSource getPositionSource() {
        return new EntityPositionSource(behemoth, behemoth.getStandingEyeHeight());
    }

    @Override
    public int getRange() {
        return ConfigEntries.behemothAggroRange;
    }

    @Override
    public boolean listen(ServerWorld world, GameEvent event, GameEvent.Emitter emitter, Vec3d emitterPos) {
        if (event.isIn(ModTags.GameEvents.BEHEMOTH_CAN_LISTEN) && !(emitter.sourceEntity() instanceof BehemothEntity)) {
            if (emitter.sourceEntity() instanceof PlayerEntity player && !player.isCreative() && !player.isSpectator()) {
                if (player.isSneaking()) {
                    if (player.distanceTo(behemoth) < ConfigEntries.behemothSneakAggroRange)
                        behemoth.setTarget(player);
                } else
                    behemoth.setTarget(player);
            }
            return true;
        }
        return false;
    }
}
