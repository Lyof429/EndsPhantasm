package net.lyof.phantasm.entity.goal;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.custom.BehemothEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

public class SleepGoal extends Goal {
    public BehemothEntity self;

    public SleepGoal(BehemothEntity self) {
        this.self = self;
    }

    @Override
    public boolean canStart() {
        return !self.isAngry();
    }

    @Override
    public boolean canStop() {
        return self.isAngry();
    }

    @Override
    public void tick() {
        Phantasm.log("zzz");
    }
}
