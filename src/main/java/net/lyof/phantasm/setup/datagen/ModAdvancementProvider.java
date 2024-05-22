package net.lyof.phantasm.setup.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.item.ModItems;
import net.lyof.phantasm.world.biome.ModBiomes;
import net.minecraft.advancement.*;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.TravelCriterion;
import net.minecraft.data.server.advancement.AdvancementProvider;
import net.minecraft.loot.condition.LocationCheckLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
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

        private BiomeCriterion(RegistryKey<Biome> biome) {
            super(Identifier.of("minecraft", "location"),
                    LootContextPredicate.create(LocationCheckLootCondition.builder(
                            LocationPredicate.Builder.create().biome(biome)).build()));
        }

        public static BiomeCriterion of(RegistryKey<Biome> biome) {
            return new BiomeCriterion(biome);
        }
    }


    public ModAdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    public static final String BASE = "advancement.phantasm.";
    public static final String DESC = ".desc";

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        Advancement DREAMING_DEN = Advancement.Builder.create()
                .display(ModBlocks.PREAM_SAPLING,
                        Text.translatable(BASE + "find_dreaming_den"),
                        Text.translatable(BASE + "find_dreaming_den" + DESC),
                        null,
                        AdvancementFrame.TASK,
                        true, true, false)
                .criterion("is_dreaming_den", BiomeCriterion.of(ModBiomes.DREAMING_DEN))
                .rewards(AdvancementRewards.NONE).build(Phantasm.makeID("find_dreaming_den"));
                //.build(consumer, "phantasm:find_dreaming_den")

        Advancement.Builder.create()
                .display(ModBlocks.CRYSTAL_SHARD,
                        Text.translatable(BASE + "get_crystal"),
                        Text.translatable(BASE + "get_crystal" + DESC),
                        null,
                        AdvancementFrame.TASK,
                        true, true, false)
                .criterion("has_crystal", InventoryChangedCriterion.Conditions.items(ModBlocks.CRYSTAL_SHARD))
                .rewards(AdvancementRewards.NONE)
                .parent(DREAMING_DEN);
                //.build(consumer, "phantasm:get_crystal");


    }
}
