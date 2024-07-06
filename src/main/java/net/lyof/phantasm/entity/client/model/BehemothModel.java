package net.lyof.phantasm.entity.client.model;

import net.lyof.phantasm.entity.animation.BehemothAnimation;
import net.lyof.phantasm.entity.custom.BehemothEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class BehemothModel<T extends BehemothEntity> extends SinglePartEntityModel<T> {
	public static final float rad = (float) Math.PI / 180f;

	private final ModelPart body;
	private final ModelPart left_horn;
	private final ModelPart right_horn;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart zzz;

	public BehemothModel(ModelPart root) {
		this.body = root.getChild("body");
		this.left_horn = this.body.getChild("left_horn");
		this.right_horn = this.body.getChild("right_horn");
		this.left_leg = this.body.getChild("left_leg");
		this.right_leg = this.body.getChild("right_leg");
		this.zzz = root.getChild("zzz");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-9.0F, -28.0F, -9.0F, 18.0F, 28.0F, 18.0F, new Dilation(0.0F))
		.uv(0, 87).cuboid(-8.0F, -27.0F, -7.0F, 16.0F, 27.0F, 14.0F, new Dilation(0.0F))
		.uv(0, 47).cuboid(-9.0F, 0.0F, -9.0F, 18.0F, 2.0F, 18.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 18.0F, 0.0F));

		ModelPartData left_horn = body.addChild("left_horn", ModelPartBuilder.create().uv(65, 39).cuboid(0.0F, -6.0F, -9.0F, 6.0F, 12.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(9.0F, -26.0F, 5.0F));
		ModelPartData right_horn = body.addChild("right_horn", ModelPartBuilder.create().uv(65, 39).mirrored().cuboid(-6.0F, -6.0F, -9.0F, 6.0F, 12.0F, 8.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-9.0F, -26.0F, 5.0F));

		ModelPartData left_leg = body.addChild("left_leg", ModelPartBuilder.create().uv(55, 0).cuboid(-3.0F, -2.0F, -3.0F, 6.0F, 8.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, 0.0F, 0.0F));
		ModelPartData right_leg = body.addChild("right_leg", ModelPartBuilder.create().uv(55, 0).mirrored().cuboid(-3.0F, -2.0F, -3.0F, 6.0F, 8.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-4.0F, 0.0F, 0.0F));

		ModelPartData zzz = modelPartData.addChild("zzz", ModelPartBuilder.create().uv(60, 101).cuboid(0, 0, 0, 8, 8, 0, new Dilation(0)), ModelTransform.pivot(8, 0, 8));

		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public void setAngles(BehemothEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		switch (entity.animation) {
			case WALKING -> {
				this.body.pitch = MathHelper.sin(entity.animTicks * 3 * rad) * 0.1f;
				this.left_leg.pitch = MathHelper.cos(limbSwing * 0.6662f + (float) Math.PI) * 1.4f * limbSwingAmount;
				this.right_leg.pitch = MathHelper.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;
			}
			case SLEEPING -> {
				this.body.pitch = rad * -90;
				this.body.zScale = (float) (1 + Math.sin(entity.animTicks * rad) * 0.1);
				this.left_horn.zScale = 1;
				this.right_horn.zScale = 1;
			}
			case WAKING_UP -> {
				this.getPart().traverse().forEach(ModelPart::resetTransform);
				this.body.pitch = MathHelper.cos(rad * entity.animTicks * 90f / BehemothAnimation.WAKING_UP.maxTime) * rad * -90;
			}
			case WAKING_DOWN -> {
				this.getPart().traverse().forEach(ModelPart::resetTransform);
				this.body.pitch = MathHelper.sin(rad * entity.animTicks * 90f / BehemothAnimation.WAKING_DOWN.maxTime) * rad * -90;
			}
		}
		//this.getPart().traverse().forEach(ModelPart::resetTransform);

		//this.updateAnimation(entity.sleepingAnimationState, ModAnimations.Behemoth.SLEEPING, ageInTicks);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		this.getPart().render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart getPart() {
		return this.body;
	}
}