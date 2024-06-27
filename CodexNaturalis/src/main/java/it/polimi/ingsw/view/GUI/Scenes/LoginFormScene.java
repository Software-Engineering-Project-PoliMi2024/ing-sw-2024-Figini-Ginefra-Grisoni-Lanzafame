package it.polimi.ingsw.view.GUI.Scenes;

import it.polimi.ingsw.view.GUI.Root;

/**
 * The scene that contains the list of lobbies
 */
public class LoginFormScene extends SceneGUI{
    public LoginFormScene() {
        super();
        content.getChildren().add(Root.LOGIN_FORM.getRoot());
    }
}
