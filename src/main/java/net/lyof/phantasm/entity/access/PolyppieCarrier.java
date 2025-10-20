package net.lyof.phantasm.entity.access;

import net.lyof.phantasm.entity.custom.PolyppieEntity;
import org.jetbrains.annotations.Nullable;

public interface PolyppieCarrier {
    void setCarriedPolyppie(@Nullable PolyppieEntity polyppie);
    @Nullable PolyppieEntity getCarriedPolyppie();
}
