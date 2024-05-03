package it.polimi.ingsw.model.tableReleted;


import it.polimi.ingsw.controller2.GameLoopController;
import it.polimi.ingsw.lightModel.Heavifier;
import it.polimi.ingsw.lightModel.diffs.GameDiff;
import it.polimi.ingsw.lightModel.diffPublishers.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffPublishers.GameDiffPublisher;
import it.polimi.ingsw.model.cardReleted.cards.*;
import it.polimi.ingsw.model.playerReleted.User;

import java.io.Serializable;


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
    /**
     * Constructs a new Game instance with a specified maximum number of players.
     */
    public Game(Lobby lobby, CardLookUp<ObjectiveCard> objectiveCardCardLookUp, CardLookUp<ResourceCard> resourceCardCardLookUp,
                CardLookUp<StartCard> startCardCardLookUp, CardLookUp<GoldCard> goldCardCardLookUp) {
        gameDiffPublisher = new GameDiffPublisher(this);
        this.name = lobby.getLobbyName();
        this.gameParty = new GameParty(lobby.getLobbyPlayerList().stream().toList());
        objectiveCardDeck = new Deck<>(0,objectiveCardCardLookUp.getQueue());
        resourceCardDeck = new Deck<>(2, resourceCardCardLookUp.getQueue());
        startingCardDeck = new Deck<>(0, startCardCardLookUp.getQueue());
        goldCardDeck = new Deck<>(2, goldCardCardLookUp.getQueue());
        this.gameLoopController = new GameLoopController(this, lobby.getPlayerController());
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

    public GameDiffPublisher getGameDiffPublisher() {
        return gameDiffPublisher;
    }

    public void subcribe(DiffSubscriber diffSubscriber, String nickname){
        gameDiffPublisher.subscribe(diffSubscriber, nickname);
    }

    public void subcribe(GameDiff gameDiff){
        gameDiffPublisher.subscribe(gameDiff);
    }

    public void subcribe(DiffSubscriber diffSubscriber, GameDiff gameDiffYou, GameDiff gameDiffOther){
        gameDiffPublisher.subscribe(diffSubscriber, gameDiffYou, gameDiffOther);
    }
    public void unsubscrive(DiffSubscriber diffSubscriber){
        gameDiffPublisher.unsubscribe(diffSubscriber);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Game && ((Game) obj).name.equals(name);
    }

    public GameLoopController getGameLoopController() {
        return gameLoopController;
    }
}
