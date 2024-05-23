package it.polimi.ingsw.controller;

import it.polimi.ingsw.Configs;

public enum LogsOnClient {
    //connectionForm related
    CONNECTION_ERROR("Failed to connect to the server."),
    CONNECTION_SUCCESS("Connected to the server successfully."),
    CONNECTION_LOST_CLIENT_SIDE("Lost connection with the server."),

    //login related
    NAME_TAKEN("The chosen nickname is already in use."),
    SERVER_JOINED("Joined the server."),
    EMPTY_NAME("Nickname cannot be empty."),

    //lobbyList related
    LOBBY_NONEXISTENT("The queried lobby does not exist."),
    LOBBY_NAME_TAKEN("The specified lobby name is already taken."),

    //lobby related
    LOBBY_IS_FULL("The lobby is currently full."),
    LOBBY_JOINED("Successfully joined the lobby."),
    LOBBY_LEFT("Left the lobby."),
    LOBBY_CREATED("Created a new lobby."),
    PLAYER_JOIN_LOBBY(" joined the lobby"),

    //joinGame related
    GAME_CREATED("Lobby is full, the game is starting."),
    NEW_GAME_JOINED("Joined a new game."),
    MID_GAME_JOINED("Rejoined a previous game."),

    //gameSetup related (startCard face and SecretObjective option selection)
    DECK_SHUFFLE("The Resource and Gold card decks have been shuffled."),
    YOU_PLACE_STARTCARD("Successfully placed your Start Card."),
    YOU_CHOSE("Successfully chose your Secret Objective."),
    PLAYER_PLACE_STARTCARD(" placed their Start Card."),
    PLAYER_CHOSE(" chose their Secret Objective."),
    EVERYONE_PLACED_STARTCARD("All players have placed their Start Cards."),
    WAIT_SECRET_OBJECTIVE("Waiting for other players to choose their Secret Objectives."),
    EVERYONE_CHOSE("All players have chosen their Secret Objectives."),

    //gameCards related (card place or draw)
    CARD_PLACED("Card successfully placed."),
    CARD_DRAWN("Card successfully drawn."),

    PLAYER_PLACED(" placed a card."),
    YOU_PLACED("Successfully placed a card in your Codex."),
    PLAYER_DRAW(" drew a card."),
    YOU_DRAW("Successfully drew a card to your Hand."),


    //gameTurn related
    YOUR_TURN("It's now your turn to play."),
    PLAYER_TURN("'s turn"),
    WAIT_STARTCARD("Waiting for other players to place their Start Cards."),
    LAST_TURN("The final round of turns has begun."),
    GAME_END("The game has ended."),

    //gameParty related
    PLAYER_REJOINED(" rejoined the game"),
    PLAYER_GAME_LEFT(" left the game"),
    LAST_PLAYER("You are the last player in the game"),

    //gameCountDown related
    COUNTDOWN_START("A CountDown of " + Configs.timerDurationSeconds + " seconds is starting now"),
    COUNTDOWN_INTERRUPTED("The CountDown has been stopped");

    private final String message;

    LogsOnClient(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
