package net.lyof.phantasm.entity.client.renderer;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.client.ModModelLayers;
import net.lyof.phantasm.entity.client.model.BehemothModel;
import net.lyof.phantasm.entity.client.model.PolyppieModel;
import net.lyof.phantasm.entity.custom.BehemothEntity;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class PolyppieRenderer extends MobEntityRenderer<PolyppieEntity, PolyppieModel<PolyppieEntity>> {
    private static final Identifier TEXTURE = Phantasm.makeID("textures/entity/crystie.png");
    private static final Identifier TEXTURE_ANGRY = Phantasm.makeID("textures/entity/behemoth_angry.png");

    public PolyppieRenderer(EntityRendererFactory.Context context) {
        super(context, new PolyppieModel<>(context.getPart(ModModelLayers.CRYSTIE)), 0.6f);
    }

    @Override
    public Identifier getTexture(PolyppieEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(PolyppieEntity entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(entity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
