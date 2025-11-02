package net.lyof.phantasm.setup.compat;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.item.ModItems;
import net.lyof.phantasm.item.ModTiers;
import net.lyof.phantasm.setup.ModRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import vectorwing.farmersdelight.common.block.CabinetBlock;
import vectorwing.farmersdelight.common.item.KnifeItem;
import vectorwing.farmersdelight.common.registry.ModCreativeTabs;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.function.Supplier;

public class FarmersDelightCompat {
    public static void setup() {
        ModRegistry.Foods.EGGS_NIHILO = new FoodComponent.Builder().hunger(5).saturationModifier(0.8f)
                .statusEffect(new StatusEffectInstance(ModEffects.NOURISHMENT.get(), 1200, 0), 1)
                .build();
    }

    public static void register() {
        ModItems.CRYSTALLINE_KNIFE = ModRegistry.ofItem("crystalline_knife",
                new KnifeItem(ModTiers.CRYSTALLINE, 1.5f, -2f, new FabricItemSettings())).build();

        ModBlocks.PREAM_CABINET = ModRegistry.ofBlock("pream_cabinet",
                new CabinetBlock(FabricBlockSettings.copyOf(Blocks.BARREL).mapColor(MapColor.TERRACOTTA_YELLOW))).build();

        ItemGroupEvents.modifyEntriesEvent(getKey(ModCreativeTabs.TAB_FARMERS_DELIGHT)).register(entries -> {
            entries.addAfter(vectorwing.farmersdelight.common.registry.ModItems.IRON_KNIFE.get(),
                    ModItems.CRYSTALLINE_KNIFE);
            entries.addAfter(vectorwing.farmersdelight.common.registry.ModBlocks.WARPED_CABINET.get(),
                    ModBlocks.PREAM_CABINET);
        });
    }

    protected static RegistryKey<ItemGroup> getKey(Supplier<ItemGroup> tab) {
        return Registries.ITEM_GROUP.getKey(tab.get()).get();
    }
}
