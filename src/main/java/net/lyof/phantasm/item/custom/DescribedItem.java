package net.lyof.phantasm.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DescribedItem extends Item {
    public DescribedItem(Settings settings) {
        super(settings);
    }

    protected String descKey;

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        String[] txt = Text.translatable(this.getOrCreateDescTranslationKey()).getString().split("\\n");
        for (String t : txt)
            tooltip.add(Text.literal(t).formatted(Formatting.GRAY));

        super.appendTooltip(stack, world, tooltip, context);
    }

    public String getOrCreateDescTranslationKey() {
        if (this.descKey == null)
            this.descKey = this.getOrCreateTranslationKey() + ".desc";
        return this.descKey;
    }
}
