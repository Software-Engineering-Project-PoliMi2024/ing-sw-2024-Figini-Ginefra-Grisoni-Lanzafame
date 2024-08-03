package it.polimi.ingsw.model.playerReleted.AgentRelated;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.playerReleted.Player;
import it.polimi.ingsw.model.playerReleted.PlayerState;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.utils.logger.LoggerLevel;
import it.polimi.ingsw.utils.logger.LoggerSources;
import it.polimi.ingsw.utils.logger.ServerLogger;

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
    private final Controller controller;
    private final Game game;

    /**
     * This constructor creates a player with the given nickname
     *
     * @param nickname the player's nickname
     */
    public Agent(String nickname, Controller controller, Game game) {
        super(nickname);
        this.controller = controller;
        this.game = game;
    }

    @Override
    public void setState(PlayerState playerState) {
        super.setState(playerState);

        ServerLogger logger = new ServerLogger(LoggerSources.AI, this.getNickname());

        switch (playerState) {
            case CHOOSE_START_CARD:
                // AI stuff to choose start card
                logger.log(LoggerLevel.INFO, "Choosing start card");
                break;
            case CHOOSE_SECRET_OBJECTIVE:
                // AI stuff to choose secret objective
                logger.log(LoggerLevel.INFO, "Choosing secret objective");
                break;
            case CHOOSE_PAWN:
                // AI stuff to choose pawn
                logger.log(LoggerLevel.INFO, "Choosing pawn");
                break;
            case PLACE:
                // AI stuff to place pawn
                logger.log(LoggerLevel.INFO, "Placing pawn");
                break;
            case DRAW:
                // AI stuff to draw card
                logger.log(LoggerLevel.INFO, "Drawing card");
                break;
        }
    }
}
