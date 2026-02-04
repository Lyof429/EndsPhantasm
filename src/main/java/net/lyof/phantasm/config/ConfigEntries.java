package net.lyof.phantasm.config;

import net.lyof.phantasm.block.challenge.Challenge;

import java.util.List;

public class ConfigEntries {
    public static void reload() {
        doRawPurpur = new ConfigEntry<>("generation.raw_purpur.enable", true).get();
        rawPurpurStart = new ConfigEntry<>("generation.raw_purpur.first_stripe", 29).get();

        doFallenStars = new ConfigEntry<>("generation.fallen_stars.enable", true).get();

        datapackMode = new ConfigEntry<>("generation.biomes.datapack_compatibility", "automatic").get();
        noiseScale = new ConfigEntry<>("generation.biomes.noise_scale", 2).get();

        endHighlandsWeight = new ConfigEntry<>("generation.biomes.weights.end_highlands", 1.2).get();
        dreamingDenWeight = new ConfigEntry<>("generation.biomes.weights.dreaming_den", 1.5).get();
        acidburntAbyssesWeight = new ConfigEntry<>("generation.biomes.weights.acidburnt_abysses", 1.2).get();

        outerEndFirst = new ConfigEntry<>("general.outer_end_before_dragon", true).get();
        beginCutscene = outerEndFirst && new ConfigEntry<>("general.show_cutscene", true).get();

        darkEnd = new ConfigEntry<>("general.dark_end", false).get();

        altChallengeBarrier = new ConfigEntry<>("general.challenge.alternate_barrier", false).get();
        Challenge.R = new ConfigEntry<>("general.challenge.radius", 16).get() - 0.01f;

        explosiveFireballs = new ConfigEntry<>("general.dragon_fight.explosive_fireballs", true).get();
        passiveEndermen = new ConfigEntry<>("general.dragon_fight.passive_endermen", false).get();

        prettyTowers = new ConfigEntry<>("general.dragon_fight.obsidian_tower.beautify", true).get();
        noCrystalCage = new ConfigEntry<>("general.dragon_fight.obsidian_tower.no_crystal_cages", false).get();
        extraTowerHeight = new ConfigEntry<>("general.dragon_fight.obsidian_tower.extra_height", 0).get();

        elytraBoostAdvancement = new ConfigEntry<>("general.equipment.elytra.boost_advancement", "minecraft:end/kill_dragon").get();
        elytraChallenge = new ConfigEntry<>("general.equipment.elytra.has_challenge", true).get();
        elytraChallengeOffset = new ConfigEntry<>("general.equipment.elytra.challenge_rune_offset", List.of(0d, 2d, 8d)).get();

        shatteredPendantDurability = new ConfigEntry<>("general.equipment.shattered_pendant.durability", 6).get();

        behemothAggroRange = new ConfigEntry<>("dreaming_den.entities.behemoth.aggro_range", 6).get();
        behemothSneakAggroRange = new ConfigEntry<>("dreaming_den.entities.behemoth.sneaking_aggro_range", 0).get();

        chorusSaladTeleportation = new ConfigEntry<>("dreaming_den.equipment.chorus_fruit_salad.teleportation", true).get();
        chorusSaladStack = new ConfigEntry<>("dreaming_den.equipment.chorus_fruit_salad.stack_size", 4).get();

        crystallineDurability = new ConfigEntry<>("dreaming_den.equipment.crystalline_tools.durability", 712).get();
        crystallineXpBoost = new ConfigEntry<>("dreaming_den.equipment.crystalline_tools.efficiency_boost", 3).get();

        choralArrowCrossbowRange = new ConfigEntry<>("acidburnt_abysses.equipment.choral_arrow.crossbow_range", 10).get();
        choralArrowCharm = new ConfigEntry<>("acidburnt_abysses.equipment.choral_arrow.charm_duration", 20).get();
        choralArrowCharmPlayer = new ConfigEntry<>("acidburnt_abysses.equipment.choral_arrow.player_charm_duration", 40).get();

        subwooferRange = new ConfigEntry<>("acidburnt_abysses.equipment.subwoofer.range", 8).get();

        corrosionMultiplier = new ConfigEntry<>("acidburnt_abysses.equipment.corrosion.damage_multiplier", 0.2).get();

        pombSliceCorrosive = new ConfigEntry<>("acidburnt_abysses.equipment.corrosion.corrosive_food.pomb_slice", 60).get();
        popRockCandyCorrosive = new ConfigEntry<>("acidburnt_abysses.equipment.corrosion.corrosive_food.pop_rock_candy", 120).get();
        corrosiveDuration = new ConfigEntry<>("acidburnt_abysses.equipment.corrosion.corrosive_food.duration", 80).get();
        corrosiveAmplifier = new ConfigEntry<>("acidburnt_abysses.equipment.corrosion.corrosive_food.amplifier", 0).get();

        fallingPombDamage = new ConfigEntry<>("acidburnt_abysses.equipment.corrosion.falling_pomb.damage", 5).get();
        fallingPombDuration = new ConfigEntry<>("acidburnt_abysses.equipment.corrosion.falling_pomb.duration", 200).get();
        fallingPombAmplifier = new ConfigEntry<>("acidburnt_abysses.equipment.corrosion.falling_pomb.amplifier", 0).get();
    }

    public static boolean doRawPurpur;
    public static int rawPurpurStart;

    public static boolean doFallenStars;

    public static String datapackMode = "";
    public static double noiseScale = 2;
    public static double endHighlandsWeight;
    public static double dreamingDenWeight;
    public static double acidburntAbyssesWeight;

    public static boolean outerEndFirst;
    public static boolean beginCutscene;

    public static boolean darkEnd;

    public static boolean altChallengeBarrier;
    // challenge radius stored directly in Challenge

    public static boolean explosiveFireballs;
    public static boolean passiveEndermen;

    public static boolean prettyTowers;
    public static boolean noCrystalCage;
    public static int extraTowerHeight;

    public static String elytraBoostAdvancement = "";
    public static boolean elytraChallenge;
    public static List<Double> elytraChallengeOffset = List.of(0d, 2d, 8d);

    public static int shatteredPendantDurability;

    public static int behemothAggroRange;
    public static int behemothSneakAggroRange;

    public static boolean chorusSaladTeleportation;
    public static int chorusSaladStack;

    public static int crystallineDurability;
    public static double crystallineXpBoost;

    public static int choralArrowCrossbowRange;
    public static int choralArrowCharm;
    public static int choralArrowCharmPlayer;

    public static int subwooferRange;

    public static double corrosionMultiplier;

    public static int pombSliceCorrosive;
    public static int popRockCandyCorrosive;
    public static int corrosiveDuration;
    public static int corrosiveAmplifier;

    public static double fallingPombDamage;
    public static int fallingPombDuration;
    public static int fallingPombAmplifier;
}
