package it.polimi.ingsw.view.GUI.Scenes;

import it.polimi.ingsw.view.GUI.Root;

public class GameScene extends SceneGUI{
    public GameScene() {
        super();
        content.getChildren().add(Root.GAME.getRoot());
    }
}
