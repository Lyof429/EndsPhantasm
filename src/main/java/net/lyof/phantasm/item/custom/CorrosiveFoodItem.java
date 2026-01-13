package net.lyof.phantasm.item.custom;

import net.lyof.phantasm.entity.access.Corrosive;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CorrosiveFoodItem extends DescribedItem {
    public CorrosiveFoodItem(int corrosiveTicks, Settings settings) {
        super(settings);
        this.corrosiveTicks = corrosiveTicks;
    }

    private static final String DESC_KEY = "item.phantasm.pomb_slice.desc";
    protected int corrosiveTicks;

    @Override
    public String getOrCreateDescTranslationKey() {
        return DESC_KEY;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof Corrosive corrosive) corrosive.setCorrosiveTicks(this.corrosiveTicks);
        return super.finishUsing(stack, world, user);
    }
}
