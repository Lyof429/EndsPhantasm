package net.lyof.phantasm.mixin;

import net.lyof.phantasm.setup.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusPlantBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChorusPlantBlock.class)
public class ChorusPlantBlockMixin {
    @Redirect(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    public boolean canPlaceAtNihilium(BlockState instance, Block block) {
        if (block == Blocks.END_STONE)
            return instance.isIn(ModTags.Blocks.END_PLANTS_GROWABLE_ON);
        return instance.isOf(block);
    }

    @Redirect(method = "getStateForNeighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    public boolean getStateForNeighborUpdateNihilium(BlockState instance, Block block) {
        if (block == Blocks.END_STONE)
            return instance.isIn(ModTags.Blocks.END_PLANTS_GROWABLE_ON);
        return instance.isOf(block);
    }

    @Redirect(method = "withConnectionProperties", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    public boolean withConnectionPropertiesNihilium(BlockState instance, Block block) {
        if (block == Blocks.END_STONE)
            return instance.isIn(ModTags.Blocks.END_PLANTS_GROWABLE_ON);
        return instance.isOf(block);
    }
}
