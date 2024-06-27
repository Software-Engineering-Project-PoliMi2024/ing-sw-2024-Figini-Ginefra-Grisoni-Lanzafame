package it.polimi.ingsw.view.GUI.Components.HandRelated;

import it.polimi.ingsw.utils.designPatterns.Observer;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.view.GUI.Components.Utils.AnchoredPopUp;
import it.polimi.ingsw.view.GUI.Components.CardRelated.CardGUI;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.Arrays;
import java.util.Objects;

/**
 * This class represents the GUI component to show the hand of a player that is not the player that is using the GUI.
 */
public class HandOthersGUI implements Observer {
    /** The hand of the player */
    private final HBox hand = new HBox();
    /** The cards in the hand */
    private final CardGUI[] handCards = new CardGUI[3];
    /** The pop up that contains the hand */
    private AnchoredPopUp handPopUp;
    /** The player that this hand refers to */
    private final String targetPlayer;
    /** The parent of the component */
    public HandOthersGUI(String targetPlayer) {
        this.targetPlayer = targetPlayer;
        hand.setSpacing(10);
        hand.setAlignment(Pos.CENTER);

        AnchorPane.setBottomAnchor(hand, 0.0);
        AnchorPane.setLeftAnchor(hand, 0.0);
        AnchorPane.setRightAnchor(hand, 0.0);
        AnchorPane.setTopAnchor(hand, 0.0);
    }
    /** Attaches this to the hand of the player */
    public void attach(){
        GUI.getLightGame().getHandOthers().get(this.targetPlayer).attach(this);
    }

    /** Adds this to the given parent */
    public void addThisTo(AnchorPane parent){
        handPopUp = new AnchoredPopUp(parent, 0.8f, 0.2f, Pos.BOTTOM_CENTER, 0.25f);
        handPopUp.getContent().setStyle(handPopUp.getContent().getStyle() +  "-fx-background-color: transparent");
        handPopUp.getContent().getChildren().add(hand);

        hand.prefWidthProperty().bind(handPopUp.getContent().prefWidthProperty());
        hand.prefHeightProperty().bind(handPopUp.getContent().prefHeightProperty());
    }

    /** Sets the size bindings of the given card */
    private void setSizeBindings(CardGUI card){
        card.getImageView().setFitWidth(handPopUp.getContent().getMaxWidth()/ (handCards.length + 1) - hand.spacingProperty().getValue());
        card.getImageView().fitWidthProperty().bind(handPopUp.getContent().maxWidthProperty().divide(handCards.length + 1).subtract(hand.spacingProperty().getValue()));
    }

    /** Adds the given card to the hand */
    private void addCard(CardGUI card){
        for(int i = 0; i < handCards.length; i++){
            if(handCards[i] == null){
                handCards[i] = card;
                hand.getChildren().add(card.getImageView());
                setSizeBindings(card);
                return;
            }
        }
        throw new IllegalStateException("Hand is full");
    }

    /** Removes the given card from the hand */
    private void removeCard(CardGUI card){
        for(int i = 0; i < handCards.length; i++){
            if(handCards[i] == card){
                handCards[i] = null;
                hand.getChildren().remove(card.getImageView());
                return;
            }
        }
        throw new IllegalStateException("Card not found");
    }

    /** Updates the hand to reflect the current state of the light game. This method is called by the observer pattern */
    @Override
    public void update() {
        LightBack[] cards = GUI.getLightGame().getHandOthers().get(this.targetPlayer).getCards();

        int freeSpots = Arrays.stream(cards).filter(Objects::nonNull).mapToInt(c -> 1).sum();

        if(freeSpots == Arrays.stream(this.handCards).filter(Objects::nonNull).mapToInt(c -> 1).sum()){
            return;
        }

        for(int i = 0; i < handCards.length; i++){
            if(handCards[i] != null && cards[i] == null){
                removeCard(handCards[i]);
            }

            if(handCards[i] == null && cards[i] != null){
                addCard(new CardGUI(cards[i]));
            }
        }
    }
}
