package it.polimi.ingsw.model.tableReleted;


import it.polimi.ingsw.model.cardReleted.*;
import it.polimi.ingsw.model.playerReleted.User;


/**
 * @author Samuele
 * This class represents the game as a whole.
 * It contains a reference to all the players who are playing the match
 * and the decks that are being used.
 */
public class Game{

    final private Deck<ObjectiveCard> objectiveCardDeck;
    final private Deck<ResourceCard> resourceCardDeck;
    final private Deck<GoldCard> goldCardDeck;
    final private Deck<StartCard> startingCardDeck;

    private final String name;

    private final GameParty gameParty;

    /**
     * Constructs a new Game instance with a specified maximum number of players.
     */
    public Game(String name, int numberOfMaxPlayer) {
        this.name = name;
        this.gameParty = new GameParty(numberOfMaxPlayer);

        CardFactory cardFactory = new CardFactory( "../cards.json");
        objectiveCardDeck =
                new Deck<>(0, cardFactory.getFactoryOf(ObjectiveCard.class).getCards());
        resourceCardDeck =
                new Deck<>(2, cardFactory.getFactoryOf(ResourceCard.class).getCards());
        goldCardDeck =
                new Deck<>(2, cardFactory.getFactoryOf(GoldCard.class).getCards());
        startingCardDeck =
                new Deck<>(0, cardFactory.getFactoryOf(StartCard.class).getCards());
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
