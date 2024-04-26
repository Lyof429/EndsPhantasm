package net.lyof.phantasm.mixin;

import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableWorld;
import net.minecraft.world.gen.feature.EndSpikeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EndSpikeFeature.class)
public abstract class EndSpikeFeatureMixin {
    @Redirect(method = "generateSpike", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/world/gen/feature/EndSpikeFeature;setBlockState(Lnet/minecraft/world/ModifiableWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"))
    public void randomizeObsidian(EndSpikeFeature spike, ModifiableWorld world, BlockPos pos, BlockState state) {
        double crying = (pos.getY() - 65) / 35d;
        if (state.isOf(Blocks.OBSIDIAN) && ConfigEntries.improveEndSpires) {
            if (Math.random() < crying * crying)
                state = Blocks.CRYING_OBSIDIAN.getDefaultState();
            else if (Math.random() < 0.2)
                state = Math.random() < 0.5 ? ModBlocks.POLISHED_OBSIDIAN.getDefaultState() : ModBlocks.POLISHED_OBSIDIAN_BRICKS.getDefaultState();
        }

        world.setBlockState(pos, state, Block.NOTIFY_ALL);
    }
}
