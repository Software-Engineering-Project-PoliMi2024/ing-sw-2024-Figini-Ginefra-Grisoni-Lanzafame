package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.Arrays;

public class HandGUI {
    private final HBox hand = new HBox();

    private final CardGUI[] cards = new CardGUI[3];

    public HandGUI() {
        for (int i = 0; i < 3; i++) {
            cards[i] = new CardGUI(GUI.getLightGame().getHand().getCards()[i], CardFace.FRONT);
            hand.getChildren().add(cards[i].getImageView());
        }
        hand.setSpacing(10);
        hand.setAlignment(Pos.CENTER);

        AnchorPane.setBottomAnchor(hand, 10.0);
        AnchorPane.setLeftAnchor(hand, 10.0);
        AnchorPane.setRightAnchor(hand, 10.0);
    }

    public HBox getHand() {
        return hand;
    }

    public void update(){
        for (int i = 0; i < 3; i++) {
            cards[i].setTarget(GUI.getLightGame().getHand().getCards()[i]);
        }
    }
}
