package net.lyof.phantasm.entity.goal;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.custom.CrystieEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.EnumSet;

public class DiveBombGoal extends Goal {
    public CrystieEntity self;
    public int tick = 0;

    public DiveBombGoal(CrystieEntity entity) {
        this.self = entity;
        this.setControls(EnumSet.of(Goal.Control.MOVE));
    }

    @Override
    public boolean canStart() {
        return self.isAngry && self.getWorld().getClosestPlayer(self, 16) != null;
    }

    @Override
    public void start() {
        World world = self.getWorld();
        PlayerEntity target = world.getClosestPlayer(self, 16);

        if (target != null) {
            this.tick = 0;

            Phantasm.log("trying to attack " + target);
            self.getNavigation().startMovingAlong(self.getNavigation().findPathTo(target, 16), 1);
            //self.setVelocity(target.getPos().subtract(self.getPos()).normalize().multiply(0.2));
            //self.lookAtEntity(target, 360, 360);
        }
        else this.stop();
    }

    @Override
    public void stop() {
        self.getNavigation().stop();
        self.isAngry = false;
        this.tick = 0;
    }

    @Override
    public void tick() {
        this.tick++;
        if (this.tick > 100) this.stop();

        PlayerEntity target = self.getWorld().getClosestPlayer(self, 16);
        if (target == null) {
            this.stop();
            return;
        }

        self.getNavigation().startMovingAlong(self.getNavigation().findPathTo(target, 16), 1);
        //self.setVelocity(target.getPos().subtract(self.getPos()).normalize().multiply(0.2));
        //self.lookAtEntity(target, 360, 360);
        if (self.distanceTo(target) < 2) {
            this.stop();
            self.explode();
        }
    }
}
