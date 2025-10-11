package net.lyof.phantasm.mixin;

import net.lyof.phantasm.world.structure.VariantStructure;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.OceanRuinStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(OceanRuinStructure.class)
public class OceanRuinStructureMixin implements VariantStructure {
    @Unique private String variant = "";

    @Override
    public void setVariant(String value) {
        this.variant = value;
    }

    @Override
    public String getVariant() {
        return this.variant;
    }
}
