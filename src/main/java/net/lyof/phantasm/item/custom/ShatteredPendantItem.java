package net.lyof.phantasm.item.custom;

import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShatteredPendantItem extends Item {
    public ShatteredPendantItem(Settings settings) {
        super(settings.rarity(Rarity.RARE).maxCount(1).maxDamage(ConfigEntries.voidEyeDurability).fireproof());
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0x349988;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    public static boolean canSeeSky(BlockPos pos, World world) {
        int top = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());
        return top <= pos.getY();
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity.age % 20 == 0 && entity.isOnGround() && canSeeSky(entity.getBlockPos(), world)) {
            stack.getOrCreateNbt().putInt("SavedX", entity.getBlockX());
            stack.getOrCreateNbt().putInt("SavedY", entity.getBlockY());
            stack.getOrCreateNbt().putInt("SavedZ", entity.getBlockZ());
            stack.getOrCreateNbt().putString("SavedDim", world.getRegistryKey().toString());
        }
        else if (entity instanceof LivingEntity living && entity.age % 20 == 0 && entity.getY() <= 0
                && world.getRegistryKey().getValue().toString().equals("minecraft:the_end")) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20, 0, true, false));
            this.finishUsing(stack, world, living);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return super.use(world, user, hand);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 30;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPYGLASS;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (nbt.getInt("SavedX") == 0 && nbt.getInt("SavedY") == 0 && nbt.getInt("SavedZ") == 0)
            return super.finishUsing(stack, world, user);

        if (world.getRegistryKey().toString().equals(nbt.getString("SavedDim"))) {
            user.fallDistance = 0;
            user.teleport(nbt.getInt("SavedX"), nbt.getInt("SavedY"), nbt.getInt("SavedZ"));
            if (user instanceof PlayerEntity player) {
                player.getItemCooldownManager().set(this, 200);
                stack.damage(1, player, playerEntity -> playerEntity.sendEquipmentBreakStatus(
                        user.getActiveHand() == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));
            }
            user.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1, 2);
        }
        return super.finishUsing(stack, world, user);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        String[] txt = Text.translatable("item.phantasm.shattered_pendant.desc").getString().split("\\n");
        for (String t : txt)
            tooltip.add(Text.literal(t));
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        super.usageTick(world, user, stack, remainingUseTicks);

        float sin = (float) Math.sin(remainingUseTicks * Math.PI / 10);
        float cos = (float) Math.cos(remainingUseTicks * Math.PI / 10);
        world.addParticle(ParticleTypes.END_ROD, user.getX() + sin, user.getEyeY() - 0.5, user.getZ() + cos, 0, 0, 0);
    }
}
