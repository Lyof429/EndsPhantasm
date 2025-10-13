package net.lyof.phantasm.world.structure.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lyof.phantasm.world.structure.ModProcessorTypes;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class RandomReplaceStructureProcessor extends StructureProcessor {
    public static final Codec<RandomReplaceStructureProcessor> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                Codec.floatRange(0, 1).fieldOf("chance").forGetter(processor -> processor.chance),
                TagKey.codec(RegistryKeys.BLOCK).fieldOf("affected").forGetter(processor -> processor.affectedBlocks),
                Registries.BLOCK.getCodec().fieldOf("result").forGetter(processor -> processor.result)
            ).apply(instance, RandomReplaceStructureProcessor::new));

    protected final float chance;
    protected final TagKey<Block> affectedBlocks;
    protected final Block result;

    public RandomReplaceStructureProcessor(float chance, TagKey<Block> affectedBlocks, Block result) {
        this.chance = chance;
        this.affectedBlocks = affectedBlocks;
        this.result = result;
    }

    @Override
    public @Nullable StructureTemplate.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot,
                                                                  StructureTemplate.StructureBlockInfo original,
                                                                  StructureTemplate.StructureBlockInfo current,
                                                                  StructurePlacementData data) {
        current = super.process(world, pos, pivot, original, current, data);
        if (current != null && current.state().isIn(this.affectedBlocks) && data.getRandom(current.pos()).nextFloat() < this.chance)
            return new StructureTemplate.StructureBlockInfo(current.pos(), this.result.getDefaultState(), current.nbt());
        return current;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModProcessorTypes.RANDOM_REPLACE;
    }
}
