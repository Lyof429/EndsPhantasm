package net.lyof.phantasm.item.custom;

import net.lyof.phantasm.entity.access.Corrosive;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PombSliceItem extends DescribedItem {
    public PombSliceItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof Corrosive corrosive) corrosive.setCorrosiveTicks(60);
        return super.finishUsing(stack, world, user);
    }
}
