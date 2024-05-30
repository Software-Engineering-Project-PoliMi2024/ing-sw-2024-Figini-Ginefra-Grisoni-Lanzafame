package it.polimi.ingsw.view.GUI.Components.HandRelated;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.GUI.Components.Utils.AnchoredPopUp;
import it.polimi.ingsw.view.GUI.Components.CardRelated.CardGUI;
import it.polimi.ingsw.view.GUI.Components.CardRelated.FlippableCardGUI;
import it.polimi.ingsw.view.GUI.Components.CardRelated.FlippablePlayableCard;
import it.polimi.ingsw.view.GUI.Components.CodexRelated.CodexGUI;
import it.polimi.ingsw.view.GUI.Components.CodexRelated.FrontierCardGUI;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import it.polimi.ingsw.view.GUI.StateGUI;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.Arrays;
import java.util.Objects;

public class HandGUI implements Observer {
    private final HBox hand = new HBox();

    private CodexGUI codex;

    private final FlippablePlayableCard[] handCards = new FlippablePlayableCard[3];

    private FlippableCardGUI secretObjective;
    private FlippablePlayableCard positioningCard = null;
    private CardGUI stubCard = null;
    private AnchoredPopUp handPopUp;
    private FrontierCardGUI closestFrontier;

    public HandGUI() {
        hand.setSpacing(10);
        hand.setAlignment(Pos.CENTER);

        AnchorPane.setBottomAnchor(hand, 0.0);
        AnchorPane.setLeftAnchor(hand, 0.0);
        AnchorPane.setRightAnchor(hand, 0.0);
        AnchorPane.setTopAnchor(hand, 0.0);

        GUI.getStateProperty().addListener((obs, oldState, newState) -> {

            if(newState == StateGUI.PLACE_CARD || newState == StateGUI.CHOOSE_START_CARD || newState == StateGUI.SELECT_OBJECTIVE) {
                handPopUp.open();
                handPopUp.setLocked(true);
            }
            else{
                handPopUp.close();
                handPopUp.setLocked(false);
            }
        });
    }

    public HBox getHand() {
        return hand;
    }

    public synchronized void addHandTo(AnchorPane parent){
        handPopUp = new AnchoredPopUp(parent, 0.8f, 0.2f, Pos.BOTTOM_CENTER, 0.25f);
        handPopUp.getContent().getChildren().add(hand);
        //handPopUp.getContent().setStyle(handPopUp.getContent().getStyle() +  "-fx-background-color: transparent");

        AnchorPane.setBottomAnchor(hand, 0.0);
        AnchorPane.setLeftAnchor(hand, 0.0);
        AnchorPane.setRightAnchor(hand, 0.0);
        AnchorPane.setTopAnchor(hand, 0.0);

        GUI.getLightGame().getHand().attach(this);
        //hand.setStyle(hand.getStyle() +  "-fx-background-color: transparent");
    }

    private void setSizeBindings(CardGUI card){
        card.getImageView().fitWidthProperty().bind(handPopUp.getContent().maxWidthProperty().subtract((handCards.length) * hand.spacingProperty().getValue()).divide(handCards.length + 1));
    }

