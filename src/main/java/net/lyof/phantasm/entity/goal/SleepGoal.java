package net.lyof.phantasm.entity.goal;

import net.lyof.phantasm.entity.custom.BehemothEntity;
import net.minecraft.entity.ai.goal.Goal;

public class SleepGoal extends Goal {
    public SleepGoal(BehemothEntity self) {}

    @Override
    public boolean canStart() {
        return false;
    }
}
