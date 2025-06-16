package net.lyof.phantasm.block.entity;

import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlockEntities;
import static net.lyof.phantasm.world.feature.custom.ObsidianTowerStructure.R;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.event.listener.GameEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChallengeRuneBlockEntity extends BlockEntity {
    private final List<UUID> completedPlayers;
    private final List<Vec3i> towerBases;

    public ChallengeRuneBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CHALLENGE_RUNE, pos, state);
        this.completedPlayers = new ArrayList<>();
        this.towerBases = new ArrayList<>();
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
        Phantasm.log(this.towerBases);
    }

    public void complete(PlayerEntity player) {
        this.completedPlayers.add(player.getUuid());
    }

    public boolean hasCompleted(PlayerEntity player) {
        return this.completedPlayers.contains(player.getUuid());
    }

    public boolean canStart(PlayerEntity player) {
        return !this.hasCompleted(player);
    }

    public void startChallenge(PlayerEntity player) {
        Phantasm.log("Starting challenge at " + this.getPos().toShortString());
        this.complete(player);
    }
}
