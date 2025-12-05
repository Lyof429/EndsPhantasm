package net.lyof.phantasm.entity.goal;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

public class PolyppieTowerGoal extends Goal {
    private static final TargetPredicate VALID_MATE_PREDICATE = TargetPredicate.createNonAttackable().setBaseMaxDistance(8.0).ignoreVisibility();
    protected final PolyppieEntity polyppie;
    protected final World world;
    @Nullable protected PolyppieEntity target;
    private int timer;
    private final double speed;

    public PolyppieTowerGoal(PolyppieEntity polyppie, double speed) {
        this.polyppie = polyppie;
        this.world = polyppie.getWorld();
        this.speed = speed;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    public boolean canStart() {
        if (!this.polyppie.getBand().canMove())
            return false;
        this.target = this.findMate();
        return this.target != null;
    }

    public boolean shouldContinue() {
        return this.target != null && this.target.isAlive() && this.timer < 60;
    }

    public void stop() {
        this.target = null;
        this.timer = 0;
    }

    public void tick() {
        this.polyppie.getLookControl().lookAt(this.target, 10.0F, (float) this.polyppie.getMaxLookPitchChange());
        this.polyppie.getNavigation().startMovingTo(this.target, this.speed);
        this.timer++;
        if (this.timer >= this.getTickCount(60) && this.polyppie.squaredDistanceTo(this.target) < 9.0) {
            this.polyppie.joinBand(this.target);
            this.target = null;
            Phantasm.log("Joined");
        }
    }

    @Nullable
    private PolyppieEntity findMate() {
        List<? extends PolyppieEntity> list = this.world.getTargets(PolyppieEntity.class, VALID_MATE_PREDICATE, this.polyppie, this.polyppie.getBoundingBox().expand(8.0));
        double d = Double.MAX_VALUE;
        PolyppieEntity target = null;

        for (PolyppieEntity it : list) {
            if (!it.getBand().contains(this.polyppie.getId()) && this.polyppie.squaredDistanceTo(it) < d) {
                target = it;
                d = this.polyppie.squaredDistanceTo(it);
            }
        }

        return target;
    }
}
