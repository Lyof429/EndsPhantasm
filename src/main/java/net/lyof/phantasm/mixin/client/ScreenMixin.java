package net.lyof.phantasm.mixin.client;

import net.fabricmc.fabric.mixin.screen.ScreenAccessor;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.mixin.access.ClickableWidgetAccessor;
import net.lyof.phantasm.mixin.access.HandledScreenAccessor;
import net.lyof.phantasm.screen.access.PolyppieInventory;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "addDrawableChild", at = @At("HEAD"))
    private <T extends Element & Drawable & Selectable> void polyppiePositionFix(T element, CallbackInfoReturnable<T> cir) {
        if (
            (Screen) (Object) this instanceof HandledScreen<?> handled
            && ((HandledScreenAccessor) handled).getHandler() instanceof PolyppieInventory.Handler handler
            && handler.phantasm_isOpen()
            && element instanceof ClickableWidgetAccessor accessor
        ) {
            accessor.setY(accessor.getY() - 11);
        }
    }
}
