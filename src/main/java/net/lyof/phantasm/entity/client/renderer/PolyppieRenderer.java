package net.lyof.phantasm.entity.client.renderer;

import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.entity.client.ModModelLayers;
import net.lyof.phantasm.entity.client.model.PolyppieModel;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLoader;
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

    private static PolyppieRenderer instance = null;

    private static Quaternionf getRotation(float x, float y, float z) {
        String key = x + " " + y + " " + z;
        rotationCache.putIfAbsent(key, new Quaternionf().rotateXYZ(x, y, z));
        return rotationCache.get(key);
    }

    public PolyppieRenderer(EntityRendererFactory.Context context) {
        super(context, new PolyppieModel<>(context.getPart(ModModelLayers.POLYPPIE)), 0.4f);
        this.addFeature(new DiscRenderer(this, context.getItemRenderer()));

        instance = this;
    }

    @Override
    public Identifier getTexture(PolyppieEntity entity) {
        return entity.getVariant().texture;
    }


    public static class DiscRenderer extends FeatureRenderer<PolyppieEntity, PolyppieModel<PolyppieEntity>> {
        private final ItemRenderer itemRenderer;

        public DiscRenderer(FeatureRendererContext<PolyppieEntity, PolyppieModel<PolyppieEntity>> context, ItemRenderer itemRenderer) {
            super(context);
            this.itemRenderer = itemRenderer;
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, PolyppieEntity entity,
                           float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

            matrices.push();
            matrices.translate(0, 1.15, entity.isPlayingRecord() ?
                    -0.05 - 0.1*MathHelper.cos((entity.tickCount - entity.recordStartTick + tickDelta) * 0.08f) : -0.15);
            matrices.multiply(getRotation(-pi/2, 0, 0));
            this.itemRenderer.renderItem(entity.getStack(), ModelTransformationMode.GROUND, light, 0, matrices, vertexConsumers, entity.getWorld(), 0);
            matrices.pop();
        }
    }


    public static class CarriedRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
        public CarriedRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
            super(context);
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity self,
                           float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

            if (self instanceof PolyppieCarrier carrier && carrier.phantasm_getPolyppie() != null) {
                matrices.push();

                ModelPart torso = this.getContextModel().body;
                matrices.scale(torso.xScale, torso.yScale, torso.zScale);
                matrices.translate(0, torso.yScale * 0.6, torso.zScale * (self.isInSneakingPose() ? 0.6 : 0.35));
                matrices.multiply(getRotation(torso.pitch + pi, 0, torso.roll));

                instance.render(carrier.phantasm_getPolyppie(), tickDelta, animationProgress, matrices, vertexConsumers, light);
                matrices.pop();
            }
        }
    }
}
