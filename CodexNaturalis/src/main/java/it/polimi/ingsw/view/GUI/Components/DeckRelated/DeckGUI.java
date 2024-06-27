package it.polimi.ingsw.view.GUI.Components.DeckRelated;

import it.polimi.ingsw.utils.designPatterns.Observer;
import it.polimi.ingsw.lightModel.lightTableRelated.LightDeck;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.view.GUI.Components.CardRelated.CardGUI;
import it.polimi.ingsw.view.GUI.Components.CardRelated.FlippableCardGUI;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import it.polimi.ingsw.view.GUI.StateGUI;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;

/**
 * This class represents the GUI component responsible for housing a deck.
 */
public class DeckGUI implements Observer {
    /** The content of the component */
    private final VBox content = new VBox();
    /** The drawable card associated of the deck */
    private final CardGUI drawableCard;
    /** The buffer of the deck */
    private final FlippableCardGUI[] buffer = new FlippableCardGUI[2];
    /** The target of the deck */
    private final DrawableCard cardTarget;
    /** The parent of the component */
    private final HBox parent;

    /**
     * Creates a new DeckGUI.
     * @param cardTarget the target of the deck.
     * @param parent the parent of the component.
     */
    public DeckGUI(DrawableCard cardTarget, HBox parent){
        this.parent = parent;
        this.cardTarget = cardTarget;

        drawableCard = new CardGUI(null, CardFace.BACK);
        buffer[0] = new FlippableCardGUI(null);
        buffer[1] = new FlippableCardGUI(null);

        content.setSpacing(GUIConfigs.vCardGapInDeck);

        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);
        AnchorPane.setTopAnchor(content, 0.0);

        drawableCard.addThisTo(content);
        Arrays.stream(buffer).forEach(card -> card.addThisTo(content));

        setSizeBindings(drawableCard);
        Arrays.stream(buffer).forEach(this::setSizeBindings);

        GUI.getLightGame().getDecks().get(cardTarget).attach(this);

        drawableCard.setOnHold(e -> {
            try {
                if(GUI.getStateProperty().get() == StateGUI.DRAW_CARD)
                    GUI.getControllerStatic().draw(cardTarget, 2);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        Arrays.stream(buffer).forEach(card -> card.setOnHold(e -> {
            try {
                if(GUI.getStateProperty().get() == StateGUI.DRAW_CARD)
                    GUI.getControllerStatic().draw(cardTarget,  Arrays.asList(buffer).indexOf(card));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }));

        parent.getChildren().add(content);
    }

    /**
     * Sets the size bindings of the given card.
     * @param card the card to set the size bindings of.
     */
    private void setSizeBindings(CardGUI card){
        card.getImageView().fitWidthProperty().bind(parent.maxHeightProperty().subtract(buffer.length * GUIConfigs.vCardGapInDeck).divide(buffer.length + 1).multiply(GUIConfigs.cardWidth / GUIConfigs.cardHeight));
    }

    /**
     * Updates the component to reflect the current state of the light model. This method is called by the observer pattern.
     */
    public void update(){
        LightDeck deck = GUI.getLightGame().getDecks().get(cardTarget);

        if(drawableCard.getTarget() == null ||drawableCard.getTarget().idBack() != deck.getDeckBack().idBack()) {
            drawableCard.runBetweenRemovingAndAddingAnimation(() -> drawableCard.setTarget(deck.getDeckBack()));
        }
        for(int i = 0; i < buffer.length; i++){
            if(buffer[i].getTarget() == null || !buffer[i].getTarget().equals(deck.getCardBuffer()[i])) {
                final int finalI = i;
                buffer[i].runBetweenRemovingAndAddingAnimation(() -> buffer[finalI].setTarget(deck.getCardBuffer()[finalI]));
                buffer[i].setFace(CardFace.FRONT);
            }

        }
    }

    /**
     * Returns the content of the component.
     * @return the content of the component.
     */
    public VBox getContent(){
        return content;
    }
}
