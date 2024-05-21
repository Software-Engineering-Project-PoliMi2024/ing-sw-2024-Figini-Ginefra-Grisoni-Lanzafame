package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightTableRelated.LightDeck;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.view.GUI.Components.CardRelated.CardGUI;
import it.polimi.ingsw.view.GUI.Components.CardRelated.FlippableCardGUI;
import it.polimi.ingsw.view.GUI.Components.HandRelated.HandGUI;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import it.polimi.ingsw.view.GUI.StateGUI;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
public class DeckGUI implements Observer {
    private final VBox resourceDeck = new VBox();
    private final VBox goldDeck = new VBox();
    private final HBox commonObjective = new HBox();
    private final FlippableCardGUI[] commonObjectiveCards = new FlippableCardGUI[2];
    private final FlippableCardGUI[] resourceBuffer = new FlippableCardGUI[2];
    private final FlippableCardGUI[] goldBuffer = new FlippableCardGUI[2];
    private final VBox deckPopUpContenent;
    private CardGUI resourceDeckBackCard;
    private CardGUI goldDeckBackCard;
    private AnchoredPopUp deckPopUp;
    private HandGUI hand;

    public DeckGUI() {

        //Creating Deck witch contains the boxes for the resource and gold decks and the common objectives
        HBox decksContainer = new HBox();
        decksContainer.getChildren().addAll(resourceDeck, goldDeck);
        decksContainer.setSpacing(GUIConfigs.deckGap);

        deckPopUpContenent = new VBox();
        deckPopUpContenent.getChildren().addAll(decksContainer, commonObjective);
        AnchorPane.setBottomAnchor(deckPopUpContenent, 0.0);
        AnchorPane.setLeftAnchor(deckPopUpContenent, 0.0);
        AnchorPane.setRightAnchor(deckPopUpContenent, 0.0);
        AnchorPane.setTopAnchor(deckPopUpContenent, 0.0);
        deckPopUpContenent.setAlignment(Pos.CENTER); //set the decks in the center of the AnchorPopUp
        deckPopUpContenent.spacingProperty().setValue(GUIConfigs.deckGap); //set the width between the decks


        resourceDeck.spacingProperty().setValue(GUIConfigs.deckCardGap);
        goldDeck.spacingProperty().setValue(GUIConfigs.deckCardGap);
        commonObjective.spacingProperty().setValue(GUIConfigs.deckGap);

        for(LightDeck deck : GUI.getLightGame().getDecks().values()){
            deck.attach(this);
        }

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
     * @param parent the AnchorPane where the deck will be added
     */
    public void addDecksTo(AnchorPane parent) {
        //Add the deck to the PopUp
        deckPopUp = new AnchoredPopUp(parent, 0.2f, 0.6f, Pos.CENTER_RIGHT, 0.1f);
        deckPopUp.getContent().getChildren().add(deckPopUpContenent);
        //Set the size of the deck to be the same as the popUp
        deckPopUpContenent.prefWidthProperty().bind(deckPopUp.getContent().prefWidthProperty());
        deckPopUpContenent.prefHeightProperty().bind(deckPopUp.getContent().prefHeightProperty());
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
            if((GUI.getStateProperty().get() != StateGUI.DRAW_CARD)){
                return;
            }else{
                try {
                    GUI.getControllerStatic().draw(deckType, index);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
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
                if(bufferCard!=null){
                    resourceDeck.getChildren().add(bufferCard.getImageView());
                }
            }
        }else if(deckType == DrawableCard.GOLDCARD){
            goldDeckBackCard = card;
            goldDeck.getChildren().clear();
            goldDeck.getChildren().add(0, card.getImageView());
            for(FlippableCardGUI bufferCard : goldBuffer){
                if(bufferCard!=null){
                    goldDeck.getChildren().add(bufferCard.getImageView());
                }
            }
        }

        card.setOnHold(e-> {
            if((GUI.getStateProperty().get() != StateGUI.DRAW_CARD)) {
                return;
            }else {
                try {
                    GUI.getControllerStatic().draw(deckType, 2);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
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
            //System.out.println("Drawing from resource deck");
            drawFromDeck(new CardGUI(new LightBack(GUI.getLightGame().getDecks().get(DrawableCard.RESOURCECARD).getDeckBack().idBack())), DrawableCard.RESOURCECARD);
        }
        if(!isGoldDeckUpdated()) {
            //System.out.println("Drawing from gold deck");
            drawFromDeck(new CardGUI(new LightBack(GUI.getLightGame().getDecks().get(DrawableCard.GOLDCARD).getDeckBack().idBack())), DrawableCard.GOLDCARD);
        }
        if(!resourceDeckBufferIsUpdated()){ //draw from buffer
            //System.out.println("Drawing from resource buffer");
            for(int i=0;i<2;i++){
                if(resourceBuffer[i]==null || (resourceBuffer[i].getTarget() != GUI.getLightGame().getDecks().get(DrawableCard.RESOURCECARD).getCardBuffer()[i])){
                    drawFromBuffer(new FlippableCardGUI(GUI.getLightGame().getDecks().get(DrawableCard.RESOURCECARD).getCardBuffer()[i]), DrawableCard.RESOURCECARD, i);
                    break;
                }
            }
        }
        if(!goldBufferIsUpdated()){
            //System.out.println("Drawing from gold buffer");
            for(int i=0;i<2;i++){
                if(goldBuffer[i]==null || (goldBuffer[i].getTarget() != GUI.getLightGame().getDecks().get(DrawableCard.GOLDCARD).getCardBuffer()[i])){
                    drawFromBuffer(new FlippableCardGUI(GUI.getLightGame().getDecks().get(DrawableCard.GOLDCARD).getCardBuffer()[i]), DrawableCard.GOLDCARD, i);
                    break;
                }
            }
        }
    }

    private boolean goldBufferIsUpdated(){
        boolean isUpdated = true;
        for(int i=0;i<2;i++){
            if(GUI.getLightGame().getDecks().get(DrawableCard.GOLDCARD).getCardBuffer()[i]!=null && (goldBuffer[i]==null || goldBuffer[i].getTarget() != GUI.getLightGame().getDecks().get(DrawableCard.GOLDCARD).getCardBuffer()[i])){
                isUpdated = false;
                break;
            }
        }
        return isUpdated;
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
        double cardHeight = (deckPopUpContenent.prefHeightProperty().getValue()-(GUIConfigs.deckCardGap * 2 + GUIConfigs.deckGap))/(resourceBuffer.length + 2);
        imageView.setFitWidth(cardHeight/(imageView.getImage().getHeight()/imageView.getImage().getWidth()));
        imageView.fitWidthProperty().bind(deckPopUpContenent.prefHeightProperty().subtract(GUIConfigs.deckCardGap *2 + GUIConfigs.deckGap).divide(resourceBuffer.length + 2).multiply(imageView.getImage().getWidth()/imageView.getImage().getHeight()));
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
