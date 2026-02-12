package net.lyof.phantasm.entity.client.model;

import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class PolyppieModel<T extends PolyppieEntity> extends SinglePartEntityModel<T> {
	private final ModelPart main;
	private final ModelPart head;
	private final ModelPart legs;
	private final ModelPart legs_primary;
	private final ModelPart legs_secondary;

	public PolyppieModel(ModelPart root) {
		this.main = root.getChild("main");
		this.head = this.main.getChild("head");
		this.legs = this.main.getChild("legs");
		this.legs_primary = this.legs.getChild("legs_primary");
		this.legs_secondary = this.legs.getChild("legs_secondary");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData main = modelPartData.addChild("main", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData head = main.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, -8.0F, -3.5F, 12.0F, 7.0F, 7.0F, new Dilation(0.0F))
				.uv(14, 14).cuboid(-6.0F, -11.0F, 0.0F, 12.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData legs = main.addChild("legs", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData legs_primary = legs.addChild("legs_primary", ModelPartBuilder.create().uv(0, 14).cuboid(-5.0F, -1.0F, -2.0F, 10.0F, 1.0F, 0.0F, new Dilation(0.0F))
				.uv(0, 15).cuboid(-5.0F, -1.0F, 0.0F, 10.0F, 1.0F, 0.0F, new Dilation(0.0F))
				.uv(0, 16).cuboid(-5.0F, -1.0F, 2.0F, 10.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData legs_secondary = legs.addChild("legs_secondary", ModelPartBuilder.create().uv(0, 17).cuboid(-5.0F, -1.0F, -2.0F, 10.0F, 1.0F, 0.0F, new Dilation(0.0F))
				.uv(0, 18).cuboid(-5.0F, -1.0F, 0.0F, 10.0F, 1.0F, 0.0F, new Dilation(0.0F))
				.uv(0, 19).cuboid(-5.0F, -1.0F, 2.0F, 10.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
/*
		ModelPartData fans = main.addChild("fans", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData fans_left = fans.addChild("fans_left", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData fans_left2_r1 = fans_left.addChild("fans_left2_r1", ModelPartBuilder.create().uv(6, 20).cuboid(-3.0F, -4.5F, 0.0F, 3.0F, 9.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-6.0F, -4.5F, 0.0F, 0.7854F, 0.0F, 0.0F));
		ModelPartData fans_left1_r1 = fans_left.addChild("fans_left1_r1", ModelPartBuilder.create().uv(0, 20).cuboid(-3.0F, -4.5F, 0.0F, 3.0F, 9.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-6.0F, -4.5F, 0.0F, -0.7854F, 0.0F, 0.0F));

		ModelPartData fans_right = fans.addChild("fans_right", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData fans_right2_r1 = fans_right.addChild("fans_right2_r1", ModelPartBuilder.create().uv(18, 20).cuboid(0.0F, -4.5F, 0.0F, 3.0F, 9.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(6.0F, -4.5F, 0.0F, 0.7854F, 0.0F, 0.0F));
		ModelPartData fans_right1_r1 = fans_right.addChild("fans_right1_r1", ModelPartBuilder.create().uv(12, 20).cuboid(0.0F, -4.5F, 0.0F, 3.0F, 9.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(6.0F, -4.5F, 0.0F, -0.7854F, 0.0F, 0.0F));
*/
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
		return this.main;
	}
}