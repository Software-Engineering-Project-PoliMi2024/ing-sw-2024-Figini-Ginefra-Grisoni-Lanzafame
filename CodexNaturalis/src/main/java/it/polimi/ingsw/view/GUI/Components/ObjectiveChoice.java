package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.utils.designPatterns.Observer;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.view.GUI.Components.CardRelated.FlippableCardGUI;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.StateGUI;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.*;

/**
 * This class represents the GUI component responsible for housing the objective choice.
 */
public class ObjectiveChoice implements Observer {
    /** The objective cards */
    private FlippableCardGUI[] objectiveCards = new FlippableCardGUI[2];

    /** The container of the popup */
    private final VBox container = new VBox();

    /** The choice box */
    private final HBox choiceBox = new HBox();

    /** The parent of the popup */
    private final AnchorPane parent;

    /** Creates a new ObjectiveChoice.
     * @param parent the parent of the popup
     */
    public ObjectiveChoice(AnchorPane parent) {
        this.parent = parent;

        AnchorPane.setBottomAnchor(container, 20.0);
        AnchorPane.setLeftAnchor(container, 20.0);
        AnchorPane.setRightAnchor(container, 20.0);
        AnchorPane.setTopAnchor(container, 20.0);

        //Make it bold and centered
        Text label = new Text("Choose your secret objective");
        label.setStyle("-fx-font-size: 40; -fx-font-weight: bold; -fx-text-fill: white;");
        label.getStyleClass().add("customFont");
        label.setTextAlignment(TextAlignment.CENTER);

        container.getChildren().addAll(label, choiceBox);

        container.setAlignment(Pos.CENTER);

        GUI.getLightGame().getHand().attach(this);

        choiceBox.setAlignment(Pos.CENTER);
        choiceBox.setSpacing(10);
        choiceBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-background-radius: 20; -fx-border-radius: 20;");

        choiceBox.setPadding(new Insets(30));

        GUI.getStateProperty().addListener((obs, oldState, newState) -> {
            if(newState == StateGUI.SELECT_OBJECTIVE) {
                Platform.runLater(() -> parent.getChildren().add(container));
            }
        });

    }

    /**
     * Checks if the objectives are ready to be shown and shows them.
     */
    private void checkAndShow(){
        LightCard[] objectives = GUI.getLightGame().getHand().getSecretObjectiveOptions();
        int nonNull = (int) Arrays.stream(objectives).filter(Objects::nonNull).count();
        if(nonNull == 2){
            setFlippableCard(objectives);
            Platform.runLater(
                    () -> choiceBox.getChildren().addAll(Arrays.stream(objectiveCards).map(FlippableCardGUI::getImageView).toList())
            );

        }
    }

    /**
     * Sets the flippable card.
     * @param objectives the objectives to set
     */
    private void setFlippableCard(LightCard[] objectives){
        List<FlippableCardGUI> objectivesList = new ArrayList<>();

        for(LightCard lightCard : objectives){
            FlippableCardGUI card = new FlippableCardGUI(lightCard);
            card.setOnHold(
                    e -> {
                        try {
                            GUI.getControllerStatic().chooseSecretObjective(card.getTarget());
                            parent.getChildren().remove(container);
                        }catch (Exception ex){
                        }
                        e.consume();
                    });
            objectivesList.add(card);
        }

        objectiveCards = objectivesList.toArray(new FlippableCardGUI[0]);
    }

    /**
     * Updates the objective choice.
     */
    @Override
    public synchronized void update() {
        checkAndShow();
    }
}
