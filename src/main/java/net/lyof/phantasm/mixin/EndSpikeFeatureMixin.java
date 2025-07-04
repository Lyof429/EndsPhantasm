package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.config.ConfigEntries;
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

@Mixin(EndSpikeFeature.class)
public abstract class EndSpikeFeatureMixin {
    @WrapOperation(method = "generateSpike", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/world/gen/feature/EndSpikeFeature;setBlockState(Lnet/minecraft/world/ModifiableWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"))
    public void randomizeObsidian(EndSpikeFeature instance, ModifiableWorld modifiableWorld, BlockPos pos,
                                  BlockState state, Operation<Void> original,
                                  ServerWorldAccess world, Random random, EndSpikeFeatureConfig config,
                                  EndSpikeFeature.Spike spike) {

        double crying = (pos.getY() - 70) / (spike.getHeight() - 70d);
        if (state.isOf(Blocks.OBSIDIAN) && ConfigEntries.improveEndSpires) {
            if (Math.random() < crying * crying * crying)
                state = Blocks.CRYING_OBSIDIAN.getDefaultState();
            else if (Math.random() < 0.35)
                state = Math.random() < 0.4 ? ModBlocks.POLISHED_OBSIDIAN.getDefaultState()
                        : ModBlocks.POLISHED_OBSIDIAN_BRICKS.getDefaultState();
        }

        original.call(instance, modifiableWorld, pos, state);
    }
}
