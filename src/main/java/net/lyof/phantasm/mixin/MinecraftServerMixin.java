package net.lyof.phantasm.mixin;

import net.lyof.phantasm.world.biome.surface.ModMaterialRules;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.world.dimension.DimensionOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "createWorlds", at = @At("TAIL"))
    private void createPhantasmWorlds(WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci) {
        ModMaterialRules.addModMaterialRules((MinecraftServer) (Object) this, DimensionOptions.END);
    }
}