    public void addCardToHand(FlippablePlayableCard card, boolean draggable){
        //find first null spot
        for (int i = 0; i < handCards.length; i++) {
            if(handCards[i] == null){
                handCards[i] = card;

//                card.getImageView().fitWidthProperty().bind(hand.prefWidthProperty().divide(cards.length));
//                card.getImageView().fitHeightProperty().bind(card.getImageView().fitWidthProperty().multiply(card.getImageView().getImage().getHeight()/card.getImageView().getImage().getWidth()));

                setSizeBindings(card);
                hand.getChildren().add(card.getImageView());


                if(draggable){
                    card.setOnHold(
                            e -> {
                                if((GUI.getStateProperty().get() != StateGUI.PLACE_CARD && GUI.getStateProperty().get() != StateGUI.CHOOSE_START_CARD) || card.getFace() == CardFace.FRONT && !card.isPlayable())
                                    return;
                                //System.out.println("Holding card");
                                codex.toggleFrontier(true);

                                positioningCard = card;
                                positioningCard.disable();

                                stubCard = new CardGUI(card);

                                Point2D mousePos2 = new Point2D(e.getSceneX() - codex.getCodex().getWidth()/2, e.getSceneY() - codex.getCodex().getHeight()/2);
                                Point2D pos2 = codex.getGridPosition(mousePos2).multiply(1/codex.getScale());
                                Position position = new Position((int) pos2.getX(), (int) pos2.getY());

                                codex.addCard(stubCard, position);
                            }
                    );

                    card.getImageView().setOnDragDetected(
                            e -> {
                                if((GUI.getStateProperty().get() != StateGUI.PLACE_CARD && GUI.getStateProperty().get() != StateGUI.CHOOSE_START_CARD)|| card.getFace() == CardFace.FRONT && !card.isPlayable())
                                    return;

                                handPopUp.close();
                                handPopUp.setLocked(true);
                                e.consume();
                            }
                    );

                    card.getImageView().setOnMouseDragged(
                            e -> {
                                if(((GUI.getStateProperty().get() != StateGUI.PLACE_CARD && GUI.getStateProperty().get() != StateGUI.CHOOSE_START_CARD) && GUI.getStateProperty().get() != StateGUI.CHOOSE_START_CARD) || card.getFace() == CardFace.FRONT && !card.isPlayable())
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
                                if((GUI.getStateProperty().get() != StateGUI.PLACE_CARD && GUI.getStateProperty().get() != StateGUI.CHOOSE_START_CARD) || card.getFace() == CardFace.FRONT && !card.isPlayable())
                                    return;


                                handPopUp.open();
                                codex.toggleFrontier(false);
                                codex.removeCard(stubCard);

                                if(closestFrontier == null)
                                    positioningCard.enable();
                                else{
                                    //System.out.println("Controller.placeCard");
                                    positioningCard.removeThisByFlippablePlayable(this::removeCardFromHand);

                                    if(GUI.getStateProperty().get() == StateGUI.PLACE_CARD || GUI.getStateProperty().get() == StateGUI.CHOOSE_START_CARD){
                                        try {
                                            //System.out.println(positioningCard.getTarget());
                                            CardGUI newCard = new CardGUI(positioningCard);
                                            codex.addCard(newCard, closestFrontier.getGridPosition());

                                            GUI.getControllerStatic().place(new LightPlacement(closestFrontier.getGridPosition(), positioningCard.getTarget(), positioningCard.getFace()));

                                        } catch (Exception ex) {
                                            throw new RuntimeException(ex);
                                        }
                                    }
                                    else
                                        throw new IllegalStateException("Invalid state");

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

    synchronized public void removeCardFromHand(FlippablePlayableCard card){
        for (int i = 0; i < handCards.length; i++) {
            if(card.equals(handCards[i])){
                hand.getChildren().remove(card.getImageView());
                handCards[i] = null;
                return;
            }
        }

        throw new IllegalArgumentException("Card not found");
    }

    synchronized public void update(){
        if(secretObjective == null && GUI.getLightGame().getHand().getSecretObjective() != null){
            secretObjective = new FlippableCardGUI(GUI.getLightGame().getHand().getSecretObjective());
            setSizeBindings(secretObjective);
            secretObjective.addThisTo(hand, 0);
        }

        int freeSpots = Arrays.stream(GUI.getLightGame().getHand().getCards()).filter(Objects::isNull).toArray().length;

        if(freeSpots == Arrays.stream(handCards).filter(Objects::isNull).toArray().length){
            return;
        }

        for (int i = 0; i < handCards.length; i++) {
            if(handCards[i] == null && GUI.getLightGame().getHand().getCards()[i] != null){
                LightCard target = GUI.getLightGame().getHand().getCards()[i];
                boolean playability = GUI.getLightGame().getHand().getCardPlayability().get(target);
                FlippablePlayableCard newCard = new FlippablePlayableCard(target, playability);

                newCard.addThisByFlippablePlayable(card -> addCardToHand(card, true));
            }else if(handCards[i] != null && GUI.getLightGame().getHand().getCards()[i] != null &&
                    handCards[i].getTarget().equals(GUI.getLightGame().getHand().getCards()[i])){
                LightCard flippableCardTarget = handCards[i].getTarget();
                boolean oldPlayability = handCards[i].isPlayable();
                boolean newPlayability = GUI.getLightGame().getHand().getCardPlayability().get(flippableCardTarget);
                if(oldPlayability != newPlayability)
                    handCards[i].setPlayable(newPlayability);
            }
//            else if (handCards[i] != null && GUI.getLightGame().getHand().getCards()[i] == null){
//                handCards[i].removeThisByFlippablePlayable(this::removeCardFromHand);
//                handCards[i] = null;
//            }
        }


    }

    public void setCodex(CodexGUI codex){
        this.codex = codex;
    }
}
