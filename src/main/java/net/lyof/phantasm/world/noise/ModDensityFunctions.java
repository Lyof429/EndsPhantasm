package net.lyof.phantasm.world.noise;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.world.noise.custom.CenterDensityFunction;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctions;

public class ModDensityFunctions {
    public static void register() {}

    public static final Codec<CenterDensityFunction> CENTER = Registry.register(Registries.DENSITY_FUNCTION_TYPE,
            Phantasm.makeID("center"), CenterDensityFunction.CODEC);
}