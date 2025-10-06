package net.lyof.phantasm.mixin;

import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import net.lyof.phantasm.world.structure.VariantStructure;
import net.minecraft.registry.*;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;
import java.util.function.Consumer;

@Mixin(RegistryLoader.class)
public class RegistryLoaderMixin {
    @WrapOperation(method = "load(Lnet/minecraft/registry/RegistryOps$RegistryInfoGetter;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Ljava/util/Map;)V",
            at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/DataResult;getOrThrow(ZLjava/util/function/Consumer;)Ljava/lang/Object;"))
    private static <R, E> R attackCodecs(DataResult<R> instance, boolean allowPartial, Consumer<String> onError, Operation<R> original,
                                         RegistryOps.RegistryInfoGetter registryInfoGetter, ResourceManager resourceManager,
                                         RegistryKey<? extends Registry<E>> registryKey, MutableRegistry<E> newRegistry,
                                         Decoder<E> decoder, Map<RegistryKey<?>, Exception> exceptions,
                                         @Local Identifier identifier, @Local JsonElement json, @Local ResourceFinder resourceFinder) {

        R result = original.call(instance, allowPartial, onError);

        if (registryKey.equals(RegistryKeys.STRUCTURE)) {
            if (result instanceof VariantStructure structure && json.getAsJsonObject().has("variant")) {
                structure.setVariant(json.getAsJsonObject().get("variant").getAsString());
            }
        }

        return result;
    }
}
