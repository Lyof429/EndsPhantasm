package net.lyof.phantasm.block.entity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.lyof.phantasm.block.ModBlockEntities;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.block.challenge.Challenge;
import net.lyof.phantasm.block.challenge.ChallengeRegistry;
import net.lyof.phantasm.block.challenge.Challenger;
import net.lyof.phantasm.mixin.access.ServerPlayerEntityAccessor;
import net.lyof.phantasm.setup.ModPackets;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChallengeRuneBlockEntity extends BlockEntity {
    private final List<UUID> completedPlayerUuids;
    private final List<UUID> challengerUuids;
    public int tick;
    private int progress;
    private final ServerBossBar bossbar;
    public Challenge challenge = ChallengeRegistry.EMPTY;

    public boolean renderBase;

    public ChallengeRuneBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CHALLENGE_RUNE, pos, state);
        this.completedPlayerUuids = new ArrayList<>();
        this.challengerUuids = new ArrayList<>();
        this.bossbar = new ServerBossBar(ModBlocks.CHALLENGE_RUNE.getName(), BossBar.Color.PURPLE, BossBar.Style.NOTCHED_20);

        this.tick = -1;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        this.setChallenge(new Identifier(nbt.getString("ChallengeId")));

        int size = nbt.getInt("CompletedPlayerCount");
        for (int i = 0; i < size; i++)
            this.completedPlayerUuids.add(nbt.getUuid("Player" + i));

        size = nbt.getInt("ChallengerCount");
        for (int i = 0; i < size; i++)
            this.challengerUuids.add(nbt.getUuid("Challenger" + i));

        this.renderBase = nbt.getBoolean("RenderBase");
        this.tick = nbt.getInt("ChallengeTick");
        this.progress = nbt.getInt("Progress");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.putString("ChallengeId", this.challenge.id.toString());

        nbt.putInt("CompletedPlayerCount", this.completedPlayerUuids.size());
        for (int i = 0; i < this.completedPlayerUuids.size(); i++)
            nbt.putUuid("Player" + i, this.completedPlayerUuids.get(i));

        nbt.putInt("ChallengerCount", this.challengerUuids.size());
        for (int i = 0; i < this.challengerUuids.size(); i++)
            nbt.putUuid("Challenger" + i, this.challengerUuids.get(i));

        nbt.putBoolean("RenderBase", this.renderBase);
        nbt.putInt("ChallengeTick", this.tick);
        nbt.putInt("Progress", this.progress);
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = new NbtCompound();
        this.writeNbt(nbt);
        return nbt;
    }


    public void setChallenge(Identifier id) {
        this.challenge = ChallengeRegistry.get(id);
        this.bossbar.setStyle(BossBar.Style.byName("notched_" + this.challenge.monsterObjective));
    }

    public void complete(PlayerEntity player) {
        if (!this.hasCompleted(player))
            this.completedPlayerUuids.add(player.getUuid());
    }

    public boolean hasCompleted(PlayerEntity player) {
        return this.completedPlayerUuids.contains(player.getUuid());
    }

    public void addChallenger(PlayerEntity player) {
        this.challengerUuids.add(player.getUuid());
        ((Challenger) player).setChallengeRune(this);
    }

    public boolean canStart(ServerPlayerEntity player) {
        return this.challenge.monsterObjective > 0 && !this.isChallengeRunning() && !this.hasCompleted(player)
                && player.experienceLevel >= this.challenge.levelCost
                && (!this.challenge.postDragon || ((ServerPlayerEntityAccessor) player).getSeenCredits());
    }

    public void displayHint(ServerPlayerEntity user) {
        String message = "";
        if (this.hasCompleted(user))
            message = ".completed" + user.getRandom().nextInt(5);
        else if (this.challenge.postDragon && !((ServerPlayerEntityAccessor) user).getSeenCredits())
            message = ".dragon" + user.getRandom().nextInt(5);
        else if (user.experienceLevel < this.challenge.levelCost)
            message = ".experience" + user.getRandom().nextInt(5);

        user.sendMessage(Text.translatable("block.phantasm.challenge_rune.hint" + message).formatted(Formatting.LIGHT_PURPLE),
                true);
    }

    public void progress() {
        this.progress++;
        this.bossbar.setPercent(1 - (float) this.progress / this.challenge.monsterObjective);

        if (this.progress >= this.challenge.monsterObjective)
            this.stopChallenge(true);
    }

    public void startChallenge(PlayerEntity player) {
        this.startChallenge();

        if (!player.isCreative())
            player.addExperienceLevels(-this.challenge.levelCost);

        for (PlayerEntity participant : player.getWorld().getPlayers()) {
            if (participant.getPos().distanceTo(Vec3d.of(this.getPos())) < Challenger.R) {
                if (!this.getWorld().isClient()) {
                    PacketByteBuf packet = PacketByteBufs.create();
                    packet.writeBlockPos(this.getPos());
                    ServerPlayNetworking.send((ServerPlayerEntity) participant, ModPackets.CHALLENGE_STARTS, packet);
                }

                this.addChallenger(participant);
            }
        }
    }

    public void startChallenge() {
        this.tick = 0;
        this.progress = 0;
        this.challengerUuids.clear();

        this.getWorld().playSound(null, this.getPos(), SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS,
                10, 1);
        this.bossbar.setPercent(1);

        markDirty(world, pos, this.getCachedState());
    }

    public void stopChallenge(boolean success) {
        this.tick = -2;
        this.progress = 0;

        if (success) {
            for (UUID uuid : List.copyOf(this.challengerUuids)) {
                Challenger challenger = Challenger.get(uuid, this.getWorld());
                if (challenger == null) continue;

                if (challenger.getChallengeRune() == this && challenger.isInRange() && challenger.asPlayer().isAlive())
                    this.complete(challenger.asPlayer());
            }
        }

        if (!this.getWorld().isClient()) {
            if (success)
                this.challenge.spawnLoot(this);

            PacketByteBuf packet = PacketByteBufs.create();
            packet.writeBlockPos(this.getPos());
            packet.writeBoolean(success);

            for (PlayerEntity player : this.getWorld().getPlayers()) {
                ServerPlayNetworking.send((ServerPlayerEntity) player, ModPackets.CHALLENGE_ENDS, packet);
                this.bossbar.removePlayer((ServerPlayerEntity) player);
            }
        }

        for (Entity entity : world.getOtherEntities(null, Box.from(this.getPos().up((int) Challenger.R/2)
                        .toCenterPos()).expand(Challenger.R * 2),
                e -> e instanceof Challenger challenger && challenger.getChallengeRune() == this)) {

            if (!(entity instanceof PlayerEntity)) {
                entity.remove(Entity.RemovalReason.CHANGED_DIMENSION);
            }
        }

        this.challengerUuids.clear();
        markDirty(world, pos, this.getCachedState());
    }

    public boolean isChallengeRunning() {
        return this.tick >= 0;
    }

    public static void tick(World world, BlockPos pos, BlockState state, ChallengeRuneBlockEntity self) {
        if (!self.isChallengeRunning() || world == null) return;

        if (self.tick % 20 == 0) {
            for (UUID uuid : List.copyOf(self.challengerUuids)) {
                Challenger challenger = Challenger.get(uuid, world);
                if (challenger == null) {
                    self.challengerUuids.remove(uuid);
                    continue;
                }

                if (!world.isClient()) {
                    self.bossbar.addPlayer((ServerPlayerEntity) challenger.asPlayer());
                    self.bossbar.setPercent(1 - (float) self.progress / self.challenge.monsterObjective);
                }

                if (challenger.getChallengeRune() == null)
                    challenger.setChallengeRune(self);
                if (!challenger.isInRange() || !challenger.asPlayer().isAlive()) {
                    self.challengerUuids.remove(uuid);

                    if (!world.isClient())
                        self.bossbar.removePlayer((ServerPlayerEntity) challenger.asPlayer());
                }
            }

            if (self.challengerUuids.isEmpty())
                self.stopChallenge(false);

            markDirty(world, pos, state);
        }

        if (self.tick == 100) {
            world.getOtherEntities(null, Box.from(pos.toCenterPos()), e -> e instanceof EndCrystalEntity)
                    .stream().findFirst().ifPresent(Entity::discard);
            if (world.isClient()) world.createExplosion(null, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5,
                    2, World.ExplosionSourceType.NONE);
            world.setBlockState(pos.up(), Blocks.AIR.getDefaultState());
        }

        if (!world.isClient() && self.tick > 100 && self.tick <= 100 + 20*self.challenge.monsterObjective
                && self.tick % 20 == 0) {
            self.challenge.spawnMonster(self);
        }

        self.tick++;
    }
}
