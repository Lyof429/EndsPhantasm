package net.lyof.phantasm.mixin;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.world.structure.IdentifierAware;
import net.lyof.phantasm.world.structure.VariantStructure;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.OceanRuinStructure;
import net.minecraft.world.gen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OceanRuinStructure.class)
public class OceanRuinStructureMixin implements VariantStructure, IdentifierAware {
    @Unique private String variant = "";
    @Unique private Identifier id = null;

    @Override
    public void setVariant(String value) {
        this.variant = value;
    }

    @Override
    public String getVariant() {
        return this.variant;
    }

    @Override
    public void setIdentifier(Identifier value) {
        this.id = value;
    }

    @Override
    public Identifier getIdentifier() {
        return this.id;
    }


    @Inject(method = "addPieces", at = @At("HEAD"))
    private void addVariantPieces(StructurePiecesCollector collector, Structure.Context context, CallbackInfo ci) {
        Phantasm.log(this.getVariant() + " " + this.getIdentifier());
    }
}
