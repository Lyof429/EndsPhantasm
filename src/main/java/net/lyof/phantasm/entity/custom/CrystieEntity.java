package net.lyof.phantasm.entity.custom;

import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.entity.goal.DiveBombGoal;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.FlyGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.List;

public class CrystieEntity extends AnimalEntity {
    public boolean isAngry = false;
    public static ItemStack FIREWORK = getFirework();

    public CrystieEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 100, true);
        this.setNoGravity(true);
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new DiveBombGoal(this));
        this.goalSelector.add(1, new FlyGoal(this, 20));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, false, true) {
            @Override
            public boolean canStart() {
                return super.canStart() && CrystieEntity.this.isAngry;
            }
        });
        this.goalSelector.add(3, new TemptGoal(this, 1, Ingredient.fromTag(ModTags.Items.CRYSTAL_FLOWERS), false));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 12)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3);
    }

    @Override
    public AnimalEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.CRYSTIE.create(world);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new BirdNavigation(this, world);
    }

    @Override
    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        if (world.getBlockState(pos.down()).isIn(ModTags.Blocks.END_PLANTS_GROWABLE_ON))
            return 2;
        if (pos == this.getBlockPos())
            return -10;
        if (world.getBlockState(pos).isAir())
            return 10;
        return 0;
    }

    @Override
    public float getEyeHeight(EntityPose pose) {
        return (float) (this.getDimensions(pose).height * 0.45);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isIn(ModTags.Items.CRYSTAL_FLOWERS);
    }

    @Override
    public int getXpToDrop() {
        return Monster.NORMAL_MONSTER_XP;
    }

    public static ItemStack getFirework() {
        ItemStack stack = Items.FIREWORK_ROCKET.getDefaultStack();
        NbtCompound tag = stack.getOrCreateSubNbt("Fireworks");

        NbtList list = new NbtList();
        NbtCompound explosion = new NbtCompound();
        explosion.putByte("Type", (byte) 0);
        explosion.putByte("Trail", (byte) 1);

        NbtIntArray colors = new NbtIntArray(List.of(
                2651799,
                11250603,
                6719955,
                15790320
        ));
        explosion.put("Colors", colors);

        NbtIntArray fadecolors = new NbtIntArray(List.of(
                8073150,
                11250603,
                12801229
        ));
        explosion.put("FadeColors", fadecolors);

        list.add(explosion);
        /* Explosions:[
                {
                    Type:0,
                    Trail:1,
                    Colors:[I;2651799,11250603,6719955,15790320],
                    FadeColors:[I;8073150,2651799,11250603,6719955,12801229,15790320]
                }
            ]
        */

        tag.put("Explosions", list);
        tag.putByte("Flight", (byte) -2);
        return stack;
    }

    public void explode() {
        FireworkRocketEntity firework = new FireworkRocketEntity(this.getWorld(), FIREWORK, this);
        this.getWorld().spawnEntity(firework);

        if (!this.isDead()) this.discard();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getAttacker() instanceof PlayerEntity)
            this.isAngry = true;
        return super.damage(source, amount);
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean result = super.tryAttack(target);
        if (result) this.explode();
        return result;
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        this.explode();
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ActionResult result = super.interactMob(player, hand);

        ItemStack stack = player.getStackInHand(hand);
        if (stack.isOf(ModBlocks.VOID_CRYSTAL_GLASS.asItem())) {
            this.discard();
            stack.decrement(1);
            player.giveItemStack(Items.END_CRYSTAL.getDefaultStack());
            result = ActionResult.SUCCESS;
        }
        return result;
    }
}
