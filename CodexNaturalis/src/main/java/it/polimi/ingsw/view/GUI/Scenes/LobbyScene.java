package it.polimi.ingsw.view.GUI.Scenes;

import it.polimi.ingsw.view.GUI.Root;

public class LobbyScene extends SceneGUI{
    public LobbyScene() {
        super();
        content.getChildren().add(Root.LOBBY.getRoot());
    }
}
