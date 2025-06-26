package net.lyof.phantasm.entity.client.renderer;

import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.block.custom.ChallengeRuneBlock;
import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.lyof.phantasm.block.challenge.Challenger;
import net.lyof.phantasm.util.RenderHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
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

        if (self.getTowerBases().isEmpty())
            self.generateTowerBases();

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
            for (Vec3i vec : self.getTowerBases())
                RenderHelper.renderCube(matrices, vertexConsumers, TOWER_BASE_TEXTURE, light,
                        vec.getX(), vec.getX() + 1,
                        vec.getY() - 512, vec.getY(),
                        vec.getZ(), vec.getZ() + 1);
        }

        if (self.isChallengeRunning()) {
            // Sky
            if (player instanceof Challenger challenger && challenger.isInRange()) {
                float radius = Challenger.R * Math.min(40, self.tick + tickDelta) / 40f;
                RenderHelper.renderCube(matrices, vertexConsumers.getBuffer(RenderLayer.getEyes(TOWER_BASE_TEXTURE)), light,
                        -radius, radius + 1, -radius, radius + 1, -radius, radius + 1, true);
            }

            /*BeaconBlockEntityRenderer.renderBeam(matrices, vertexConsumers, BeaconBlockEntityRenderer.BEAM_TEXTURE, tickDelta,
                    1, self.getWorld().getTime(), 0, BeaconBlockEntityRenderer.MAX_BEAM_HEIGHT,
                    new float[]{0.5f, 0f, 0.7f}, 0.2f, 0.25f);*/
        }

        matrices.pop();
    }
}
