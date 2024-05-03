package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;

import java.util.ArrayList;
import java.util.List;

public class LobbyDiffEdit extends LobbyDiff {
    private final String lobbyName;
    private final List<String> remove;
    private final List<String> add;

    /**
     * Creates a LobbyDiff object.
     */
    public LobbyDiffEdit() {
        this.remove = new ArrayList<>();
        this.add = new ArrayList<>();
        this.lobbyName = "";
    }

    /**
     * @param remove the nicknames to remove
     * @param add the nicknames to add
     */
    public LobbyDiffEdit(List<String> add, List<String> remove, String lobbyName) {
        this.remove = new ArrayList<>(remove);
        this.add = new ArrayList<>(add);
        this.lobbyName = lobbyName;
    }

    /**
     * @param lobbyDiff the LobbyDiff to copy
     * @param add the nicknames to add
     * @param remove the nicknames to remove
     */
    public LobbyDiffEdit(LobbyDiffEdit lobbyDiff, List<String> add, List<String> remove, String lobbyName) {
        this.remove = lobbyDiff.getRemove();
        this.remove.addAll(remove);
        this.add = lobbyDiff.getAdd();
        this.add.addAll(add);
        this.lobbyName = lobbyName;
    }
    /**
     * @return the nicknames to add
     */
    public List<String> getAdd() {
        return add;
    }
    /**
     * @return the nicknames to remove
     */
    public List<String> getRemove() {
        return remove;
    }
    /**
     * @param lightLobby the LightLobby to which the diff applies
     */
    public void apply(LightLobby lightLobby) {
        lightLobby.setName(this.lobbyName);
        lightLobby.nickDiff(add, remove);
    }

}