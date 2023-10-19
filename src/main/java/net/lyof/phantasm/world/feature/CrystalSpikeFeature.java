package net.lyof.phantasm.world.feature;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.feature.config.CrystalSpikeFeatureConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class CrystalSpikeFeature extends Feature<CrystalSpikeFeatureConfig> {
    public static final Feature<CrystalSpikeFeatureConfig> INSTANCE = new CrystalSpikeFeature(CrystalSpikeFeatureConfig.CODEC);

    public CrystalSpikeFeature(Codec<CrystalSpikeFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<CrystalSpikeFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        // the origin is the place where the game starts trying to place the feature
        BlockPos origin = context.getOrigin();
        // we won't use the random here, but we could if we wanted to
        Random random = context.getRandom();
        CrystalSpikeFeatureConfig config = context.getConfig();

        // don't worry about where these come from-- we'll implement these methods soon
        int number = config.number();
        Identifier blockID = config.blockId();

        BlockState blockState = Registries.BLOCK.get(blockID).getDefaultState();
        // ensure the ID is okay
        if (blockState == null) throw new IllegalStateException(blockID + " could not be parsed to a valid block identifier!");

        // find the surface of the world
        BlockPos testPos = new BlockPos(origin);
        for (int y = 0; y < world.getHeight(); y++) {
            testPos = testPos.up();
            // the tag name is dirt, but includes grass, mud, podzol, etc.
            if (world.getBlockState(testPos).isIn(ModTags.Blocks.END_PLANTS_GROWABLE_ON)) {
                if (world.getBlockState(testPos.up()).isOf(Blocks.AIR)) {
                    for (int i = 0; i < number; i++) {
                        // create a simple pillar of blocks
                        world.setBlockState(testPos, blockState, 0x10);
                        testPos = testPos.up();

                        // ensure we don't try to place blocks outside the world
                        if (testPos.getY() >= world.getTopY()) break;
                    }
                    return true;
                }
            }
        }
        // the game couldn't find a place to put the pillar
        return false;
    }
}