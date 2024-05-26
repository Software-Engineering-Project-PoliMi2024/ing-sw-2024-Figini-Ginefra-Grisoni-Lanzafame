package it.polimi.ingsw.lightModel;


import it.polimi.ingsw.lightModel.diffs.game.GameDiffPlayerActivity;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyDiffEdit;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyDiffEditLogin;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyListDiff;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyListDiffEdit;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.model.tableReleted.Lobby;

import java.util.ArrayList;
import java.util.List;

public class DiffGenerator {
    /**
     * Generates a diff that adds a lobby to the lobbyList
     * @param addedLobby the lobby to add
     * @return the diff that adds the lobby
     */
    public static LobbyListDiff addLobbyDiff(Lobby addedLobby){
        return new LobbyListDiffEdit(List.of(Lightifier.lightify(addedLobby)), new ArrayList<LightLobby>());
    }

    /**
     * Generates a diff that removes a lobby from the lobbyList
     * @param removedLobby the lobby to remove
     * @return the diff that removes the lobby
     */
    public static LobbyListDiff removeLobbyDiff(Lobby removedLobby){
        return new LobbyListDiffEdit(new ArrayList<LightLobby>(), List.of(Lightifier.lightify(removedLobby)));
    }

    /**
     * Generates a diff that updates the lobbyList with the history of the lobbies
     * @param lobbyHistory the history of the lobbies
     * @return the diff that updates the lobbyList with the history of the lobbies
     */
    public static LobbyListDiff lobbyListHistory(List<Lobby> lobbyHistory){
        return new LobbyListDiffEdit(new ArrayList<LightLobby>(Lightifier.lightify(lobbyHistory)), new ArrayList<LightLobby>());
    }

    /**
     * Generates a diff that updates the lobby with the attributes of the lobby joined
     * @param lobbyJoined the lobby joined by the subscriber
     * @return the diff that updates the lobby with the attributes of the lobby joined
     */
    public static LobbyDiffEditLogin diffJoinLobby(Lobby lobbyJoined){
        return new LobbyDiffEditLogin(lobbyJoined.getLobbyPlayerList(), new ArrayList<>(), lobbyJoined.getLobbyName(), lobbyJoined.getNumberOfMaxPlayer());
    }

    /**
     * Generates a diff that adds a user to the lobby
     * @param nickname the nickname of the user to add
     * @return the diff that adds the user to the lobby
     */
    public static LobbyDiffEdit diffAddUserToLobby(String nickname){
        return new LobbyDiffEdit(List.of(nickname), new ArrayList<>());
    }

    /**
     * Generates a diff that removes a user from the lobby
     * @param nickname the nickname of the user to remove
     * @return the diff that removes the user from the lobby
     */
    public static LobbyDiffEdit diffRemoveUserFromLobby(String nickname){
        return new LobbyDiffEdit(new ArrayList<>(), List.of(nickname));
    }

    /**
     * Generates a diff that updates the lightGame with the name of the user joined
     * @param nickname the nickname of the user joined
     * @return the diff that updates the lightGame with the name of the user joined
     */
    public static GameDiffPlayerActivity diffAddUserToGame(String nickname){
        return new GameDiffPlayerActivity(List.of(nickname), new ArrayList<>());
    }



}
