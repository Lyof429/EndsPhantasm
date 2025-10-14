package net.lyof.phantasm.entity.client.model;

import net.lyof.phantasm.entity.client.animation.BehemothAnimation;
import net.lyof.phantasm.entity.custom.BehemothEntity;
import net.lyof.phantasm.entity.custom.CrystieEntity;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class PolyppieModel<T extends PolyppieEntity> extends SinglePartEntityModel<T> {
	private final ModelPart main;
	private final ModelPart wingsleft;
	private final ModelPart wingsright;

	public PolyppieModel(ModelPart root) {
		this.main = root.getChild("main");
		this.wingsleft = main.getChild("wings").getChild("left");
		this.wingsright = main.getChild("wings").getChild("right");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData main = modelPartData.addChild("main", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		//ModelPartData spike = main.addChild("spike", ModelPartBuilder.create().uv(12, 12).cuboid(-3.0F, -6.0F, 0.0F, 6.0F, 7.0F, 0.0F, new Dilation(0.0F))
		//		.uv(0, 6).cuboid(0.0F, -6.0F, -3.0F, 0.0F, 7.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -7.0F, 0.0F));

		ModelPartData wings = main.addChild("wings", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -7.0F, 0.0F));

		ModelPartData left = wings.addChild("left", ModelPartBuilder.create().uv(0, 19).mirrored().cuboid(2.0F, -1.0F, 2.0F, 6.0F, 5.0F, 0.0F, new Dilation(0.0F)).mirrored(false)
				.uv(12, 19).mirrored().cuboid(2.0F, 4.0F, 2.0F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData right = wings.addChild("right", ModelPartBuilder.create().uv(0, 19).cuboid(-8.0F, -1.0F, 2.0F, 6.0F, 5.0F, 0.0F, new Dilation(0.0F))
				.uv(12, 19).cuboid(-5.0F, 4.0F, 2.0F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void setAngles(PolyppieEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart getPart() {
		return main;
	}
}