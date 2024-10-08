package it.polimi.ingsw.view.GUI.Components.HandRelated;

import it.polimi.ingsw.utils.designPatterns.Observer;
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
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.Arrays;
import java.util.Objects;

/**
 * This class represents the GUI component responsible for housing the hand of the player and the secret objective.

 */
public class HandGUI implements Observer {
    /** The hand of the player */
    private final HBox hand = new HBox();
    /** The codex */
    private CodexGUI codex;
    /** the cards in the hand */
    private final FlippablePlayableCard[] handCards = new FlippablePlayableCard[3];
    /** The secret objective */
    private FlippableCardGUI secretObjective;
    /** The card that is being positioned */
    private FlippablePlayableCard positioningCard = null;

    /** The card that previews the cards position in the codex */
    private CardGUI stubCard = null;

    /** The pop up that contains the hand */
    private AnchoredPopUp handPopUp;

    /** The closest frontier to the card */
    private FrontierCardGUI closestFrontier;

    /** Builds a new HandGUI */
    public HandGUI() {
        hand.setSpacing(10);
        hand.setAlignment(Pos.CENTER);
        hand.setPadding(new Insets(10));

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

    /**
     * Returns the content of the hand.
     * @return the content of the hand.
     */
    public HBox getHand() {
        return hand;
    }

    /** Adds the hand to the given parent
     * @param parent the parent to add the hand to
     */
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

    /**
     * Sets the size bindings of the given card.
     * @param card the card to set the size bindings of.
     */
    private void setSizeBindings(CardGUI card){
        card.getImageView().fitWidthProperty().bind(handPopUp.getContent().maxWidthProperty().subtract((handCards.length) * hand.spacingProperty().getValue()).divide(handCards.length + 1));
    }

    /**
     * Finds first empty spot in the hand and adds a card there.
     * The card will be draggable if the draggable parameter is true.
     * If the hand is full, an IllegalStateException will be thrown.
     * All the listeners of the user interaction with the card will be added.
     * @param card the card to add to the hand.
     * @param draggable whether the card should be draggable or not.
     */
    public void addCardToHand(FlippablePlayableCard card, boolean draggable){
        //find first null spot and add the card there
        for (int i = 0; i < handCards.length; i++) {
            if(handCards[i] == null){
                handCards[i] = card;

                setSizeBindings(card);
                hand.getChildren().add(card.getImageView());

                if(draggable){
                    card.setOnHold(
                            e -> {
                                //Checks if the game state allows the card to be placed and if the card is playable
                                if((GUI.getStateProperty().get() != StateGUI.PLACE_CARD && GUI.getStateProperty().get() != StateGUI.CHOOSE_START_CARD) || card.getFace() == CardFace.FRONT && !card.isPlayable())
                                    return;

                                //Turns the frontiers of the codex on
                                codex.toggleFrontier(true);

                                //Sets the positioning card to the card that is being dragged
                                positioningCard = card;
                                positioningCard.disable();

                                //Creates a stub card that previews the position of the card in the codex by copying the card that is being dragged
                                stubCard = new CardGUI(card);

                                //Positions the stub card to the mouse position
                                Point2D mousePos2 = new Point2D(e.getSceneX() - codex.getCodex().getWidth()/2, e.getSceneY() - codex.getCodex().getHeight()/2);
                                Point2D pos2 = codex.getGridPosition(mousePos2).multiply(1/codex.getScale());
                                Position position = new Position((int) pos2.getX(), (int) pos2.getY());

                                //Adds the stub card to the codex
                                codex.addCard(stubCard, position);
                            }
                    );

                    card.getImageView().setOnDragDetected(
                            e -> {
                                // Checks if the game state allows the card to be placed and if the card is playable
                                if((GUI.getStateProperty().get() != StateGUI.PLACE_CARD && GUI.getStateProperty().get() != StateGUI.CHOOSE_START_CARD)|| card.getFace() == CardFace.FRONT && !card.isPlayable())
                                    return;

                                //Closes the hand
                                handPopUp.close();
                                handPopUp.setLocked(true);
                                e.consume();
                            }
                    );

                    card.getImageView().setOnMouseDragged(
                            e -> {
                                //Checks if the game state allows the card to be placed and if the card is playable
                                if(((GUI.getStateProperty().get() != StateGUI.PLACE_CARD && GUI.getStateProperty().get() != StateGUI.CHOOSE_START_CARD) && GUI.getStateProperty().get() != StateGUI.CHOOSE_START_CARD) || card.getFace() == CardFace.FRONT && !card.isPlayable())
                                    return;

                                //Checks if the stub card has been created
                                if(stubCard == null)
                                    return;

                                //Positions the stub card to the mouse position
                                Point2D mousePos = new Point2D(e.getSceneX() - codex.getCodex().getWidth()/2, e.getSceneY() - codex.getCodex().getHeight()/2);
                                FrontierCardGUI closestFrontier = codex.snapToFrontier(mousePos);
                                Point2D pos = new Point2D(closestFrontier.getCard().getTranslateX(), closestFrontier.getCard().getTranslateY());

                                //Checks if the stub card is close to a frontier, if it is, it will snap to it and the closest frontier will be saved
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
                                //Checks if the game state allows the card to be placed and if the card is playable
                                if((GUI.getStateProperty().get() != StateGUI.PLACE_CARD && GUI.getStateProperty().get() != StateGUI.CHOOSE_START_CARD) || card.getFace() == CardFace.FRONT && !card.isPlayable())
                                    return;

                                //Reopen the hand and remove the stub card
                                handPopUp.open();
                                codex.toggleFrontier(false);
                                codex.removeCard(stubCard);

                                //Checks if the card is close to a frontier, if it is, it will be placed there
                                if(closestFrontier == null)
                                    positioningCard.enable();
                                else{
                                    CardGUI newCard = new CardGUI(positioningCard);
                                    codex.addCard(newCard, closestFrontier.getGridPosition());

                                    positioningCard.removeThisByFlippablePlayable(
                                            cardToBeRemoved -> {
                                                this.removeCardFromHand(cardToBeRemoved);

                                                if(GUI.getStateProperty().get() == StateGUI.PLACE_CARD || GUI.getStateProperty().get() == StateGUI.CHOOSE_START_CARD){
                                                    try {
                                                        GUI.getControllerStatic().place(new LightPlacement(closestFrontier.getGridPosition(), positioningCard.getTarget(), positioningCard.getFace()));

                                                    } catch (Exception ex) {
                                                        throw new RuntimeException(ex);
                                                    }
                                                }
                                                else
                                                    throw new IllegalStateException("Invalid state");

                                                //Resets the positioning card and the closest frontier
                                                positioningCard = null;
                                                this.closestFrontier = null;
                                            }
                                            );
                                }
                            }
                    );
                }

                // if an empty spot is found, the card is added and the method returns
                return;
            }
        }

        // if no empty spot is found, an exception is thrown
        throw new IllegalStateException("Hand is full");
    }

    /**
     * Removes the given card from the hand.
     * @param card the card to remove from the hand.
     */
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

    /**
     * Updates the hand to reflect the current state of the light model. This method is called by the observer pattern.
     */
    synchronized public void update(){

        //Secret objective update
        if(secretObjective == null && GUI.getLightGame().getHand().getSecretObjective() != null){
            secretObjective = new FlippableCardGUI(GUI.getLightGame().getHand().getSecretObjective());
            setSizeBindings(secretObjective);
            secretObjective.addThisTo(hand, 0);
        }

        //Hand update
        int freeSpots = Arrays.stream(GUI.getLightGame().getHand().getCards()).filter(Objects::isNull).toArray().length;

        //Checks if the hand has changed
        if(freeSpots != Arrays.stream(handCards).filter(Objects::isNull).toArray().length) {
            for (int i = 0; i < handCards.length; i++) {

                //Checks if a card needs to be added to the hand
                if (handCards[i] == null && GUI.getLightGame().getHand().getCards()[i] != null) {
                    //adds the card to the hand
                    LightCard target = GUI.getLightGame().getHand().getCards()[i];
                    boolean playability = GUI.getLightGame().getHand().getCardPlayability().get(target);
                    FlippablePlayableCard newCard = new FlippablePlayableCard(target, playability);

                    newCard.addThisByFlippablePlayable(card -> addCardToHand(card, true));
                }
            }
        }

        for (int i = 0; i < handCards.length; i++) {
            //Checks if a card needs to be removed from the hand
            if (handCards[i] != null && GUI.getLightGame().getHand().getCards()[i] != null &&
                    handCards[i].getTarget().equals(GUI.getLightGame().getHand().getCards()[i])) {
                //removes the card from the hand
                LightCard flippableCardTarget = handCards[i].getTarget();
                boolean oldPlayability = handCards[i].isPlayable();
                boolean newPlayability = GUI.getLightGame().getHand().getCardPlayability().get(flippableCardTarget);
                if (oldPlayability != newPlayability)
                    handCards[i].setPlayable(newPlayability);
            }
        }
    }

    /**
     * Sets the codex of the hand.
     * @param codex the codex of the hand.
     */
    public void setCodex(CodexGUI codex){
        this.codex = codex;
    }
}
