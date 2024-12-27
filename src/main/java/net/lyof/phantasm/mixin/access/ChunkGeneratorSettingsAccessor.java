package net.lyof.phantasm.mixin.access;

import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkGeneratorSettings.class)
public interface ChunkGeneratorSettingsAccessor {
    @Accessor("surfaceRule") @Mutable
    void addSurfaceRule(MaterialRules.MaterialRule ruleSource);
}