package it.polimi.ingsw.view.GUI.Scenes;

import it.polimi.ingsw.view.GUI.Root;

public class LoginFormScene extends SceneGUI{
    public LoginFormScene() {
        super();
        content.getChildren().add(Root.LOGIN_FORM.getRoot());
    }
}
