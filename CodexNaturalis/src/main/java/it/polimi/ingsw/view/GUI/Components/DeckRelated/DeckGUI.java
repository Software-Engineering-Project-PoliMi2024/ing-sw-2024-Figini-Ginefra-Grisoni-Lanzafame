package it.polimi.ingsw.view.GUI.Components.DeckRelated;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
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

public class DeckGUI implements Observer {
    private final VBox content = new VBox();
    private CardGUI drawableCard;
    private final FlippableCardGUI[] buffer = new FlippableCardGUI[2];

    private final DrawableCard cardTarget;

    private final HBox parent;

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

        content.getChildren().add(drawableCard.getImageView());
        Arrays.stream(buffer).forEach(card -> content.getChildren().add(card.getImageView()));

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
                    GUI.getControllerStatic().draw(cardTarget,  buffer.length - 1 - Arrays.asList(buffer).indexOf(card));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }));

        parent.getChildren().add(content);
    }

    private void setSizeBindings(CardGUI card){
        card.getImageView().fitWidthProperty().bind(parent.maxHeightProperty().subtract(buffer.length * GUIConfigs.vCardGapInDeck).divide(buffer.length + 1).multiply(GUIConfigs.cardWidth / GUIConfigs.cardHeight));
    }

    public void update(){
        LightDeck deck = GUI.getLightGame().getDecks().get(cardTarget);
        drawableCard.setTarget(deck.getDeckBack());
        for(int i = 0; i < buffer.length; i++){
            buffer[i].setTarget(deck.getCardBuffer()[i]);
        }
    }

    public VBox getContent(){
        return content;
    }
}
