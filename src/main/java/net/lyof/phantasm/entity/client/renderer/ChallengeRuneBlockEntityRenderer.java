package net.lyof.phantasm.entity.client.renderer;

import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.block.challenge.Challenger;
import net.lyof.phantasm.block.custom.ChallengeRuneBlock;
import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.util.RenderHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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




    public static final Identifier TOWER_BASE_TEXTURE = Identifier.of("minecraft", "textures/block/obsidian.png");

    @Override
    public void render(ChallengeRuneBlockEntity self, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {

        World world = self.getWorld();
        BlockPos pos = self.getPos();
        if (world == null) return;
        PlayerEntity player = MinecraftClient.getInstance().player;

        BlockState state = ModBlocks.CHALLENGE_RUNE.getDefaultState()
                .with(ChallengeRuneBlock.COMPLETED, self.hasCompleted(player));


        matrices.push();

        // Base block
        this.blockRenderer.getModelRenderer().render(world, this.blockRenderer.getModel(state), state, pos,
                matrices, vertexConsumers.getBuffer(RenderLayer.getSolid()), true, world.random,
                state.getRenderingSeed(pos), OverlayTexture.DEFAULT_UV);

        // Tower base
        if (self.renderBase) {
            light = WorldRenderer.getLightmapCoordinates(world, pos.withY(-20));

            matrices.push();
            matrices.translate(0, -pos.getY(), 0);

            RenderHelper.renderCube(matrices, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    6, 7, -512, 0, -3, 4);
            RenderHelper.renderCube(matrices, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    -6, -5, -512, 0, -3, 4);
            RenderHelper.renderCube(matrices, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    -3, 4, -512, 0, 6, 7);
            RenderHelper.renderCube(matrices, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    -3, 4, -512, 0, -6, -5);

            RenderHelper.renderCube(matrices, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    5, 6, -512, 0, 4, 5);
            RenderHelper.renderCube(matrices, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    4, 5, -512, 0, 5, 6);
            RenderHelper.renderCube(matrices, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    5, 6, -512, 0, -4, -3);
            RenderHelper.renderCube(matrices, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    4, 5, -512, 0, -5, -4);
            RenderHelper.renderCube(matrices, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    -5, -4, -512, 0, 4, 5);
            RenderHelper.renderCube(matrices, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    -4, -3, -512, 0, 5, 6);
            RenderHelper.renderCube(matrices, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    -5, -4, -512, 0, -4, -3);
            RenderHelper.renderCube(matrices, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    -4, -3, -512, 0, -5, -4);

            matrices.pop();
        }

        if (self.isChallengeRunning()) {
            // Sky
            if (player instanceof Challenger challenger && challenger.isInRange()) {
                matrices.push();

                float radius = Challenger.R * Math.min(40, self.tick + tickDelta) / 40f;

                if (ConfigEntries.accessibilityChallengeBarrier) {
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(self.tick + tickDelta));

                    RenderHelper.renderCube(matrices, vertexConsumers.getBuffer(RenderLayer.getEyes(TOWER_BASE_TEXTURE)), light,
                            -radius, radius + 1, -radius * 0.5f, radius * 1.5f + 1, -radius, radius + 1, true);
                }
                else {
                    RenderHelper.renderCube(matrices, vertexConsumers.getBuffer(RenderLayer.getEndPortal()), light,
                            -radius, radius + 1, -radius * 0.5f, radius * 1.5f + 1, -radius, radius + 1, true);
                }

                matrices.pop();
            }

            /*BeaconBlockEntityRenderer.renderBeam(matrices, vertexConsumers, BeaconBlockEntityRenderer.BEAM_TEXTURE, tickDelta,
                    1, self.getWorld().getTime(), 0, BeaconBlockEntityRenderer.MAX_BEAM_HEIGHT,
                    new float[]{0.5f, 0f, 0.7f}, 0.2f, 0.25f);*/
        }

        matrices.pop();
    }
}
