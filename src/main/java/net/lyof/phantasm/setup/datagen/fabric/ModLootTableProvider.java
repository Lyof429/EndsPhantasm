package net.lyof.phantasm.setup.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.item.ModItems;
import net.lyof.phantasm.setup.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.GroupEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;

import java.util.Map;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }


    public void addSelfDropOr(Block self, ItemConvertible loot) {
        addSelfDropOr(self, loot, ConstantLootNumberProvider.create(1));
    }

    public void addSelfDropOr(Block self, ItemConvertible loot, LootNumberProvider amount) {
        addDrop(self, dropsWithSilkTouch(self,
                new GroupEntry.Builder()).pool(LootPool.builder().conditionally(WITHOUT_SILK_TOUCH)
                        .rolls(amount)
                        .with(ItemEntry.builder(loot))));
    }


    @Override
    public void generate() {
        for (Map.Entry<Block, ItemConvertible> entry : ModRegistry.BLOCK_DROPS.entrySet()) {
            if (ModRegistry.BLOCK_MODELS.get(ModRegistry.Models.SLAB).contains(entry.getKey()))
                addDrop(entry.getKey(), slabDrops(entry.getKey()));
            else if (ModRegistry.BLOCK_MODELS.get(ModRegistry.Models.DOOR).contains(entry.getKey()))
                addDrop(entry.getKey(), doorDrops(entry.getKey()));
            else
                addDrop(entry.getKey(), entry.getValue());
        }


        addDrop(ModBlocks.PREAM_LEAVES, leavesDrops(ModBlocks.PREAM_LEAVES, ModBlocks.PREAM_SAPLING, 0.1f));
        addDrop(ModBlocks.HANGING_PREAM_LEAVES, dropsWithSilkTouchOrShears(ModBlocks.HANGING_PREAM_LEAVES,
                new GroupEntry.Builder()));

        addSelfDropOr(ModBlocks.VIVID_NIHILIUM, Items.END_STONE);
        addSelfDropOr(ModBlocks.ACIDIC_NIHILIUM, Items.END_STONE);

        addDrop(ModBlocks.VIVID_NIHILIS, dropsWithShears(ModBlocks.VIVID_NIHILIS));
        addDrop(ModBlocks.TALL_VIVID_NIHILIS, dropsWithShears(ModBlocks.TALL_VIVID_NIHILIS));
        addDrop(ModBlocks.ACIDIC_NIHILIS, dropsWithShears(ModBlocks.ACIDIC_NIHILIS));
        addDrop(ModBlocks.TALL_ACIDIC_NIHILIS, dropsWithShears(ModBlocks.TALL_ACIDIC_NIHILIS));

        addDrop(ModBlocks.DRAGON_MINT, dropsWithShears(ModBlocks.DRAGON_MINT));

        addSelfDropOr(ModBlocks.CIRITE_IRON_ORE, Items.IRON_NUGGET, UniformLootNumberProvider.create(1, 4));
        addDrop(ModBlocks.RAW_PURPUR_COAL_ORE,
                dropsWithSilkTouch(ModBlocks.RAW_PURPUR_COAL_ORE,
                        applyExplosionDecay(ModBlocks.RAW_PURPUR_COAL_ORE, ItemEntry.builder(Items.COAL)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2, 4)))
                                .apply(ApplyBonusLootFunction.uniformBonusCount(Enchantments.FORTUNE)))));
    }
}
