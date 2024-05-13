package it.polimi.ingsw.view.GUI.Controllers;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.GUI.CardMuseumGUI;
import it.polimi.ingsw.view.GUI.Components.CardGUI;
import it.polimi.ingsw.view.GUI.Components.HandGUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class GameControllerGUI implements Initializable {
    @FXML
    private AnchorPane main;

    private final HandGUI hand = new HandGUI();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        cardsBox.alignmentProperty().setValue(Pos.CENTER);
//        cardsBox.setSpacing(10);
        main.getChildren().add(hand.getHand());
    }
}
