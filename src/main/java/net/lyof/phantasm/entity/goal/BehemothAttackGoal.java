package net.lyof.phantasm.entity.goal;

import net.lyof.phantasm.entity.client.animation.BehemothAnimation;
import net.lyof.phantasm.entity.custom.BehemothEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class BehemothAttackGoal extends MeleeAttackGoal {
    public BehemothEntity self;

    public BehemothAttackGoal(BehemothEntity self, double speed, boolean pauseWhenMobIdle) {
        super(self, speed, pauseWhenMobIdle);
        this.self = self;
    }

    @Override
    public boolean canStart() {
        return this.self.animation == BehemothAnimation.WALKING && super.canStart();
    }
}
