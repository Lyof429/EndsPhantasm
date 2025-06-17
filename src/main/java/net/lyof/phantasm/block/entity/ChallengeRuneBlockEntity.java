package net.lyof.phantasm.block.entity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlockEntities;
import static net.lyof.phantasm.world.feature.custom.ObsidianTowerStructure.R;

import net.lyof.phantasm.mixin.access.ServerPlayerEntityAccessor;
import net.lyof.phantasm.setup.ModPackets;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChallengeRuneBlockEntity extends BlockEntity {
    private final List<UUID> completedPlayerUuids;
    private final List<UUID> challengerUuids;
    public int tick;

    public boolean renderBase;
    private final List<Vec3i> towerBases;

    public ChallengeRuneBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CHALLENGE_RUNE, pos, state);
        this.completedPlayerUuids = new ArrayList<>();
        this.challengerUuids = new ArrayList<>();
        this.tick = -1;
        this.towerBases = new ArrayList<>();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        int size = nbt.getInt("CompletedPlayerCount");
        for (int i = 0; i < size; i++)
            this.completedPlayerUuids.add(nbt.getUuid("Player" + i));

        size = nbt.getInt("ChallengerCount");
        for (int i = 0; i < size; i++)
            this.challengerUuids.add(nbt.getUuid("Challenger" + i));

        this.renderBase = nbt.getBoolean("RenderBase");
        this.tick = nbt.getInt("ChallengeTick");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.putInt("CompletedPlayerCount", this.completedPlayerUuids.size());
        for (int i = 0; i < this.completedPlayerUuids.size(); i++)
            nbt.putUuid("Player" + i, this.completedPlayerUuids.get(i));

        nbt.putInt("ChallengerCount", this.challengerUuids.size());
        for (int i = 0; i < this.challengerUuids.size(); i++)
            nbt.putUuid("Challenger" + i, this.challengerUuids.get(i));

        nbt.putBoolean("RenderBase", this.renderBase);
        nbt.putInt("ChallengeTick", this.tick);
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


    public List<Vec3i> getTowerBases() {
        return this.towerBases;
    }

    public void generateTowerBases() {
        this.towerBases.clear();
        int y = this.getWorld().getBottomY() - this.getPos().getY();
        for (int sx = -R; sx <= R; sx++) {
            for (int sz = -R; sz <= R; sz++) {
                if (sx*sx + sz*sz < R*R && sx*sx + sz*sz >= (R-1)*(R-1))
                    this.towerBases.add(new Vec3i(sx, y, sz));
            }
        }
    }


    public void complete(PlayerEntity player) {
        this.completedPlayerUuids.add(player.getUuid());
    }

    public boolean hasCompleted(PlayerEntity player) {
        return this.completedPlayerUuids.contains(player.getUuid());
    }

    public boolean canStart(ServerPlayerEntity player) {
        return !this.isChallengeRunning() && !this.hasCompleted(player)
                && player.experienceLevel >= 5 && ((ServerPlayerEntityAccessor) player).getSeenCredits();
    }

    public void displayHint(ServerPlayerEntity user) {
        String message = "";
        if (this.hasCompleted(user))
            message = ".completed" + user.getRandom().nextInt(5);
        else if (!((ServerPlayerEntityAccessor) user).getSeenCredits())
            message = ".dragon" + user.getRandom().nextInt(5);
        else if (user.experienceLevel < 5)
            message = ".experience" + user.getRandom().nextInt(5);

        user.sendMessage(Text.translatable("block.phantasm.challenge_rune.hint" + message).formatted(Formatting.LIGHT_PURPLE),
                true);
    }

    public void startChallenge(PlayerEntity player) {
        this.tick = 0;
        this.challengerUuids.clear();

        for (PlayerEntity participant : player.getWorld().getPlayers()) {
            if (participant.getPos().distanceTo(Vec3d.of(this.getPos())) < 10) {
                if (!this.getWorld().isClient()) {
                    PacketByteBuf packet = PacketByteBufs.create();
                    packet.writeBlockPos(this.getPos());
                    ServerPlayNetworking.send((ServerPlayerEntity) player, ModPackets.CHALLENGE_STARTS, packet);
                }

                this.challengerUuids.add(participant.getUuid());
                ((Challenger) participant).phantasm$setRune(this);
            }
        }

        this.getWorld().playSound(player, this.getPos(), SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS,
                10, 1);
    }

    public void stopChallenge(boolean success) {
        this.tick = -2;

        if (!this.getWorld().isClient()) {
            PacketByteBuf packet = PacketByteBufs.create();
            packet.writeBlockPos(this.getPos());
            packet.writeBoolean(success);

            for (PlayerEntity player : this.getWorld().getPlayers())
                ServerPlayNetworking.send((ServerPlayerEntity) player, ModPackets.CHALLENGE_ENDS, packet);
        }
    }

    public boolean isChallengeRunning() {
        return this.tick >= 0;
    }

    public static void tick(World world, BlockPos pos, BlockState state, ChallengeRuneBlockEntity self) {
        if (!self.isChallengeRunning() || world == null) return;

        if (self.tick % 20 == 0) {
            for (UUID uuid : List.copyOf(self.challengerUuids)) {
                Challenger challenger = Challenger.get(uuid, world);
                if (challenger == null) continue;

                if (challenger.phantasm$getRune() == null)
                    challenger.phantasm$setRune(self);
                if (!challenger.isInRange() || !challenger.phantasm$asPlayer().isAlive()) {
                    self.challengerUuids.remove(challenger.phantasm$asPlayer().getUuid());
                }
            }

            if (self.challengerUuids.isEmpty()) {
                self.stopChallenge(false);
            }
        }

        self.tick++;
    }
}
