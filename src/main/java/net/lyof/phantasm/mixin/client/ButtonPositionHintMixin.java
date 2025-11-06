package net.lyof.phantasm.mixin.client;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.mixin.access.HandledScreenAccessor;
import net.lyof.phantasm.screen.custom.PolyppieInventory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.Widget;
import org.anti_ad.mc.ipnext.integration.ButtonPositionHint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ButtonPositionHint.class, remap = false)
public class ButtonPositionHintMixin {
    @Shadow private int bottom;

    @Inject(method = "getBottom", at = @At("HEAD"), cancellable = true)
    private void fixBottom(CallbackInfoReturnable<Integer> cir) {
        if (
            MinecraftClient.getInstance().currentScreen instanceof HandledScreen<?> handled
            && ((HandledScreenAccessor) handled).getHandler() instanceof PolyppieInventory.Handler handler
            && handler.phantasm_isOpen()
        ) {
            cir.setReturnValue(this.bottom + 22);
        }
    }
}
