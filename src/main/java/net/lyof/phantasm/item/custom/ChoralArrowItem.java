package net.lyof.phantasm.item.custom;

import net.lyof.phantasm.entity.custom.ChoralArrowEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChoralArrowItem extends ArrowItem {
    public ChoralArrowItem(Settings settings) {
        super(settings);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return ChoralArrowEntity.create(world, shooter);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(Text.translatable("item.phantasm.choral_arrow.desc.bow").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.phantasm.choral_arrow.desc.crossbow").formatted(Formatting.GRAY));
    }
}
