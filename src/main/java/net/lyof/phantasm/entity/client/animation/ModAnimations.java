package net.lyof.phantasm.entity.client.animation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

@Environment(value = EnvType.CLIENT)
public class ModAnimations {
    public static class Behemoth {
        public static final Animation USING_TONGUE = Animation.Builder.create(0.5f)
                .addBoneAnimation("head", new Transformation(Transformation.Targets.ROTATE,
                        new Keyframe(0.0f, AnimationHelper.createRotationalVector(0.0f, 0.0f, 0.0f),
                                Transformation.Interpolations.LINEAR),
                        new Keyframe(0.0833f, AnimationHelper.createRotationalVector(-60.0f, 0.0f, 0.0f),
                                Transformation.Interpolations.LINEAR),
                        new Keyframe(0.4167f, AnimationHelper.createRotationalVector(-60.0f, 0.0f, 0.0f),
                                Transformation.Interpolations.LINEAR),
                        new Keyframe(0.5f, AnimationHelper.createRotationalVector(0.0f, 0.0f, 0.0f),
                                Transformation.Interpolations.LINEAR)))
                .addBoneAnimation("head", new Transformation(Transformation.Targets.SCALE,
                        new Keyframe(0.0f, AnimationHelper.createRotationalVector(1.0f, 1.0f, 1.0f),
                                Transformation.Interpolations.LINEAR),
                        new Keyframe(0.0833f, AnimationHelper.createRotationalVector(0.998f, 1.0f, 1.0f),
                                Transformation.Interpolations.LINEAR),
                        new Keyframe(0.4167f, AnimationHelper.createRotationalVector(0.998f, 1.0f, 1.0f),
                                Transformation.Interpolations.LINEAR),
                        new Keyframe(0.5f, AnimationHelper.createRotationalVector(1.0f, 1.0f, 1.0f),
                                Transformation.Interpolations.LINEAR))).build();

        //public static final Animation WALKING = Animation.Builder.create(1).addBoneAnimation("body", ).looping().build();
        public static final Animation SLEEPING = Animation.Builder.create(1)
                .addBoneAnimation("body", new Transformation(Transformation.Targets.SCALE,
                        new Keyframe(0, AnimationHelper.createScalingVector(1, 1, 1),
                                Transformation.Interpolations.LINEAR),
                        new Keyframe(0.25f, AnimationHelper.createScalingVector(1, 1, 0),
                                Transformation.Interpolations.LINEAR),
                        new Keyframe(0.5f, AnimationHelper.createScalingVector(1, 1, 1),
                                Transformation.Interpolations.LINEAR),
                        new Keyframe(0.75f, AnimationHelper.createScalingVector(1, 1, 2),
                                Transformation.Interpolations.LINEAR)
                )).looping().build();
    }
}
