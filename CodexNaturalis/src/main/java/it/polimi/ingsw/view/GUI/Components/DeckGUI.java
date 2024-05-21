package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightDeck;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.Hand;
import it.polimi.ingsw.view.GUI.Components.CardRelated.CardGUI;
import it.polimi.ingsw.view.GUI.Components.CardRelated.FlippableCardGUI;
import it.polimi.ingsw.view.GUI.Components.CardRelated.FlippablePlayableCard;
import it.polimi.ingsw.view.GUI.Components.HandRelated.HandGUI;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import it.polimi.ingsw.view.GUI.StateGUI;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

public class DeckGUI implements Observer {
    private final VBox resourceDeck = new VBox();
    private final VBox goldDeck = new VBox();
    private final HBox commonObjective = new HBox();
    private final FlippableCardGUI[] commonObjectiveCards = new FlippableCardGUI[2];
    private final FlippableCardGUI[] resourceBuffer = new FlippableCardGUI[2];
    private final FlippableCardGUI[] goldBuffer = new FlippableCardGUI[2];
    private final VBox deck;
    private CardGUI resourceDeckBackCard;
    private CardGUI goldDeckBackCard;
    private AnchoredPopUp deckPopUp;
    private HandGUI hand;

    public DeckGUI() {
        for(LightDeck deck : GUI.getLightGame().getDecks().values()){
            deck.attach(this);
        }

        //Creating Deck witch contains the boxes for the resource and gold decks and the common objectives
        HBox decksContainer = new HBox();
        decksContainer.getChildren().addAll(resourceDeck, goldDeck);
        decksContainer.setSpacing(GUIConfigs.deckGap);

        deck = new VBox();
        deck.getChildren().addAll(decksContainer, commonObjective);
        AnchorPane.setBottomAnchor(deck, 0.0);
        AnchorPane.setLeftAnchor(deck, 0.0);
        AnchorPane.setRightAnchor(deck, 0.0);
        AnchorPane.setTopAnchor(deck, 0.0);
        deck.setAlignment(Pos.CENTER); //set the decks in the center of the AnchorPopUp
        deck.spacingProperty().setValue(GUIConfigs.deckGap); //set the width between the decks


        resourceDeck.spacingProperty().setValue(GUIConfigs.deckCardGap);
        goldDeck.spacingProperty().setValue(GUIConfigs.deckCardGap);
        commonObjective.spacingProperty().setValue(GUIConfigs.deckGap);

        GUI.getStateProperty().addListener((obs, oldState, newState) -> {
            if(newState == StateGUI.DRAW_CARD){
                deckPopUp.open();
                deckPopUp.setLocked(true);
            }
            else{
                deckPopUp.close();
                deckPopUp.setLocked(false);
            }
        });
    }

    /**
     * Adds the deck to the AnchorPane parent
     * @param parent
     */
    public void addDecksTo(AnchorPane parent) {
        //Add the deck to the PopUp
        deckPopUp = new AnchoredPopUp(parent, 0.2f, 0.6f, Pos.CENTER_RIGHT, 0.1f);
        deckPopUp.getContent().getChildren().add(deck);
        //Set the size of the deck to be the same as the popUp
        deck.prefWidthProperty().bind(deckPopUp.getContent().prefWidthProperty());
        deck.prefHeightProperty().bind(deckPopUp.getContent().prefHeightProperty());
        //deckPopUp.getContent().setStyle(deckPopUp.getContent().getStyle() +  "-fx-background-color: transparent");

    }

