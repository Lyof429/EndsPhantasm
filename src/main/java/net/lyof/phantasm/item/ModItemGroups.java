package net.lyof.phantasm.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public class ModItemGroups {
    public static final ItemGroup PHANTASM = register(Phantasm.MOD_ID,
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.phantasm"))
                    .icon(() -> new ItemStack(ModBlocks.FALLEN_STAR))
                    .entries((displayContext, entries) -> {
                        if (false) return;

                        //for (Item item : ModRegistry.ITEMS)
                        //    entries.add(item);

                        entries.add(ModBlocks.FALLEN_STAR);
                        entries.add(ModItems.PREAM_BERRY);

                    })
                    .build());


    public static ItemGroup register(String id, ItemGroup tab) {
        return Registry.register(Registries.ITEM_GROUP, Phantasm.makeID(id), tab);
    }

    public static void register() {
        Phantasm.log("Registering ItemGroups for modid : " + Phantasm.MOD_ID);
    }
}
