package net.lyof.phantasm.setup.compat;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.item.ModItems;
import net.lyof.phantasm.item.ModTiers;
import net.lyof.phantasm.setup.ModRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.registry.Registries;
import vectorwing.farmersdelight.common.item.KnifeItem;
import vectorwing.farmersdelight.common.registry.ModCreativeTabs;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class FarmersDelightCompat {
    public static void setup() {
        ModRegistry.Foods.EGGS_NIHILO = new FoodComponent.Builder().hunger(5).saturationModifier(0.8f)
                .statusEffect(new StatusEffectInstance(ModEffects.NOURISHMENT.get(), 1200, 0), 1)
                .build();
    }

    public static void register() {
        ModItems.CRYSTALLINE_KNIFE = ModRegistry.ofItem("crystalline_knife",
                new KnifeItem(ModTiers.CRYSTALLINE, 1.5f, -2f, new FabricItemSettings())).build();

        ItemGroupEvents.modifyEntriesEvent(Registries.ITEM_GROUP.getKey(ModCreativeTabs.TAB_FARMERS_DELIGHT.get()).get()).register(entries -> {

            entries.addAfter(vectorwing.farmersdelight.common.registry.ModItems.IRON_KNIFE.get(),
                    ModItems.CRYSTALLINE_KNIFE);
        });
    }
}
