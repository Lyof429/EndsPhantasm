package net.lyof.phantasm.block.entity;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlockEntities;
import net.lyof.phantasm.entity.listener.ChallengeRuneEventListener;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.listener.GameEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChallengeRuneBlockEntity extends BlockEntity implements GameEventListener.Holder<ChallengeRuneEventListener> {
    private final ChallengeRuneEventListener listener;
    private final List<UUID> completedPlayers;

    public ChallengeRuneBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CHALLENGE_RUNE, pos, state);
        this.listener = new ChallengeRuneEventListener(this);
        this.completedPlayers = new ArrayList<>();
    }

    @Override
    public ChallengeRuneEventListener getEventListener() {
        return this.listener;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        int size = nbt.getInt("CompletedPlayerCount");
        for (int i = 0; i < size; i++)
            this.completedPlayers.add(nbt.getUuid("Player" + i));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("CompletedPlayerCount", this.completedPlayers.size());
        for (int i = 0; i < this.completedPlayers.size(); i++)
            nbt.putUuid("Player" + i, this.completedPlayers.get(i));
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = new NbtCompound();
        this.writeNbt(nbt);
        return nbt;
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }


    public void complete(PlayerEntity player) {
        this.completedPlayers.add(player.getUuid());
    }

    public boolean hasCompleted(PlayerEntity player) {
        return this.completedPlayers.contains(player.getUuid());
    }

    public void startChallenge(PlayerEntity player) {
        Phantasm.log("Starting challenge at " + this.getPos().toShortString());
    }
}
