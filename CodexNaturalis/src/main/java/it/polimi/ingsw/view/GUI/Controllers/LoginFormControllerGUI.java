package it.polimi.ingsw.view.GUI.Controllers;

import it.polimi.ingsw.view.ActualView;
import it.polimi.ingsw.view.ViewInterface;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginFormControllerGUI {
    private static ActualView view;
    @FXML
    private TextField nicknameText;

    public void login() {
        String nickname = nicknameText.getText();
        if(!nickname.isEmpty())
            try {
                view.getController().login(nickname);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }

    public static void setView(ActualView view) {
        LoginFormControllerGUI.view = view;
    }
}
