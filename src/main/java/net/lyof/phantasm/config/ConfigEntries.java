package net.lyof.phantasm.config;

public class ConfigEntries {
    public static void reload() {
        dataCompatMode = new ConfigEntry<>("world_gen.biomes.datapack_compatibility", "automatic").get();
        forceMainIsland = new ConfigEntry<>("world_gen.biomes.force_main_island", true).get();

        doDreamingDenBiome = new ConfigEntry<>("world_gen.biomes.dreaming_den.generate", true).get();
        dreamingDenWeight = new ConfigEntry<>("world_gen.biomes.dreaming_den.generation_weight", 1.5).get();

        doAcidburntAbyssesBiome = new ConfigEntry<>("world_gen.biomes.acidburnt_abysses.generate", true).get();
        acidburntAbyssesWeight = new ConfigEntry<>("world_gen.biomes.acidburnt_abysses.generation_weight", 2.5).get();

        doFallenStars = new ConfigEntry<>("world_gen.do_fallen_stars", true).get();
        doRawPurpur = new ConfigEntry<>("world_gen.do_raw_purpur", true).get();
        rawPurpurOffset = new ConfigEntry<>("world_gen.raw_purpur_offset", 0).get();

        improveEndSpires = new ConfigEntry<>("world_gen.end_spires.beautify", true).get();
        noCrystalCages = new ConfigEntry<>("world_gen.end_spires.no_crystal_cages", false).get();
        extraSpiresHeight = new ConfigEntry<>("world_gen.end_spires.extra_height", 0).get();

        outerEndIntegration = new ConfigEntry<>("gameplay.outer_end_in_gameloop", true).get();

        behemothAggroRange = new ConfigEntry<>("gameplay.behemoth.aggro_range", 6).get();
        behemothAggroRangeSneaking = new ConfigEntry<>("gameplay.behemoth.aggro_range_sneaking", 0).get();

        explosiveDragonFireballs = new ConfigEntry<>("gameplay.dragon.explosive_fireballs", true).get();
        noEndermenFight = new ConfigEntry<>("gameplay.dragon.no_pesky_endermen", false).get();

        elytraBoostAdvancement = new ConfigEntry<>("equipment.elytra_boost_advancement", "minecraft:end/kill_dragon").get();
        crystalXPBoost = new ConfigEntry<>("equipment.crystal_xp_boost", 3).get();

        chorusSaladTp = new ConfigEntry<>("equipment.chorus_fruit_salad_teleportation", true).get();
        chorusSaladStack = new ConfigEntry<>("equipment.chorus_fruit_salad_stack_size", 1).get();

        shatteredPendantDurability = new ConfigEntry<>("equipment.shattered_pendant_durability", 4).get();
        subwooferRange = new ConfigEntry<>("equipment.subwoofer_range", 6).get();
    }

    public static String dataCompatMode = "";
    public static boolean forceMainIsland;

    public static boolean doDreamingDenBiome;
    public static double dreamingDenWeight;

    public static boolean doAcidburntAbyssesBiome;
    public static double acidburntAbyssesWeight;

    public static boolean doFallenStars;
    public static boolean doRawPurpur;
    public static int rawPurpurOffset;

    public static boolean improveEndSpires;
    public static boolean noCrystalCages;
    public static int extraSpiresHeight;

    public static boolean outerEndIntegration;

    public static int behemothAggroRange;
    public static int behemothAggroRangeSneaking;

    public static boolean explosiveDragonFireballs;
    public static boolean noEndermenFight;

    public static String elytraBoostAdvancement = "";
    public static double crystalXPBoost;
    public static boolean chorusSaladTp;
    public static int chorusSaladStack;

    public static int subwooferRange;

    public static int shatteredPendantDurability;
}
