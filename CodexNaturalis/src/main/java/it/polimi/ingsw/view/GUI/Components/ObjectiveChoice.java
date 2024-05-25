package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.view.GUI.Components.CardRelated.FlippableCardGUI;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.StateGUI;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.*;
import java.util.stream.Collectors;

public class ObjectiveChoice implements Observer {
    private FlippableCardGUI[] objectivesCard = new FlippableCardGUI[2];
    private final HBox choiceBox = new HBox();
    private final AnchorPane parent;

    private double pastX = 0;
    private double pastY = 0;

    public ObjectiveChoice(AnchorPane parent) {
//        HandDiff diff = new HandDiffAddOneSecretObjectiveOption(new LightCard(1, 1));
//        diff.apply(GUI.getLightGame());
//        diff = new HandDiffAddOneSecretObjectiveOption(new LightCard(2, 11));
//        diff.apply(GUI.getLightGame());

        this.parent = parent;

        GUI.getLightGame().getHand().attach(this);

        choiceBox.setAlignment(Pos.CENTER);
        choiceBox.setSpacing(10);

        AnchorPane.setBottomAnchor(choiceBox, 0.0);
        AnchorPane.setLeftAnchor(choiceBox, 0.0);
        AnchorPane.setRightAnchor(choiceBox, 0.0);
        AnchorPane.setTopAnchor(choiceBox, 0.0);

        GUI.getStateProperty().addListener((obs, oldState, newState) -> {
            if(newState == StateGUI.SELECT_OBJECTIVE) {
                Platform.runLater(() -> parent.getChildren().add(choiceBox));
            }
        });

    }

    private void checkAndShow(){
        LightCard[] objectives = GUI.getLightGame().getHand().getSecretObjectiveOptions();
        int nonNull = (int) Arrays.stream(objectives).filter(Objects::nonNull).count();
        if(nonNull == 2 && GUI.getStateProperty().get() == StateGUI.SELECT_OBJECTIVE){
            setFlippableCard(objectives);
            choiceBox.getChildren().addAll(Arrays.stream(objectivesCard).map(FlippableCardGUI::getImageView).toList());
        }
    }

    private void setFlippableCard(LightCard[] objectives){
        List<FlippableCardGUI> objectivesList = new ArrayList<>();

        for(LightCard lightCard : objectives){
            FlippableCardGUI card = new FlippableCardGUI(lightCard);
            card.setOnHold(
                    e -> {
                        try {
                            GUI.getControllerStatic().choseSecretObjective(card.getTarget());
                            parent.getChildren().remove(choiceBox);
                        }catch (Exception ex){
                        }
                        e.consume();
                    });
            objectivesList.add(card);
        }

        objectivesCard = objectivesList.toArray(new FlippableCardGUI[0]);
    }

    public HBox getChoiceDisplay() {
        return choiceBox;
    }
    @Override
    public void update() {
        checkAndShow();
    }
}
