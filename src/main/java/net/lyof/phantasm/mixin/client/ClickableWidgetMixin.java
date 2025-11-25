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

    /**
     * @author Lyof (End's Phantasm
     * @reason No idea why but regular mixins do not work
     */
    @Overwrite
    public int getY() {
        //Phantasm.log("Why");
        if (
            MinecraftClient.getInstance().currentScreen instanceof HandledScreen<?> handled
            && ((HandledScreenAccessor) handled).getHandler() instanceof PolyppieInventory.Handler handler
            && handler.phantasm_isVisible()
        ) {
            return this.y - 11;
        }
        return this.y;
    }
}
