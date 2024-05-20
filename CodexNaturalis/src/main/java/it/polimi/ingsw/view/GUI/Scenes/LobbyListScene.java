package it.polimi.ingsw.view.GUI.Scenes;

import it.polimi.ingsw.view.GUI.Root;

public class LobbyListScene extends SceneGUI{
    public LobbyListScene() {
        super();
        content.getChildren().add(Root.LOBBY_LIST.getRoot());
    }
}
