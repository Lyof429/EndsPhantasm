package net.lyof.phantasm.item.custom;

import net.lyof.phantasm.entity.extra.Corrosive;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PomeSliceItem extends DescribedItem {
    public PomeSliceItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof Corrosive corrosive) corrosive.setCorrosiveTicks(60);
        return super.finishUsing(stack, world, user);
    }
}
