package net.lyof.phantasm.setup;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.lyof.phantasm.Phantasm;
import net.minecraft.util.Identifier;

public class ModPackets {
    public static final Identifier BEHEMOTH_WAKES_UP = Phantasm.makeID("behemoth_wakes_up");

    public static final Identifier CHALLENGE_STARTS = Phantasm.makeID("challenge_starts");
    public static final Identifier CHALLENGE_ENDS = Phantasm.makeID("challenge_ends");

    public static final Identifier BEGIN_CUTSCENE_STARTS = Phantasm.makeID("begin_cutscene_starts");
    public static final Identifier BEGIN_CUTSCENE_ENDS = Phantasm.makeID("begin_cutscene_ends");

    public static final Identifier POLYPPIE_UPDATES = Phantasm.makeID("polyppie_updates");
}
