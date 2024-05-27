package net.lyof.phantasm.entity.client.renderer;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.client.ModModelLayers;
import net.lyof.phantasm.entity.client.model.CrystieModel;
import net.lyof.phantasm.entity.custom.CrystieEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class CrystieRenderer extends MobEntityRenderer<CrystieEntity, CrystieModel<CrystieEntity>> {
    private static final Identifier TEXTURE = Phantasm.makeID("textures/entity/crystie.png");

    public CrystieRenderer(EntityRendererFactory.Context context) {
        super(context, new CrystieModel<>(context.getPart(ModModelLayers.CRYSTIE)), 0.6f);
        this.addFeature(new EyesFeatureRenderer<>(this) {
            @Override
            public RenderLayer getEyesTexture() {
                return RenderLayer.getEyes(TEXTURE);
            }
        });
    }

    @Override
    public Identifier getTexture(CrystieEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(CrystieEntity mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
