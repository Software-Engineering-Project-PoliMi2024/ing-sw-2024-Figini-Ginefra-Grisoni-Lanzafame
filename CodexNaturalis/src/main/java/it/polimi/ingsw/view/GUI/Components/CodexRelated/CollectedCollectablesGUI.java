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

/**
 * This class represents the GUI of the collectables that the player has collected so far.
 */
public class CollectedCollectablesGUI implements Observer {
    /** The counters of the collectables */
    private final Map<Collectable, ImageCounter> counters = new LinkedHashMap<>();
    /** The container of the counters */
    private final HBox container = new HBox();

    /**
     * Creates a new CollectedCollectablesGUI.
     */
    public CollectedCollectablesGUI() {
        Arrays.stream(Resource.values()).forEach(resource -> counters.put(resource, new ImageCounter(AssetsGUI.loadResource(resource))));

        Arrays.stream(WritingMaterial.values()).forEach(writingMaterial -> counters.put(writingMaterial, new ImageCounter(AssetsGUI.loadWritingMaterial(writingMaterial))));

        AnchorPane.setBottomAnchor(container, 0.0);
        AnchorPane.setRightAnchor(container, 0.0);
        AnchorPane.setTopAnchor(container, 0.0);
        AnchorPane.setLeftAnchor(container, 0.0);

        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10));

        container.setSpacing(20);

        counters.values().forEach(imageCounter -> container.getChildren().add(imageCounter.getContent()));
    }

    /**
     * Attaches this to the codex.
     */
    public void attachToCodex(){
        this.getLightCodex().attach(this);
    }

    /**
     * Sets the counter of the given collectable to the given value.
     * @param collectable the collectable to set the counter of.
     * @param counter the new counter of the collectable.
     */
    public void setCounter(Collectable collectable, int counter) {
        counters.get(collectable).setCounter(counter);
    }

    /**
     * Returns the content of the counters.
     * @return the content of the counters.
     */
    public HBox getContent() {
        return container;
    }

    /**
     * Adds this to the given parent.
     * @param parent the parent to add this to.
     */
    public void addThisTo(AnchorPane parent){
        AnchoredPopUp popUp = new AnchoredPopUp(parent, 0.4f, 0.08f, Pos.TOP_CENTER, 0.25f);
        popUp.setLocked(true);
        popUp.open();
        popUp.getContent().getChildren().add(container);
    }

    /**
     * Updated the counters according to the codex. This is called when the codex changes by the observer pattern.
     */
    @Override
    public void update() {
        this.getLightCodex().getEarnedCollectables().forEach((collectable, integer) -> {
                if(counters.containsKey(collectable))
                    setCounter(collectable, integer);
        });
    }

    /**
     * Returns the LightCodex of the player that this codex represents.
     * @return the LightCodex of the player that this codex represents.
     */
    protected LightCodex getLightCodex(){
        return GUI.getLightGame().getMyCodex();
    }
}
