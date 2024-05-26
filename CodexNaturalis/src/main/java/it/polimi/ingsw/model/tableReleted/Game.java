package it.polimi.ingsw.model.tableReleted;


import it.polimi.ingsw.controller.GameLoopController;
import it.polimi.ingsw.controller3.mediators.loggerAndUpdaterMediators.GameMediator;
import it.polimi.ingsw.controller3.mediators.gameJoinerAndTurnTakerMediators.TurnTaker;
import it.polimi.ingsw.lightModel.diffs.game.GameDiff;
import it.polimi.ingsw.lightModel.diffPublishers.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffPublishers.GameDiffPublisher;
import it.polimi.ingsw.model.cardReleted.cards.*;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.view.ViewInterface;

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
    private final GameParty gameParty;

    private boolean isLastTurn;
    private final Object isLastTurnLock = new Object();

    private final List<ObjectiveCard> commonObjective;
    private final GameMediator gameMediator;
    /**
     * Constructs a new Game instance with a specified maximum number of players.
     */
    public Game(Lobby lobby, CardLookUp<ObjectiveCard> objectiveCardCardLookUp, CardLookUp<ResourceCard> resourceCardCardLookUp,
                CardLookUp<StartCard> startCardCardLookUp, CardLookUp<GoldCard> goldCardCardLookUp) {
        this.name = lobby.getLobbyName();
        this.gameParty = new GameParty(lobby.getLobbyPlayerList());
        objectiveCardDeck = new Deck<>(0,objectiveCardCardLookUp.getQueue());
        resourceCardDeck = new Deck<>(2, resourceCardCardLookUp.getQueue());
        startingCardDeck = new Deck<>(0, startCardCardLookUp.getQueue());
        goldCardDeck = new Deck<>(2, goldCardCardLookUp.getQueue());
        this.commonObjective = new ArrayList<>();
        this.populateCommonObjective();
        gameDiffPublisher = new GameDiffPublisher(this);
        this.gameLoopController = new GameLoopController(this);
        gameMediator = new GameMediator();
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

    //game party methods

    public GameParty getGameParty() {
        return gameParty;
    }

    /**
     * This method is used to set the player index in the game
     * @param index the index of the player that is playing
     */
    public void setPlayerIndex(int index){
        gameParty.setPlayerIndex(index);
    }

    /**
     * This method is used to get the index of the next player that is supposed to play
     * not considering the possible disconnection that may have occurred
     * @return the index of the next player
     */
    public int getNextPlayerIndex() {
        return gameParty.getNextPlayerIndex();
    }

    /** @return list of the User in this match*/
    public List<User> getUsersList() {
        return gameParty.getUsersList();
    }

    /**
     * This method is used to get the user from its nickname
     * if the user is not in the gameParty, it returns null
     * @param nickname the nickname of the user
     * @return the user with the given nickname; returns null if the user is not in the gameParty
     */
    public User getUserFromNick(String nickname){
        return gameParty.getUserFromNick(nickname);
    }

    /**
     * This method is used to get the player currently playing
     * @return the player currently playing
     */
    public User getCurrentPlayer() {
        return gameParty.getCurrentPlayer();
    }

    /***
     * @return the number Of Player needed to start the game
     */
    public int getNumberOfMaxPlayer() {
        return gameParty.getNumberOfMaxPlayer();
    }

    /**
     * Remove a user from the gameParty preventing them from joining the game later
     * @param nickname of the user being removed
     */
    public void removeUser(String nickname){
        gameParty.removeUser(nickname);
    }

    /**
     * subscribe a turnTaker to the activeTurnTakerMediator in order to receive notification
     * on when it is their turn to play
     * @param nickname the nickname of the player that is subscribing
     * @param LoggerAndUpdater the view that is subscribing
     * @param turnTaker the turnTaker that is subscribing
     */
    public void subscribe(String nickname, ViewInterface LoggerAndUpdater, TurnTaker turnTaker, boolean sendPublicObj){
        this.gameMediator.subscribe(nickname, LoggerAndUpdater, this, sendPublicObj);
        gameParty.subscribe(nickname, turnTaker);
    }

    /**
     * unsubscribe a player from the activeTurnTakerMediator
     * removing them from the list of players that will receive notifications
     * @param nickname the nickname of the player that is unsubscribing
     */
    public void unsubscribe(String nickname){
        this.gameMediator.unsubscribe(nickname);
        gameParty.unsubscribe(nickname);
    }

    /**
     * notify all players that the startCardFace selection phase has finished
     * and move to the next phase
     */
    public void fromStartCardMoveOnToSecretObjectiveSelection(){
        gameMediator.notifyAllChoseStartCardFace();
        gameParty.notifyChooseObjective();
    }

    /**
     * notify all players that the secretObjective selection phase has finished
     * and move to the next phase
     */
    public void fromSecretObjectiveMoveOnToGame(){
        gameMediator.notifyAllChoseSecretObjective();
        gameParty.notifyTurn();
    }

    /**
     * @return true if the players in game are choosing the startCard
     */
    public boolean isInStartCardState(){
        return gameParty.getUsersList().stream().allMatch(user ->user.getUserHand().getStartCard()!=null);
    }

    /**
     * @return true if the players in game are choosing the secretObjective
     */
    public boolean inInSecretObjState(){
        return gameParty.getUsersList().stream().allMatch(user->user.getUserHand().getSecretObjectiveChoices() != null);
    }

    /**
     * @return true if the players in game are in the setup phase
     * (choosing StartCardFace or SecretObjective)
     */
    public boolean isInSetup(){
        return inInSecretObjState() || isInStartCardState();
    }
    /**
     * notify a player that it is their turn to play by
     * calling the takeTurnMediator method of the subscriber
     */
    public void notifyTurn(){
        gameParty.notifyTurn();
    }

    /**
     * This method is used to notify all players that
     * it is his turn to choose the objective
     */
    public void notifyChooseObjective(){
        gameParty.notifyChooseObjective();
    }
    /**
     * get the list of active players
     * @return the list of active players
     */
    public List<String> getActivePlayers(){
        return gameParty.getActivePlayers();
    }

    /**
     * This method is used to check if a player is active
     * @param nickname the nickname of the player that wants to check if it is active
     * @return true if the player is active, false otherwise
     */
    public synchronized boolean isPlayerActive(String nickname){
        return gameParty.isPlayerActive(nickname);
    }

    /**
     * This method is used to lock the current player lock
     */
    public void lockCurrentPlayer(){
        gameParty.lockCurrentPlayer();
    }

    /**
     * This method is used to unlock the current player lock
     */
    public void unlockCurrentPlayer(){
        gameParty.unlockCurrentPlayer();
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

    public boolean decksAreEmpty(){
        return goldCardDeck.isEmpty() && resourceCardDeck.isEmpty();
    }

    public void setLastTurn(boolean lastTurn) {
        synchronized (isLastTurnLock) {
            isLastTurn = lastTurn;
        }
    }

    public boolean isLastTurn() {
        synchronized (isLastTurnLock) {
            return isLastTurn;
        }
    }

    private void populateCommonObjective(){
        synchronized (commonObjective) {
            for (int i = 0; i < 2; i++) {
                commonObjective.add(objectiveCardDeck.drawFromDeck());
            }
        }
    }
    public List<ObjectiveCard> getCommonObjective() {
        synchronized (commonObjective) {
            return new ArrayList<>(commonObjective);
        }
    }

    public void removeUser(User user){
        this.gameParty.removeUser(user.getNickname());
    }

    public User nextPlayer(){
        return  this.gameParty.nextPlayer();
    }
}
