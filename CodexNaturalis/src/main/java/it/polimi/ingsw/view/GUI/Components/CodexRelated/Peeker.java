package it.polimi.ingsw.view.GUI.Components.CodexRelated;

import it.polimi.ingsw.view.GUI.Components.HandRelated.HandOthersGUI;
import it.polimi.ingsw.view.GUI.Components.Utils.PopUp;
import javafx.scene.layout.AnchorPane;

/**
 * This class represents the GUI component to peek at the codex of a player that is not the player that is using the GUI.
 */
public class Peeker {
    /** The pop up that contains the codex. */
    private final PopUp popUp;

    /**
     * Creates a new Peeker.
     * @param parent the parent of the pop up.
     * @param targetPlayer the player that this peeker refers to.
     */
    public Peeker(AnchorPane parent, String targetPlayer){
        popUp = new PopUp(parent);
        CodexOthers codexOthers = new CodexOthers(targetPlayer);
        codexOthers.attachToCodex();

        CollectedCollectablesOthers collectedCollectablesOthers = new CollectedCollectablesOthers(targetPlayer);
        collectedCollectablesOthers.attachToCodex();

        HandOthersGUI handOthersGUI = new HandOthersGUI(targetPlayer);
        handOthersGUI.attach();

        popUp.getContent().getChildren().addAll(codexOthers.getCodex());
        collectedCollectablesOthers.addThisTo(popUp.getContent());
        handOthersGUI.addThisTo(popUp.getContent());
    }

    /**
     * Opens the pop up.
     */
    public void open(){
        popUp.open();
    }
}
