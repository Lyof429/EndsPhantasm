package net.lyof.phantasm.entity.goal;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.custom.CrystieEntity;
import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

import java.util.EnumSet;

public class FlyRandomlyGoal extends Goal {
    private final CrystieEntity crystie;

    public FlyRandomlyGoal(CrystieEntity crystie) {
        this.crystie = crystie;
        this.setControls(EnumSet.of(Goal.Control.MOVE));
    }

    @Override
    public boolean canStart() {
        double f;
        double e;
        MoveControl moveControl = this.crystie.getMoveControl();
        if (!moveControl.isMoving()) {
            return true;
        }
        double d = moveControl.getTargetX() - this.crystie.getX();
        double g = d * d + (e = moveControl.getTargetY() - this.crystie.getY()) * e + (f = moveControl.getTargetZ() - this.crystie.getZ()) * f;
        return g < 1.0 || g > 3600.0;
    }

    @Override
    public boolean shouldContinue() {
        return false;
    }

    @Override
    public void start() {
        Vec3d vec = AboveGroundTargeting.find(this.crystie, 8, 7, 1, 1, 1.5707964f, 3, 1);
        Phantasm.log(vec);
        if (vec == null)
            vec = this.crystie.getPos().addRandom(this.crystie.getRandom(), 20);
        this.crystie.getMoveControl().moveTo(vec.x, vec.y, vec.z, 0.2);
    }
}
