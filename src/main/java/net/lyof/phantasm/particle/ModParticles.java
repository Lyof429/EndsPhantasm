package net.lyof.phantasm.particle;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.lyof.phantasm.Phantasm;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModParticles {
    public static void register() {}

    public static final DefaultParticleType ZZZ = Registry.register(Registries.PARTICLE_TYPE, Phantasm.makeID("zzz"),
            FabricParticleTypes.simple());
}
