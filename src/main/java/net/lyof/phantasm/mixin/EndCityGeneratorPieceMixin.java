package net.lyof.phantasm.mixin;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.block.Block;
import net.minecraft.structure.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EndCityGenerator.Piece.class)
public abstract class EndCityGeneratorPieceMixin extends SimpleStructurePiece {
    @Unique private static final Identifier CHALLENGE_ID = Phantasm.makeID("elytra");
    @Unique private static final Identifier STRUCTURE_ID = Identifier.of("minecraft", "end_city/ship");

    @Shadow protected abstract Identifier getId();

    public EndCityGeneratorPieceMixin(StructurePieceType type, int length, StructureTemplateManager structureTemplateManager, Identifier id, String template, StructurePlacementData placementData, BlockPos pos) {
        super(type, length, structureTemplateManager, id, template, placementData, pos);
    }

    @Inject(method = "handleMetadata", at = @At("HEAD"), cancellable = true)
    public void placeElytraChallenge(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox, CallbackInfo ci) {
        if (ConfigEntries.elytraChallenge && metadata.startsWith("Elytra") && this.getId().equals(STRUCTURE_ID)) {
            if (ConfigEntries.elytraChallengeOffset.size() < 3) ConfigEntries.elytraChallengeOffset = List.of(0d, 2d, 8d);

            Direction axis = this.placementData.getRotation().rotate(Direction.SOUTH);
            pos = pos.offset(axis.rotateYClockwise(), ConfigEntries.elytraChallengeOffset.get(0).intValue())
                    .down(ConfigEntries.elytraChallengeOffset.get(1).intValue())
                    .offset(axis, ConfigEntries.elytraChallengeOffset.get(2).intValue());
            world.setBlockState(pos, ModBlocks.CHALLENGE_RUNE.getDefaultState(), Block.NOTIFY_ALL);

            if (world.getBlockEntity(pos) instanceof ChallengeRuneBlockEntity rune) {
                rune.setChallenge(CHALLENGE_ID);
            }

            ci.cancel();
        }
    }
}
