package net.lyof.phantasm.entity.client.model;

import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class PolyppieModel<T extends PolyppieEntity> extends SinglePartEntityModel<T> {
	private final ModelPart root;

	public PolyppieModel(ModelPart root) {
		this.root = root;
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 22.0F, 0.0F));
		ModelPartData body = all.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -8.0F, -8.0F, 16.0F, 8.0F, 16.0F, new Dilation(0.0F))
				.uv(0, 44).cuboid(-8.0F, -16.0F, -8.0F, 16.0F, 4.0F, 16.0F, new Dilation(0.05F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData left_legs = all.addChild("left_legs", ModelPartBuilder.create(), ModelTransform.pivot(5.0F, 0.0F, 5.0F));
		ModelPartData left_leg1 = left_legs.addChild("left_leg1", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -0.1F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, -10.0F));
		ModelPartData left_leg2 = left_legs.addChild("left_leg2", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -0.1F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, -5.0F));
		ModelPartData left_leg3 = left_legs.addChild("left_leg3", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -0.1F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData right_legs = all.addChild("right_legs", ModelPartBuilder.create(), ModelTransform.pivot(-5.0F, 0.0F, 5.0F));
		ModelPartData right_leg1 = right_legs.addChild("right_leg1", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-1.0F, -0.1F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, -10.0F));
		ModelPartData right_leg2 = right_legs.addChild("right_leg2", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-1.0F, -0.1F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, -5.0F));
		ModelPartData right_leg3 = right_legs.addChild("right_leg3", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-1.0F, -0.1F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		return TexturedModelData.of(modelData, 64, 64);
	}

	public static TexturedModelData getTransparentModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 24).cuboid(-8.0F, 0.0F, -8.0F, 16.0F, 4.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0, 10, 0));

		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void setAngles(PolyppieEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		this.getPart().render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart getPart() {
		return this.root;
	}
}