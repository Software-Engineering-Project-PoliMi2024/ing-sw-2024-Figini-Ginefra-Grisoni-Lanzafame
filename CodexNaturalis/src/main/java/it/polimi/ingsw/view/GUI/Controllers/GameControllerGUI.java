package it.polimi.ingsw.view.GUI.Controllers;

import it.polimi.ingsw.model.cardReleted.cards.StartCard;
import it.polimi.ingsw.view.GUI.Components.CodexRelated.CodexGUI;
import it.polimi.ingsw.view.GUI.Components.HandGUI;
import it.polimi.ingsw.view.GUI.Components.LogsGUI;
import it.polimi.ingsw.view.GUI.Components.PopUp;
import it.polimi.ingsw.view.GUI.Components.StartCardGUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class GameControllerGUI implements Initializable {
    @FXML
    private AnchorPane main;

    private final HandGUI hand = new HandGUI();

    private final CodexGUI codex = new CodexGUI();
    private final LogsGUI logs = new LogsGUI();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        cardsBox.alignmentProperty().setValue(Pos.CENTER);
//        cardsBox.setSpacing(10);
        hand.setCodex(codex);

        main.getChildren().add(codex.getCodex());

        main.getChildren().add(hand.getHand());

        main.getChildren().add(logs.getLogsDisplay());

        PopUp popUp = new PopUp(main);
        Button button = new Button("Open PopUp");
        button.setOnAction(e -> popUp.open());

        StartCardGUI startCardGUI = new StartCardGUI(main);

        main.getChildren().add(button);
    }
}
