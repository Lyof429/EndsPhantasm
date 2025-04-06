package net.lyof.phantasm.mixin;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.config.ModConfig;
import net.lyof.phantasm.setup.datagen.config.ConfiguredData;
import net.lyof.phantasm.setup.datagen.config.ConfiguredDataResourcePack;
import net.minecraft.resource.*;
import net.minecraft.util.Identifier;
import org.apache.commons.io.input.CharSequenceInputStream;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Predicate;

@Mixin(LifecycledResourceManagerImpl.class)
public class LifecycledResourceManagerImplMixin {
    @Unique
    private static Resource readAndApply(Optional<Resource> resource, ConfiguredData data) {
        Phantasm.log("Applying configured data: " + data.target);

        String result = "";
        if (resource.isEmpty())
            result = data.apply(null);
        else {
            try {
                result = data.apply(new String(resource.get().getInputStream().readAllBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String finalResult = result;
        return new Resource(ConfiguredDataResourcePack.INSTANCE,
                () -> new CharSequenceInputStream(finalResult, Charset.defaultCharset()));
    }

    @Unique
    private static Resource readAndApply(Resource resource, ConfiguredData data) {
        if (resource.getPack() instanceof ConfiguredDataResourcePack) return resource;
        return readAndApply(Optional.of(resource), data);
    }

    @Inject(method = "getResource", at = @At("RETURN"), cancellable = true)
    public void getConfiguredResource(Identifier id, CallbackInfoReturnable<Optional<Resource>> cir) {
        ConfiguredData data = ConfiguredData.get(id);
        if (data == null || !data.enabled.get() || (cir.getReturnValue().isPresent() && cir.getReturnValue().get().getPack() instanceof ConfiguredDataResourcePack))
            return;

        cir.setReturnValue(Optional.of(readAndApply(cir.getReturnValue(), data)));
    }

    @Inject(method = "getAllResources", at = @At("RETURN"), cancellable = true)
    public void getAllConfiguredResource(Identifier id, CallbackInfoReturnable<List<Resource>> cir) {
        ConfiguredData data = ConfiguredData.get(id);
        if (data == null || !data.enabled.get()) return;

        List<Resource> result = cir.getReturnValue().stream()
                .map(resource -> readAndApply(resource, data)).toList();
        cir.setReturnValue(result);
    }

    @Inject(method = "findResources", at = @At("RETURN"), cancellable = true)
    public void findConfiguredResources(String startingPath, Predicate<Identifier> allowedPathPredicate,
                                        CallbackInfoReturnable<Map<Identifier, Resource>> cir) {

        for (ConfiguredData data : ConfiguredData.INSTANCES) {
            if (data.enabled.get() && data.target.getPath().startsWith(startingPath + "/") && allowedPathPredicate.test(data.target)) {
                if (!cir.getReturnValue().containsKey(data.target)) {
                    cir.getReturnValue().put(data.target, readAndApply(Optional.empty(), data));
                }
            }
        }

        List<Identifier> ids = cir.getReturnValue().keySet().stream().toList();
        for (Identifier id : ids) {
            ConfiguredData data = ConfiguredData.get(id);
            if (data == null || !data.enabled.get()) continue;

            cir.getReturnValue().replace(id, readAndApply(cir.getReturnValue().get(id), data));
        }
    }

    @Inject(method = "findAllResources", at = @At("RETURN"), cancellable = true)
    public void findAllConfiguredResources(String startingPath, Predicate<Identifier> allowedPathPredicate,
                                           CallbackInfoReturnable<Map<Identifier, List<Resource>>> cir) {

        for (ConfiguredData data : ConfiguredData.INSTANCES) {
            if (data.enabled.get() && data.target.getPath().startsWith(startingPath) && allowedPathPredicate.test(data.target)) {
                if (!cir.getReturnValue().containsKey(data.target)) {
                    cir.getReturnValue().put(data.target, List.of(readAndApply(Optional.empty(), data)));
                }
            }
        }

        List<Identifier> ids = cir.getReturnValue().keySet().stream().toList();
        for (Identifier id : ids) {
            ConfiguredData data = ConfiguredData.get(id);
            if (data == null || !data.enabled.get()) continue;

            cir.getReturnValue().replace(id, cir.getReturnValue().get(id).stream()
                    .map(resource -> readAndApply(resource, data)).toList());
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void reloadConfigs(ResourceType type, List<ResourcePack> packs, CallbackInfo ci) {
        ModConfig.register();
    }
}
