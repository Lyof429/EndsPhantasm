package net.lyof.phantasm.block.challenge;

import com.google.common.collect.ImmutableSet;
import net.lyof.phantasm.Phantasm;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChallengeRegistry {
    private static final Map<Identifier, Challenge> REGISTRY = new HashMap<>();
    public static final Challenge EMPTY = register(Phantasm.makeID("/empty"),
            new Challenge(Phantasm.makeID("/empty"), Phantasm.makeID("/empty"), List.of(), 0, 0, false));

    public static Challenge register(Identifier id, Challenge data) {
        REGISTRY.putIfAbsent(id, data);
        return data;
    }

    public static Challenge get(Identifier id) {
        if (!REGISTRY.containsKey(id)) Phantasm.log("Requesting ChallengeData for a non registered id: " + id, 2);
        return REGISTRY.getOrDefault(id, EMPTY);
    }

    public static void clear() {
        ImmutableSet.copyOf(REGISTRY.entrySet()).stream().filter(e -> e.getValue().dataDriven).forEach(REGISTRY::remove);
    }
}
