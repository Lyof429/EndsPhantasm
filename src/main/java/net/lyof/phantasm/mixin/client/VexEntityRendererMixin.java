package net.lyof.phantasm.mixin.client;

import net.lyof.phantasm.Phantasm;
import net.minecraft.client.render.entity.VexEntityRenderer;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VexEntityRenderer.class)
public class VexEntityRendererMixin {
    private static final Identifier TEXTURE = Phantasm.makeID("textures/entity/ender_vex.png");
    private static final Identifier CHARGING_TEXTURE = Phantasm.makeID("textures/entity/ender_vex_charging.png");

    @Inject(method = "getTexture(Lnet/minecraft/entity/mob/VexEntity;)Lnet/minecraft/util/Identifier;", at = @At("HEAD"), cancellable = true)
    public void enderTowerTexture(VexEntity vex, CallbackInfoReturnable<Identifier> cir) {
        if (vex.getWorld().getRegistryKey().getValue().toString().equals("minecraft:the_end"))
            cir.setReturnValue(vex.isCharging() ? CHARGING_TEXTURE : TEXTURE);
    }
}
