package it.polimi.ingsw.view.GUI.Components;

import java.util.*;

public class PlayerPawnManager {
    private Map<String, PawnsGui> playerPawnMap;
    private List<PawnsGui> availablePawns;

    public PlayerPawnManager() {
        playerPawnMap = new HashMap<>();
        availablePawns = new ArrayList<>(Arrays.asList(PawnsGui.values()));
        availablePawns.remove(PawnsGui.BLACK); // Remove BLACK as it is assigned to the first player
    }

    public PawnsGui assignPawn(String playerName, boolean isFirstPlayer) {
        if (isFirstPlayer) {
            playerPawnMap.put(playerName, PawnsGui.BLACK);
            return PawnsGui.BLACK;
        }
        if (!playerPawnMap.containsKey(playerName) && !availablePawns.isEmpty()) {
            playerPawnMap.put(playerName, availablePawns.remove(0));
        }
        return playerPawnMap.get(playerName);
    }

    public PawnsGui getPawn(String playerName) {
        return playerPawnMap.get(playerName);
    }

    public List<PawnsGui> getAvailablePawns() {
        return new ArrayList<>(availablePawns);
    }

    public Map<String, PawnsGui> getPlayerPawnMap() {
        return playerPawnMap;
    }
}