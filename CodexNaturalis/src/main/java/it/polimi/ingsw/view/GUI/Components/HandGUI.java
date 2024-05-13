package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.GUI.Components.CardRelated.CardGUI;
import it.polimi.ingsw.view.GUI.Components.CardRelated.FlippableCardGUI;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class HandGUI implements Observer {
    private final HBox hand = new HBox();

    private final FlippableCardGUI[] cards = new FlippableCardGUI[3];

    public HandGUI() {
        GUI.getLightGame().getHand().attach(this);

        for (int i = 0; i < 3; i++) {
            cards[i] = new FlippableCardGUI(GUI.getLightGame().getHand().getCards()[i]);
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
