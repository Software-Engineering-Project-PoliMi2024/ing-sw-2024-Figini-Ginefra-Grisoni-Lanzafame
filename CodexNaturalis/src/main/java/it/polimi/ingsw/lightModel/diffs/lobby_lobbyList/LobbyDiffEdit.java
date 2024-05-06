package it.polimi.ingsw.lightModel.diffs.lobby_lobbyList;

import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;

import java.util.ArrayList;
import java.util.List;

public class LobbyDiffEdit extends LobbyDiff {
    private final List<String> remove;
    private final List<String> add;

    /**
     * Creates a LobbyDiff object.
     */
    public LobbyDiffEdit() {
        this.remove = new ArrayList<>();
        this.add = new ArrayList<>();
    }

    /**
     * @param remove the nicknames to remove
     * @param add the nicknames to add
     */
    public LobbyDiffEdit(List<String> add, List<String> remove) {
        this.remove = new ArrayList<>(remove);
        this.add = new ArrayList<>(add);;
    }

    /**
     * @param lobbyDiff the LobbyDiff to copy
     * @param add the nicknames to add
     * @param remove the nicknames to remove
     */
    public LobbyDiffEdit(LobbyDiffEdit lobbyDiff, List<String> add, List<String> remove) {
        this.remove = lobbyDiff.getRemove();
        this.remove.addAll(remove);
        this.add = lobbyDiff.getAdd();
        this.add.addAll(add);
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
        lightLobby.nickDiff(add, remove);
    }

}