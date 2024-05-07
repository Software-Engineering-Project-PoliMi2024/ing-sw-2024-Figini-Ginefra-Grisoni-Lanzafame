package it.polimi.ingsw.controller2;

public enum LogsOnClient {
    CONNECTION_ERROR("Connection error"),
    CONNECTION_SUCCESS("Connection success"),
    NAME_TAKEN("The nickname is already present in the server"),
    SERVER_JOINED("Server Joined"),
    PLAYER_JOINED_LOBBY(" joined the lobby"),
    LOBBY_IS_FULL("The lobby is full"),
    LOBBY_JOINED("Lobby joined"),
    LOBBY_LEFT("Lobby left"),
    LOBBY_NONEXISTENT("The lobby does not exist"),
    LOBBY_NAME_TAKEN("The lobby name is already taken"),
    LOBBY_CREATED("Lobby created"),
    NEW_GAME_JOINED("Joined a new game not already started"),
    MID_GAME_JOINED("Joined your previous game"),
    START_CARD_PLACED("StartCard successfully placed"),
    SECRET_OBJECTIVE_CHOSE("Secret Objective chosen"),
    CARD_PLACED("Card placed"),
    CARD_DRAWN("Card Drawn"),
    YOUR_TURN("It's your time to play"),
    WAIT_STARTCARD("Waiting for others to place their StartCard"),
    WAIT_SECRET_OBJECTIVE("Waiting for others to choose their Secret Objective"),
    EMPTY_NAME("The name cannot be empty"),
    LAST_TURN("The last round of turns is starting now"),
    GAME_END("Game ended"),
    GAME_CREATED("Lobby is full, the game is starting"),
    PLAYER_PLACE_STARTCARD(" placed his StartCard"),
    YOU_PLACE_STARTCARD("The StartCard successfully placed on your Codex"),
    EVERYONE_PLACED_STARTCARD("Everybody placed their StartCard"),
    PLAYER_CHOSE(" chose his SecretObjective"),
    YOU_CHOSE("SecrectObjective successfully selected"),
    EVERYONE_CHOSE("Everybody chose their SecretObjective"),
    CONNECTION_LOST_CLIENT_SIDE("Lost connection with the server"),
    PLAYER_PLACED(" placed a new Card"),
    YOU_PLACED("A new Card was added to your Codex"),
    PLAYER_DRAW(" drawn a new Card"),
    YOU_DRAW("A new Card was added to your Hand"),
    DECK_SHUFFLE("The Resource and Gold card decks are now shuffled");

    private final String message;
    private String prefix = "";

    LogsOnClient(String message) {
        this.message = message;
    }

    public String getMessage() {
        return prefix + message;
    }

    public void setPrefix(String prefix){
        this.prefix = prefix;
    }
}
