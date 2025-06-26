package net.lyof.phantasm.block.challenge;

import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
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
        return Math.abs(this.asPlayer().getX() - 0.5 - this.getChallengeRune().getPos().getX()) < R
                && Math.abs(this.asPlayer().getEyeY() - 0.5 - this.getChallengeRune().getPos().getY()) < R
                && Math.abs(this.asPlayer().getZ() - 0.5 - this.getChallengeRune().getPos().getZ()) < R;
    }
}
