package net.lyof.phantasm.mixin.client;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.mixin.access.HandledScreenAccessor;
import net.lyof.phantasm.screen.custom.PolyppieInventory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClickableWidget.class)
public class ClickableWidgetMixin {
    @Shadow private int y;

    @Inject(method = "getY", at = @At("HEAD"), cancellable = true)
    private void fixBottom(CallbackInfoReturnable<Integer> cir) {
        Phantasm.log("hi");
        if (
            MinecraftClient.getInstance().currentScreen instanceof HandledScreen<?> handled
            && ((HandledScreenAccessor) handled).getHandler() instanceof PolyppieInventory.Handler handler
            && handler.phantasm_isOpen()
        ) {
            Phantasm.log("Moving " + this);
            cir.setReturnValue(this.y - 11);
        }
    }
}
