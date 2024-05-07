package it.polimi.ingsw.controller2;

public enum LogsOnClient {
    CONNECTION_ERROR("Failed to connect to the server."),
    CONNECTION_SUCCESS("Connected to the server successfully."),
    NAME_TAKEN("The chosen nickname is already in use."),
    SERVER_JOINED("Joined the server."),
    PLAYER_JOINED_LOBBY(" joined the lobby."),
    LOBBY_IS_FULL("The lobby is currently full."),
    LOBBY_JOINED("Successfully joined the lobby."),
    LOBBY_LEFT("Left the lobby."),
    LOBBY_NONEXISTENT("The requested lobby does not exist."),
    LOBBY_NAME_TAKEN("The specified lobby name is already taken."),
    LOBBY_CREATED("Created a new lobby."),
    NEW_GAME_JOINED("Joined a new game."),
    MID_GAME_JOINED("Rejoined a previous game."),
    START_CARD_PLACED("Start Card successfully placed."),
    SECRET_OBJECTIVE_CHOSE("Secret Objective successfully chosen."),
    CARD_PLACED("Card successfully placed."),
    CARD_DRAWN("Card successfully drawn."),
    YOUR_TURN("It's now your turn to play."),
    PLAYER_TURN("'s turn"),
    WAIT_STARTCARD("Waiting for other players to place their Start Cards."),
    WAIT_SECRET_OBJECTIVE("Waiting for other players to choose their Secret Objectives."),
    EMPTY_NAME("Nickname cannot be empty."),
    LAST_TURN("The final round of turns has begun."),
    GAME_END("The game has ended."),
    GAME_CREATED("Lobby is full, the game is starting."),
    PLAYER_REJOINED(" rejoined the game"),
    PLAYER_JOIN_LOBBY(" joined the lobby"),
    PLAYER_PLACE_STARTCARD(" placed their Start Card."),
    YOU_PLACE_STARTCARD("Successfully placed your Start Card."),
    EVERYONE_PLACED_STARTCARD("All players have placed their Start Cards."),
    PLAYER_CHOSE(" chose their Secret Objective."),
    YOU_CHOSE("Successfully chose your Secret Objective."),
    EVERYONE_CHOSE("All players have chosen their Secret Objectives."),
    CONNECTION_LOST_CLIENT_SIDE("Lost connection with the server."),
    PLAYER_PLACED(" placed a card."),
    YOU_PLACED("Successfully placed a card in your Codex."),
    PLAYER_DRAW(" drew a card."),
    YOU_DRAW("Successfully drew a card to your Hand."),
    DECK_SHUFFLE("The Resource and Gold card decks have been shuffled."),
    GAME_LEFT("Game left"),
    PLAYER_GAME_LEFT(" left the game");
    private final String message;

    LogsOnClient(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
