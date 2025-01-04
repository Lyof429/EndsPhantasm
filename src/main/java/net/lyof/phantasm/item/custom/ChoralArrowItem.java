package net.lyof.phantasm.item.custom;

import net.lyof.phantasm.entity.custom.ChoralArrowEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ChoralArrowItem extends ArrowItem {
    public ChoralArrowItem(Settings settings) {
        super(settings);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new ChoralArrowEntity(world, shooter);
    }
}
