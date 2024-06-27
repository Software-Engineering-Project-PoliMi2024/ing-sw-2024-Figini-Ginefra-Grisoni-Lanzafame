package it.polimi.ingsw.view.GUI.Components.DeckRelated;

import it.polimi.ingsw.utils.designPatterns.Observer;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.view.GUI.Components.CardRelated.FlippableCardGUI;
import it.polimi.ingsw.view.GUI.Components.Utils.AnchoredPopUp;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import it.polimi.ingsw.view.GUI.StateGUI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class represents the GUI component responsible for housing the decks and the common objectives.
 */
public class DeckAreaGUI implements Observer {
    /** The pop up that contains the decks. */
    private AnchoredPopUp popUp;
    /** The container of the decks */
    private final HBox decksContainer = new HBox();
    /** The content of the component */
    private final VBox content = new VBox();
    /** The map that associates each drawable card to its deck */
    private final Map<DrawableCard, DeckGUI> decksMap = new LinkedHashMap<>();
    /** The common objectives */
    private final FlippableCardGUI[] commonObjectives = new FlippableCardGUI[2];

    /**
     * Creates a new DeckAreaGUI.
     */
    public DeckAreaGUI(){
        Arrays.stream(DrawableCard.values()).forEach(card -> {
            DeckGUI deck = new DeckGUI(card, decksContainer);
            decksMap.put(card, deck);
        });

        HBox commonObjectivesContainer = new HBox();
        for (int i = 0; i < 2; i++) {
            commonObjectives[i] = new FlippableCardGUI(null);
            commonObjectivesContainer.getChildren().add(commonObjectives[i].getImageView());
        }

        GUI.getStateProperty().addListener((observable, oldValue, newValue) -> {
            if(popUp != null) {
                if (newValue == StateGUI.DRAW_CARD) {
                    popUp.open();
                    popUp.setLocked(true);
                } else {
                    popUp.close();
                    popUp.setLocked(false);
                }
            }
        });

        decksContainer.setAlignment(Pos.CENTER);
        decksContainer.setSpacing(GUIConfigs.hDeckGap);

        commonObjectivesContainer.setAlignment(Pos.CENTER);
        commonObjectivesContainer.setSpacing(GUIConfigs.hDeckGap);

        content.getChildren().add(decksContainer);
        content.getChildren().add(commonObjectivesContainer);


        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);
        AnchorPane.setTopAnchor(content, 0.0);

        content.setSpacing(GUIConfigs.vCardGapInDeck);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(15));

        GUI.getLightGame().getDecks().forEach((drawableCard, lightDeck) -> lightDeck.attach(this));
    }

    /**
     * Adds this to the given parent.
     * @param parent the parent to add this to.
     */
    public void addThisTo(AnchorPane parent){
        popUp = new AnchoredPopUp(parent, 0.3f, 0.5f, Pos.CENTER_RIGHT, 0.25f);

        AnchorPane.setBottomAnchor(decksContainer, 0.0);
        AnchorPane.setLeftAnchor(decksContainer, 0.0);
        AnchorPane.setRightAnchor(decksContainer, 0.0);
        AnchorPane.setTopAnchor(decksContainer, 0.0);

        decksContainer.maxHeightProperty().bind(popUp.getContent().maxHeightProperty());

        Arrays.stream(commonObjectives).forEach(card ->
                card.getImageView().fitWidthProperty().bind(decksContainer.maxHeightProperty().subtract(2 * GUIConfigs.hDeckGap).divide(3).multiply(GUIConfigs.cardWidth / GUIConfigs.cardHeight)));

        popUp.getContent().getChildren().add(content);
    }

    /**
     * Updates the component to reflect the current state of the light game. This method is called by the observer pattern.
     */
    public void update(){
        LightCard[] publicObjectives = GUI.getLightGame().getPublicObjective();
        for(int i = 0; i < publicObjectives.length; i++){
            commonObjectives[i].setTarget(publicObjectives[i]);
        }
    }

}
