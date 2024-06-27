package it.polimi.ingsw.lightModel.lightTableRelated;

import it.polimi.ingsw.utils.designPatterns.Observed;
import it.polimi.ingsw.utils.designPatterns.Observer;
import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightChat;
import it.polimi.ingsw.model.playerReleted.PawnColors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

/**
 * This class represents the game party seen by the view.
 * It holds all the information about the player, their activity status (activer or not)
 * the currentPlayer, the first player and the PawnColor chosen by each Player.
 * It also holds the chat of the game.
 */
public class LightGameParty implements Differentiable, Observed {
    /**The name of the client */
    private String yourName;
    /** The name of the game */
    private String gameName;
    /**A map holding the status of each player (true if active, false if not) */
    final private Map<String,Boolean> playerActiveMap;
    /**The nickname of the current player */
    private String currentPlayer;
    /**The nickname of the first player in the order turn*/
    private String firstPlayerName;
    /**The list of pawn colors that the player can choose from*/
    private List<PawnColors> pawnChoices = new ArrayList<>();
    /**The map of the player and the color of the pawn they chose.
     * If a player has not already chosen a color, the PawnColor is set to null */
    private final Map<String, PawnColors> playersColor = new HashMap<>();
    /**The list of observers of this class*/
    private final List<Observer> observers = new LinkedList<>();
    /**The chat of the game */
    private final LightChat lightChat = new LightChat();

    /**
     * Constructor of the class
     * Initialize the playerActiveMap
     */
    public LightGameParty() {
        this.playerActiveMap = new HashMap<>();
    }

    /**
     * Set the name of the game
     * @param gameName the name of the game
     */
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    /**
     * Set the possible pawn colors that the player can choose from
     * @param pawnColors the list of pawn colors
     */
    public void setPawnChoices(List<PawnColors> pawnColors){
        this.pawnChoices = pawnColors;
    }

    /**
     * @return the map of the player and the color of the pawn they chose
     */
    public Map<String, PawnColors> getPlayersColor(){
        return playersColor;
    }

    /** Return the color of a player
     * @param nickname the nickname of the player
     * @return the color of the player or null if the player has not chosen a color yet
     */
    public PawnColors getPlayerColor(String nickname){
        return playersColor.get(nickname);
    }

    /**
     * @return the list of pawn colors that the player can choose from
     */
    public List<PawnColors> getPawnChoices(){
        return pawnChoices;
    }

    /**
     * Add a player and the color of the pawn they chose
     * Notify the observers after the update
     * @param nickname the nickname of the player
     * @param color the color of the pawn
     */
    public void addPlayerColor(String nickname, PawnColors color){
        playersColor.put(nickname, color);
        this.notifyObservers();
    }

    /**
     * @return the map of the player and if they are active (true) or not (false)
     */
    public Map<String, Boolean> getPlayerActiveMap() {
        return playerActiveMap;
    }

    /**
     * @return the current player nickname
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @return the nickname of the first player in the turn order
     */
    public String getFirstPlayerName() {
        return firstPlayerName;
    }

    /**
     * Set the nickname of the first player in the turn order
     * @param firstPlayerName
     */
    public void setFirstPlayerName(String firstPlayerName) {
        this.firstPlayerName = firstPlayerName;
    }

    /**
     * Complete remove a player from the game by removing them from the playerActiveMap
     * Update the observers after the update
     * @param player the nickname of the player to remove
     */
    public void removePlayer(String player){
        playerActiveMap.remove(player);
        this.notifyObservers();
    }

    /**
     * Set the new current player
     * Notify the observers after the update
     * @param currentPlayer the current player playing
     */
    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.notifyObservers();
    }
    /**
     * @return the name of the game
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * Set the player as inactive in the playerActiveMap.
     * If the player is already inactive, the method does nothing
     * If the player is not present in the map, it adds the player with the value false
     * Notify the observers after the update
     * @param player the nickname of the player to set inactive
     */
    public void setInactivePlayer(String player){
        if(playerActiveMap.get(player)==null || playerActiveMap.get(player)){
            playerActiveMap.put(player,false);
            this.notifyObservers();
        }
    }

    /**
     * Set the player as active in the playerActiveMap.
     * If the player is already active, the method does nothing
     * If the player is not present in the map, it adds the player with the value true
     */
    public void setActivePlayer(String player){
        playerActiveMap.put(player,true);
        this.notifyObservers();
    }

    /**
     * Set the client name in the lightGameParty
     * @param yourName the name of the player who owns the client
     */
    public void setYourName(String yourName) {
        this.yourName = yourName;
    }

    /**
     * @return the pointer to the chat of the game
     */
    public LightChat getLightChat() {
        return lightChat;
    }

    /**
     * @return the list of pawns that are not taken by any player yet
     */
    public List<PawnColors> getFreePawns(){
        List<PawnColors> freePawns = new ArrayList<>();
        for(PawnColors pawn : PawnColors.values()){
            if(!playersColor.containsValue(pawn) && pawn != PawnColors.BLACK){
                freePawns.add(pawn);
            }
        }
        return freePawns;
    }

    /**
     * @return the name of the client
     */
    public String getYourName() {
        return this.yourName ;
    }
    @Override
    public void attach(Observer observer) {
        observer.update();
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }
}
