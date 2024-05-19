package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.GUI.Components.CardRelated.CardGUI;
import it.polimi.ingsw.view.GUI.Components.CardRelated.DraggableCard;
import it.polimi.ingsw.view.GUI.Components.CardRelated.FlippableCardGUI;
import it.polimi.ingsw.view.GUI.Components.CodexRelated.CodexGUI;
import it.polimi.ingsw.view.GUI.Components.CodexRelated.FrontierCardGUI;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import it.polimi.ingsw.view.GUI.StateGUI;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.Arrays;
import java.util.Objects;

public class HandGUI implements Observer {
    private final HBox hand = new HBox();

    private CodexGUI codex;

    private final FlippableCardGUI[] cards = new FlippableCardGUI[4];

    private final FlippableCardGUI secretObjective;
    private FlippableCardGUI positioningCard = null;
    private DraggableCard stubCard = null;
    private AnchoredPopUp handPopUp;
    private FrontierCardGUI closestFrontier;

    public HandGUI() {
        GUI.getLightGame().getHand().attach(this);

        hand.setSpacing(10);
        hand.setAlignment(Pos.CENTER);

        AnchorPane.setBottomAnchor(hand, 0.0);
        AnchorPane.setLeftAnchor(hand, 0.0);
        AnchorPane.setRightAnchor(hand, 0.0);
        AnchorPane.setTopAnchor(hand, 0.0);

        GUI.getStateProperty().addListener((obs, oldState, newState) -> {
            if(newState == StateGUI.PLACE_CARD){
                handPopUp.open();
                handPopUp.setLocked(true);
            }
            else{
                handPopUp.close();
                handPopUp.setLocked(false);
            }
        });

        secretObjective = new FlippableCardGUI(new LightCard(1));
        this.addCard(secretObjective, false);
    }

    public HBox getHand() {
        return hand;
    }

    public void addHandTo(AnchorPane parent){
        handPopUp = new AnchoredPopUp(parent, 0.8f, 0.2f, Pos.BOTTOM_CENTER, 0.25f);

        handPopUp.getContent().getChildren().add(hand);

        hand.prefWidthProperty().bind(handPopUp.getContent().prefWidthProperty());
        hand.prefHeightProperty().bind(handPopUp.getContent().prefHeightProperty());
    }

    public void addCard(FlippableCardGUI card, boolean draggable){
        //find first null spot
        for (int i = 0; i < cards.length; i++) {
            if(cards[i] == null){
                cards[i] = card;

//                card.getImageView().fitWidthProperty().bind(hand.prefWidthProperty().divide(cards.length));
//                card.getImageView().fitHeightProperty().bind(card.getImageView().fitWidthProperty().multiply(card.getImageView().getImage().getHeight()/card.getImageView().getImage().getWidth()));

                card.getImageView().setFitWidth(hand.prefWidthProperty().getValue()/ cards.length - hand.spacingProperty().getValue());
                card.getImageView().fitWidthProperty().bind(hand.prefWidthProperty().divide(cards.length).subtract(hand.spacingProperty().getValue()));

                hand.getChildren().add(card.getImageView());


                if(draggable){
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
                                if(GUI.getStateProperty().get() != StateGUI.PLACE_CARD)
                                    return;
                                System.out.println("Holding card");
                                codex.toggleFrontier(true);

                                positioningCard = card;
                                positioningCard.disable();

                                stubCard = new DraggableCard(card);

                                Point2D mousePos2 = new Point2D(e.getSceneX() - codex.getCodex().getWidth()/2, e.getSceneY() - codex.getCodex().getHeight()/2);
                                Point2D pos2 = codex.getGridPosition(mousePos2).multiply(1/codex.getScale());
                                Position position = new Position((int) pos2.getX(), (int) pos2.getY());

                                codex.addCard(stubCard, position);
                            }
                    );

                    card.getImageView().setOnDragDetected(
                            e -> {
                                if(GUI.getStateProperty().get() != StateGUI.PLACE_CARD)
                                    return;

                                handPopUp.close();
                                handPopUp.setLocked(true);
                                e.consume();
                            }
                    );

                    card.getImageView().setOnMouseDragged(
                            e -> {
                                if(GUI.getStateProperty().get() != StateGUI.PLACE_CARD)
                                    return;

                                if(stubCard == null)
                                    return;

                                Point2D mousePos = new Point2D(e.getSceneX() - codex.getCodex().getWidth()/2, e.getSceneY() - codex.getCodex().getHeight()/2);
                                FrontierCardGUI closestFrontier = codex.snapToFrontier(mousePos);
                                Point2D pos = new Point2D(closestFrontier.getCard().getTranslateX(), closestFrontier.getCard().getTranslateY());

                                if (Math.abs(pos.getX() - mousePos.getX()) < GUIConfigs.cardWidth/2 * codex.getScale() && Math.abs(pos.getY() - mousePos.getY()) < GUIConfigs.cardHeight/2 * codex.getScale()){
                                    stubCard.setTranslation(pos.getX(), pos.getY());
                                    this.closestFrontier = closestFrontier;
                                }
                                else{
                                    stubCard.setTranslation(mousePos.getX(), mousePos.getY());
                                    this.closestFrontier = null;
                                }

                                e.consume();
                            }
                    );

                    card.setOnHoldRelease(
                            e -> {
                                if(GUI.getStateProperty().get() != StateGUI.PLACE_CARD)
                                    return;


                                handPopUp.open();
                                codex.toggleFrontier(false);
                                codex.removeCard(stubCard);

                                if(closestFrontier == null)
                                    positioningCard.enable();
                                else{
                                    System.out.println("Controller.placeCard");
                                    codex.addCard(new CardGUI(positioningCard), closestFrontier.getGridPosition());
                                    this.removeCard(positioningCard);
                                    positioningCard = null;
                                    this.closestFrontier = null;
                                }
                            }
                    );
                }


                return;
            }
        }

        throw new IllegalStateException("Hand is full");
    }

    public void removeCard(FlippableCardGUI card){
        for (int i = 0; i < 4; i++) {
            if(cards[i] == card){
                hand.getChildren().remove(card.getImageView());
                cards[i] = null;
                return;
            }
        }

        throw new IllegalArgumentException("Card not found");
    }

    public void update(){
        secretObjective.setTarget(GUI.getLightGame().getHand().getSecretObjective());


        int freeSpots = Arrays.stream(GUI.getLightGame().getHand().getCards()).filter(Objects::isNull).toArray().length;

        if(freeSpots == Arrays.stream(cards).filter(Objects::isNull).toArray().length){
            return;
        }

        for (int i = 1; i < 4; i++) {
            if(cards[i] == null && GUI.getLightGame().getHand().getCards()[i-1] != null){
                addCard(new FlippableCardGUI(GUI.getLightGame().getHand().getCards()[i-1]), true);
            }
            else if (cards[i] != null && GUI.getLightGame().getHand().getCards()[i-1] == null){
                hand.getChildren().remove(cards[i].getImageView());
                cards[i] = null;
            }
        }


    }

    public void setCodex(CodexGUI codex){
        this.codex = codex;
    }
}
