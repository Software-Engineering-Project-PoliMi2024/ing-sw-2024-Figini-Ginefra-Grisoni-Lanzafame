package it.polimi.ingsw.model.tableReleted;


import it.polimi.ingsw.Configs;
import it.polimi.ingsw.model.cardReleted.cards.*;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.model.playerReleted.Player;
import it.polimi.ingsw.model.playerReleted.PlayerState;
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

    final private Deck<ObjectiveCard> objectiveCardDeck;
    final private Deck<ResourceCard> resourceCardDeck;
    final private Deck<GoldCard> goldCardDeck;
    final private Deck<StartCard> startingCardDeck;

    private final String name;
    private final GameParty gameParty;
    private final List<ObjectiveCard> commonObjective;

    private GameState gameState = GameState.CHOOSE_START_CARD;
    private Integer lastTurnsCounter = null;
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
    }

    public void setState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getState() {
        return gameState;
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

    public List<PawnColors> getPawnChoices(){
        return gameParty.getPawnChoices();
    }

    public void removeChoice(PawnColors color){
        gameParty.removeChoice(color);
    }

    /** @return list of the User in this match*/
    public List<Player> getPlayersList() {
        return gameParty.getPlayersList();
    }

    /**
     * adds points given by common objective and by the secret objective of the user
     * to the codex of the user
     */
    public void addObjectivePoints(){
            for (Player player : new ArrayList<>(getPlayersList())) {
                for (ObjectiveCard commonObj : commonObjective) {
                    player.getUserCodex().pointsFromObjective(commonObj);
                }
                if (player.getUserHand().getSecretObjective() != null)
                    player.getUserCodex().pointsFromObjective(player.getUserHand().getSecretObjective());
            }
    }

    /**
     * @return a Map that maps to each user nickName their points earned
     */
    public Map<String, Integer> getPointPerPlayerMap(){
        return gameParty.getPointPerPlayerMap();
    }
    /**
     * This method is used to get the user from its nickname
     * if the user is not in the gameParty, it returns null
     * @param nickname the nickname of the user
     * @return the user with the given nickname; returns null if the user is not in the gameParty
     */
    public Player getPlayerFromNick(String nickname){
        return gameParty.getPlayerFromNick(nickname);
    }

    /**
     * This method is used to get the player currently playing
     * @return the player currently playing
     */
    public Player getCurrentPlayer() {
        return gameParty.getCurrentPlayer();
    }

    public int getCurrentPlayerIndex() {
        return gameParty.getCurrentPlayerIndex();
    }


    /**
     * endGame triggered if anyPlayer have at least 20 points or decks are empty
     * @return true if the conditions for triggering the last turn are met
     */
    public boolean checkForChickenDinner() {
        List<Integer> playerPoints = getPlayersList().stream().map(Player::getUserCodex).map(Codex::getPoints).toList();
        synchronized (goldCardDeck) {
            synchronized (resourceCardDeck) {
                return areDeckEmpty() || playerPoints.stream().anyMatch(p -> p >= Configs.pointsToStartGameEnding);
            }
        }
    }
    /**
     * Remove a user from the gameParty preventing them from joining the game later
     * @param nickname of the user being removed
     */
    public void removePlayer(String nickname) {
        gameParty.removePlayer(nickname);

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
                drawnCard = drawACard(goldCardDeck, cardID);
                cardReplacement = goldCardDeck.peekCardInDecks(cardID);
            }
        }else {
            synchronized (resourceCardDeck) {
                drawnCard = drawACard(resourceCardDeck, cardID);
                cardReplacement = resourceCardDeck.peekCardInDecks(cardID);
            }
        }
        return new Pair<>(drawnCard, cardReplacement);
    }

    /**
     * @param cardID the position from where draw the card (buffer/deck)
     * @return the card drawn
     */
    public <T> T drawACard(Deck<T> deck, int cardID) {
        T drawCard;
        if (cardID == Configs.actualDeckPos) {
            drawCard = deck.drawFromDeck();
        } else {
            drawCard = deck.drawFromBuffer(cardID);
        }
        return drawCard;
    }

    /**
     * This method is used to check if the decks are empty
     * @return true if the decks are empty, false otherwise
     */
    public boolean areDeckEmpty(){
        return goldCardDeck.isEmpty() && resourceCardDeck.isEmpty();
    }

    @Override
    public String toString() {

        return "Game{" +
                "name='" + name + '\'' +
                ", usersList=" + gameParty.getPlayersList().stream().map(Player::getNickname).reduce("", (a, b) -> a + " " + b) +
                //", currentPlayer=" + currentPlayer.getNickname() +
                ", numberOfMaxPlayer=" + gameParty.getNumberOfMaxPlayer() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Game && ((Game) obj).name.equals(name);
    }

    public void startLastTurnsCounter() {
        lastTurnsCounter = 2;
    }

    public void decrementLastTurnsCounter() {
        if (lastTurnsCounter > 0) {
            lastTurnsCounter -= 1;
        } else
            throw new IllegalStateException("game.decrementLastTurnsCounter: the counter cannot be decremented under 0");
    }

    public boolean duringLastTurns() {
        return lastTurnsCounter != null;
    }

    public boolean noMoreTurns() {
        if (lastTurnsCounter != null)
            return lastTurnsCounter == 0;
        return false;
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
        gameParty.getPlayersList().forEach(player-> {
            if (player.getUserHand().getStartCard() == null && player.getState().equals(PlayerState.CHOOSE_START_CARD)) {
                StartCard startCard = drawStartCard();
                player.getUserHand().setStartCard(startCard);
            }
        });
    }
}
