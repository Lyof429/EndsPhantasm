package net.lyof.phantasm.item.custom;

import net.lyof.phantasm.Phantasm;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;

public class EggsNihiloBlockItem extends BlockItem {
    public EggsNihiloBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return this.place(new ItemPlacementContext(context));
    }

    protected String descKey;

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        if (Phantasm.isFarmersDelightLoaded())
            TextUtils.addFoodEffectTooltip(stack, tooltip, 1);

        String[] txt = Text.translatable(this.getOrCreateDescTranslationKey()).getString().split("\\n");
        for (String t : txt)
            tooltip.add(Text.literal(t).formatted(Formatting.GRAY));
    }

    public String getOrCreateDescTranslationKey() {
        if (this.descKey == null)
            this.descKey = this.getOrCreateTranslationKey() + ".desc";
        return this.descKey;
    }
}
