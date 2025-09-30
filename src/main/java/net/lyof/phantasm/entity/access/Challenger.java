package net.lyof.phantasm.entity.access;

import net.lyof.phantasm.block.challenge.Challenge;
import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface Challenger {
    static Challenger get(UUID uuid, World world) {
        return (Challenger) world.getPlayerByUuid(uuid);
    }

    PlayerEntity asPlayer();

    @Nullable ChallengeRuneBlockEntity getChallengeRune();
    void setChallengeRune(ChallengeRuneBlockEntity rune);

    default boolean isInRange() {
        if (this.getChallengeRune() == null) return false;

        Vec3d self = this.asPlayer().getEyePos();
        Vec3d rune = this.getChallengeRune().getPos().up((int) Challenge.R/2).toCenterPos();
        return Math.abs(self.x - rune.x) < Challenge.R
                && Math.abs(self.y - rune.y) < Challenge.R
                && Math.abs(self.z - rune.z) < Challenge.R;
    }
}
