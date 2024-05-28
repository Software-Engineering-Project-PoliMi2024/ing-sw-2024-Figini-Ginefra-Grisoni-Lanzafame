package it.polimi.ingsw.model.tableReleted;


import it.polimi.ingsw.controller.GameLoopController;
import it.polimi.ingsw.controller3.mediators.gameJoinerAndTurnTakerMediators.TurnTakerMediator;
import it.polimi.ingsw.controller3.mediators.loggerAndUpdaterMediators.GameMediator;
import it.polimi.ingsw.controller3.mediators.gameJoinerAndTurnTakerMediators.TurnTaker;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffs.game.GameDiff;
import it.polimi.ingsw.lightModel.diffPublishers.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffPublishers.GameDiffPublisher;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.cards.*;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.Hand;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;


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
    private final List<ObjectiveCard> commonObjective;

    private boolean isLastTurn;
    private final Object isLastTurnLock = new Object();
    private final ReentrantLock calculateNextStateLock = new ReentrantLock(true);

    private final TurnTakerMediator activeTurnTakerMediator = new TurnTakerMediator();
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
    public void subscribe(String nickname, ViewInterface LoggerAndUpdater, TurnTaker turnTaker, boolean rejoining){
        this.gameMediator.subscribe(nickname, LoggerAndUpdater, rejoining);
        activeTurnTakerMediator.subscribe(nickname, turnTaker);
    }

    public void joinStartCard(String joiner, Game game){
        gameMediator.updateJoinStartCard(joiner, game);
    }

    public void joinSecretObjective(String joiner, Game game){
        gameMediator.updateJoinObjectiveSelect(joiner, game);
    }

    public void joinMidGame(String joiner, Game game){
        gameMediator.updateJoinActualGame(joiner, game);
    }
    /**
     * unsubscribe a player from the activeTurnTakerMediator
     * removing them from the list of players that will receive notifications
     * @param nickname the nickname of the player that is unsubscribing
     */
    public void unsubscribe(String nickname){
        this.gameMediator.unsubscribe(nickname);
        activeTurnTakerMediator.unsubscribe(nickname);
    }

    /**
     * notify all players that the startCardFace selection phase has finished
     * and move to the next phase
     */
    public void notifyMoveToSelectObjState(){
        gameMediator.notifyAllChoseStartCardFace();
        activeTurnTakerMediator.notifyChooseObjective();
    }

    /**
     * notify all players that the secretObjective selection phase has finished
     * and move to the next phase
     */
    public void notifyEndSetupStartActualGame(){
        List<LightCard> lightCommonObjective = commonObjective.stream().map(Lightifier::lightifyToCard).toList();
        gameMediator.notifyAllChoseSecretObjective(lightCommonObjective);
        activeTurnTakerMediator.notifyTurn();
    }

    public void notifyStartCardFaceChoice(String placer, User user, LightPlacement placement){
        gameMediator.notifyStartCardFaceChoice(placer, user, placement);
    }

    public void notifySecretObjectiveChoice(String choice, LightCard objCard){
        gameMediator.notifySecretObjectiveChoice(choice, objCard);
    }

    public void notifyPlacement(String placer, LightPlacement newPlacement, Codex placerCodex, Map<LightCard, Boolean> playability){
        gameMediator.notifyPlacement(placer, newPlacement, placerCodex, playability);
    }

    /**
     * @return true if the players in game are choosing the startCard
     */
    public synchronized boolean isInStartCardState(){
        return gameParty.getUsersList().stream().map(User::getUserHand).map(Hand::getStartCard).anyMatch(Objects::nonNull);
    }

    /**
     * @return true if the players in game are choosing the secretObjective
     */
    public synchronized boolean inInSecretObjState(){
        return gameParty.getUsersList().stream().map(User::getUserHand).map(Hand::getSecretObjectiveChoices).anyMatch(Objects::nonNull);
    }

    /**
     * @return true if the players in game are in the setup phase
     * (choosing StartCardFace or SecretObjective)
     */
    public synchronized boolean isInSetup(){
        return inInSecretObjState() || isInStartCardState();
    }
    /**
     * notify a player that it is their turn to play by
     * calling the takeTurnMediator method of the subscriber
     * @param nicknameOfNextPlayer the nickname of the player that is goint to play next
     */
    public void notifyTurn(String nicknameOfNextPlayer){
        gameMediator.notifyTurnChange(nicknameOfNextPlayer);
        activeTurnTakerMediator.notifyTurn();
    }

    /**
     * This method is used to notify all players a change in the decks
     * @param deckType the type of the deck that has changed (GOLD, RESOURCE)
     * @param pos the position of the card that has changed (2 = from deck, 1,0 = buffer)
     * @param card the card that has changed
     * @param drawerNickname the nickname of the player that has drawn the card
     */
    public void notifyDraw(DrawableCard deckType, int pos, LightCard card, String drawerNickname, boolean playability){
        gameMediator.notifyDraw(drawerNickname, deckType, pos, card, playability);
    }

    /**
     * This method is used to check if the decks are empty
     * @return true if the decks are empty, false otherwise
     */
    public boolean areDeckEmpty(){
        return goldCardDeck.isEmpty() && resourceCardDeck.isEmpty();
    }

    /**
     * @param deck from which drawn a Card
     * @param cardID the position from where draw the card (buffer/deck)
     * @return the card drawn
     * @param <T> a CardInHand (GoldCard/ResourceCard)
     */
    public  <T extends CardInHand> T drawACard(Deck<T> deck, int cardID) {
        T drawCard;
        if (cardID == 2) {
            drawCard = deck.drawFromDeck();
        } else {
            drawCard = deck.drawFromBuffer(cardID);
        }
        return drawCard;
    }

    /**
     * This method is used to notify all players that
     * it is his turn to choose the objective
     */
    public void notifyChooseObjective(){
        activeTurnTakerMediator.notifyChooseObjective();
    }

    public void secretObjectiveSetup(){
        gameMediator.secretObjectiveSetup(this);
    }
    /**
     * get the list of active players
     * @return the list of active players
     */
    public List<String> getActivePlayers(){
        return activeTurnTakerMediator.getActivePlayers();
    }

    /**
     * This method is used to check if a player is active
     * @param nickname the nickname of the player that wants to check if it is active
     * @return true if the player is active, false otherwise
     */
    public boolean isPlayerActive(String nickname){
        return activeTurnTakerMediator.isPlayerActive(nickname);
    }

    /**
     * This method is used to get the player from its index
     * @param index the index of the player
     * @return the player with the given index
     */
    public User getPlayerFromIndex(int index){
        return gameParty.getPlayerFromIndex(index);
    }
    /**
     * This method is used to lock the current player lock
     */
    public void lock(){
        calculateNextStateLock.lock();
    }

    /**
     * This method is used to unlock the current player lock
     */
    public void unlock(){
        calculateNextStateLock.unlock();
    }

    public synchronized boolean othersHadAllPlacedStartCard(String nicknamePerspective){
        boolean check = false;
        if (!isInStartCardState())
            throw new IllegalCallerException("Controller.checkIfLastToPlaceStartCard: Game is not in StartCardState");

        if(gameParty.getUsersList().stream().allMatch(user ->
                !user.getNickname().equals(nicknamePerspective) && user.hasPlacedStartCard())){
            check = true;
        }
        return check;
    }

    public synchronized boolean othersHadAllChooseSecretObjective(String nicknamePerspective){
        boolean check = false;

        if(!inInSecretObjState())
            throw new IllegalCallerException("Controller.checkIfLastToChooseSecretObjective: Game is not in SelectSecretObjectiveState");

        if(gameParty.getUsersList().stream().allMatch(user ->
                !user.getNickname().equals(nicknamePerspective) && user.hasChosenObjective())){
            check = true;
        }
        return check;
    }

    public synchronized boolean isYourTurnToPlace(String nickname){
        User you = getUserFromNick(nickname);

        return (isInStartCardState() && !you.hasPlacedStartCard()) || (
                getCurrentPlayer().getNickname().equals(nickname)
                        && you.getUserHand().getHand().size() == 3);
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
