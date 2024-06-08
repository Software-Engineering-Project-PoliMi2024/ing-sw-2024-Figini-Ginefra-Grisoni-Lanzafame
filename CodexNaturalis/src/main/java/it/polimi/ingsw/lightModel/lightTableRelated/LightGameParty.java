package it.polimi.ingsw.lightModel.lightTableRelated;

import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.model.playerReleted.PawnColors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LightGameParty implements Differentiable {
    private String yourName;
    private String gameName;
    final private Map<String,Boolean> playerActiveList;
    private String currentPlayer;
    private String firstPlayerName;
    private List<PawnColors> pawnChoices = new ArrayList<>();
    private final Map<String, PawnColors> playersColor = new HashMap<>();

    public LightGameParty() {
        this.playerActiveList = new HashMap<>();
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setPawnChoices(List<PawnColors> pawnColors){
        this.pawnChoices = pawnColors;
    }

    public Map<String, PawnColors> getPlayersColor(){
        return playersColor;
    }

    public List<PawnColors> getPawnChoices(){
        return pawnChoices;
    }

    public void addPlayerColor(String nickname, PawnColors color){
        playersColor.put(nickname, color);
    }

    /**
     * @param gameName the name of the game
     * @param playerNicknames  the list of the player nicknames
     * @param currentPlayer the current player
     */
    public LightGameParty(String gameName, List<String> playerNicknames, String currentPlayer){
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

    public String getFirstPlayerName() {
        return firstPlayerName;
    }

    public void setFirstPlayerName(String firstPlayerName) {
        this.firstPlayerName = firstPlayerName;
    }

    public void removePlayer(String player){
        playerActiveList.remove(player);
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

    public void setYourName(String yourName) {
        this.yourName = yourName;
    }
    public String getYourName() {
        return this.yourName ;
    }
}
