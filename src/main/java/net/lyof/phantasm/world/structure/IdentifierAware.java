package net.lyof.phantasm.world.structure;

import net.minecraft.util.Identifier;

public interface IdentifierAware {
    void setIdentifier(Identifier value);
    Identifier getIdentifier();
}
