package net.lyof.phantasm.setup.compat;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.lyof.phantasm.item.ModItems;
import net.lyof.phantasm.item.ModTiers;
import net.lyof.phantasm.setup.ModRegistry;
import vectorwing.farmersdelight.common.item.KnifeItem;

public class FarmersDelightCompat {
    public static void register() {
        ModItems.CRYSTALLINE_KNIFE = ModRegistry.ofItem("crystalline_knife",
                new KnifeItem(ModTiers.CRYSTALLINE, 1.5f, -2f, new FabricItemSettings())).build();
    }
}
