package net.lyof.phantasm.block.challenge;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Challenge {
    public static float R = 15.99f;

    public boolean dataDriven = false;

    public final Identifier id;
    public final Identifier lootTable;
    private final List<ChallengeMonster> monsters;
    public final int monsterObjective;
    public final int levelCost;
    public final boolean postDragon;
    private final int totalWeight;

    public Challenge(Identifier id, Identifier lootTable, List<ChallengeMonster> monsters, int monsterObjective,
                     int levelCost, boolean postDragon) {
        this.id = id;
        this.lootTable = lootTable;
        this.monsters = monsters;
        this.monsterObjective = monsterObjective;
        this.totalWeight = this.monsters.stream().reduce(0, (sum, monster) -> sum + monster.weight, Integer::sum);
        this.levelCost = levelCost;
        this.postDragon = postDragon;
    }

    public Challenge setDataDriven(boolean value) {
        this.dataDriven = value;
        return this;
    }

    public void spawnMonster(ChallengeRuneBlockEntity rune) {
        int select = rune.getWorld().getRandom().nextInt(this.totalWeight);
        for (ChallengeMonster monster : this.monsters) {
            select -= monster.weight;
            if (select < 0) {
                LivingEntity entity = monster.create(rune);
                rune.getWorld().spawnEntity(entity);
                return;
            }
        }
    }

    public void spawnLoot(ChallengeRuneBlockEntity rune) {
        if (!(rune.getWorld() instanceof ServerWorld world)) return;

        world.getServer().getLootManager().getLootTable(this.lootTable)
                .generateLoot(new LootContext.Builder(new LootContextParameterSet.Builder(world)
                        .add(LootContextParameters.ORIGIN, rune.getPos().toCenterPos()).build(LootContextTypes.CHEST))
                                .build(null),
                        stack -> world.spawnEntity(new ItemEntity(world, rune.getPos().getX() + 0.5,
                                rune.getPos().getY() + 1, rune.getPos().getZ() + 0.5, stack)));
    }


    public static void read(Identifier id, JsonObject json) {
        if (json.has("loot_table") && json.has("monsters") && json.has("objective")
                && json.has("level_cost") && json.has("post_dragon")) {

            List<ChallengeMonster> monsters = new ArrayList<>();
            for (JsonElement elmt : json.getAsJsonArray("monsters"))
                ChallengeMonster.read(elmt.getAsJsonObject(), monsters);

            ChallengeRegistry.register(id, new Challenge(
                    id,
                    new Identifier(json.get("loot_table").getAsString()),
                    monsters,
                    json.get("objective").getAsInt(),
                    json.get("level_cost").getAsInt(),
                    json.get("post_dragon").getAsBoolean()
            ).setDataDriven(true));
        }
    }
}
