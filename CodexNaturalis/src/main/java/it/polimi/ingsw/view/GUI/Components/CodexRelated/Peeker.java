package it.polimi.ingsw.view.GUI.Components.CodexRelated;

import it.polimi.ingsw.view.GUI.Components.PopUp;
import javafx.scene.layout.AnchorPane;

public class Peeker {
    private final PopUp popUp;
    private final CodexOthers codexOthers;
    private final CollectedCollectablesOthers collectedCollectablesOthers;

    public Peeker(AnchorPane parent, String targetPlayer){
        popUp = new PopUp(parent);
        codexOthers = new CodexOthers(targetPlayer);
        codexOthers.attachToCodex();

        collectedCollectablesOthers = new CollectedCollectablesOthers(targetPlayer);
        collectedCollectablesOthers.attachToCodex();

        popUp.getContent().getChildren().addAll(codexOthers.getCodex());
        collectedCollectablesOthers.addThisTo(popUp.getContent());
    }

    public void open(){
        popUp.open();
    }
}
