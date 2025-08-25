package net.lyof.phantasm.mixin;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.world.structure.VariantStructure;
import net.minecraft.structure.OceanRuinGenerator;
import net.minecraft.structure.StructurePiecesHolder;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.structure.OceanRuinStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OceanRuinGenerator.class)
public class OceanRuinGeneratorMixin {
    @Unique private static final Identifier[] WARM_RUINS = new Identifier[]{Phantasm.makeID("underwater_ruin/warm_1"), Phantasm.makeID("underwater_ruin/warm_2"), Phantasm.makeID("underwater_ruin/warm_3"), Phantasm.makeID("underwater_ruin/warm_4"), Phantasm.makeID("underwater_ruin/warm_5"), Phantasm.makeID("underwater_ruin/warm_6"), Phantasm.makeID("underwater_ruin/warm_7"), Phantasm.makeID("underwater_ruin/warm_8")};
    @Unique private static final Identifier[] CRACKED_RUINS = new Identifier[]{Phantasm.makeID("underwater_ruin/cracked_1"), Phantasm.makeID("underwater_ruin/cracked_2"), Phantasm.makeID("underwater_ruin/cracked_3"), Phantasm.makeID("underwater_ruin/cracked_4"), Phantasm.makeID("underwater_ruin/cracked_5"), Phantasm.makeID("underwater_ruin/cracked_6"), Phantasm.makeID("underwater_ruin/cracked_7"), Phantasm.makeID("underwater_ruin/cracked_8")};
    @Unique private static final Identifier[] BIG_WARM_RUINS = new Identifier[]{Phantasm.makeID("underwater_ruin/big_warm_4"), Phantasm.makeID("underwater_ruin/big_warm_5"), Phantasm.makeID("underwater_ruin/big_warm_6"), Phantasm.makeID("underwater_ruin/big_warm_7")};


    @Inject(method = "addPieces(Lnet/minecraft/structure/StructureTemplateManager;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/BlockRotation;Lnet/minecraft/structure/StructurePiecesHolder;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/world/gen/structure/OceanRuinStructure;ZF)V",
            at = @At("HEAD"), cancellable = true)
    private static void addVariantPieces(StructureTemplateManager manager, BlockPos pos, BlockRotation rotation,
                                         StructurePiecesHolder holder, Random random, OceanRuinStructure structure, boolean large,
                                         float integrity, CallbackInfo ci) {

        if (structure instanceof VariantStructure variant && variant.getVariant().equals("acidburnt")) {
            holder.addPiece(new OceanRuinGenerator.Piece(manager, Util.getRandom(WARM_RUINS, random), pos, rotation, integrity, structure.biomeTemperature, large));
            holder.addPiece(new OceanRuinGenerator.Piece(manager, Util.getRandom(CRACKED_RUINS, random), pos, rotation, 0.7F, structure.biomeTemperature, large));
            holder.addPiece(new OceanRuinGenerator.Piece(manager, Util.getRandom(BIG_WARM_RUINS, random), pos, rotation, 0.5F, structure.biomeTemperature, large));

            ci.cancel();
        }
    }
}
