package it.polimi.ingsw.view.GUI.Controllers;

import it.polimi.ingsw.view.ActualView;
import it.polimi.ingsw.view.ViewInterface;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * This class is the controller for the login form SceneBuilder scene.
 */
public class LoginFormControllerGUI {
    private static ActualView view;
    @FXML
    private TextField nicknameText;

    /**
     * This method is called when the login button is pressed.
     * It fetches the nickname from the text field and sends it to the controller.
     */
    public void login() {
        String nickname = nicknameText.getText();
        if(!nickname.isEmpty())
            try {
                view.getController().login(nickname);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }

    /**
     * This method is called when the controller is initialized.
     * @param view the view
     */
    public static void setView(ActualView view) {
        LoginFormControllerGUI.view = view;
    }
}
