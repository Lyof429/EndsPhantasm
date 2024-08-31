package net.lyof.phantasm.world.feature.custom;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.block.custom.CrystalShardBlock;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.feature.custom.config.CrystalSpikeFeatureConfig;
import net.lyof.phantasm.world.feature.custom.config.DralgaeFeatureConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class DralgaeFeature extends Feature<DralgaeFeatureConfig> {
    public static final Feature<DralgaeFeatureConfig> INSTANCE = new DralgaeFeature(DralgaeFeatureConfig.CODEC);

    public DralgaeFeature(Codec<DralgaeFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DralgaeFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        int originy = origin.getY();
        Random random = context.getRandom();
        DralgaeFeatureConfig config = context.getConfig();

        if (!world.getBlockState(origin.down()).isIn(ModTags.Blocks.DRALGAE_GROWABLE_ON)) return false;

        int size = config.size().get(random);

        for (int i = 0; i < size; i++) {
            if (originy + i > world.getTopY() || !world.getBlockState(origin.up(i)).isAir()) return true;

            this.setBlockState(world, origin.up(i), config.stem().get(random, origin.up(i)));
        }

        if (random.nextInt(2) == 0)
            this.setBlockState(world, origin.up(size), config.fruit().get(random, origin.up(size)));
        return true;
    }
}