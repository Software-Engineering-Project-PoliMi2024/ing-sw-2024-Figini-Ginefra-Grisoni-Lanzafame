package it.polimi.ingsw.lightModel.diffs.lobby_lobbyList;

import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;

import java.util.ArrayList;
import java.util.List;

public class LobbyDiffEditLogin extends  LobbyDiffEdit{
    private final String lobbyName;
    private final int numberMaxPlayer;
    private final List<String> remove;
    private final List<String> add;

    /**
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
     * @param lobbyDiff the LobbyDiff to copy
     * @param remove the nicknames to remove
     * @param add the nicknames to add
     * @param lobbyName the name of the Lobby
     * @param numberMaxPlayer the number of player needed to start the Game
     */
    public LobbyDiffEditLogin(LobbyDiffEdit lobbyDiff, List<String> add, List<String> remove, String lobbyName, int numberMaxPlayer) {
        this.remove = lobbyDiff.getRemove();
        this.remove.addAll(remove);
        this.add = lobbyDiff.getAdd();
        this.add.addAll(add);
        this.lobbyName = lobbyName;
        this.numberMaxPlayer = numberMaxPlayer;
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
        lightLobby.setName(this.lobbyName);
        lightLobby.setNumberMaxPlayer(this.numberMaxPlayer);
    }

}
