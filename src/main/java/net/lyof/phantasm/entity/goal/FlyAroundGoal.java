package net.lyof.phantasm.entity.goal;

import net.lyof.phantasm.Phantasm;
import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class FlyAroundGoal extends Goal {
    private static final int MAX_DISTANCE = 22;
    public PathAwareEntity self;

    public FlyAroundGoal(PathAwareEntity entity) {
        this.self = entity;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    public boolean canStart() {
        return self.getNavigation().isIdle() && self.getRandom().nextInt(10) == 0;
    }

    public boolean shouldContinue() {
        return self.getNavigation().isFollowingPath();
    }

    public void start() {
        Phantasm.log("starting flying goal");
        Vec3d vec3d = this.getRandomLocation();
        if (vec3d != null) {
            self.getNavigation().startMovingAlong(self.getNavigation().findPathTo(BlockPos.ofFloored(vec3d), 1), 1.0);
        }
    }

    @Nullable
    private Vec3d getRandomLocation() {
        Vec3d vec3d2 = self.getRotationVec(0.0F);

        Vec3d vec3d3 = AboveGroundTargeting.find(self, 8, 7, vec3d2.x, vec3d2.z, 1.5707964F, 3, 1);
        return vec3d3 != null ? vec3d3 : NoPenaltySolidTargeting.find(self, 8, 4, -2, vec3d2.x, vec3d2.z, 1.5707963705062866);
    }
}
