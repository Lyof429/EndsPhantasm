package net.lyof.phantasm.block.challenge;

import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface Challenger {
    static Challenger get(UUID uuid, World world) {
        return (Challenger) world.getPlayerByUuid(uuid);
    }
    float R = 15.99f;

    PlayerEntity asPlayer();

    @Nullable ChallengeRuneBlockEntity getChallengeRune();
    void setChallengeRune(ChallengeRuneBlockEntity challengeRune);

    default double getDistance() {
        if (this.getChallengeRune() == null) return 0;
        return this.asPlayer().getPos().subtract(this.getChallengeRune().getPos().toCenterPos()).length();
    }

    default boolean isInRange() {
        if (this.getChallengeRune() == null) return false;

        Vec3d self = this.asPlayer().getEyePos();
        Vec3d rune = this.getChallengeRune().getPos().up((int) R/2).toCenterPos();
        return Math.abs(self.x - rune.x) < R
                && Math.abs(self.y - rune.y) < R
                && Math.abs(self.z - rune.z) < R;
    }
}
