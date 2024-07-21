package me.auvq.region.flag;

import lombok.Getter;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class FlagManager {

    public enum FlagType {
        BLOCK_BREAK,
        BLOCK_PLACE,
        ENTITY_DAMAGE,
        INTERACT
    }

    @Getter
    private static final Map<FlagType, Supplier<Flag>> flagCreators = new EnumMap<>(FlagType.class);

    public static void registerFlagCreator(FlagType type, Supplier<Flag> creator) {
        flagCreators.put(type, creator);
    }

    public static Flag createFlagInstance(FlagType type) {
        Supplier<Flag> creator = getFlag(type);
        return creator != null ? creator.get() : null;
    }

    private static Supplier<Flag> getFlag(FlagType type) {
        return flagCreators.get(type);
    }

    public static List<Flag> createAllFlagInstances() {
        List<Flag> allFlags = new ArrayList<>();
        for (Supplier<Flag> creator : flagCreators.values()) {
            allFlags.add(creator.get());
        }
        return allFlags;
    }
}