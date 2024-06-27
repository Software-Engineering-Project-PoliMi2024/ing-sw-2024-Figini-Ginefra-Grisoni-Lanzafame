package it.polimi.ingsw.model.playerReleted;

/**
 * Enum that represents the possible states of a player during the game.
 */
public enum PlayerState {
    CHOOSE_START_CARD,
    CHOOSE_PAWN,
    CHOOSE_SECRET_OBJECTIVE,
    WAIT,
    IDLE,
    PLACE,
    DRAW,
    END_GAME,
}
