package net.lyof.phantasm.block.challenge;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;

public class ChallengeData {
    public final Identifier id;
    private final Identifier lootTable;
    private final List<Monster> monsters;
    public final int monsterObjective;
    public final int levelCost;
    public final boolean postDragon;
    private final int totalWeight;

    public ChallengeData(Identifier id, Identifier lootTable, List<Monster> monsters, int monsterObjective,
                         int levelCost, boolean postDragon) {
        this.id = id;
        this.lootTable = lootTable;
        this.monsters = monsters;
        this.monsterObjective = monsterObjective;
        this.totalWeight = this.monsters.stream().reduce(0, (sum, monster) -> sum + monster.weight, Integer::sum);
        this.levelCost = levelCost;
        this.postDragon = postDragon;
    }

    public void spawnMonster(ChallengeRuneBlockEntity rune) {
        int select = rune.getWorld().getRandom().nextInt(this.totalWeight);
        for (Monster monster : this.monsters) {
            select -= monster.weight;
            if (select < 0) {
                LivingEntity entity = monster.create(rune);
                rune.getWorld().spawnEntity(entity);
            }
        }
    }

    public void spawnLoot(ChallengeRuneBlockEntity rune) {
        if (!(rune.getWorld() instanceof ServerWorld world)) return;

        world.getServer().getLootManager().getLootTable(this.lootTable)
                .generateLoot(new LootContext.Builder(new LootContextParameterSet.Builder(world)
                        .add(LootContextParameters.ORIGIN, rune.getPos().toCenterPos()).build(LootContextTypes.CHEST)).build(null),
                        stack -> world.spawnEntity(new ItemEntity(world, rune.getPos().getX() + 0.5,
                                rune.getPos().getY() + 1, rune.getPos().getZ() + 0.5, stack)));
    }


    public static class Monster {
        public final int weight;

        private final EntityType<? extends LivingEntity> entityType;
        private final float healthMultiplier;
        private final float damageMultiplier;

        public Monster(int weight, EntityType<? extends LivingEntity> entityType, float healthMultiplier, float damageMultiplier) {
            this.weight = weight;
            this.entityType = entityType;
            this.healthMultiplier = healthMultiplier;
            this.damageMultiplier = damageMultiplier;
        }

        public LivingEntity create(ChallengeRuneBlockEntity rune) {
            LivingEntity entity = this.entityType.create(rune.getWorld());
            entity.setPosition(rune.getPos().up().toCenterPos().add(0, -0.5, 0));
            entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).addPersistentModifier(new EntityAttributeModifier(
                    "Challenge bonus", this.healthMultiplier, EntityAttributeModifier.Operation.MULTIPLY_BASE));
            entity.setHealth(entity.getMaxHealth());
            entity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).addPersistentModifier(new EntityAttributeModifier(
                    "Challenge bonus", this.damageMultiplier, EntityAttributeModifier.Operation.MULTIPLY_BASE));

            entity.addCommandTag(Phantasm.MOD_ID + ".challenge");
            ((Challenger) entity).phantasm$setRune(rune);

            return entity;
        }
    }
}
