package net.lyof.phantasm.entity.goal;

import net.lyof.phantasm.entity.custom.BehemothEntity;
import net.minecraft.entity.ai.goal.Goal;

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
    public void tick() {/*
        if (self.age % 20 == 0 && self.getWorld() instanceof ServerWorld world)
            world.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    self.getX(), self.getEyeY(), self.getZ(), 1, 0, 0, 0, 0);*/
    }
}
