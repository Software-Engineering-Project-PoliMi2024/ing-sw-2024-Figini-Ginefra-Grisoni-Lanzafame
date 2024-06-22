package it.polimi.ingsw.lightModel.lightTableRelated;

import it.polimi.ingsw.utils.Observed;
import it.polimi.ingsw.utils.Observer;
import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightChat;
import it.polimi.ingsw.model.playerReleted.PawnColors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

public class LightGameParty implements Differentiable, Observed {
    private String yourName;
    private String gameName;
    final private Map<String,Boolean> playerActiveList;
    private String currentPlayer;
    private String firstPlayerName;
    private List<PawnColors> pawnChoices = new ArrayList<>();
    private final Map<String, PawnColors> playersColor = new HashMap<>();
    private final List<Observer> observers = new LinkedList<>();
    private final LightChat lightChat = new LightChat();

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

    public PawnColors getPlayerColor(String nickname){
        return playersColor.get(nickname);
    }

    public List<PawnColors> getPawnChoices(){
        return pawnChoices;
    }

    public void addPlayerColor(String nickname, PawnColors color){
        playersColor.put(nickname, color);
        this.notifyObservers();
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
        this.notifyObservers();
    }

    /**
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
     * @param player the nickname of the player to set inactive
     */
    public void setInactivePlayer(String player){
        playerActiveList.put(player,false);
        this.notifyObservers();
    }
    /**
     * @param player nickname of the player to set active
     */
    public void setActivePlayer(String player){
        playerActiveList.put(player,true);
        this.notifyObservers();
    }

    public void setYourName(String yourName) {
        this.yourName = yourName;
    }
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

    public LightChat getLightChat() {
        return lightChat;
    }

    /**
     * @return the list of pawns that are not taken by any player
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
}
