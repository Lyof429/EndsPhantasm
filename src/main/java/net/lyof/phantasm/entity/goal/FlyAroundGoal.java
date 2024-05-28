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
        self.getNavigation().startMovingTo(vec3d.x, vec3d.y, vec3d.z, 1);
    }

    private Vec3d getRandomLocation() {
        Vec3d view = self.getRotationVec(0.0F);

        Vec3d vec3d3 = AboveGroundTargeting.find(self, 8, 7, view.x, view.z, 1.5707964F, 3, 1);
        Phantasm.log(vec3d3);
        if (vec3d3 != null && vec3d3.distanceTo(self.getPos()) > 4) return vec3d3;

        Phantasm.log("weirding out");
        return self.getPos().add(view.multiply(4)).addRandom(self.getRandom(), 1);
    }
}
