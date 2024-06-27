package it.polimi.ingsw.lightModel.diffs.lobby_lobbyList;

import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent an updated to the LightLobby of the client containing the nicknames of players who are joining and leaving it
 * for a user who is joining the lobby
 */
public class LobbyDiffEditLogin extends  LobbyDiffEdit{
    /**The name of the lobby*/
    private final String lobbyName;
    /**The number of player needed to start the Game*/
    private final int numberMaxPlayer;
    /**A list of nicknames of player who left the lobby*/
    private final List<String> remove;
    /**A list of nicknames of player who joined the lobby*/
    private final List<String> add;

    /**
     * Creator, creates a LobbyDiff object with the given lists of nicknames to add and remove, the name of the lobby and the number of player needed to start the Game
     * @param remove the nicknames to remove
     * @param add the nicknames to add
     * @param lobbyName the name of the Lobby
     * @param numberMaxPlayer the number of player needed to start the Game
     */
    public LobbyDiffEditLogin(List<String> add, List<String> remove, String lobbyName, int numberMaxPlayer) {
        this.remove = new ArrayList<>(remove);
        this.add = new ArrayList<>(add);
        this.lobbyName = lobbyName;
        this.numberMaxPlayer =  numberMaxPlayer;
    }

    /**
     * Adds and remove the nicknames from the LightLobby, set the name of the lobby and the number of player needed to start the Game
     * @param lightLobby the LightLobby to which the diff applies
     */
    public void apply(LightLobby lightLobby) {
        lightLobby.nickDiff(add, remove);
        lightLobby.setName(this.lobbyName);
        lightLobby.setNumberMaxPlayer(this.numberMaxPlayer);
    }

}
