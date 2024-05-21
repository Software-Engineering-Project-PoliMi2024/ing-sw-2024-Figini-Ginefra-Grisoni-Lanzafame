package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.view.GUI.Components.CardRelated.FlippableCardGUI;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.StateGUI;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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

        choiceBox.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            AnchorPane.setLeftAnchor(choiceBox, (parent.getWidth() - newBounds.getWidth()) / 2);
            AnchorPane.setTopAnchor(choiceBox, (parent.getHeight() - newBounds.getHeight()) / 2);
        });

        choiceBox.setOnMousePressed(
                e -> {
                    pastX = e.getSceneX();
                    pastY = e.getSceneY();
                }
        );

        choiceBox.setOnMouseDragged(
                e -> {
                    double deltaX = e.getSceneX() - pastX;
                    double deltaY = e.getSceneY() - pastY;

                    for (int i = 0; i < choiceBox.getChildren().size(); i++) {
                        choiceBox.getChildren().get(i).setTranslateX(choiceBox.getChildren().get(i).getTranslateX() + deltaX);
                        choiceBox.getChildren().get(i).setTranslateY(choiceBox.getChildren().get(i).getTranslateY() + deltaY);
                    }

                    pastX = e.getSceneX();
                    pastY = e.getSceneY();
                }
        );


        GUI.getStateProperty().addListener((obs, oldState, newState) -> {

            if(newState == StateGUI.SELECT_OBJECTIVE){
                checkAndShow();
            }else{
                Platform.runLater(()->choiceBox.getChildren().clear());
            }
        });

    }

    private void checkAndShow(){
        Set<LightCard> objectives = Arrays.stream(GUI.getLightGame().getHand().getSecretObjectiveOptions()).collect(Collectors.toSet());
        if(objectives.size() == 2 && GUI.getStateProperty().get() == StateGUI.SELECT_OBJECTIVE){
            setFlippableCard(objectives);
            choiceBox.getChildren().addAll(Arrays.stream(objectivesCard).map(FlippableCardGUI::getImageView).toList());
        }
    }
    private void setFlippableCard(Set<LightCard> objectives){
        List<FlippableCardGUI> objectivesList = new ArrayList<>();

        for(LightCard lightCard : objectives){
            FlippableCardGUI card = new FlippableCardGUI(lightCard);
            card.setOnHold(
                    e -> {
                        try {
                            GUI.getControllerStatic().choseSecretObjective(card.getTarget());
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
