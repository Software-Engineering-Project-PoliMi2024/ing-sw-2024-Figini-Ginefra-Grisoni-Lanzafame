package it.polimi.ingsw.model.tableReleted;


import it.polimi.ingsw.model.cardReleted.cardFactories.GoldCardFactory;
import it.polimi.ingsw.model.cardReleted.cardFactories.ObjectiveCardFactory;
import it.polimi.ingsw.model.cardReleted.cardFactories.ResourceCardFactory;
import it.polimi.ingsw.model.cardReleted.cardFactories.StartCardFactory;
import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;
import it.polimi.ingsw.model.playerReleted.User;

import java.io.Serializable;


/**
 * @author Samuele
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

    private GameParty gameParty;

    /**
     * Constructs a new Game instance with a specified maximum number of players.
     */
    public Game(Lobby lobby) {
        this.name = lobby.getLobbyName();
        this.gameParty = new GameParty(lobby.getLobbyList());
        String filePath = ".\\cards\\";
        String sourceFileName = "cards.json";
        objectiveCardDeck =
                new Deck<>(0, new ObjectiveCardFactory(filePath+sourceFileName, filePath).getCards());
        resourceCardDeck =
                new Deck<>(2, new ResourceCardFactory(filePath+sourceFileName, filePath).getCards());
        goldCardDeck =
                new Deck<>(2, new GoldCardFactory(filePath+sourceFileName, filePath).getCards());
        startingCardDeck =
                new Deck<>(0, new StartCardFactory(filePath+sourceFileName, filePath).getCards());
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
        return gameParty;
    }

    public void setGameParty(GameParty gameParty){
        this.gameParty = gameParty;
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

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Game && ((Game) obj).name.equals(name);
    }
}
