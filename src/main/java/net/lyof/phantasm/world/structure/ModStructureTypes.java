package net.lyof.phantasm.world.structure;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.Phantasm;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.structure.OceanRuinStructure;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

public class ModStructureTypes {
    public static void register() {}

    private static <S extends Structure, T extends StructureType<S>> StructureType<S> register(String name, Codec<S> codec) {
        return Registry.register(Registries.STRUCTURE_TYPE, Phantasm.makeID(name), () -> codec);
    }


    public static final StructureType<OceanRuinStructure> END_RUINS = register("end_ruins",
            OceanRuinStructure.CODEC);
}
