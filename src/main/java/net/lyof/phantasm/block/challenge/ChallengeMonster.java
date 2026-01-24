package net.lyof.phantasm.block.challenge;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.lyof.phantasm.entity.access.Challenger;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChallengeMonster {
    public final int weight;

    private final EntityType<? extends LivingEntity> entityType;
    private final Map<EntityAttribute, Float> attributeMultipliers;

    public ChallengeMonster(int weight, EntityType<? extends LivingEntity> entityType, Map<EntityAttribute, Float> attributeMultipliers) {
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
            vex.setBounds(rune.getPos().up(5));
        if (entity instanceof SlimeEntity slime)
            slime.setSize((int) Math.pow(2, rune.getWorld().getRandom().nextInt(3)), true);

        return entity;
    }

    @SuppressWarnings("unchecked")
    public static void read(JsonObject json, List<ChallengeMonster> monsters) {
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

            monsters.add(new ChallengeMonster(
                    json.has("weight") ? json.get("weight").getAsInt() : 1,
                    (EntityType<? extends LivingEntity>) entity,
                    attributes
            ));
        }
    }
}