    private void drawFromBuffer(FlippableCardGUI card, DrawableCard deckType, int index){
        setSizeBindings(card);
        if(deckType == DrawableCard.RESOURCECARD) {
           resourceDeck.getChildren().clear();
           if(resourceDeckBackCard!=null){
               resourceDeck.getChildren().add(0, resourceDeckBackCard.getImageView());
           }
            resourceBuffer[index] = card;
            for(FlippableCardGUI bufferCard : resourceBuffer){
                if(bufferCard!=null){
                    resourceDeck.getChildren().add(bufferCard.getImageView());
                }
            }
        }else if(deckType == DrawableCard.GOLDCARD){
            goldDeck.getChildren().clear();
            if(goldDeckBackCard!=null){
                goldDeck.getChildren().add(0, goldDeckBackCard.getImageView());
            }
            goldBuffer[index] = card;
            for(FlippableCardGUI bufferCard : goldBuffer){
                if(bufferCard!=null){
                    goldDeck.getChildren().add(bufferCard.getImageView());
                }
            }
        }

        card.setOnHold(e -> {
            if((GUI.getStateProperty().get() != StateGUI.DRAW_CARD)) {
                return;
            }else {
                //todo review. Atm I'm not checking if the card is playable or not because the card is not in the hand yet
                //boolean playability = GUI.getLightGame().getHand().getCardPlayability().get(card.getTarget());
                hand.addCardToHand(new FlippablePlayableCard(card.getTarget(), true), true);
                if (deckType == DrawableCard.RESOURCECARD) {
                    resourceBuffer[index] = null;
                    resourceDeck.getChildren().remove(index+1);
                } else if (deckType == DrawableCard.GOLDCARD){
                    goldBuffer[index] = null;
                    goldDeck.getChildren().remove(index+1);
                }
            }
            try {
                GUI.getControllerStatic().draw(deckType, index);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void drawFromDeck(CardGUI card, DrawableCard deckType){
        setSizeBindings(card);
        if(deckType == DrawableCard.RESOURCECARD) {
            resourceDeckBackCard = card;
            resourceDeck.getChildren().clear();
            resourceDeck.getChildren().add(0, card.getImageView());
            for(FlippableCardGUI bufferCard : resourceBuffer){
                resourceDeck.getChildren().add(bufferCard.getImageView());
            }
        }else if(deckType == DrawableCard.GOLDCARD){
            goldDeckBackCard = card;
            goldDeck.getChildren().clear();
            goldDeck.getChildren().add(0, card.getImageView());
            for(FlippableCardGUI bufferCard : goldBuffer){
                goldDeck.getChildren().add(bufferCard.getImageView());
            }
        }

        card.setOnHold(e-> {
            try {
                GUI.getControllerStatic().draw(deckType, 2);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void update() {
        if(!areCommonObjectivesUpdated()){
            System.out.println("Updating common objectives");
            for(int i=0; i<2; i++){
                commonObjectiveCards[i] = new FlippableCardGUI(GUI.getLightGame().getPublicObjective()[i]);
                setSizeBindings(commonObjectiveCards[i]);
                commonObjective.getChildren().add(commonObjectiveCards[i].getImageView());
            }
        }
        //Drawn from Deck
        if(!isResourceDeckUpdated()){
            System.out.println("Drawing from resource deck");
            drawFromDeck(new CardGUI(new LightBack(GUI.getLightGame().getDecks().get(DrawableCard.RESOURCECARD).getDeckBack().idBack())), DrawableCard.RESOURCECARD);
        }else if(!isGoldDeckUpdated()) {
            System.out.println("Drawing from gold deck");
            drawFromDeck(new CardGUI(new LightBack(GUI.getLightGame().getDecks().get(DrawableCard.GOLDCARD).getDeckBack().idBack())), DrawableCard.GOLDCARD);
        }else if(!resourceDeckBufferIsUpdated()){ //draw from buffer
            System.out.println("Drawing from resource buffer");
            for(int i=0;i<2;i++){
                if(resourceBuffer[i]==null || (resourceBuffer[i].getTarget() != GUI.getLightGame().getDecks().get(DrawableCard.RESOURCECARD).getCardBuffer()[i])){
                    drawFromBuffer(new FlippableCardGUI(GUI.getLightGame().getDecks().get(DrawableCard.RESOURCECARD).getCardBuffer()[i]), DrawableCard.RESOURCECARD, i);
                    break;
                }
            }
        }else{ //!goldBufferIsUpdated
            System.out.println("Drawing from gold buffer");
            for(int i=0;i<2;i++){
                if(goldBuffer[i]==null || (goldBuffer[i].getTarget() != GUI.getLightGame().getDecks().get(DrawableCard.GOLDCARD).getCardBuffer()[i])){
                    drawFromBuffer(new FlippableCardGUI(GUI.getLightGame().getDecks().get(DrawableCard.GOLDCARD).getCardBuffer()[i]), DrawableCard.GOLDCARD, i);
                    break;
                }
            }
        }
    }

    private boolean resourceDeckBufferIsUpdated(){
        boolean isUpdated = true;
        for(int i=0;i<2;i++){
            if(GUI.getLightGame().getDecks().get(DrawableCard.RESOURCECARD).getCardBuffer()[i]!=null && (resourceBuffer[i]==null || resourceBuffer[i].getTarget() != GUI.getLightGame().getDecks().get(DrawableCard.RESOURCECARD).getCardBuffer()[i])){
                isUpdated = false;
                break;
            }
        }
        return isUpdated;
    }

    private boolean isResourceDeckUpdated(){
        return !(GUI.getLightGame().getDecks().get(DrawableCard.RESOURCECARD).getDeckBack()!=null && (resourceDeckBackCard == null || resourceDeckBackCard.getTarget().idBack() != GUI.getLightGame().getDecks().get(DrawableCard.RESOURCECARD).getDeckBack().idBack()));
    }

    private boolean isGoldDeckUpdated(){
        return !(GUI.getLightGame().getDecks().get(DrawableCard.RESOURCECARD).getDeckBack()!=null && (goldDeckBackCard == null || goldDeckBackCard.getTarget().idBack() != GUI.getLightGame().getDecks().get(DrawableCard.GOLDCARD).getDeckBack().idBack()));
    }
    private void setSizeBindings(CardGUI card){
        ImageView imageView = card.getImageView();
        double cardHeight = (deck.prefHeightProperty().getValue()-(GUIConfigs.deckCardGap * 2 + GUIConfigs.deckGap))/(resourceBuffer.length + 2);
        imageView.setFitWidth(cardHeight/(imageView.getImage().getHeight()/imageView.getImage().getWidth()));
        imageView.fitWidthProperty().bind(deck.prefHeightProperty().subtract(GUIConfigs.deckCardGap *2 + GUIConfigs.deckGap).divide(resourceBuffer.length + 2).multiply(imageView.getImage().getWidth()/imageView.getImage().getHeight()));
    }

    private boolean areCommonObjectivesUpdated(){
        boolean isUpdated = true;
        for(int i=0;i<2;i++){
            if(GUI.getLightGame().getPublicObjective()[i]!=null && (commonObjectiveCards[i]==null || (commonObjectiveCards[i].getTarget() != GUI.getLightGame().getPublicObjective()[i]))){
                isUpdated = false;
                break;
            }
        }
        return isUpdated;
    }

    public HandGUI getHand() {
        return hand;
    }

    public void setHand(HandGUI hand) {
        this.hand = hand;
    }

}
