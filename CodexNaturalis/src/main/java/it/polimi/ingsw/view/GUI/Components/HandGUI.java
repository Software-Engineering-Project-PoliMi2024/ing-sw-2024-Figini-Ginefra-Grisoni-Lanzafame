package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.view.GUI.Components.CardRelated.FlippableCardGUI;
import it.polimi.ingsw.view.GUI.Components.CodexRelated.CodexGUI;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.Arrays;
import java.util.Objects;

public class HandGUI implements Observer {
    private final HBox hand = new HBox();

    private CodexGUI codex;

    private final FlippableCardGUI[] cards = new FlippableCardGUI[3];

    public HandGUI() {
        GUI.getLightGame().getHand().attach(this);


        hand.setSpacing(10);
        hand.setAlignment(Pos.CENTER);

        AnchorPane.setBottomAnchor(hand, 0.0);
        AnchorPane.setLeftAnchor(hand, 0.0);
        AnchorPane.setRightAnchor(hand, 0.0);
    }

    public HBox getHand() {
        return hand;
    }

    public void addHand(AnchorPane parent){
        parent.getChildren().add(hand);

        hand.prefWidthProperty().bind(parent.widthProperty().multiply(0.9));
        hand.prefHeightProperty().bind(parent.heightProperty(). multiply(0.9));
    }

    public void addCard(FlippableCardGUI card){
        //find first null spot
        for (int i = 0; i < 3; i++) {
            if(cards[i] == null){
                cards[i] = card;
                hand.getChildren().add(card.getImageView());

                card.getImageView().fitWidthProperty().bind(hand.prefWidthProperty().divide(3));
                card.getImageView().fitHeightProperty().bind(card.getImageView().fitWidthProperty().multiply(card.getImageView().getImage().getHeight()/card.getImageView().getImage().getWidth()));

                card.getImageView().setOnMouseEntered(
                        e -> {
                            card.getImageView().setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
                            e.consume();
                        }
                );

                card.getImageView().setOnMouseExited(
                        e -> {
                            card.getImageView().setStyle("");
                            e.consume();
                        }
                );

                card.setOnHold(
                        e -> {
                            System.out.println("Holding card");
                        }
                );

                card.setOnHoldRelease(
                        e -> {
                            System.out.println("Releasing card");
                            e.consume();
                        }
                );

//                card.getImageView().setOnMousePressed(
//                        e -> {
//                            card.getImageView().setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
//
//                            card.setScale(codex.getScale());
//
//                            hand.getChildren().remove(card.getImageView());
//                            codex.getCodex().getChildren().add(card.getImageView());
//
//                            double mouseX = e.getSceneX() - codex.getCodex().getWidth()/2;
//                            double mouseY = e.getSceneY() - codex.getCodex().getHeight()/2;
//
//                            card.setTranslation(mouseX, mouseY);
//                            card.getImageView().setTranslateX(mouseX);
//                            card.getImageView().setTranslateY(mouseY);
//
//                            codex.toggleFrontier();
//                            e.consume();
//                        }
//                );
//                card.getImageView().setOnMouseDragged(
//                        e -> {
//                            card.getImageView().setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
//
//                            Point2D mousePos = new Point2D(e.getSceneX() - codex.getCodex().getWidth()/2, e.getSceneY() - codex.getCodex().getHeight()/2);
//
//                            Point2D pos = codex.snapToFrontier(mousePos);
//
//                            //Check if pos is close enough to the mouse position
//                            if (Math.abs(pos.getX() - mousePos.getX()) < GUIConfigs.cardWidth/2 * codex.getScale() && Math.abs(pos.getY() - mousePos.getY()) < GUIConfigs.cardHeight/2 * codex.getScale()){
//                                card.setTranslation(pos.getX(), pos.getY());
//                            }
//                            else{
//                                card.setTranslation(mousePos.getX(), mousePos.getY());
//                            }
//
//                            e.consume();
//                        }
//                );

//                card.getImageView().setOnMouseReleased(
//                        e -> {
//                            card.setScale(1);
//                            card.getImageView().setStyle("");
//                            codex.getCodex().getChildren().remove(card.getImageView());
//                            hand.getChildren().add(card.getImageView());
//                            //Reset position
//                            card.setTranslation(0, 0);
//                            codex.toggleFrontier();
//                            e.consume();
//                        }
//                );


                return;
            }
        }

        throw new IllegalStateException("Hand is full");
    }

    public void removeCard(FlippableCardGUI card){
        for (int i = 0; i < 3; i++) {
            if(cards[i] == card){
                hand.getChildren().remove(card.getImageView());
                cards[i] = null;
                return;
            }
        }

        throw new IllegalArgumentException("Card not found");
    }

    public void update(){
        System.out.println("HandGUI update");
        int freeSpots = Arrays.stream(GUI.getLightGame().getHand().getCards()).filter(Objects::isNull).toArray().length;

        if(freeSpots == Arrays.stream(cards).filter(Objects::isNull).toArray().length){
            return;
        }

        for (int i = 0; i < 3; i++) {
            if(cards[i] == null && GUI.getLightGame().getHand().getCards()[i] != null){
                addCard(new FlippableCardGUI(GUI.getLightGame().getHand().getCards()[i]));
            }
            else if (cards[i] != null && GUI.getLightGame().getHand().getCards()[i] == null){
                hand.getChildren().remove(cards[i].getImageView());
                cards[i] = null;
            }
        }
    }

    public void setCodex(CodexGUI codex){
        this.codex = codex;
    }
}
