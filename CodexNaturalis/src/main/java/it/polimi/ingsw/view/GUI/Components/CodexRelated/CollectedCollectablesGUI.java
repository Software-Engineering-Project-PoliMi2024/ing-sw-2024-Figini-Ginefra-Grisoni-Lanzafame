package it.polimi.ingsw.view.GUI.Components.CodexRelated;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.diffs.game.CodexDiff;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.view.GUI.AssetsGUI;
import it.polimi.ingsw.view.GUI.Components.AnchoredPopUp;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.*;

public class CollectedCollectablesGUI implements Observer {
    private final Map<Collectable, ImageCounter> counters = new HashMap<>();
    private final HBox container = new HBox();

    private AnchoredPopUp popUp;


    public CollectedCollectablesGUI() {
        Arrays.stream(Resource.values()).forEach(resource -> counters.put(resource, new ImageCounter(AssetsGUI.loadResource(resource))));


        GUI.getLightGame().getMyCodex().attach(this);

        ModelDiffs<LightGame> diffs = new CodexDiff("Player1", 0, Map.of(
                Resource.PLANT, 2,
                Resource.ANIMAL, 3,
                Resource.INSECT, 4,
                Resource.FUNGI, 5
        ), new LinkedList<>(), GUI.getLightGame().getMyCodex().getFrontier().frontier());

        diffs.apply(GUI.getLightGame());


        AnchorPane.setBottomAnchor(container, 0.0);
        AnchorPane.setRightAnchor(container, 0.0);
        AnchorPane.setTopAnchor(container, 0.0);
        AnchorPane.setLeftAnchor(container, 0.0);

        container.setAlignment(Pos.CENTER);

        container.setSpacing(20);

        container.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);");

        counters.values().forEach(imageCounter -> container.getChildren().add(imageCounter.getContent()));
    }

    public void setCounter(Collectable collectable, int counter) {
        counters.get(collectable).setCounter(counter);
    }

    public HBox getContent() {
        return container;
    }

    public void addThisTo(AnchorPane parent){
        popUp = new AnchoredPopUp(parent, 0.6f, 0.1f, Pos.TOP_CENTER, 0.25f);
        popUp.setLocked(true);
        popUp.open();
        popUp.getContent().getChildren().add(container);
    }

    @Override
    public void update() {
        GUI.getLightGame().getMyCodex().getEarnedCollectables().forEach((collectable, integer) -> {
                if(counters.containsKey(collectable))
                    setCounter(collectable, integer);
        });
    }
}
