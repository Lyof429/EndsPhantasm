package net.lyof.phantasm.entity.goal;

import net.lyof.phantasm.Phantasm;
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
        return this.canStart();
    }

    @Override
    public void start() {
        Phantasm.log("avoiding ground");
        self.getNavigation().startMovingTo(self.getX(), self.getY() + 3, self.getZ(), 1);
        Phantasm.log(self.getNavigation().getTargetPos());
        //self.setVelocity(self.getVelocity().add(0, 0.1, 0));
    }

    @Override
    public void stop() {
        super.stop();
        //self.getNavigation().stop();
    }
}
