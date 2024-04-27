package it.polimi.ingsw.lightModel;

import it.polimi.ingsw.model.playerReleted.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LightGame implements Differentiable{
    private final String gameName;
    final private Map<String,Boolean> playerActiveList;
    private String currentPlayer;

    /**
     * @param gameName the name of the game
     * @param playerActiveList the list of the player and if they are active
     * @param currentPlayer the current player
     */
    public LightGame(String gameName, Map<String,Boolean> playerActiveList, String currentPlayer) {
        this.gameName = gameName;
        this.playerActiveList = playerActiveList;
        this.currentPlayer = currentPlayer;
    }

    /**
     * @param gameName the name of the game
     * @param playerNicknames  the list of the player nicknames
     * @param currentPlayer the current player
     */
    public LightGame(String gameName, List<String> playerNicknames, String currentPlayer){
        this.gameName = gameName;
        this.currentPlayer = currentPlayer;
        this.playerActiveList = new HashMap<>();
        for (String player : playerNicknames) {
            playerActiveList.put(player, true);
        }
    }
    /**
     * @return the map of the player and if they are active
     */
    public Map<String, Boolean> getPlayerActiveList() {
        return playerActiveList;
    }
    /**
     * @return the current player playing
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @param currentPlayer the current player playing
     */
    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    /**
     * @return the name of the game
     */
    public String getGameName() {
        return gameName;
    }
    /**
     * @param player the nickname of the player to set inactive
     */
    public void setInactivePlayer(String player){
        playerActiveList.put(player,false);
    }
    /**
     * @param player nickname of the player to set active
     */
    public void setActivePlayer(String player){
        playerActiveList.put(player,true);
    }
}
