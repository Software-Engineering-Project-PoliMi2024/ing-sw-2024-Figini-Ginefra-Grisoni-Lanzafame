package it.polimi.ingsw.model.tableReleted;


import it.polimi.ingsw.Configs;
import it.polimi.ingsw.controller.GameLoopController;
import it.polimi.ingsw.controller3.Controller3;
import it.polimi.ingsw.controller3.mediators.gameJoinerAndTurnTakerMediators.TurnTakerMediator;
import it.polimi.ingsw.controller3.mediators.loggerAndUpdaterMediators.GameMediator;
import it.polimi.ingsw.controller3.mediators.gameJoinerAndTurnTakerMediators.TurnTaker;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffs.game.GameDiff;
import it.polimi.ingsw.lightModel.diffPublishers.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffPublishers.GameDiffPublisher;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.cards.*;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.Hand;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.utilities.Pair;
import it.polimi.ingsw.view.ViewInterface;

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

    private boolean isLastTurn;
    private final Object turnLock = new Object();

    private final TurnTakerMediator activeTurnTakerMediator = new TurnTakerMediator();
    private final GameMediator gameMediator;
    private final GameMaster gameMaster = new GameMaster(this);

    private Timer countdownTimer;
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

    public void join(String nickname, ViewInterface view, TurnTaker turnTaker, Controller3 controller){
        gameMaster.join(nickname, view, turnTaker, controller);
    }
        public void joinStartGame(String nickname, ViewInterface view, TurnTaker turnTaker){
        gameMaster.joinStartGame(nickname, view, turnTaker);
    }

    public void placeStartCard(String nickname, Placement startCardPlacement){
        gameMaster.placeStartCard(nickname, startCardPlacement);
    }

    public void chooseSecretObjective(String nickname, ObjectiveCard objChoice){
        gameMaster.chooseSecretObjective(nickname, objChoice);
    }

    public void place(String nickname, Placement placement){
        gameMaster.place(nickname, placement);
    }

    public void draw(String nickname, DrawableCard deckType, int cardID){
        gameMaster.draw(nickname, deckType, cardID);
    }

    public void leave(String nickname){
        //gameMaster.leave(nickname);
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

    public void joinMidGame(String joiner){
        gameMediator.updateJoinActualGame(joiner, this);
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

    public void notifyStartCardFaceChoice(String placer, User user, LightPlacement placement, LightBack resourceBack, LightBack goldBack){
        gameMediator.notifyStartCardFaceChoice(placer, user, placement, resourceBack, goldBack);
    }

    public void notifySecretObjectiveChoice(String choice, LightCard objCard){
        gameMediator.notifySecretObjectiveChoice(choice, objCard);
    }

    public void notifyPlacement(String placer, LightPlacement newPlacement, Codex placerCodex, Map<LightCard, Boolean> playability){
        gameMediator.notifyPlacement(placer, newPlacement, placerCodex, playability);
    }

    public void notifyGameEnded(Map<String, Integer> pointsPerPlayerMap, List<String> ranking){
        gameMediator.notifyGameEnded(pointsPerPlayerMap, ranking);
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
     * @return true if the players in game are in the setup phase
     * (choosing StartCardFace or SecretObjective)
     */
    public boolean isInSetup(){
        synchronized (turnLock) {
            return inInSecretObjState() || isInStartCardState();
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

    public void notifyFirstTurn(String nicknameOfNextPlayer){
        gameMediator.notifyTurnChange(nicknameOfNextPlayer);
    }

    /**
     * This method is used to notify all players a change in the decks
     * @param deckType the type of the deck that has changed (GOLD, RESOURCE)
     * @param pos the position of the card that has changed (2 = from deck, 1,0 = buffer)
     * @param drawnCard the card drawn by the user
     * @param drawnReplaceCard the card that has replaced the drawn card in the decks/buffers
     * @param drawerNickname the nickname of the player that has drawn the card
     */
    public void notifyDraw(DrawableCard deckType, int pos, LightCard drawnCard, LightCard drawnReplaceCard, String drawerNickname, boolean playability){
        gameMediator.notifyDraw(drawerNickname, deckType, pos, drawnCard, drawnReplaceCard, playability);
    }

    public void notifyLastTurn(){
        gameMediator.notifyLastTurn();
    }
    /**
     * This method is used to check if the decks are empty
     * @return true if the decks are empty, false otherwise
     */
    public boolean areDeckEmpty(){
        return goldCardDeck.isEmpty() && resourceCardDeck.isEmpty();
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

    public void setLastTurn(boolean lastTurn) {
        synchronized (turnLock) {
            isLastTurn = lastTurn;
        }
    }

    public boolean isLastTurn() {
        synchronized (turnLock) {
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

}
