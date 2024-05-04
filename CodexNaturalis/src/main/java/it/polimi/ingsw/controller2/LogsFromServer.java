package it.polimi.ingsw.controller2;

public enum LogsFromServer {
    CONNECTION_ERROR("Connection error"),
    CONNECTION_SUCCESS("Connection success"),
    NAME_TAKEN("The nickname is already present in the server"),
    SERVER_JOINED("Server Joined"),
    LOBBY_IS_FULL("The lobby is full"),
    LOBBY_JOINED("Lobby joined"),
    LOBBY_LEFT("Lobby left"),
    LOBBY_NONEXISTENT("The lobby does not exist"),
    LOBBY_NAME_TAKEN("The lobby name is already taken"),
    LOBBY_CREATED("Lobby created"),
    NEW_GAME_JOINED("Joined a new game not already started"),
    MID_GAME_JOINED("Joined your previous game"),
    START_CARD_PLACED("StartCard successfully placed"),
    SECRET_OBJECTIVE_CHOSE("Secret Objective chose"),
    CARD_PLACED("Card placed"),
    CARD_DRAWN("Card Drawn"),
    YOUR_TURN("It's your time to play"),
    WAIT_STARTCARD("Waiting for the other players to place their StartCard"),
    WAIT_SECRET_OBJECTIVE("Waiting for the other players to choose their Secret Objective");

    private final String message;

    LogsFromServer(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
