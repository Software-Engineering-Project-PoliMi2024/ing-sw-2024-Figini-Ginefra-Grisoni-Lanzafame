package it.polimi.ingsw.view.GUI.Components;

import com.sun.scenario.effect.light.Light;
import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.diffs.game.HandDiff;
import it.polimi.ingsw.lightModel.diffs.game.HandDiffAddOneSecretObjectiveOption;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.view.GUI.Components.CardRelated.FlippableCardGUI;
import it.polimi.ingsw.view.GUI.Components.CardRelated.FlippablePlayableCard;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.scene.layout.HBox;

import java.util.Arrays;

public class ObjectiveChoice implements Observer {
    private FlippableCardGUI[] objectives = new FlippableCardGUI[2];
    private final HBox choiceBox = new HBox();

    public ObjectiveChoice() {
        HandDiff diff = new HandDiffAddOneSecretObjectiveOption(new LightCard(1, 1));
        diff.apply(GUI.getLightGame());
        diff = new HandDiffAddOneSecretObjectiveOption(new LightCard(2, 1));
        diff.apply(GUI.getLightGame());

        GUI.getLightGame().getHand().attach(this);
        checkAndDisplay();
    }

    private void checkAndDisplay(){
        if(GUI.getLightGame().getHand().getSecretObjectiveOptions().length != 2)
            return;

        objectives = Arrays.stream(GUI.getLightGame().getHand().getSecretObjectiveOptions()).map(FlippableCardGUI::new).toArray(FlippableCardGUI[]::new);

    }

    @Override
    public void update() {
        checkAndDisplay();
    }
}
