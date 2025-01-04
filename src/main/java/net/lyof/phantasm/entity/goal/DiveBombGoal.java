package net.lyof.phantasm.entity.goal;

import net.lyof.phantasm.entity.custom.CrystieEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class DiveBombGoal extends Goal {
    public CrystieEntity self;

    public DiveBombGoal(CrystieEntity entity) {
        this.self = entity;
        this.setControls(EnumSet.of(Goal.Control.MOVE));
    }

    @Override
    public boolean shouldContinue() {
        return self.getMoveControl().isMoving() && self.getTarget() != null && self.getTarget().isAlive();
    }

    @Override
    public boolean canStart() {
        return self.getTarget() != null && !self.getMoveControl().isMoving();
    }

    @Override
    public void start() {
        LivingEntity livingentity = self.getTarget();
        Vec3d vec3d = livingentity.getEyePos();
        self.getMoveControl().moveTo(vec3d.x, vec3d.y, vec3d.z, 20);
    }

    @Override
    public void tick() {
        LivingEntity livingentity = self.getTarget();
        if (self.getBoundingBox().intersects(livingentity.getBoundingBox())) {
            self.tryAttack(livingentity);
        } else {
            double d0 = self.distanceTo(livingentity);
            if (d0 < 32) {
                Vec3d vec3d = livingentity.getEyePos();
                self.getMoveControl().moveTo(vec3d.x, vec3d.y, vec3d.z, 20);
            }
        }
    }
}
