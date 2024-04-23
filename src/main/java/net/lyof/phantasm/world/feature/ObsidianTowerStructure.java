package net.lyof.phantasm.world.feature;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.Phantasm;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.feature.EndSpikeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class ObsidianTowerStructure extends Feature<CountConfig> {
    public static final Feature<CountConfig> INSTANCE = new ObsidianTowerStructure(CountConfig.CODEC);

    public ObsidianTowerStructure(Codec<CountConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<CountConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        CountConfig config = context.getConfig();
        origin = world.getChunk(origin).getPos().getCenterAtY(origin.getY());

        int maxy = config.getCount().get(random) + origin.getY();

        for (int sy = 0; sy < maxy; sy++) {
            for (int sx = -7; sx < 8; sx++) {
                for (int sz = -7; sz < 8; sz++) {
                    if (sx*sx + sz*sz <= 49 && sx*sx + sz*sy > 36)
                        world.setBlockState(origin.withY(sy).east(sx).north(sz),
                                Blocks.OBSIDIAN.getDefaultState(), Block.NOTIFY_NEIGHBORS);
                }
            }
        }

        Phantasm.log("Hi " + context.getOrigin());
        return true;
    }
}
