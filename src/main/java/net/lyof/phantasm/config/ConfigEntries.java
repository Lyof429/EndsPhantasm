package net.lyof.phantasm.config;

import net.lyof.phantasm.Phantasm;

public class ConfigEntries {
    public static void reload() {
        doDreamingDenBiome = new ConfigEntry<>("world_gen.biomes.dreaming_den.generate", true).get();
        dreamingDenWeight = new ConfigEntry<>("world_gen.biomes.dreaming_den.generation_weight", 2.5).get();

        doCrystalSpikes = new ConfigEntry<>("world_gen.biomes.dreaming_den.do_crystal_spikes", true).get();
        doPreamTrees = new ConfigEntry<>("world_gen.biomes.dreaming_den.do_pream_trees", true).get();
        doTallPreamTrees = new ConfigEntry<>("world_gen.biomes.dreaming_den.do_tall_pream_trees", true).get();

        doFallenStars = new ConfigEntry<>("world_gen.do_fallen_stars", true).get();
        doRawPurpur = new ConfigEntry<>("world_gen.do_raw_purpur", true).get();

        doRawPurpurCabins = new ConfigEntry<>("world_gen.structures.do_raw_purpur_cabins", true).get();
    }

    public static boolean doDreamingDenBiome;
    public static double dreamingDenWeight;

    public static boolean doCrystalSpikes;
    public static boolean doPreamTrees;
    public static boolean doTallPreamTrees;

    public static boolean doFallenStars;
    public static boolean doRawPurpur;

    public static boolean doRawPurpurCabins;
}
