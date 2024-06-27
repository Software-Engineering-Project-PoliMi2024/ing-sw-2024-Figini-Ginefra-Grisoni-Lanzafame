package it.polimi.ingsw.lightModel.diffs.lobby_lobbyList;

import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent an updated to the LightLobby of the client containing the nicknames of players who are joining and leaving it
 * for a user who is already in the lobby
 */
public class LobbyDiffEdit extends LobbyDiff {
    /**A list of nicknames of player who left the lobby*/
    private final List<String> remove;
    /**A list of nicknames of player who joined the lobby*/
    private final List<String> add;

    /**
     * Creates a LobbyDiff object without adding or removing any name.
     */
    public LobbyDiffEdit() {
        this.remove = new ArrayList<>();
        this.add = new ArrayList<>();
    }

    /**
     * Creator, creates a LobbyDiff object with the given lists of nicknames to add and remove
     * @param remove a list of nicknames to remove
     * @param add a list of nicknames to add
     */
    public LobbyDiffEdit(List<String> add, List<String> remove) {
        this.remove = new ArrayList<>(remove);
        this.add = new ArrayList<>(add);;
    }

    /**
     * @return the list of nicknames to add
     */
    public List<String> getAdd() {
        return add;
    }
    /**
     * @return the list of nicknames to remove
     */
    public List<String> getRemove() {
        return remove;
    }
    /**
     * Adds and remove the nicknames from the LightLobby
     * @param lightLobby the LightLobby to which the diff applies
     */
    public void apply(LightLobby lightLobby) {
        lightLobby.nickDiff(add, remove);
    }

}