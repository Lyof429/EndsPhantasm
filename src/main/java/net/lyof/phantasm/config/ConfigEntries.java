package net.lyof.phantasm.config;

import net.lyof.phantasm.block.challenge.Challenge;

import java.util.List;

public class ConfigEntries {
    public static void reload() {
        dataCompatMode = new ConfigEntry<>("world_gen.biomes.datapack_compatibility", "automatic").get();
        customBiomesWeight = new ConfigEntry<>("world_gen.biomes.custom_biomes_weight", 0.6).get();
        noiseScale = new ConfigEntry<>("world_gen.biomes.noise_scale", 2).get();

        doDreamingDenBiome = new ConfigEntry<>("world_gen.biomes.dreaming_den.enabled", true).get();
        dreamingDenWeight = new ConfigEntry<>("world_gen.biomes.dreaming_den.generation_weight", 1.5).get();

        doAcidburntAbyssesBiome = new ConfigEntry<>("world_gen.biomes.acidburnt_abysses.enabled", true).get();
        acidburntAbyssesWeight = new ConfigEntry<>("world_gen.biomes.acidburnt_abysses.generation_weight", 1.2).get();

        doFallenStars = new ConfigEntry<>("world_gen.do_fallen_stars", true).get();
        doRawPurpur = new ConfigEntry<>("world_gen.do_raw_purpur", true).get();
        rawPurpurOffset = new ConfigEntry<>("world_gen.raw_purpur_offset", 10).get();

        improveEndSpires = new ConfigEntry<>("world_gen.end_spires.beautify", true).get();
        noCrystalCages = new ConfigEntry<>("world_gen.end_spires.no_crystal_cages", false).get();
        extraSpiresHeight = new ConfigEntry<>("world_gen.end_spires.extra_height", 0).get();

        outerEndIntegration = new ConfigEntry<>("gameplay.outer_end_in_gameloop", true).get();
        beginCutscene = outerEndIntegration && new ConfigEntry<>("gameplay.cutscene", true).get();
        darkEnd = new ConfigEntry<>("gameplay.dark_end", false).get();

        behemothAggroRange = new ConfigEntry<>("gameplay.behemoth.aggro_range", 6).get();
        behemothAggroRangeSneaking = new ConfigEntry<>("gameplay.behemoth.aggro_range_sneaking", 0).get();

        explosiveDragonFireballs = new ConfigEntry<>("gameplay.dragon.explosive_fireballs", true).get();
        noEndermenFight = new ConfigEntry<>("gameplay.dragon.no_pesky_endermen", false).get();

        polyppieSlotAnchor = new ConfigEntry<>("gameplay.polyppie.slot_anchor", 1).get();
        polyppieSlotOffset = new ConfigEntry<>("gameplay.polyppie.slot_offset", 0).get();

        accessibilityChallengeBarrier = new ConfigEntry<>("gameplay.challenge.accessibility_barrier", false).get();
        Challenge.R = new ConfigEntry<>("gameplay.challenge.radius", 16).get() - 0.01f;

        elytraBoostAdvancement = new ConfigEntry<>("equipment.elytra.boost_advancement", "minecraft:end/kill_dragon").get();
        elytraChallenge = new ConfigEntry<>("equipment.elytra.has_challenge", true).get();
        elytraChallengeOffset = new ConfigEntry<>("equipment.elytra.challenge_rune_offset", List.of(0d, 2d, 8d)).get();

        crystalXPBoost = new ConfigEntry<>("equipment.crystal_xp_boost", 3).get();

        chorusSaladTp = new ConfigEntry<>("equipment.chorus_fruit_salad.teleportation", true).get();
        chorusSaladStack = new ConfigEntry<>("equipment.chorus_fruit_salad.stack_size", 4).get();

        shatteredPendantDurability = new ConfigEntry<>("equipment.shattered_pendant_durability", 4).get();
        subwooferRange = new ConfigEntry<>("equipment.subwoofer_range", 6).get();
    }

    public static String dataCompatMode = "";
    public static double customBiomesWeight;
    public static double noiseScale;

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
    public static boolean beginCutscene;
    public static boolean darkEnd;

    public static int behemothAggroRange;
    public static int behemothAggroRangeSneaking;

    public static boolean explosiveDragonFireballs;
    public static boolean noEndermenFight;

    public static int polyppieSlotAnchor;
    public static int polyppieSlotOffset;

    public static boolean accessibilityChallengeBarrier;

    public static String elytraBoostAdvancement = "";
    public static boolean elytraChallenge;
    public static List<Double> elytraChallengeOffset;

    public static double crystalXPBoost;
    public static boolean chorusSaladTp;
    public static int chorusSaladStack;

    public static int shatteredPendantDurability;
    public static int subwooferRange;
}
