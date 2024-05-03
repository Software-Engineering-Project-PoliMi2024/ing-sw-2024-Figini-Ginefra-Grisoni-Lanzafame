package it.polimi.ingsw.view;

import java.io.Serializable;

public enum ViewState implements Serializable {
    SERVER_CONNECTION, //the state that asks for the server ip and port
    LOGIN_FORM, //the state that asks for the nickname
    JOIN_LOBBY,
    LOBBY,
    CHOOSE_START_CARD,
    SELECT_OBJECTIVE,
    IDLE,
    DRAW_CARD,
    PLACE_CARD,
    GAME_WAITING,
    GAME_ENDING,
}
