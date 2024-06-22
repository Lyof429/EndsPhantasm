package net.lyof.phantasm.entity.goal;

import net.lyof.phantasm.entity.custom.BehemothEntity;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;

public class BehemothWanderAroundGoal extends WanderAroundGoal {
    public BehemothWanderAroundGoal(BehemothEntity mob, double speed) {
        super(mob, speed);
    }

    @Override
    public boolean canStart() {
        return super.canStart() && ((BehemothEntity) this.mob).isAngry();
    }
}
