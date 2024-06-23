package it.polimi.ingsw.view.GUI.Components.CodexRelated;

import it.polimi.ingsw.utils.designPatterns.Observer;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCodex;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.cardReleted.utilityEnums.WritingMaterial;
import it.polimi.ingsw.view.GUI.AssetsGUI;
import it.polimi.ingsw.view.GUI.Components.Utils.AnchoredPopUp;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.*;

public class CollectedCollectablesGUI implements Observer {
    private final Map<Collectable, ImageCounter> counters = new LinkedHashMap<>();
    private final HBox container = new HBox();

    private AnchoredPopUp popUp;


    public CollectedCollectablesGUI() {
        Arrays.stream(Resource.values()).forEach(resource -> counters.put(resource, new ImageCounter(AssetsGUI.loadResource(resource))));

        Arrays.stream(WritingMaterial.values()).forEach(writingMaterial -> counters.put(writingMaterial, new ImageCounter(AssetsGUI.loadWritingMaterial(writingMaterial))));

//        ModelDiffs<LightGame> diffs = new CodexDiff("Player1", 0, Map.of(
//                Resource.PLANT, 2,
//                Resource.ANIMAL, 3,
//                Resource.INSECT, 4,
//                Resource.FUNGI, 5,
//                WritingMaterial.INKWELL, 6,
//                WritingMaterial.MANUSCRIPT, 7,
//                WritingMaterial.QUILL, 8
//        ), new LinkedList<>(), GUI.getLightGame().getMyCodex().getFrontier().frontier());
//
//        diffs.apply(GUI.getLightGame());


        AnchorPane.setBottomAnchor(container, 0.0);
        AnchorPane.setRightAnchor(container, 0.0);
        AnchorPane.setTopAnchor(container, 0.0);
        AnchorPane.setLeftAnchor(container, 0.0);

        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10));

        container.setSpacing(20);

        counters.values().forEach(imageCounter -> container.getChildren().add(imageCounter.getContent()));
    }

    public void attachToCodex(){
        this.getLightCodex().attach(this);
    }

    public void setCounter(Collectable collectable, int counter) {
        counters.get(collectable).setCounter(counter);
    }

    public HBox getContent() {
        return container;
    }

    public void addThisTo(AnchorPane parent){
        popUp = new AnchoredPopUp(parent, 0.4f, 0.08f, Pos.TOP_CENTER, 0.25f);
        popUp.setLocked(true);
        popUp.open();
        popUp.getContent().getChildren().add(container);
    }

    @Override
    public void update() {
        this.getLightCodex().getEarnedCollectables().forEach((collectable, integer) -> {
                if(counters.containsKey(collectable))
                    setCounter(collectable, integer);
        });
    }

    protected LightCodex getLightCodex(){
        return GUI.getLightGame().getMyCodex();
    }
}
