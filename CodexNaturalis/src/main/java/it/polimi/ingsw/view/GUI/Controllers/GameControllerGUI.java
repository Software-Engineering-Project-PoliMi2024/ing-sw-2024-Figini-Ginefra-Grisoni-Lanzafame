package it.polimi.ingsw.view.GUI.Controllers;

import it.polimi.ingsw.view.GUI.Components.CodexGUI;
import it.polimi.ingsw.view.GUI.Components.HandGUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class GameControllerGUI implements Initializable {
    @FXML
    private AnchorPane main;

    private final HandGUI hand = new HandGUI();

    private final CodexGUI codex = new CodexGUI();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        cardsBox.alignmentProperty().setValue(Pos.CENTER);
//        cardsBox.setSpacing(10);
        main.getChildren().add(codex.getCodex());

        main.getChildren().add(hand.getHand());
    }
}
