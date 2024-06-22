package net.lyof.phantasm.entity.goal;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.custom.BehemothEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

public class AngerGoal extends Goal {
    public BehemothEntity self;

    public AngerGoal(BehemothEntity self) {
        this.self = self;
    }

    @Override
    public boolean canStart() {
        return !self.isAngry();
    }

    @Override
    public void start() {
        for (Entity entity : self.getWorld().getOtherEntities(self, new Box(self.getBlockPos()).expand(4))) {
            if (entity instanceof PlayerEntity player && !player.isCreative() && !player.isSpectator()) {
                self.damage(player.getDamageSources().playerAttack(player), 1);
                //self.setTarget(player);

            }
        }
    }
}
