package net.lyof.phantasm.setup;

import net.lyof.phantasm.Phantasm;
import net.minecraft.util.Identifier;

public class ModPackets {
    public static final Identifier INITIALIZE = Phantasm.makeID("initialize");

    public static final Identifier BEHEMOTH_WAKES_UP = Phantasm.makeID("behemoth_wakes_up");

    public static final Identifier CHALLENGE_STARTS = Phantasm.makeID("challenge_starts");
    public static final Identifier CHALLENGE_ENDS = Phantasm.makeID("challenge_ends");

    public static final Identifier BEGIN_CUTSCENE_STARTS = Phantasm.makeID("begin_cutscene_starts");
    public static final Identifier BEGIN_CUTSCENE_ENDS = Phantasm.makeID("begin_cutscene_ends");

    public static final Identifier POLYPPIE_UPDATES = Phantasm.makeID("polyppie_updates");
    public static final Identifier POLYPPIE_STARTS_BEING_CARRIED = Phantasm.makeID("polyppie_starts_being_carried");
    public static final Identifier POLYPPIE_STOPS_BEING_CARRIED = Phantasm.makeID("polyppie_stops_being_carried");
    public static final Identifier POLYPPIE_SETS_VARIANT = Phantasm.makeID("polyppie_sets_variant");
}
