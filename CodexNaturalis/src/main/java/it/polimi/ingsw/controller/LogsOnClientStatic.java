package it.polimi.ingsw.controller;

import it.polimi.ingsw.Configs;

public class LogsOnClientStatic {
    //connectionForm related
    public static String CONNECTION_SUCCESS = "Connected to the server successfully.";
    public static String CONNECTION_ERROR = "Failed to connect to the server.";
    public static String CONNECTION_LOST_CLIENT_SIDE = "Lost connection with the server.";

    //login related
    public static String NAME_TAKEN = "The chosen nickname is already in use.";
    public static String SERVER_JOINED = "Joined the server.";
    public static String SERVER_LEFT = "Left the server.";
    public static String NOT_VALID_NICKNAME = "The chosen nickname is not valid.";

    //lobbyList related
    public static String JOIN_LOBBY_LIST = "Joined the lobbyList.";
    public static String LEFT_LOBBY_LIST = "Left the lobbyList.";
    public static String LOBBY_NONEXISTENT = "The queried lobby does not exist.";
    public static String LOBBY_NAME_TAKEN = "The chosen lobby name is already taken.";
    public static String NOT_VALID_LOBBY_NAME = "The chosen lobby name is not valid.";
    public static String INVALID_MAX_PLAYER_COUNT = "the number of player per game must be 1 < n < 5.";
    public static String LOBBY_CREATED_YOU = "New Lobby creation successful.";
    public static String LOBBY_CREATED_OTHERS = "Added a new lobby.";
    public static String LOBBY_REMOVED_YOU = "Lobby removal successful.";

    //lobby related
    public static String LOBBY_JOIN_YOU = "Successfully joined the lobby.";
    public static String LOBBY_LEFT = "Left the lobby.";
    public static String LOBBY_LEFT_OTHER = " left the lobby.";
    public static String LOBBY_JOIN_OTHER = " joined the lobby";

    //joinGame related
    public static String GAME_CREATED = "Lobby is full, the game is starting.";
    public static String GAME_JOINED = "Joined game.";

    //gameSetup related  = startCard face and SecretObjective option selection)
    public static String YOU_PLACE_STARTCARD = "Successfully placed your Start Card.";
    public static String YOU_CHOSE_PAWN = "Successfully chose your Pawn.";
    public static String PLAYER_CHOSE_PAWN = " chose their Pawn.";
    public static String EVERYONE_CHOSE_PAWN = "All players have chosen their Pawn.";
    public static String PAWN_TAKEN = "The pawn chose is already taken.";
    public static String YOU_CHOSE_SECRET_OBJ = "Successfully chose your Secret Objective.";
    public static String PLAYER_PLACE_STARTCARD = " placed their Start Card.";
    public static String PLAYER_CHOSE_SECRET_OBJ = " chose their Secret Objective.";
    public static String EVERYONE_PLACED_STARTCARD = "All players have placed their Start Cards.";
    public static String WAIT_SECRET_OBJECTIVE = "Waiting for other players to choose their Secret Objectives.";
    public static String EVERYONE_CHOSE_OBJ = "All players have chosen their Secret Objectives.";

    //gameCards related  = card place or draw)
    public static String CARD_NOT_PLACEABLE = "Card cannot be placed.";

    public static String PLAYER_PLACED = " placed a card.";
    public static String YOU_PLACED = "Successfully placed a card in your Codex.";
    public static String PLAYER_DRAW = " drew a card.";
    public static String YOU_DRAW = "Successfully drew a card to your Hand.";

    //gameTurn related
    public static String YOUR_TURN = "It's now your turn to play.";
    public static String PLAYER_TURN = "'s turn";
    public static String WAIT_STARTCARD = "Waiting for other players to place their Start Cards.";
    public static String LAST_TURN = "The final round of turns has begun.";
    public static String WAIT_PAWN = "Waiting for other players to choose their pawn.";
    public static String GAME_END = "The game has ended.";

    //gameParty related
    public static String PLAYER_JOINED = " joined the game";
    public static String PLAYER_GAME_LEFT = " left the game";
    public static String LAST_PLAYER = "You are the last player in the game";

    //gameCountDown related
    public static String COUNTDOWN_START = "A CountDown of " +Configs.lastInGameTimerSeconds + " seconds is starting now";
    public static String COUNTDOWN_INTERRUPTED = "The CountDown has been stopped";

    //chat related
    public static String RECEIVED_PRIVATE_MESSAGE = "You received a private message from: ";
    public static String SENT_PRIVATE_MESSAGE = "You sent a private message to: ";
    public static String RECEIVED_PUBLIC_MESSAGE = "A new message has been posted in the public chat";
    public static String SENT_PUBLIC_MESSAGE = "You posted a new message in the public chat";
}
