package it.polimi.ingsw.controller2;

public enum LogsFromServer {
    CONNECTION_ERROR("Connection error"),
    CONNECTION_SUCCESS("Connection success"),
    NAME_TAKEN("The nickname is already present in the server"),
    SERVER_JOINED("Server Joined"),
    LOBBY_IS_FULL("The lobby is full"),
    LOBBY_JOINED("Lobby joined"),
    LOBBY_LEFT("Lobby left"),
    LOBBY_INEXISTENT("The lobby does not exist"),
    LOBBY_CREATED("Lobby created"),
    NEW_GAME_JOINED("Joined a new game from the lobby"),
    MID_GAME_JOINED("Joined your previous game");
    private final String message;

    LogsFromServer(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
