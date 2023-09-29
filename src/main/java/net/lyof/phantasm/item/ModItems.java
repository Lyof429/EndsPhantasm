package net.lyof.phantasm.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.lyof.phantasm.ModRegistry;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;

public class ModItems {
    public static void register() {
        Phantasm.log("Registering Items for modid : " + Phantasm.MOD_ID);

        if (false) return;
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.add(PREAM_BERRY);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.add(ModBlocks.FALLEN_STAR);
        });
    }


    public static final Item PREAM_BERRY = ModRegistry.ofItem("pream_berry",
            new Item(new FabricItemSettings()
                    .food(new FoodComponent.Builder().alwaysEdible().hunger(4).statusEffect(
                            new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 0, true, false),
                            1)
                    .build()))).build();
}
