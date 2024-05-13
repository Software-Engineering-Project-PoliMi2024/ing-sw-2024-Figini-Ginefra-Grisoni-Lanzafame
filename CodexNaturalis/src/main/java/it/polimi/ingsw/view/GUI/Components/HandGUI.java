package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.utilities.Pair;
import it.polimi.ingsw.view.GUI.Components.CardRelated.CardGUI;
import it.polimi.ingsw.view.GUI.Components.CardRelated.FlippableCardGUI;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class HandGUI implements Observer {
    private final HBox hand = new HBox();

    private CodexGUI codex;

    private final FlippableCardGUI[] cards = new FlippableCardGUI[3];

    public HandGUI() {
        GUI.getLightGame().getHand().attach(this);

        for (int i = 0; i < 3; i++) {
            cards[i] = new FlippableCardGUI(GUI.getLightGame().getHand().getCards()[i]);
            hand.getChildren().add(cards[i].getImageView());
            int finalI = i;
            FlippableCardGUI card = cards[i];
            cards[i].getImageView().setOnMousePressed(
                    e -> {
                        hand.getChildren().remove(card.getImageView());
                        codex.getCodex().getChildren().add(card.getImageView());
                        Pair<Double, Double> pos = codex.snapToFrontier(e.getSceneX(), e.getSceneY());
                        cards[finalI].getImageView().setTranslateX(pos.first());
                        cards[finalI].getImageView().setTranslateY(pos.second());
                        e.consume();
                    }
            );
            cards[i].getImageView().setOnMouseDragged(
                    e -> {
                        Pair<Double, Double> pos = codex.snapToFrontier(e.getSceneX(), e.getSceneY());
                        cards[finalI].getImageView().setTranslateX(pos.first());
                        cards[finalI].getImageView().setTranslateY(pos.second());
                        e.consume();
                    }
            );
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

    public void setCodex(CodexGUI codex){
        this.codex = codex;
    }
}
