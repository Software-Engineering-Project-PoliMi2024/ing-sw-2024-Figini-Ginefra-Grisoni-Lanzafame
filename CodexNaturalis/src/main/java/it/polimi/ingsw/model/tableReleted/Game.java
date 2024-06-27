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
    /** the deck of objective cards */
    final private Deck<ObjectiveCard> objectiveCardDeck;
    /** the deck of resource cards */
    final private Deck<ResourceCard> resourceCardDeck;
    /** the deck of gold cards */
    final private Deck<GoldCard> goldCardDeck;
    /** the deck of starting cards */
    final private Deck<StartCard> startingCardDeck;
    
    /** the name of the game */
    private final String name;
    /** the party of players who are playing the game */
    private final GameParty gameParty;
    /** the common objective cards */
    private final List<ObjectiveCard> commonObjective;
    
    /** the state of the game */
    private GameState gameState = GameState.CHOOSE_START_CARD;
    /** the counter before the ending for the ending turns */
    private Integer endingTurnsCounter = null;

    /**
     * This constructor is used to create a new game
     * @param lobby the lobby from which the game is created, from which the players are taken and the name
     * @param objectiveCardDeck the deck of objective cards
     * @param resourceCardDeck the deck of resource cards
     * @param goldCardDeck the deck of gold cards
     * @param startingCardDeck the deck of starting cards
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

    /** @param gameState the state to set */
    public void setState(GameState gameState) {
        this.gameState = gameState;
    }
    
    /** @return the state of the game */
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
    
    /**
     * This method is used to draw a card from the starting card deck
     * @return the card drawn
     */
    public StartCard drawStartCard(){
        synchronized (startingCardDeck) {
            return startingCardDeck.drawFromDeck();
        }
    }
    
    /**
     * This method is used to draw a card from the objective card deck
     * @return the card drawn
     */
    public ObjectiveCard drawObjectiveCard(){
        synchronized (objectiveCardDeck) {
            return objectiveCardDeck.drawFromDeck();
        }
    }
    
    /** @return the name of the game */
    public String getName() {
        return name;
    }

    //game party methods
    /** @return the game party */
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
    
    /** @return the list of the pawns that the players can choose */
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

    /**
     * This method is used to get the index of the player currently playing
     * @return the index of the player currently playing
     */
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
     * @return the pair _drawnCard, newCard_ where the drawn card is
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
            if(cardID < deck.getBuffer().stream().toList().size())
                drawCard = deck.drawFromBuffer(cardID);
            else
                drawCard = null;
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

    /** start the ending turns counter and sets them equals to numberOfEndingTurns */
    public void startEndingTurnsCounter() {
        endingTurnsCounter = Configs.numberOfEndingTurns;
    }

    /** decrement the ending turns counter by one while positive */
    public void decrementEndingTurnsCounter() {
        if (endingTurnsCounter > 0) {
            endingTurnsCounter -= 1;
        } else
            throw new IllegalStateException("game.decrementLastTurnsCounter: the counter cannot be decremented under 0");
    }

    /** @return true if the game is in the ending turns */
    public boolean duringEndingTurns() {
        return endingTurnsCounter != null;
    }

    /** @return true if there are no more turns to play */
    public boolean noMoreTurns() {
        if (endingTurnsCounter != null)
            return endingTurnsCounter == 0;
        return false;
    }

    /** add objective cards to the common objective */
    private void populateCommonObjective(){
        synchronized (commonObjective) {
            for (int i = 0; i < 2; i++) {
                commonObjective.add(objectiveCardDeck.drawFromDeck());
            }
        }
    }
    /** @return the common objective cards */
    public List<ObjectiveCard> getCommonObjective() {
        synchronized (commonObjective) {
            return new ArrayList<>(commonObjective);
        }
    }

    /** draw for every player in game a start card */
    private void setupStartCard(){
        gameParty.getPlayersList().forEach(player-> {
            if (player.getUserHand().getStartCard() == null && player.getState().equals(PlayerState.CHOOSE_START_CARD)) {
                StartCard startCard = drawStartCard();
                player.getUserHand().setStartCard(startCard);
            }
        });
    }
}
