package it.polimi.ingsw.model.tableReleted;


import it.polimi.ingsw.controller2.GameLoopController;
import it.polimi.ingsw.lightModel.diffs.game.GameDiff;
import it.polimi.ingsw.lightModel.diffPublishers.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffPublishers.GameDiffPublisher;
import it.polimi.ingsw.model.cardReleted.cards.*;
import it.polimi.ingsw.model.playerReleted.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * This class represents the game as a whole.
 * It contains a reference to all the players who are playing the match
 * and the decks that are being used.
 */
public class Game implements Serializable {
    private final GameDiffPublisher gameDiffPublisher;
    private final GameLoopController gameLoopController;
    final private Deck<ObjectiveCard> objectiveCardDeck;
    final private Deck<ResourceCard> resourceCardDeck;
    final private Deck<GoldCard> goldCardDeck;
    final private Deck<StartCard> startingCardDeck;
    private final String name;
    private GameParty gameParty;
    private boolean isLastTurn;
    private final List<ObjectiveCard> commonObjective;
    /**
     * Constructs a new Game instance with a specified maximum number of players.
     */
    public Game(Lobby lobby, CardLookUp<ObjectiveCard> objectiveCardCardLookUp, CardLookUp<ResourceCard> resourceCardCardLookUp,
                CardLookUp<StartCard> startCardCardLookUp, CardLookUp<GoldCard> goldCardCardLookUp) {
        gameDiffPublisher = new GameDiffPublisher(this);
        this.name = lobby.getLobbyName();
        this.gameParty = new GameParty(lobby.getLobbyPlayerList());
        objectiveCardDeck = new Deck<>(0,objectiveCardCardLookUp.getQueue());
        resourceCardDeck = new Deck<>(2, resourceCardCardLookUp.getQueue());
        startingCardDeck = new Deck<>(0, startCardCardLookUp.getQueue());
        goldCardDeck = new Deck<>(2, goldCardCardLookUp.getQueue());
        this.commonObjective = new ArrayList<>();
        this.populateCommonObjective();
        this.gameLoopController = new GameLoopController(this);
    }

    /** @return the Objective Card Deck*/
    public Deck<ObjectiveCard> getObjectiveCardDeck() {
        return objectiveCardDeck;
    }

    /** @return the Resource Card Deck*/
    public Deck<ResourceCard> getResourceCardDeck() {
        return resourceCardDeck;
    }

    /** @return the Gold Card Deck*/
    public Deck<GoldCard> getGoldCardDeck() {
        return goldCardDeck;
    }

    /** @return the Starting Card Deck*/
    public Deck<StartCard> getStartingCardDeck() {
        return startingCardDeck;
    }

    public String getName() {
        return name;
    }

    public GameParty getGameParty() {
        return new GameParty(gameParty);
    }

    public void setGameParty(GameParty gameParty){

        this.gameParty = new GameParty(gameParty);
    }
    @Override
    public String toString() {

        return "Game{" +
                "name='" + name + '\'' +
                ", usersList=" + gameParty.getUsersList().stream().map(User::getNickname).reduce("", (a, b) -> a + " " + b) +
                //", currentPlayer=" + currentPlayer.getNickname() +
                ", numberOfMaxPlayer=" + gameParty.getNumberOfMaxPlayer() +
                '}';
    }

    public void subscribe(DiffSubscriber diffSubscriber, String nickname){
        gameDiffPublisher.subscribe(diffSubscriber, nickname);
    }

    public void subscribe(GameDiff gameDiff){
        gameDiffPublisher.subscribe(gameDiff);
    }

    public void subscribe(DiffSubscriber diffSubscriber, GameDiff gameDiffYou, GameDiff gameDiffOther){
        gameDiffPublisher.subscribe(diffSubscriber, gameDiffYou, gameDiffOther);
    }
    public void unsubscribe(DiffSubscriber diffSubscriber){
        gameDiffPublisher.unsubscribe(diffSubscriber);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Game && ((Game) obj).name.equals(name);
    }

    public GameLoopController getGameLoopController() {
        return gameLoopController;
    }

    public User getUserFromNick(String nickname){
        for(User user : this.getGameParty().getUsersList()){
            if(user.getNickname().equals(nickname)){
                return user;
            }
        }
        throw new IllegalCallerException("Nickname not found in this game");
    }

    public boolean decksAreEmpty(){
        return goldCardDeck.isEmpty() && resourceCardDeck.isEmpty();
    }

    public void setLastTurn(boolean lastTurn) {
        isLastTurn = lastTurn;
    }

    public boolean isLastTurn() {
        return isLastTurn;
    }

    private void populateCommonObjective(){
        for(int i = 0;i<2;i++){
            commonObjective.add(objectiveCardDeck.drawFromDeck());
        }
    }
    public List<ObjectiveCard> getCommonObjective() {
        return commonObjective;
    }

    public void removeUser(User user){
        this.gameParty.removeUser(user);
    }

    public User nextPlayer(){
        return  this.gameParty.nextPlayer();
    }
}
