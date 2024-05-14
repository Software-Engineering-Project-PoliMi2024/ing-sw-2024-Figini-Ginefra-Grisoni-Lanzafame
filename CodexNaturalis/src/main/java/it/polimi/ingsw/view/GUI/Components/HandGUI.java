package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.model.utilities.Pair;
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
            FlippableCardGUI card = cards[i];

            cards[i].getImageView().setOnMouseEntered(
                    e -> {
                        card.getImageView().setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
                        e.consume();
                    }
            );

            cards[i].getImageView().setOnMouseExited(
                    e -> {
                        card.getImageView().setStyle("");
                        e.consume();
                    }
            );

            cards[i].getImageView().setOnMousePressed(
                    e -> {
                        card.getImageView().setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

                        hand.getChildren().remove(card.getImageView());
                        codex.getCodex().getChildren().add(card.getImageView());

                        double mouseX = e.getSceneX() - codex.getCodex().getWidth()/2;
                        double mouseY = e.getSceneY() - codex.getCodex().getHeight()/2;

                        card.setTargetTranslation(mouseX, mouseY);
                        card.getImageView().setTranslateX(mouseX);
                        card.getImageView().setTranslateY(mouseY);

                        codex.toggleFrontier();
                        e.consume();
                    }
            );
            cards[i].getImageView().setOnMouseDragged(
                    e -> {
                        card.getImageView().setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

                        Pair<Double, Double> pos = codex.snapToFrontier(e.getSceneX(), e.getSceneY());

                        Double mouseX = e.getSceneX() - codex.getCodex().getWidth()/2;
                        Double mouseY = e.getSceneY() - codex.getCodex().getHeight()/2;
                        double width = card.getImageView().getImage().getWidth() * 0.3;
                        double height = card.getImageView().getImage().getHeight() * 0.3;
                        //Check if pos is close enough to the mouse position
                        if (Math.abs(pos.first() - mouseX) < width/2 && Math.abs(pos.second() - mouseY) < height/2){
                            card.setTargetTranslation(pos.first(), pos.second());
                        }
                        else{
                            card.setTargetTranslation(mouseX, mouseY);
                        }

                        e.consume();
                    }
            );
            cards[i].getImageView().setOnMouseReleased(
                    e -> {
                        card.getImageView().setStyle("");
                        codex.getCodex().getChildren().remove(card.getImageView());
                        hand.getChildren().add(card.getImageView());
                        //Reset position
                        card.setTargetTranslation(0, 0);
                        codex.toggleFrontier();
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
