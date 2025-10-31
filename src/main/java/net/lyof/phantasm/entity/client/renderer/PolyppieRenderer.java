package net.lyof.phantasm.entity.client.renderer;

import net.lyof.phantasm.Phantasm;
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
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.Map;

public class PolyppieRenderer extends MobEntityRenderer<PolyppieEntity, PolyppieModel<PolyppieEntity>> {
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
    public void render(PolyppieEntity entity, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int i) {
        matrices.push();
        super.render(entity, f, g, matrices, vertexConsumers, i);
        matrices.translate(0, 0, 1);
        this.itemRenderer.renderItem(entity.getStack(), ModelTransformationMode.FIXED, i, 0, matrices, vertexConsumers, entity.getWorld(), 0);
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

        private static final float pi = (float) Math.PI;
        private static final Map<String, Quaternionf> rotationCache = new HashMap<>();

        private static Quaternionf getRotation(float x, float y, float z) {
            String key = x + " " + y + " " + z;
            rotationCache.putIfAbsent(key, new Quaternionf().rotateXYZ(x, y, z));
            return rotationCache.get(key);
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity self,
                           float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

            if (self instanceof PolyppieCarrier carrier && carrier.getCarriedPolyppie() != null) {
                matrices.push();

                ModelPart torso = this.getContextModel().body;
                renderer.model.getPart().xScale = torso.xScale;
                renderer.model.getPart().yScale = torso.yScale;
                renderer.model.getPart().zScale = torso.zScale;
                matrices.translate(0, torso.yScale * 0.5, torso.zScale * (self.isInSneakingPose() ? 0.5 : 0.3));
                matrices.multiply(getRotation(torso.pitch + pi, 0, torso.roll));

                renderer.render(carrier.getCarriedPolyppie(), tickDelta, animationProgress, matrices, vertexConsumers, light);
                matrices.pop();
            }
        }
    }
}
