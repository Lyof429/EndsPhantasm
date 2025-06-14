package net.lyof.phantasm.world.feature.custom.tree.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lyof.phantasm.world.feature.custom.tree.ModTreePlacerTypes;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class PreamTrunkPlacer extends TrunkPlacer {
    public static final Codec<PreamTrunkPlacer> CODEC = RecordCodecBuilder.create(instance ->
            fillTrunkPlacerFields(instance).apply(instance, PreamTrunkPlacer::new));

    public PreamTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
    }

    @Override
    protected TrunkPlacerType<?> getType() {
        return ModTreePlacerTypes.PREAM_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.TreeNode> generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random,
                                                 int height, BlockPos startPos, TreeFeatureConfig config) {

        StraightTrunkPlacer.setToDirt(world, replacer, random, startPos.down(), config);

        height = this.getHeight(random);

        int f = height >= this.getHeight(random) ? random.nextBetween(3, height) : -1;
        List<FoliagePlacer.TreeNode> foliages = new ArrayList<>();

        for (int i = 0; i < height; ++i)
            this.getAndSetState(world, replacer, random, startPos.up(i), config);

        if (f >= 0)
            foliages.add(new FoliagePlacer.TreeNode(startPos.up(f), 0, false));
        foliages.add(new FoliagePlacer.TreeNode(startPos.up(height), 0, false));
        return foliages;
    }
}
