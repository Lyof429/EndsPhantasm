package net.lyof.phantasm.world.feature.custom.tree;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.mixin.access.FoliagePlacerTypeInvoker;
import net.lyof.phantasm.mixin.access.TrunkPlacerTypeInvoker;
import net.lyof.phantasm.world.feature.custom.tree.custom.PreamFoliagePlacer;
import net.lyof.phantasm.world.feature.custom.tree.custom.PreamTrunkPlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

public class ModTreePlacerTypes {
    public static void register() {
        Phantasm.log("Registering Trunk and Foliage Placers for modid : " + Phantasm.MOD_ID);
    }


    public static final TrunkPlacerType<?> PREAM_TRUNK_PLACER = TrunkPlacerTypeInvoker.callRegister("pream_trunk_placer",
            PreamTrunkPlacer.CODEC);

    public static final FoliagePlacerType<?> PREAM_FOLIAGE_PLACER = FoliagePlacerTypeInvoker.callRegister("pream_foliage_placer",
            PreamFoliagePlacer.CODEC);
}
