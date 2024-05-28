package net.lyof.phantasm.entity.goal;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;

import java.util.EnumSet;

public class AvoidGroundGoal extends Goal {
    public PathAwareEntity self;

    public AvoidGroundGoal(PathAwareEntity entity) {
        this.self = entity;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        return !self.getWorld().isAir(self.getBlockPos().down());
    }

    @Override
    public boolean shouldContinue() {
        return false;
    }

    @Override
    public void tick() {
        self.setVelocity(self.getVelocity().add(0, 0.3, 0));
    }

    @Override
    public void stop() {
        super.stop();
    }
}
