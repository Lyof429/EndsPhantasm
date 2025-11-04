package net.lyof.phantasm.entity.access;

import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface PolyppieCarrier {
    void phantasm_setPolyppie(@Nullable PolyppieEntity polyppie);
    @Nullable PolyppieEntity phantasm_getPolyppie();
}
