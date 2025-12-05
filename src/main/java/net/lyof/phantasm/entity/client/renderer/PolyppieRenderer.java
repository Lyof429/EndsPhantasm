package net.lyof.phantasm.entity.client.renderer;

import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.entity.client.ModModelLayers;
import net.lyof.phantasm.entity.client.model.PolyppieModel;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.Map;

public class PolyppieRenderer extends MobEntityRenderer<PolyppieEntity, PolyppieModel<PolyppieEntity>> {
    private static final float pi = (float) Math.PI;
    private static final float rad = pi / 180f;
    private static final Map<String, Quaternionf> rotationCache = new HashMap<>();

    private static Quaternionf getRotation(float x, float y, float z) {
        String key = x + " " + y + " " + z;
        rotationCache.putIfAbsent(key, new Quaternionf().rotateXYZ(x, y, z));
        return rotationCache.get(key);
    }

    private final ItemRenderer itemRenderer;

    public PolyppieRenderer(EntityRendererFactory.Context context) {
        super(context, new PolyppieModel<>(context.getPart(ModModelLayers.CRYSTIE)), 0.6f);
        this.itemRenderer = context.getItemRenderer();

        CarriedRenderer.setupCarriedRenderer(context);
    }

    @Override
    public Identifier getTexture(PolyppieEntity entity) {
        return entity.getVariant().texture;
    }

    @Override
    public void render(PolyppieEntity self, float tickDelta, float animationProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(self, tickDelta, animationProgress, matrices, vertexConsumers, light);

        matrices.push();

        matrices.translate(0, self.getHeight()*this.model.getPart().yScale*0.5f, 0);
        matrices.multiply(getRotation(0,
                rad*(180 - self.bodyYaw),
                self.isPlayingRecord() ? 0.5f*MathHelper.sin(0.05f*(tickDelta + self.tickCount - self.recordStartTick)) : 0));
        matrices.translate(0, 0, -self.getWidth()*this.model.getPart().zScale*0.5f);

        this.itemRenderer.renderItem(self.getStack(), ModelTransformationMode.FIXED, light, 0, matrices, vertexConsumers, self.getWorld(), 0);
        matrices.pop();
    }


    public static class CarriedRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
        public CarriedRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
            super(context);
        }

        private static void setupCarriedRenderer(EntityRendererFactory.Context context) {
            if (flag) return;
            flag = true;
            renderer = new PolyppieRenderer(context);
        }

        public static PolyppieRenderer renderer = null;
        public static boolean flag = false;

        @Override
        public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity self,
                           float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

            if (self instanceof PolyppieCarrier carrier && carrier.phantasm_getPolyppie() != null) {
                matrices.push();

                ModelPart torso = this.getContextModel().body;
                matrices.scale(torso.xScale, torso.yScale, torso.zScale);
                matrices.translate(0, torso.yScale * 0.7, torso.zScale * (self.isInSneakingPose() ? 0.5 : 0.45));
                matrices.multiply(getRotation(torso.pitch + pi, 0, torso.roll));

                renderer.render(carrier.phantasm_getPolyppie(), tickDelta, animationProgress, matrices, vertexConsumers, light);
                matrices.pop();
            }
        }
    }
}
