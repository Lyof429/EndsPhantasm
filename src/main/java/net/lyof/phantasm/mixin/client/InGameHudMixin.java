package net.lyof.phantasm.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.entity.access.Corrosive;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Unique private static final Identifier VANILLA_ICONS = new Identifier("textures/gui/icons.png");
    @Unique private static final Identifier VANILLA_WIDGETS = new Identifier("textures/gui/widgets.png");

    @Unique private static final Identifier CORROSION_ARMOR = Phantasm.makeID("textures/gui/corrosion_armor.png");
    @Unique private static final Identifier CORROSION_ATTACK = Phantasm.makeID("textures/gui/corrosion_attack_indicator.png");
    @Unique private static final Identifier CORROSION_HOTBAR = Phantasm.makeID("textures/gui/corrosion_hotbar.png");

    @WrapOperation(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"))
    public void renderCorrodedArmor(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height,
                                    Operation<Void> original) {

        if (v == 9 && (u == 16 || u == 25 || u == 34) && texture.equals(VANILLA_ICONS)
                && this.getCameraPlayer().hasStatusEffect(ModEffects.CORROSION)) {

            int offset = (int) Math.round(Math.pow(Math.sin((x + this.getCameraPlayer().getWorld().getTimeOfDay()) / 2f), 2));
            instance.drawTexture(CORROSION_ARMOR, x, y + offset, 0, 0, 9, 9, 9, 9);
        }
        else
            original.call(instance, texture, x, y, u, v, width, height);
    }

    @WrapOperation(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"))
    public void renderCorrosiveCrosshair(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height,
                                         Operation<Void> original) {

        if (v == 94 && (u == 52 || u == 68) && texture.equals(VANILLA_ICONS)
                && this.getCameraPlayer() instanceof Corrosive corrosive && corrosive.isCorrosive()) {

            instance.drawTexture(CORROSION_ATTACK, x, y, u - 52, 18, width, 16, 32, 32);
        }
        else
            original.call(instance, texture, x, y, u, v, width, height);
    }

    @WrapOperation(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"))
    public void renderCorrosiveHotbar(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height,
                                      Operation<Void> original, @Local(ordinal = 2) int p) {

        if (this.getCameraPlayer() instanceof Corrosive corrosive && corrosive.isCorrosive()) {
            if (texture.equals(VANILLA_WIDGETS) && u == 0 && v == 22)
                instance.drawTexture(CORROSION_HOTBAR, x - 4, y - 4, 0, 0, 32, 32, 32, 32);
            else if (texture.equals(VANILLA_ICONS) && u == 18 && v == 112 - p)
                instance.drawTexture(CORROSION_ATTACK, x, y, 0, v - 94, width, height, 32, 32);

            else original.call(instance, texture, x, y, u, v, width, height);
        }
        else original.call(instance, texture, x, y, u, v, width, height);
    }
}
