package net.lyof.phantasm.entity.client.renderer;

import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.block.custom.ChallengeRuneBlock;
import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.lyof.phantasm.util.RenderHelper;
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
        if (entity.renderBase) {
            light = WorldRenderer.getLightmapCoordinates(world, pos.withY(-20));
            for (Vec3i vec : entity.getTowerBases())
                RenderHelper.renderCube(matrices, vertexConsumers, TOWER_BASE_TEXTURE, light,
                        vec.getX(), vec.getX() + 1,
                        vec.getY() - 512, vec.getY(),
                        vec.getZ(), vec.getZ() + 1);
        }

        // Sky
        RenderHelper.renderCube(matrices, vertexConsumers.getBuffer(RenderLayer.getEndGateway()), light,
                -10, 11, -10, 11, -10, 11);

        /*BeaconBlockEntityRenderer.renderBeam(matrices, vertexConsumers, BeaconBlockEntityRenderer.BEAM_TEXTURE, tickDelta,
                1, entity.getWorld().getTime(), 0, BeaconBlockEntityRenderer.MAX_BEAM_HEIGHT,
                new float[]{0.5f, 0f, 0.7f}, 0.2f, 0.25f);*/
    }
}
