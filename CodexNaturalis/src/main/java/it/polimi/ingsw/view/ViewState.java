package it.polimi.ingsw.view;

import it.polimi.ingsw.model.playerReleted.PlayerState;
import it.polimi.ingsw.view.TUI.States.StateTUI;

import java.io.Serializable;
import java.util.Map;

public enum ViewState implements Serializable {
    SERVER_CONNECTION, //the state that asks for the server ip and port
    LOGIN_FORM, //the state that asks for the nickname
    JOIN_LOBBY,
    LOBBY,
    CHOOSE_START_CARD,
    CHOOSE_PAWN,
    SELECT_OBJECTIVE,
    WAITING_STATE,
    IDLE,
    DRAW_CARD,
    PLACE_CARD,
    GAME_WAITING,
    GAME_ENDING;
}
