package it.polimi.ingsw.controller2;

public enum LogsFromServer {
    LOBBY_IS_FULL("The lobby is full"),
    LOBBY_JOINED("Lobby joined"),
    LOBBY_LEFT("Lobby left"),
    LOBBY_CREATED("Lobby created");

    private final String message;

    LogsFromServer(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
