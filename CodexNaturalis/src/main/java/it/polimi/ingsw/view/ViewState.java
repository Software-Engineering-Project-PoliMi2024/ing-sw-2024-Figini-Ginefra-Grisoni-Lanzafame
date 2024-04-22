package it.polimi.ingsw.view;

public enum ViewState {
    SERVER_CONNECTION, //the state that asks for the server ip and port
    CONNECT_FORM, //the state that asks for the nickname
    JOIN_GAME,
    CREATE_GAME,
    LOBBY,
    CHOOSE_START_CARD,
    SELECT_OBJECTIVE,
    IDLE,
    DRAW_CARD,
    PLACE_CARD,
    GAME_ENDING,
}
