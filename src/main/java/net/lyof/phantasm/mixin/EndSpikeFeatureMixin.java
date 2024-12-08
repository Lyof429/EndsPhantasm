package net.lyof.phantasm.mixin;

import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ModifiableWorld;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.feature.EndSpikeFeature;
import net.minecraft.world.gen.feature.EndSpikeFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EndSpikeFeature.class)
public abstract class EndSpikeFeatureMixin {
    @Redirect(method = "generateSpike", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/world/gen/feature/EndSpikeFeature;setBlockState(Lnet/minecraft/world/ModifiableWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"))
    public void randomizeObsidian(EndSpikeFeature spike, ModifiableWorld world, BlockPos pos, BlockState state,
                                  ServerWorldAccess serverworld, Random random, EndSpikeFeatureConfig config,
                                  EndSpikeFeature.Spike spikedata) {
        if (state.isOf(Blocks.IRON_BARS) && ConfigEntries.noCrystalCages)
            return;

        double crying = (pos.getY() - 60) / (spikedata.getHeight() - 60d);
        if (state.isOf(Blocks.OBSIDIAN) && ConfigEntries.improveEndSpires) {
            if (crying > 0 && Math.random() < crying * crying)
                state = Blocks.CRYING_OBSIDIAN.getDefaultState();
            else if (Math.random() < 0.2)
                state = Math.random() < 0.5 ? ModBlocks.POLISHED_OBSIDIAN.getDefaultState() : ModBlocks.POLISHED_OBSIDIAN_BRICKS.getDefaultState();
        }

        world.setBlockState(pos, state, Block.NOTIFY_ALL);
    }
}
