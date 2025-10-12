package net.lyof.phantasm.mixin;

import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.BlockRotStructureProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRotStructureProcessor.class)
public class BlockRotStructureProcessorMixin {
    @Inject(method = "process", at = @At("HEAD"), cancellable = true)
    private void safeProcess(WorldView world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo original,
                             StructureTemplate.StructureBlockInfo current, StructurePlacementData data, CallbackInfoReturnable<StructureTemplate.StructureBlockInfo> cir) {
        if (world.getBlockEntity(current.pos()) instanceof LootableContainerBlockEntity)
            cir.setReturnValue(current);
    }
}
