package net.lyof.phantasm.item.custom;

import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class ChorusFruitSaladItem extends DescribedItem {
    public ChorusFruitSaladItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (world instanceof ServerWorld server && user.canUsePortals() && !user.isSneaking() && ConfigEntries.chorusSaladTp) {
            RegistryKey<World> registryKey = world.getRegistryKey() == World.END ? World.OVERWORLD : World.END;
            ServerWorld serverWorld = server.getServer().getWorld(registryKey);
            if (serverWorld == null) {
                return super.finishUsing(stack, world, user);
            }
            user.moveToWorld(serverWorld);
        }

        super.finishUsing(stack, world, user);
        if (user instanceof PlayerEntity player) {
            if (stack.isEmpty()) {
                if (player.getInventory().contains(this.getRecipeRemainder(stack)))
                    player.giveItemStack(this.getRecipeRemainder(stack));
                else
                    return this.getRecipeRemainder(stack);
            }
            else if (!player.isCreative())
                player.giveItemStack(this.getRecipeRemainder(stack));
        }
        return stack;
    }
}
