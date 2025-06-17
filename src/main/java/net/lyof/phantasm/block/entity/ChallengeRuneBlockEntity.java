package net.lyof.phantasm.block.entity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
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
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChallengeRuneBlockEntity extends BlockEntity {
    private final List<UUID> completedPlayers;
    public int tick;

    public boolean renderBase;
    private final List<Vec3i> towerBases;

    public ChallengeRuneBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CHALLENGE_RUNE, pos, state);
        this.completedPlayers = new ArrayList<>();
        this.tick = -1;
        this.towerBases = new ArrayList<>();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        int size = nbt.getInt("CompletedPlayerCount");
        for (int i = 0; i < size; i++)
            this.completedPlayers.add(nbt.getUuid("Player" + i));
        this.renderBase = nbt.getBoolean("RenderBase");
        this.tick = nbt.getInt("ChallengeTick");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("CompletedPlayerCount", this.completedPlayers.size());
        for (int i = 0; i < this.completedPlayers.size(); i++)
            nbt.putUuid("Player" + i, this.completedPlayers.get(i));
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
        this.completedPlayers.add(player.getUuid());
    }

    public boolean hasCompleted(PlayerEntity player) {
        return this.completedPlayers.contains(player.getUuid());
    }

    public boolean canStart(ServerPlayerEntity player) {
        return !this.hasCompleted(player) && player.experienceLevel >= 5 && ((ServerPlayerEntityAccessor) player).getSeenCredits();
    }

    public void displayHint(ServerPlayerEntity user) {
        String message = "" + user.getRandom().nextInt(5);
        if (this.hasCompleted(user))
            message = ".completed" + message;
        else if (!((ServerPlayerEntityAccessor) user).getSeenCredits())
            message = ".dragon" + message;
        else if (user.experienceLevel < 5)
            message = ".experience" + message;

        user.sendMessage(Text.translatable("block.phantasm.challenge_rune.hint" + message).formatted(Formatting.LIGHT_PURPLE),
                true);
    }

    public void startChallenge(PlayerEntity player) {
        if (!player.getWorld().isClient()) {
            PacketByteBuf packet = PacketByteBufs.create();
            packet.writeBlockPos(this.getPos());
            ServerPlayNetworking.send((ServerPlayerEntity) player, ModPackets.CHALLENGE_STARTS, packet);
        }

        Phantasm.log("Starting challenge at " + this.getPos().toShortString());
        this.tick = 0;
    }

    public static void tick(World world, BlockPos pos, BlockState state, ChallengeRuneBlockEntity self) {
        if (self.tick < 0) return;


        self.tick++;
    }
}
