package it.polimi.ingsw.view.GUI.Components.DeckRelated;

import it.polimi.ingsw.utils.Observer;
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

public class DeckAreaGUI implements Observer {
    private AnchoredPopUp popUp;
    private final HBox decksContainer = new HBox();
    private final HBox commonObjectivesContainer = new HBox();

    private final VBox content = new VBox();

    private final Map<DrawableCard, DeckGUI> decksMap = new LinkedHashMap<>();
    private final FlippableCardGUI[] commonObjectives = new FlippableCardGUI[2];

    public DeckAreaGUI(){
        Arrays.stream(DrawableCard.values()).forEach(card -> {
            DeckGUI deck = new DeckGUI(card, decksContainer);
            decksMap.put(card, deck);
        });

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

    public void update(){
        LightCard[] publicObjectives = GUI.getLightGame().getPublicObjective();
        for(int i = 0; i < publicObjectives.length; i++){
            commonObjectives[i].setTarget(publicObjectives[i]);
        }
    }

}
