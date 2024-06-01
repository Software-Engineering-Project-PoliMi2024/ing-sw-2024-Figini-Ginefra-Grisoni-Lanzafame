package it.polimi.ingsw.model.tableReleted;


import it.polimi.ingsw.Configs;
import it.polimi.ingsw.controller.GameLoopController;
import it.polimi.ingsw.controller3.mediators.gameJoinerAndTurnTakerMediators.TurnTakerMediator;
import it.polimi.ingsw.controller3.mediators.loggerAndUpdaterMediators.GameMediator;
import it.polimi.ingsw.lightModel.diffs.game.GameDiff;
import it.polimi.ingsw.lightModel.diffPublishers.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffPublishers.GameDiffPublisher;
import it.polimi.ingsw.model.cardReleted.cards.*;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.Hand;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.utilities.Pair;

import java.io.Serializable;
import java.util.*;
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
    private final List<ObjectiveCard> commonObjective;

    private Integer lastTurnsCounter = null;
    private final Object turnLock = new Object();

    private final TurnTakerMediator activeTurnTakerMediator = new TurnTakerMediator();
    private final GameMediator gameMediator;

    private Timer countdownTimer;
    /**
     * Constructs a new Game instance with a specified maximum number of players.
     */
    public Game(Lobby lobby, Deck<ObjectiveCard> objectiveCardDeck, Deck<ResourceCard> resourceCardDeck,
                Deck<GoldCard> goldCardDeck, Deck<StartCard> startingCardDeck) {
        this.name = lobby.getLobbyName();
        this.gameParty = new GameParty(lobby.getLobbyPlayerList());
        this.objectiveCardDeck = objectiveCardDeck;
        this.resourceCardDeck = resourceCardDeck;
        this.startingCardDeck = startingCardDeck;
        this.goldCardDeck = goldCardDeck;
        this.commonObjective = new ArrayList<>();
        this.populateCommonObjective();
        this.setupStartCard();

        gameDiffPublisher = new GameDiffPublisher(this);
        this.gameLoopController = new GameLoopController(this);
        gameMediator = new GameMediator();
    }

    /** @return the Objective Card Deck*/
    public Deck<ObjectiveCard> getObjectiveCardDeck() {
        synchronized (objectiveCardDeck) {
            return new Deck<>(objectiveCardDeck);
        }
    }

    /** @return the Resource Card Deck*/
    public Deck<ResourceCard> getResourceCardDeck() {
        synchronized (resourceCardDeck) {
            return new Deck<>(resourceCardDeck);
        }
    }

    /** @return the Gold Card Deck*/
    public Deck<GoldCard> getGoldCardDeck() {
        synchronized (goldCardDeck){
            return new Deck<>(goldCardDeck);
        }
    }

    /** @return the Starting Card Deck*/
    public Deck<StartCard> getStartingCardDeck() {
        synchronized (startingCardDeck) {
            return startingCardDeck;
        }
    }

    public StartCard drawStartCard(){
        synchronized (startingCardDeck) {
            return startingCardDeck.drawFromDeck();
        }
    }

    public ObjectiveCard drawObjectiveCard(){
        synchronized (objectiveCardDeck) {
            return objectiveCardDeck.drawFromDeck();
        }
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
    public void setCurrentPlayerIndex(int index){
        gameParty.setCurrentPlayerIndex(index);
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
     * @return the active player that is the first in order in the game turn
     */
    public String getFirstActivePlayer() {
        synchronized (turnLock) {
            List<String> activePlayers = this.getActivePlayers();
            List<String> turnsOrder = this.getUsersList().stream().map(User::getNickname).toList();

            return turnsOrder.stream().filter(activePlayers::contains).findFirst().orElse(null);
        }
    }

    /**
     * @return the active player that is the last in order in the game turn
     */
    public String getLastActivePlayer(){
        List<String> activePlayers = this.getActivePlayers();
        ArrayList<String> turnsOrder = new ArrayList<>(this.getUsersList().stream().map(User::getNickname).toList());
        Collections.reverse(turnsOrder);

        return turnsOrder.stream().filter(activePlayers::contains).findFirst().orElse(null);
    }

    /**
     * adds points given by common objective and by the secret objective of the user
     * to the codex of the user
     */
    public void addObjectivePoints(){
        for(User user : getUsersList()){
            for(ObjectiveCard commonObj : commonObjective){
                user.getUserCodex().pointsFromObjective(commonObj);
            }
            user.getUserCodex().pointsFromObjective(user.getUserHand().getSecretObjective());
        }
    }

    /**
     * @return a Map that maps to each user nickName their points earned
     */
    public Map<String, Integer> getPointPerPlayerMap(){
        return gameParty.getPointPerPlayerMap();
    }

    /**
     * @return a List containing the winner(s) of the game
     */
    public List<String> getWinners(){
        Map<String, Integer> pointsPerPlayer = getPointPerPlayerMap();
        //Calculate all possibleWinners player(s) who scored the max amount of points in the game
        int maxPoint = pointsPerPlayer.values().stream().max(Integer::compareTo).orElse(0);
        List<String> playerMaxPoint = new ArrayList<>(pointsPerPlayer.keySet().stream().filter(nick -> pointsPerPlayer.get(nick) == maxPoint).toList());
        //calculate the number of objective cards completed
        Map<String, Integer> objectiveCompleted = new HashMap<>();
        if(playerMaxPoint.size() > 1){
            getUsersList().forEach(user ->{
                if(playerMaxPoint.contains(user.getNickname())){
                    int completedObj = 0;
                    for(ObjectiveCard obj : commonObjective){
                        completedObj += obj.getPoints(user.getUserCodex()) / obj.getPoints();
                    }
                    completedObj += user.getUserHand().getSecretObjective().getPoints(user.getUserCodex()) / user.getUserHand().getSecretObjective().getPoints();
                    objectiveCompleted.put(user.getNickname(), completedObj);
                }
            });
            int maxObj = objectiveCompleted.values().stream().max(Integer::compareTo).orElse(0);
            List<String> playerMaxObj = objectiveCompleted.keySet().stream().filter(nick -> objectiveCompleted.get(nick) == maxObj).toList();

            //intersect the two lists to get the winner(s)
            playerMaxPoint.retainAll(playerMaxObj);
        }

        return playerMaxPoint;
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

    public int getCurrentPlayerIndex() {
        return gameParty.getCurrentPlayerIndex();
    }

    public int getNextActivePlayerIndex(){
        synchronized (turnLock) {
            User nextPlayer = getUsersList().get(getNextPlayerIndex());
            while (!isPlayerActive(nextPlayer.getNickname())) {
                nextPlayer = getUsersList().get(getNextPlayerIndex());
            }

            return getUsersList().indexOf(nextPlayer);
        }
    }

    /***
     * @return the number Of Player needed to start the game
     */
    public int getNumberOfMaxPlayer() {
        return gameParty.getNumberOfMaxPlayer();
    }

    /**
     * endGame triggered if anyPlayer have at least 20 points or decks are empty
     * @return true if the conditions for triggering the last turn are met
     */
    public boolean checkForChickenDinner() {
        synchronized (turnLock) {
            List<Integer> playerPoints = getUsersList().stream().map(User::getUserCodex).map(Codex::getPoints).toList();
            synchronized (goldCardDeck) {
                synchronized (resourceCardDeck) {
                    return areDeckEmpty() || playerPoints.stream().anyMatch(p -> p >= Configs.pointsToStartGameEnding);
                }
            }
        }
    }
    /**
     * Remove a user from the gameParty preventing them from joining the game later
     * @param nickname of the user being removed
     */
    public void removeUser(String nickname){
        synchronized (turnLock){
            gameParty.removeUser(nickname);
        }
    }

    /**
     * draw a card from one deck (depending on the type passed as parameter) and returns a
     * pair containing the card drawn and the card that took its place in the decks
     * @param deckType the deckType (resource or gold)
     * @param cardID the position from which the user draws buffer pos or deck
     * @return the pair <drawnCard, newCard> where the drawn card is
     * the card drawn and new card is the card replacing it
     */
    public Pair<CardInHand, CardInHand> drawAndGetReplacement(DrawableCard deckType, int cardID){
        CardInHand drawnCard;
        CardInHand cardReplacement;
        if(deckType == DrawableCard.GOLDCARD){
            synchronized (goldCardDeck) {
                drawnCard = goldCardDeck.drawACard(cardID);
                cardReplacement = goldCardDeck.peekCardInDecks(cardID);
            }
        }else {
            synchronized (resourceCardDeck) {
                drawnCard = resourceCardDeck.drawACard(cardID);
                cardReplacement = resourceCardDeck.peekCardInDecks(cardID);
            }
        }
        return new Pair<>(drawnCard, cardReplacement);
    }

    public void leave(String nickname){
        //gameMaster.leave(nickname);
    }

    public void joinSecretObjective(String joiner, Game game){
        gameMediator.updateJoinObjectiveSelect(joiner, game);
    }

    public void joinActualGame(String joiner){
        gameMediator.updateJoinActualGame(joiner, this);
    }
    /**
     * @return true if the players in game are choosing the startCard
     */
    public boolean isInStartCardState(){
        synchronized (turnLock) {
            return gameParty.getUsersList().stream().map(User::getUserHand).map(Hand::getStartCard).anyMatch(Objects::nonNull);
        }
    }

    /**
     * @return true if the players in game are choosing the secretObjective
     */
    public boolean inInSecretObjState(){
        synchronized (turnLock){
            return gameParty.getUsersList().stream().map(User::getUserHand).map(Hand::getSecretObjectiveChoices).anyMatch(Objects::nonNull);
        }
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
     * This method is used to check if the decks are empty
     * @return true if the decks are empty, false otherwise
     */
    public boolean areDeckEmpty(){
        return goldCardDeck.isEmpty() && resourceCardDeck.isEmpty();
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
     * this method checks if all other players have placed their startCard
     * other with respect to the player with the nickname passed as parameter
     * if so it removes the inactive players from the game
     * @param nicknamePerspective the nickname of the player that is checking
     * @return true if all other players have placed their startCard, false otherwise
     * true means that is time to move on to secret objective choice phase
     */
    public boolean checkAndMoveToSecretObjectiveChoicePhase(String nicknamePerspective){
        boolean allPlaced = true;
        synchronized (turnLock) {
            for (String nick : getActivePlayers()) {
                if (!nick.equals(nicknamePerspective) && !getUserFromNick(nick).hasPlacedStartCard()) {
                    allPlaced = false;
                }
            }
            if(allPlaced){
                removeInactivePlayers();
            }
        }
        return allPlaced;
    }

    private void removeInactivePlayers(){
        synchronized (turnLock) {
            for(User user : getUsersList()){
                if(!getActivePlayers().contains(user.getNickname())){
                    gameParty.removeUser(user.getNickname());
                }
            }
        }
    }

    public boolean othersHadAllChooseSecretObjective(String nicknamePerspective){
        boolean check = true;
        synchronized (turnLock) {
            for (User user : gameParty.getUsersList()) {
                if (!user.getNickname().equals(nicknamePerspective) && !user.hasChosenObjective()) {
                    check = false;
                }
            }
        }
        return check;
    }

    public boolean isYourTurnToPlace(String nickname){
        User you = getUserFromNick(nickname);
        synchronized (turnLock) {
            return (isInStartCardState() && !you.hasPlacedStartCard()) || (
                    getCurrentPlayer().getNickname().equals(nickname)
                            && you.getUserHand().getHand().size() == 3);
        }
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

    public void startLastTurnsCounter() {
        synchronized (turnLock) {
            lastTurnsCounter = 2;
        }
    }

    public void decrementLastTurnsCounter(){
        synchronized (turnLock){
            if(lastTurnsCounter>0){
                lastTurnsCounter -= 1;
            }else
                throw new IllegalStateException("game.decrementLastTurnsCounter: the counter cannot be decremented under 0");
        }
    }

    public boolean duringLastTurns() {
        synchronized (turnLock) {
            return lastTurnsCounter !=null;
        }
    }

    public boolean hasEnded(){
        synchronized (turnLock){
            return lastTurnsCounter == 0;
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

    private void setupStartCard(){
        gameParty.getUsersList().forEach(user-> {
            if (user.getUserHand().getStartCard() == null && !user.hasPlacedStartCard()) {
                StartCard startCard = drawStartCard();
                user.getUserHand().setStartCard(startCard);
            }
        });
    }
}
