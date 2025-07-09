package net.lyof.phantasm.mixin;

import net.lyof.phantasm.Phantasm;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ActiveTargetGoal.class)
public abstract class ActiveTargetGoalMixin extends TrackTargetGoal {
    @Shadow @Nullable protected LivingEntity targetEntity;

    public ActiveTargetGoalMixin(MobEntity mob, boolean checkVisibility) {
        super(mob, checkVisibility);
    }

    @Inject(method = "findClosestTarget", at = @At("TAIL"))
    public void preventChallengeTargeting(CallbackInfo ci) {
        if (this.mob.getType() == EntityType.ENDERMAN && this.targetEntity != null &&
                this.targetEntity.getType() == EntityType.ENDERMITE && this.targetEntity.getCommandTags().contains(Phantasm.MOD_ID + ".challenge"))
            this.targetEntity = null;
    }
}
