package it.polimi.ingsw.controller2;

public enum LogsOnClient {
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
    WAIT_SECRET_OBJECTIVE("Waiting for the other players to choose their Secret Objective"),
    EMPTY_NAME("The name cannot be empty"),
    LAST_TURN("The last round of turns is starting now"),
    GAME_END("Game ended"),
    CONNECTION_LOST_CLIENT_SIDE("Lost connection with the server"),;

    private final String message;

    LogsOnClient(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
