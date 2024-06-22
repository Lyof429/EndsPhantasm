package net.lyof.phantasm.entity.goal;

import net.lyof.phantasm.entity.custom.BehemothEntity;
import net.minecraft.entity.ai.goal.Goal;

public class AngerGoal extends Goal {
    public AngerGoal(BehemothEntity self) {}

    @Override
    public boolean canStart() {
        return false;
    }
}
