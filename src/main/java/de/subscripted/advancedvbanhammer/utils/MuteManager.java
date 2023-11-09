package de.subscripted.advancedvbanhammer.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MuteManager {
    private final Map<UUID, Boolean> mutedPlayers;

    public MuteManager() {
        mutedPlayers = new HashMap<>();
    }

    public void mutePlayer(UUID playerId, boolean muted) {
        mutedPlayers.put(playerId, muted);
    }

    public boolean isMuted(UUID playerId) {
        return mutedPlayers.getOrDefault(playerId, false);
    }

    public void unmutePlayer(UUID playerId) {
        mutedPlayers.remove(playerId);
    }
}