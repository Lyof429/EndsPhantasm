package net.lyof.phantasm.entity.client.renderer;

import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.block.custom.ChallengeRuneBlock;
import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.pack.ExperimentalWarningScreen;
import net.minecraft.client.gui.screen.world.ExperimentsScreen;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class ChallengeRuneBlockEntityRenderer implements BlockEntityRenderer<ChallengeRuneBlockEntity> {
    private final BlockRenderManager blockRenderer;

    public ChallengeRuneBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.blockRenderer = context.getRenderManager();
    }

    @Override
    public boolean rendersOutsideBoundingBox(ChallengeRuneBlockEntity entity) {
        return true;
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }

    @Override
    public boolean isInRenderDistance(ChallengeRuneBlockEntity entity, Vec3d vec3d) {
        return Vec3d.ofCenter(entity.getPos()).multiply(1.0, 0.0, 1.0).isInRange(vec3d.multiply(1.0, 0.0, 1.0), this.getRenderDistance());
    }


    public void renderFace(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Identifier texture, int light,
                           float yStart, float yEnd, float xStart, float xEnd, float zStart, float zEnd) {

        matrices.push();

        float maxv = Math.abs(yEnd - yStart);

        MatrixStack.Entry entry = matrices.peek();
        Matrix4f position = entry.getPositionMatrix();
        Matrix3f normal = entry.getNormalMatrix();
        VertexConsumer vertices = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(texture));

        // Front face
        vertices.vertex(position, xStart, yStart, zStart)
                .color(255, 255, 255, 255)
                .texture(1, maxv)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normal, 1, 1, 1)
                .next();
        vertices.vertex(position, xStart, yEnd, zStart)
                .color(255, 255, 255, 255)
                .texture(1, 0)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normal, 1, 1, 1)
                .next();
        vertices.vertex(position, xEnd, yEnd, zEnd)
                .color(255, 255, 255, 255)
                .texture(0, 0)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normal, 1, 1, 1)
                .next();
        vertices.vertex(position, xEnd, yStart, zEnd)
                .color(255, 255, 255, 255)
                .texture(0, maxv)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normal, 1, 1, 1)
                .next();

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180), (xStart + xEnd) / 2, yStart, (zStart + zEnd) / 2);
        entry = matrices.peek();
        position = entry.getPositionMatrix();
        normal = entry.getNormalMatrix();

        // Back face
        vertices.vertex(position, xStart, yStart, zStart)
                .color(255, 255, 255, 255)
                .texture(1, maxv)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normal, 1, 1, 1)
                .next();
        vertices.vertex(position, xStart, yEnd, zStart)
                .color(255, 255, 255, 255)
                .texture(1, 0)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normal, 1, 1, 1)
                .next();
        vertices.vertex(position, xEnd, yEnd, zEnd)
                .color(255, 255, 255, 255)
                .texture(0, 0)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normal, 1, 1, 1)
                .next();
        vertices.vertex(position, xEnd, yStart, zEnd)
                .color(255, 255, 255, 255)
                .texture(0, maxv)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normal, 1, 1, 1)
                .next();

        matrices.pop();
    }

    public void renderCube(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Identifier texture, int light,
                           float yStart, float yEnd, float xStart, float xEnd, float zStart, float zEnd) {

        this.renderFace(matrices, vertexConsumers, texture, light, yStart, yEnd, xStart, xEnd, zStart, zStart);
        this.renderFace(matrices, vertexConsumers, texture, light, yStart, yEnd, xEnd, xEnd, zStart, zEnd);
        this.renderFace(matrices, vertexConsumers, texture, light, yStart, yEnd, xEnd, xStart, zEnd, zEnd);
        this.renderFace(matrices, vertexConsumers, texture, light, yStart, yEnd, xStart, xStart, zEnd, zStart);
    }

    public static final Identifier TOWER_BASE_TEXTURE = Identifier.of("minecraft", "textures/block/obsidian.png");

    @Override
    public void render(ChallengeRuneBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       int light, int overlay) {

        World world = entity.getWorld();
        BlockPos pos = entity.getPos();
        if (world == null) return;

        if (entity.getTowerBases().isEmpty())
            entity.generateTowerBases();

        BlockState state = ModBlocks.CHALLENGE_RUNE.getDefaultState()
                .with(ChallengeRuneBlock.COMPLETED, entity.hasCompleted(MinecraftClient.getInstance().player));


        // Base block
        this.blockRenderer.getModelRenderer().render(world, this.blockRenderer.getModel(state), state, pos,
                matrices, vertexConsumers.getBuffer(RenderLayer.getSolid()), true, world.random,
                state.getRenderingSeed(pos), OverlayTexture.DEFAULT_UV);

        // Tower base
        light = WorldRenderer.getLightmapCoordinates(world, pos.withY(-20));
        for (Vec3i vec : entity.getTowerBases())
            this.renderCube(matrices, vertexConsumers, TOWER_BASE_TEXTURE, light, vec.getY() - 512, vec.getY(),
                    vec.getX(), vec.getX() + 1,
                    vec.getZ(), vec.getZ() + 1);

        /*BeaconBlockEntityRenderer.renderBeam(matrices, vertexConsumers, BeaconBlockEntityRenderer.BEAM_TEXTURE, tickDelta,
                1, entity.getWorld().getTime(), 0, BeaconBlockEntityRenderer.MAX_BEAM_HEIGHT,
                new float[]{0.5f, 0f, 0.7f}, 0.2f, 0.25f);*/
    }
}
