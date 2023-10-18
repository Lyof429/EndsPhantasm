package net.lyof.phantasm.world.feature;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.world.gen.feature.BlockColumnFeature;
import net.minecraft.world.gen.feature.BlockColumnFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class CrystalSpikeFeature extends BlockColumnFeature {
    public CrystalSpikeFeature() {
        super(BlockColumnFeatureConfig.CODEC);
    }

    @Override
    public boolean generate(FeatureContext<BlockColumnFeatureConfig> context) {
        Phantasm.log(context.getOrigin() + " " + context.getConfig());
        context.getWorld().setBlockState(context.getOrigin(), ModBlocks.CRYSTAL_BLOCK.getDefaultState(), 0);
        return true;
    }
}
