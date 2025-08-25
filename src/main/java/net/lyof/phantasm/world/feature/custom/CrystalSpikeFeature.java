package net.lyof.phantasm.world.feature.custom;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.block.custom.CrystalShardBlock;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.feature.custom.config.CrystalSpikeFeatureConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
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
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        CrystalSpikeFeatureConfig config = context.getConfig();

        int size = config.size().get(random);
        float chance = config.voidChance();

        BlockState top = ModBlocks.CRYSTAL_SHARD.getDefaultState();
        BlockState bottom = random.nextFloat() < chance
                ? ModBlocks.VOID_CRYSTAL_SHARD.getDefaultState()
                : ModBlocks.CRYSTAL_SHARD.getDefaultState();

        BlockPos pos = new BlockPos(origin).withY(1);
        while (pos.getY() < world.getHeight() && !(world.getBlockState(pos).isIn(ModTags.Blocks.END_PLANTS_GROWABLE_ON)
                && world.getBlockState(pos.up()).isOf(Blocks.AIR))) {

            pos = pos.up();
        }
        pos = pos.up();

        if (!world.getBiome(pos).isIn(ModTags.Biomes.DREAMING_DEN))
            return false;

        for (int i = 0; i < size; i++) {
            if (pos.getY() >= world.getHeight() - 1)
                return false;

            this.setBlockState(world, pos, top.with(CrystalShardBlock.IS_TIP, i == size - 1));
            pos = pos.up();
        }


        pos = new BlockPos(origin).withY(0);
        while (pos.getY() < world.getHeight() && !(world.getBlockState(pos.up()).isIn(ModTags.Blocks.END_PLANTS_GROWABLE_ON)
                && world.getBlockState(pos).isOf(Blocks.AIR))) {

            pos = pos.up();
        }

        for (int i = 0; i < size; i++) {
            if (pos.getY() <= world.getBottomY() + 1 || pos.getY() >= 250)
                return false;

            this.setBlockState(world, pos, bottom.with(CrystalShardBlock.IS_UP, false).with(CrystalShardBlock.IS_TIP, i == size - 1));
            pos = pos.down();
        }

        return true;
    }
}