package net.lyof.phantasm.world.structure;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.world.structure.processor.RandomReplaceStructureProcessor;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.structure.processor.BlockRotStructureProcessor;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.world.gen.structure.OceanRuinStructure;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

public class ModProcessorTypes {
    public static void register() {}

    private static <T extends StructureProcessor> StructureProcessorType<T> register(String name, Codec<T> codec) {
        return Registry.register(Registries.STRUCTURE_PROCESSOR, Phantasm.makeID(name), () -> codec);
    }


    public static final StructureProcessorType<RandomReplaceStructureProcessor> RANDOM_REPLACE = register("random_replace",
            RandomReplaceStructureProcessor.CODEC);
}
