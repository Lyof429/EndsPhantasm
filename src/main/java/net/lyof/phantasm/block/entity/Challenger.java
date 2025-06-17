package net.lyof.phantasm.block.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface Challenger {
    static Challenger get(UUID uuid, World world) {
        return (Challenger) world.getPlayerByUuid(uuid);
    }

    PlayerEntity phantasm$asPlayer();

    @Nullable ChallengeRuneBlockEntity phantasm$getRune();
    void phantasm$setRune(ChallengeRuneBlockEntity challengeRune);

    default double getDistance() {
        if (this.phantasm$getRune() == null) return 0;
        return this.phantasm$asPlayer().getPos().subtract(this.phantasm$getRune().getPos().toCenterPos()).length();
    }

    default boolean isInRange() {
        if (this.phantasm$getRune() == null) return false;
        return Math.abs(this.phantasm$asPlayer().getX() - 0.5 - this.phantasm$getRune().getPos().getX()) < 10
                && Math.abs(this.phantasm$asPlayer().getEyeY() - 0.5 - this.phantasm$getRune().getPos().getY()) < 10
                && Math.abs(this.phantasm$asPlayer().getZ() - 0.5 - this.phantasm$getRune().getPos().getZ()) < 10;
    }
}
