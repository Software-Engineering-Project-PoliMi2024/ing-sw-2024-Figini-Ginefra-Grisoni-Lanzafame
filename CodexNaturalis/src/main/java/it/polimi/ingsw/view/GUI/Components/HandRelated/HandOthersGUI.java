package it.polimi.ingsw.view.GUI.Components.HandRelated;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.GUI.Components.AnchoredPopUp;
import it.polimi.ingsw.view.GUI.Components.CardRelated.CardGUI;
import it.polimi.ingsw.view.GUI.Components.CardRelated.FlippableCardGUI;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import it.polimi.ingsw.view.GUI.StateGUI;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.Arrays;
import java.util.Objects;

public class HandOthersGUI implements Observer {
    private final HBox hand = new HBox();
    private final CardGUI[] handCards = new CardGUI[3];
    private AnchoredPopUp handPopUp;

    private final String targetPlayer;

    public HandOthersGUI(String targetPlayer) {
        this.targetPlayer = targetPlayer;
        hand.setSpacing(10);
        hand.setAlignment(Pos.CENTER);

        AnchorPane.setBottomAnchor(hand, 0.0);
        AnchorPane.setLeftAnchor(hand, 0.0);
        AnchorPane.setRightAnchor(hand, 0.0);
        AnchorPane.setTopAnchor(hand, 0.0);
    }

    public void attach(){
        GUI.getLightGame().getHandOthers().get(this.targetPlayer).attach(this);
    }

    public void addThisTo(AnchorPane parent){
        handPopUp = new AnchoredPopUp(parent, 0.8f, 0.2f, Pos.BOTTOM_CENTER, 0.25f);
        handPopUp.getContent().setStyle(handPopUp.getContent().getStyle() +  "-fx-background-color: transparent");
        handPopUp.getContent().getChildren().add(hand);

        hand.prefWidthProperty().bind(handPopUp.getContent().prefWidthProperty());
        hand.prefHeightProperty().bind(handPopUp.getContent().prefHeightProperty());
    }

    private void setSizeBindings(CardGUI card){
        card.getImageView().setFitWidth(hand.prefWidthProperty().getValue()/ (handCards.length + 1) - hand.spacingProperty().getValue());
        card.getImageView().fitWidthProperty().bind(hand.prefWidthProperty().divide(handCards.length + 1).subtract(hand.spacingProperty().getValue()));
    }

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
