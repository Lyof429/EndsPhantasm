package net.lyof.phantasm.item.custom;

import net.fabricmc.loader.api.FabricLoader;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.setup.compat.FarmersDelightCompat;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.FeastBlock;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;

public class EggsNihiloBlockItem extends BlockItem {
    public EggsNihiloBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    protected String descKey;

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        if (Phantasm.isFarmersDelight())
            FarmersDelightCompat.appendTooltip(stack, tooltip, 1);

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
