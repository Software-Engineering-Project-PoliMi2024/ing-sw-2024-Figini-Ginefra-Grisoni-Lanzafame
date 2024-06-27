package it.polimi.ingsw.view.GUI.Scenes;

import it.polimi.ingsw.view.GUI.Root;

/**
 * The scene that contains the list of lobbies
 */
public class LobbyScene extends SceneGUI{
    public LobbyScene() {
        super();
        content.getChildren().add(Root.LOBBY.getRoot());
    }
}
