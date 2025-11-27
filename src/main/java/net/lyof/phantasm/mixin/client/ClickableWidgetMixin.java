package net.lyof.phantasm.mixin.client;

import net.lyof.phantasm.mixin.access.HandledScreenAccessor;
import net.lyof.phantasm.screen.access.PolyppieInventory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClickableWidget.class)
public abstract class ClickableWidgetMixin {
    @Shadow private int y;
    @Shadow private int x;

    /**
     * @author Lyof (End's Phantasm)
     * @reason No idea why but regular mixins do not work
     */
    @Overwrite
    public int getY() {
        if (
            MinecraftClient.getInstance().currentScreen instanceof HandledScreen<?> handled
            && handled instanceof HandledScreenAccessor accessor
            && accessor.getHandler() instanceof PolyppieInventory.Handler handler
            && handler.phantasm_isVisible()
            && accessor.getX() <= this.x && accessor.getX() + accessor.getBackgroundWidth() >= this.x
            && accessor.getY() <= this.y && accessor.getY() + accessor.getBackgroundHeight() >= this.y
        ) {
            return this.y - 11;
        }
        return this.y;
    }
}
