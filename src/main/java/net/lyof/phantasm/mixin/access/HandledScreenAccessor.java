package net.lyof.phantasm.mixin.access;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HandledScreen.class)
public interface HandledScreenAccessor {
    @Accessor <T extends ScreenHandler> T getHandler();
    @Accessor("x") int getX();
    @Accessor("y") int getY();
    @Accessor int getBackgroundWidth();
    @Accessor int getBackgroundHeight();
}
