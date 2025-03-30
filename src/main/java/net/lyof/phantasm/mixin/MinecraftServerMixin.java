package net.lyof.phantasm.mixin;

import net.lyof.phantasm.config.ModConfig;
import net.lyof.phantasm.world.biome.surface.ModMaterialRules;
import net.minecraft.resource.DataConfiguration;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.world.dimension.DimensionOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "createWorlds", at = @At("RETURN"))
    private void createEPLevels(WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci) {
        ModMaterialRules.addModMaterialRules((MinecraftServer) (Object) this, DimensionOptions.END);
    }
}
