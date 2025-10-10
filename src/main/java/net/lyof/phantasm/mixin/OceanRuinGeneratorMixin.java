package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.world.structure.VariantStructure;
import net.minecraft.structure.*;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.structure.OceanRuinStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OceanRuinGenerator.class)
public class OceanRuinGeneratorMixin {
    @Unique private static final Identifier[] PIECES = new Identifier[]{
            Phantasm.makeID("acidburnt_ruin/sulphurs_1"),
            Phantasm.makeID("acidburnt_ruin/sulphurs_2"),
            Phantasm.makeID("acidburnt_ruin/sulphurs_3")};

    @Inject(method = "addPieces(Lnet/minecraft/structure/StructureTemplateManager;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/BlockRotation;Lnet/minecraft/structure/StructurePiecesHolder;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/world/gen/structure/OceanRuinStructure;ZF)V",
            at = @At("HEAD"), cancellable = true)
    private static void addVariantPieces(StructureTemplateManager manager, BlockPos pos, BlockRotation rotation,
                                         StructurePiecesHolder holder, Random random, OceanRuinStructure structure, boolean large,
                                         float integrity, CallbackInfo ci) {

        if (structure instanceof VariantStructure variant && variant.getVariant().equals("acidburnt")) {
            for (int i = 0; i < random.nextBetween(2, 5); i++)
                holder.addPiece(new OceanRuinGenerator.Piece(manager, Util.getRandom(PIECES, random), pos, rotation, 0.95f, structure.biomeTemperature, large));

            ci.cancel();
        }
    }

    @Mixin(OceanRuinGenerator.Piece.class)
    public abstract static class PieceMixin extends SimpleStructurePiece {
        public PieceMixin(StructurePieceType type, int length, StructureTemplateManager structureTemplateManager, Identifier id, String template, StructurePlacementData placementData, BlockPos pos) {
            super(type, length, structureTemplateManager, id, template, placementData, pos);
        }

        @WrapWithCondition(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/structure/SimpleStructurePiece;generate(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/BlockPos;)V"))
        private boolean cancelVoidGeneration(SimpleStructurePiece instance, StructureWorldAccess world,
                                             StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random,
                                             BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {

            return this.pos.getY() > 4;
        }
    }
}
