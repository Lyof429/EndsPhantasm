package net.lyof.phantasm.config;

public class ConfigEntries {
    public static void reload() {
        doDreamingDenBiome = new ConfigEntry<>("world_gen.biomes.dreaming_den.generate", true).get();
        dreamingDenWeight = new ConfigEntry<>("world_gen.biomes.dreaming_den.generation_weight", 2.5).get();

        doCrystalSpikes = new ConfigEntry<>("world_gen.biomes.dreaming_den.do_crystal_spikes", true).get();
        doPreamTrees = new ConfigEntry<>("world_gen.biomes.dreaming_den.do_pream_trees", true).get();

        doAcidburntAbyssesBiome = new ConfigEntry<>("world_gen.biomes.acidburnt_abysses.generate", true).get();
        acidburntAbyssesWeight = new ConfigEntry<>("world_gen.biomes.acidburnt_abysses.generation_weight", 2.5).get();

        doFallenStars = new ConfigEntry<>("world_gen.do_fallen_stars", true).get();
        doRawPurpur = new ConfigEntry<>("world_gen.do_raw_purpur", true).get();

        improveEndSpires = new ConfigEntry<>("world_gen.improve_end_spires", true).get();
        noCrystalCages = new ConfigEntry<>("world_gen.no_crystal_cages", true).get();

        outerEndIntegration = new ConfigEntry<>("gameplay.outer_end_in_gameloop", true).get();

        behemothAggroRange = new ConfigEntry<>("gameplay.behemoth.aggro_range", 6).get();
        behemothAggroRangeSneaking = new ConfigEntry<>("gameplay.behemoth.aggro_range_sneaking", 0).get();

        explosiveDragonFireballs = new ConfigEntry<>("gameplay.dragon.explosive_fireballs", true).get();
        noEndermenFight = new ConfigEntry<>("gameplay.dragon.no_pesky_endermen", false).get();

        elytraBoostAdvancement = new ConfigEntry<>("equipment.elytra_boost_advancement", "minecraft:end/kill_dragon").get();
        crystalXPBoost = new ConfigEntry<>("equipment.crystal_xp_boost", 3).get();
        chorusSaladTp = new ConfigEntry<>("equipment.chorus_fruit_salad_teleportation", true).get();
        chorusSaladStack = new ConfigEntry<>("equipment.chorus_fruit_salad_stack_size", 1).get();

        //voidEyeDurability = new ConfigEntry<>("equipment.void_eye_durability", 4).get();
    }

    public static boolean doDreamingDenBiome;
    public static double dreamingDenWeight;

    public static boolean doCrystalSpikes;
    public static boolean doPreamTrees;

    public static boolean doAcidburntAbyssesBiome;
    public static double acidburntAbyssesWeight;

    public static boolean doFallenStars;
    public static boolean doRawPurpur;

    public static boolean improveEndSpires;
    public static boolean noCrystalCages;

    public static boolean outerEndIntegration;

    public static int behemothAggroRange;
    public static int behemothAggroRangeSneaking;

    public static boolean explosiveDragonFireballs;
    public static boolean noEndermenFight;

    public static String elytraBoostAdvancement = "";
    public static double crystalXPBoost;
    public static boolean chorusSaladTp;
    public static int chorusSaladStack;

    public static int voidEyeDurability = 4;
}
