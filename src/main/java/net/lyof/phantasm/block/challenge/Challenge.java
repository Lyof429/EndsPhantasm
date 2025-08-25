package net.lyof.phantasm.block.challenge;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Challenge {
    public boolean dataDriven = false;

    public final Identifier id;
    private final Identifier lootTable;
    private final List<Monster> monsters;
    public final int monsterObjective;
    public final int levelCost;
    public final boolean postDragon;
    private final int totalWeight;

    public Challenge(Identifier id, Identifier lootTable, List<Monster> monsters, int monsterObjective,
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
        for (Monster monster : this.monsters) {
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

            List<Monster> monsters = new ArrayList<>();
            for (JsonElement elmt : json.getAsJsonArray("monsters"))
                Monster.read(elmt.getAsJsonObject(), monsters);

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


    public static class Monster {
        public final int weight;

        private final EntityType<? extends LivingEntity> entityType;
        private final Map<EntityAttribute, Float> attributeMultipliers;

        public Monster(int weight, EntityType<? extends LivingEntity> entityType, Map<EntityAttribute, Float> attributeMultipliers) {
            this.weight = weight;
            this.entityType = entityType;
            this.attributeMultipliers = attributeMultipliers;
        }

        public LivingEntity create(ChallengeRuneBlockEntity rune) {
            LivingEntity entity = this.entityType.create(rune.getWorld());
            entity.setPosition(rune.getPos().up().toCenterPos().add(0, -0.5, 0));

            for (Map.Entry<EntityAttribute, Float> entry : this.attributeMultipliers.entrySet())
                entity.getAttributeInstance(entry.getKey()).addPersistentModifier(new EntityAttributeModifier(
                        "Challenge bonus", entry.getValue(), EntityAttributeModifier.Operation.MULTIPLY_BASE));
            entity.setHealth(entity.getMaxHealth());

            entity.addCommandTag(Phantasm.MOD_ID + ".challenge");
            ((Challenger) entity).setChallengeRune(rune);
            if (entity instanceof MobEntity mob)
                mob.setPersistent();
            if (entity instanceof VexEntity vex)
                vex.setBounds(rune.getPos().up());

            return entity;
        }

        @SuppressWarnings("unchecked")
        public static void read(JsonObject json, List<Monster> monsters) {
            if (json.has("entity")) {
                EntityType<?> entity = Registries.ENTITY_TYPE.get(new Identifier(json.get("entity").getAsString()));
                if (entity == EntityType.PIG && !json.get("entity").getAsString().equals("minecraft:pig")) return;
                try { EntityType<? extends LivingEntity> e = (EntityType<? extends LivingEntity>) entity; }
                catch (Exception ignored) { return; }

                JsonObject attributesObject = json.has("attributes")
                        ? json.getAsJsonObject("attributes") : new JsonObject();
                Map<EntityAttribute, Float> attributes = new HashMap<>();
                for (Map.Entry<String, JsonElement> entry : attributesObject.entrySet()) {
                    if (!entry.getValue().isJsonPrimitive() || !entry.getValue().getAsJsonPrimitive().isNumber())
                        continue;

                    EntityAttribute attr = Registries.ATTRIBUTE.get(new Identifier(entry.getKey()));
                    if (attr == null) continue;

                    attributes.putIfAbsent(attr, entry.getValue().getAsFloat());
                }

                monsters.add(new Monster(
                        json.has("weight") ? json.get("weight").getAsInt() : 1,
                        (EntityType<? extends LivingEntity>) entity,
                        attributes
                ));
            }
        }
    }
}
