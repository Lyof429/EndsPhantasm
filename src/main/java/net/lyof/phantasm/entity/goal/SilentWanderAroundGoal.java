package net.lyof.phantasm.entity.goal;

import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;

public class SilentWanderAroundGoal extends WanderAroundGoal {
    public SilentWanderAroundGoal(PolyppieEntity mob, double speed) {
        super(mob, speed);
    }

    @Override
    public boolean canStart() {
        return this.mob instanceof PolyppieEntity polyppie && polyppie.getBand().canMove() && super.canStart();
    }
}
