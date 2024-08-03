package it.polimi.ingsw.model.playerReleted.AgentRelated;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.model.cardReleted.cards.Card;
import it.polimi.ingsw.model.cardReleted.cards.CardWithCorners;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.*;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.utils.logger.LoggerLevel;
import it.polimi.ingsw.utils.logger.LoggerSources;
import it.polimi.ingsw.utils.logger.ServerLogger;

import java.util.Arrays;

/**
 * This class represents an AI player in the game.
 * It contains the player's nickname,
 * the player's codex,
 * the player's hand,
 * the player's pawn color
 * and the player's state.
 * Overrides the setState method to compute and then perform the agent's actions
 */
public class Agent extends Player {
    private transient GameController gameController;
    private final Game game;

    /**
     * This constructor creates a player with the given nickname
     *
     * @param nickname the player's nickname
     */
    public Agent(String nickname, Game game) {
        super(nickname);
        this.game = game;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void setState(PlayerState playerState) {
        super.setState(playerState);
        Thread playThread = new Thread(() -> play(playerState));
        playThread.start();
    }

    private void play(PlayerState playerState) {
        //Sleep for a random amount of time to simulate thinking
        try {
            Thread.sleep((long) (Math.random() * 1500));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        ServerLogger logger = new ServerLogger(LoggerSources.AI, this.getNickname());
        switch (playerState) {
            case CHOOSE_START_CARD:
                logger.log(LoggerLevel.INFO, "Choosing start card");
                StartCard startCard = this.getUserHand().getStartCard();
                Placement startPlacement = new Placement(new Position(0, 0), startCard, CardFace.FRONT);
                gameController.place(this.getNickname(), Lightifier.lightify(startPlacement));
                break;
            case CHOOSE_SECRET_OBJECTIVE:
                logger.log(LoggerLevel.INFO, "Choosing secret objective");
                ObjectiveCard secretObjective = this.getUserHand().getSecretObjectiveChoices().get(Math.random() < 0.5 ? 0 : 1);
                gameController.chooseSecretObjective(this.getNickname(), Lightifier.lightifyToCard(secretObjective));
                break;
            case CHOOSE_PAWN:
                logger.log(LoggerLevel.INFO, "Choosing pawn");
                while (this.getState() == PlayerState.CHOOSE_PAWN) {
                    PawnColors randomColor = game.getPawnChoices().get((int) (Math.random() * game.getPawnChoices().size()));
                    gameController.choosePawn(this.getNickname(), randomColor);
                }
                break;
            case PLACE:
                logger.log(LoggerLevel.INFO, "Placing card");
                Position randomPosition = this.getUserCodex().getFrontier().getFrontier().stream().findAny().orElse(null);
                CardFace randomFace = Math.random() < 0.5 ? CardFace.FRONT : CardFace.BACK;

                CardWithCorners randomCard = null;
                if(randomFace == CardFace.FRONT)
                    randomCard = this.getUserHand().getHand().stream().filter(c -> c.canBePlaced(this.getUserCodex())).findAny().orElse(null);

                if(randomCard == null) {
                    randomFace = CardFace.BACK;
                    randomCard = this.getUserHand().getHand().stream().findAny().orElse(null);
                }

                logger.log(LoggerLevel.INFO, "Placing card " + randomCard + " at position " + randomPosition + " with face " + randomFace);
                Placement randomPlacement = new Placement(randomPosition, randomCard, randomFace);
                gameController.place(this.getNickname(), Lightifier.lightify(randomPlacement));
                break;
            case DRAW:
                logger.log(LoggerLevel.INFO, "Drawing card");
                DrawableCard randomCardType = Math.random() < 0.5 ? DrawableCard.GOLDCARD : DrawableCard.RESOURCECARD;
                int randomCardIndex = (int) (Math.random() * 3);
                gameController.draw(this.getNickname(), randomCardType, randomCardIndex);
                break;
        }
    }
}
