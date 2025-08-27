package net.lyof.phantasm.world.noise.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.densityfunction.DensityFunction;

public record CenterDensityFunction(int distance) implements DensityFunction.Base {
    public static final Codec<CenterDensityFunction> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codecs.NONNEGATIVE_INT.fieldOf("distance").forGetter(CenterDensityFunction::distance)
            ).apply(instance, CenterDensityFunction::new));
    public static final CodecHolder<CenterDensityFunction> CODEC_HOLDER = CodecHolder.of(CODEC);


    @Override
    public double sample(NoisePos pos) {
        float f = pos.blockX();
        float h = pos.blockZ();
        float distance = MathHelper.sqrt(f * f + h * h);
        return distance > this.distance() ? this.minValue() : this.maxValue();
    }

    @Override
    public double minValue() {
        return 0;
    }

    @Override
    public double maxValue() {
        return 1;
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return CODEC_HOLDER;
    }
}
