package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.diffs.game.HandDiffRemove;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.Arrays;

public class HandGUI implements Observer {
    private final HBox hand = new HBox();

    private final CardGUI[] cards = new CardGUI[3];

    public HandGUI() {
        GUI.getLightGame().getHand().attach(this);

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