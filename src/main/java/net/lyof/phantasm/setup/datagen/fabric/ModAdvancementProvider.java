package net.lyof.phantasm.setup.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.item.ModItems;
import net.lyof.phantasm.world.biome.ModBiomes;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.advancement.criterion.PlayerGeneratesContainerLootCriterion;
import net.minecraft.loot.condition.LocationCheckLootCondition;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {
    public static class BiomeCriterion extends AbstractCriterionConditions {
        public RegistryKey<Biome> biome;

        private BiomeCriterion(RegistryKey<Biome> biome, int min, int max) {
            super(Identifier.of("minecraft", "location"),
                    LootContextPredicate.create(LocationCheckLootCondition.builder(
                            LocationPredicate.Builder.create().biome(biome).y(NumberRange.FloatRange.between(min, max))).build()));
        }

        public static BiomeCriterion of(RegistryKey<Biome> biome) {
            return BiomeCriterion.of(biome, 0, 256);
        }

        public static BiomeCriterion of(RegistryKey<Biome> biome, int min, int max) {
            return new BiomeCriterion(biome, min, max);
        }
    }

    public static final Identifier SYNTHETIC_CHALLENGE = Phantasm.makeID("/challenge");


    public ModAdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    protected static final String BASE = "advancement.phantasm.";
    protected static final String DESC = ".desc";

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        Advancement DREAMING_DEN = Advancement.Builder.create()
                .display(ModBlocks.VIVID_NIHILIUM,
                        Text.translatable(BASE + "find_dreaming_den"),
                        Text.translatable(BASE + "find_dreaming_den" + DESC),
                        null,
                        AdvancementFrame.TASK,
                        true, true, false)
                .criterion("is_dreaming_den", BiomeCriterion.of(ModBiomes.DREAMING_DEN, 50, 256))
                .rewards(AdvancementRewards.NONE).build(Phantasm.makeID("find_dreaming_den"));
                //.build(consumer, "phantasm:find_dreaming_den")

        Advancement CRYSTAL_SHARD = Advancement.Builder.create()
                .display(ModBlocks.CRYSTAL_SHARD,
                        Text.translatable(BASE + "get_crystal"),
                        Text.translatable(BASE + "get_crystal" + DESC),
                        null,
                        AdvancementFrame.TASK,
                        true, true, false)
                .criterion("has_crystal", InventoryChangedCriterion.Conditions.items(ModBlocks.CRYSTAL_SHARD))
                .rewards(AdvancementRewards.NONE)
                .parent(DREAMING_DEN)
                .build(consumer, "phantasm:get_crystal");

        Advancement UNDERISLAND = Advancement.Builder.create()
                .display(ModBlocks.OBLIVION,
                        Text.translatable(BASE + "find_underisland"),
                        Text.translatable(BASE + "find_underisland" + DESC),
                        null,
                        AdvancementFrame.GOAL,
                        true, true, false)
                .criterion("is_underisland", BiomeCriterion.of(ModBiomes.DREAMING_DEN, 0, 29))
                .rewards(AdvancementRewards.NONE)
                .parent(DREAMING_DEN)
                .build(consumer, "phantasm:find_underisland");

        Advancement CRYSTAL_TOOLS = Advancement.Builder.create()
                .display(ModItems.CRYSTALLINE_PICKAXE,
                        Text.translatable(BASE + "get_crystal_tools"),
                        Text.translatable(BASE + "get_crystal_tools" + DESC),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true, true, false)
                .criterion("has_crystal_tool",
                        InventoryChangedCriterion.Conditions.items(
                                ModItems.CRYSTALLINE_AXE,
                                ModItems.CRYSTALLINE_SWORD,
                                ModItems.CRYSTALLINE_PICKAXE,
                                ModItems.CRYSTALLINE_HOE,
                                ModItems.CRYSTALLINE_SHOVEL))
                .rewards(AdvancementRewards.Builder.experience(100))
                .parent(CRYSTAL_SHARD)
                .build(consumer, "phantasm:get_crystal_tools");

        Advancement ACIDBURNT_ABYSSES = Advancement.Builder.create()
                .display(ModBlocks.ACIDIC_NIHILIUM,
                        Text.translatable(BASE + "find_acidburnt_abysses"),
                        Text.translatable(BASE + "find_acidburnt_abysses" + DESC),
                        null,
                        AdvancementFrame.TASK,
                        true, true, false)
                .criterion("is_acidburnt_abysses", BiomeCriterion.of(ModBiomes.ACIDBURNT_ABYSSES, 50, 256))
                .rewards(AdvancementRewards.NONE).build(Phantasm.makeID("find_acidburnt_abysses"));
                //.build(consumer, "phantasm:find_acidburnt_abysses");

        Advancement SOUR_SLUDGE = Advancement.Builder.create()
                .display(ModBlocks.ACIDIC_MASS,
                        Text.translatable(BASE + "kill_sour_sludge"),
                        Text.translatable(BASE + "kill_sour_sludge" + DESC),
                        null,
                        AdvancementFrame.TASK,
                        true, true, false)
                .criterion("killed_sour_sludge", OnKilledCriterion.Conditions.createPlayerKilledEntity(
                        EntityPredicate.Builder.create().type(ModEntities.SOUR_SLUDGE),
                        DamageSourcePredicate.EMPTY
                ))
                .rewards(AdvancementRewards.NONE)
                .parent(ACIDBURNT_ABYSSES)
                .build(consumer, "phantasm:kill_sour_sludge");

        Advancement CHORAL_RIFF = Advancement.Builder.create()
                .display(ModBlocks.CHORAL_BLOCK,
                        Text.translatable(BASE + "find_choral_riff"),
                        Text.translatable(BASE + "find_choral_riff" + DESC),
                        null,
                        AdvancementFrame.GOAL,
                        true, true, false)
                .criterion("is_choral_riff", BiomeCriterion.of(ModBiomes.ACIDBURNT_ABYSSES, 0, 29))
                .rewards(AdvancementRewards.NONE)
                .parent(ACIDBURNT_ABYSSES)
                .build(consumer, "phantasm:find_choral_riff");

        Advancement CHORAL_ARROW = Advancement.Builder.create()
                .display(ModItems.CHORAL_ARROW,
                        Text.translatable(BASE + "use_choral_arrow"),
                        Text.translatable(BASE + "use_choral_arrow" + DESC),
                        null,
                        AdvancementFrame.TASK,
                        true, true, false)
                .criterion("shot_choral_arrow", OnKilledCriterion.Conditions.createPlayerKilledEntity(
                        EntityPredicate.ANY,
                        DamageSourcePredicate.Builder.create()
                                .directEntity(EntityPredicate.Builder.create().type(ModEntities.CHORAL_ARROW))
                ))
                .rewards(AdvancementRewards.NONE)
                .parent(CHORAL_RIFF)
                .build(consumer, "phantasm:use_choral_arrow");

        Advancement DORMANT_POLYPPIE = Advancement.Builder.create()
                .display(ModBlocks.DORMANT_POLYPPIE,
                        Text.translatable(BASE + "get_dormant_polyppie"),
                        Text.translatable(BASE + "get_dormant_polyppie" + DESC),
                        null,
                        AdvancementFrame.TASK,
                        true, true, false)
                .criterion("has_dormant_polyppie", InventoryChangedCriterion.Conditions.items(ModBlocks.DORMANT_POLYPPIE))
                .rewards(AdvancementRewards.NONE)
                .parent(CHORAL_RIFF)
                .build(consumer, "phantasm:get_dormant_polyppie");

        Advancement CHALLENGE = Advancement.Builder.create()
                .display(ModBlocks.CHALLENGE_RUNE,
                        Text.translatable(BASE + "beat_challenge"),
                        Text.translatable(BASE + "beat_challenge" + DESC),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true, true, false)
                .criterion("beat_challenge", PlayerGeneratesContainerLootCriterion.Conditions.create(SYNTHETIC_CHALLENGE))
                .rewards(AdvancementRewards.Builder.experience(50))
                .build(Phantasm.makeID("beat_challenge"));
    }
}
