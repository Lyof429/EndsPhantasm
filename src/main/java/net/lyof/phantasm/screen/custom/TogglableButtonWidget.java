package net.lyof.phantasm.screen.custom;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class TogglableButtonWidget extends TexturedButtonWidget {
    public TogglableButtonWidget(int x, int y, int width, int height, int u, int v, Identifier texture, Supplier<Boolean> toggle, PressAction pressAction) {
        super(x, y, width, height, u, v, texture, pressAction);
        this.toggled = toggle;
    }

    protected Supplier<Boolean> toggled;

    @Override
    public void drawTexture(DrawContext context, Identifier texture, int x, int y, int u, int v, int hoveredVOffset, int width, int height, int textureWidth, int textureHeight) {
        if (!this.active) return;
        
        this.setFocused(false);
        super.drawTexture(context, texture, x, y, this.toggled.get() ? u + this.width : u, v, hoveredVOffset, width, height,
                textureWidth, textureHeight);
    }
}
