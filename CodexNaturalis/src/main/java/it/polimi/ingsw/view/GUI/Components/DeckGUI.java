package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightTableRelated.LightDeck;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.view.GUI.Components.CardRelated.CardGUI;
import it.polimi.ingsw.view.GUI.Components.CardRelated.FlippableCardGUI;
import it.polimi.ingsw.view.GUI.Components.HandRelated.HandGUI;
import it.polimi.ingsw.view.GUI.Components.Utils.AnchoredPopUp;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import it.polimi.ingsw.view.GUI.StateGUI;
import javafx.application.Platform;
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
    private final VBox deckPopUpContent;
    private CardGUI resourceDeckBackCard;
    private CardGUI goldDeckBackCard;
    private AnchoredPopUp deckPopUp;
    private HandGUI hand;

    /**
     * Constructor for the DeckGUI. Create the boxes for the resource and gold decks and the common objectives
     */
    public DeckGUI() {

        //Creating Deck witch contains the boxes for the resource and gold decks and the common objectives
        HBox decksContainer = new HBox();
        decksContainer.getChildren().addAll(resourceDeck, goldDeck);
        decksContainer.setSpacing(GUIConfigs.deckGap);

        deckPopUpContent = new VBox();
        deckPopUpContent.getChildren().addAll(decksContainer, commonObjective);
        AnchorPane.setBottomAnchor(deckPopUpContent, 0.0);
        AnchorPane.setLeftAnchor(deckPopUpContent, 0.0);
        AnchorPane.setRightAnchor(deckPopUpContent, 0.0);
        AnchorPane.setTopAnchor(deckPopUpContent, 0.0);
        deckPopUpContent.setAlignment(Pos.CENTER); //set the decks in the center of the AnchorPopUp
        deckPopUpContent.spacingProperty().setValue(GUIConfigs.deckGap); //set the width between the decks


        resourceDeck.spacingProperty().setValue(GUIConfigs.deckCardGap);
        goldDeck.spacingProperty().setValue(GUIConfigs.deckCardGap);
        commonObjective.spacingProperty().setValue(GUIConfigs.deckGap);

        for(LightDeck deck : GUI.getLightGame().getDecks().values()){
            deck.attach(this);
        }

        GUI.getStateProperty().addListener((obs, oldState, newState) -> {
            if(newState == StateGUI.SELECT_OBJECTIVE){
                Platform.runLater(() -> fillDeckPopUp(deckPopUp));
            }
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
     * Adds the deckPopUp, while being transparent, to the parent
     * @param parent the parent to add the deckPopUp to
     */
    public void addDecksTo(AnchorPane parent){
        deckPopUp = new AnchoredPopUp(parent, 0.2f, 0.6f, Pos.CENTER_RIGHT, 0.1f);
        deckPopUp.getContent().setStyle(deckPopUp.getContent().getStyle() +  "-fx-background-color: transparent");
    }

    /**
     * Fills the deckPopUp with the decks
     * @param deckPopUp the deckPopUp to fill
     */
    private void fillDeckPopUp(AnchoredPopUp deckPopUp) {
        //Add the deck to the PopUp
        deckPopUp.getContent().getChildren().add(deckPopUpContent);
        //Set the size of the deck to be the same as the popUp
        deckPopUpContent.prefWidthProperty().bind(deckPopUp.getContent().prefWidthProperty());
        deckPopUpContent.prefHeightProperty().bind(deckPopUp.getContent().prefHeightProperty());
        deckPopUp.getContent().setStyle(deckPopUp.getContent().getStyle() +  "; -fx-background-color: black");

    }

    /**
     * Update the decks buffer with the new card after a draw from the buffer
     * Set the action to be performed when the card is holdON
     * @param card the card to be shown in the buffer
     * @param deckType the type of buffer to which was draw from
     * @param index the index of the buffer
     */
    private void updateBuffer(FlippableCardGUI card, DrawableCard deckType, int index){
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

    /**
     * Update the decks with the new cardOnTop after a draw from the deck
     * Set the action to be performed when the card is holdON
     * @param card the card to be shown on top of the deck
     * @param deckType the type of deck to draw from
     */
    private void updateTopDeck(CardGUI card, DrawableCard deckType){
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
        System.out.println("TopCardOnDeck: " + resourceDeckBackCard.getTarget());
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

    /**
     * Updates the deckGUI with the new information from the lightGame
     */
    @Override
    public void update() {
        if(!areCommonObjectivesUpdated()){
            //System.out.println("Updating common objectives");
            for(int i=0; i<2; i++){
                commonObjectiveCards[i] = new FlippableCardGUI(GUI.getLightGame().getPublicObjective()[i]);
                setSizeBindings(commonObjectiveCards[i]);
                commonObjective.getChildren().add(commonObjectiveCards[i].getImageView());
            }
        }
        //Diff about a draw from Deck
        if(!isResourceDeckUpdated()){
            System.out.println("BackResource in LightGame: " + GUI.getLightGame().getDecks().get(DrawableCard.RESOURCECARD).getDeckBack().idBack());
            //System.out.println("Drawing from resource deck");
            updateTopDeck(new CardGUI(new LightBack(GUI.getLightGame().getDecks().get(DrawableCard.RESOURCECARD).getDeckBack().idBack())), DrawableCard.RESOURCECARD);
        }
        if(!isGoldDeckUpdated()) {
            System.out.println("BackGold in LightGame: " + GUI.getLightGame().getDecks().get(DrawableCard.GOLDCARD).getDeckBack().idBack());
            //System.out.println("Drawing from gold deck");
            updateTopDeck(new CardGUI(new LightBack(GUI.getLightGame().getDecks().get(DrawableCard.GOLDCARD).getDeckBack().idBack())), DrawableCard.GOLDCARD);
        }
        //Diff about a draw from buffer
        if(!resourceDeckBufferIsUpdated()){
            //System.out.println("Drawing from resource buffer");
            for(int i=0;i<2;i++){
                if(resourceBuffer[i]==null || (resourceBuffer[i].getTarget() != GUI.getLightGame().getDecks().get(DrawableCard.RESOURCECARD).getCardBuffer()[i])){
                    updateBuffer(new FlippableCardGUI(GUI.getLightGame().getDecks().get(DrawableCard.RESOURCECARD).getCardBuffer()[i]), DrawableCard.RESOURCECARD, i);
                    break;

                }
            }
        }
        if(!goldBufferIsUpdated()){
            //System.out.println("Drawing from gold buffer");
            for(int i=0;i<2;i++){
                if(goldBuffer[i]==null || (goldBuffer[i].getTarget() != GUI.getLightGame().getDecks().get(DrawableCard.GOLDCARD).getCardBuffer()[i])){
                    updateBuffer(new FlippableCardGUI(GUI.getLightGame().getDecks().get(DrawableCard.GOLDCARD).getCardBuffer()[i]), DrawableCard.GOLDCARD, i);
                    break;
                }
            }
        }
    }

    /**
     * Checks if the gold buffer is updated to match the buffer in the lightGame
     * @return true if the gold buffer is updated, false otherwise
     */
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

    /**
     * Checks if the resource buffer is updated to match the buffer in the lightGame
     * @return true if the resource buffer is updated, false otherwise
     */
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

    /**
     * Checks if the resource deck is updated to match the deck in the lightGame
     * @return true if the resource deck is updated, false otherwise
     */
    private boolean isResourceDeckUpdated(){
        return !(GUI.getLightGame().getDecks().get(DrawableCard.RESOURCECARD).getDeckBack()!=null && (resourceDeckBackCard == null || resourceDeckBackCard.getTarget().idBack() != GUI.getLightGame().getDecks().get(DrawableCard.RESOURCECARD).getDeckBack().idBack()));
    }

    /**
     * Checks if the gold deck is updated to match the deck in the lightGame
     * @return true if the gold deck is updated, false otherwise
     */
    private boolean isGoldDeckUpdated(){
        return !(GUI.getLightGame().getDecks().get(DrawableCard.RESOURCECARD).getDeckBack()!=null && (goldDeckBackCard == null || goldDeckBackCard.getTarget().idBack() != GUI.getLightGame().getDecks().get(DrawableCard.GOLDCARD).getDeckBack().idBack()));
    }

    /**
     * Sets the size bindings for the card
     * @param card the card to set the size bindings for
     */
    private void setSizeBindings(CardGUI card){
        ImageView imageView = card.getImageView();
        double cardHeight = (deckPopUpContent.prefHeightProperty().getValue()-(GUIConfigs.deckCardGap * 2 + GUIConfigs.deckGap))/(resourceBuffer.length + 2);
        imageView.setFitWidth(cardHeight/(imageView.getImage().getHeight()/imageView.getImage().getWidth()));
        imageView.fitWidthProperty().bind(deckPopUpContent.prefHeightProperty().subtract(GUIConfigs.deckCardGap *2 + GUIConfigs.deckGap).divide(resourceBuffer.length + 2).multiply(imageView.getImage().getWidth()/imageView.getImage().getHeight()));
    }

    /**
     * Checks if the common objectives are updated to match the objectives in the lightGame
     * @return true if the common objectives are updated, false otherwise
     */
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

    /**
     * @return the hand
     */
    public HandGUI getHand() {
        return hand;
    }

    /**
     * @param hand the hand to set
     */
    public void setHand(HandGUI hand) {
        this.hand = hand;
    }

}
