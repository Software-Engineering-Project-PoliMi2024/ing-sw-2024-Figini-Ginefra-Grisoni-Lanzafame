package it.polimi.ingsw.view.GUI.Components.CodexRelated;

import it.polimi.ingsw.view.GUI.Components.PopUp;
import javafx.scene.layout.AnchorPane;

public class Peeker {
    private final PopUp popUp;

    private final CodexOthers codexOthers;

    public Peeker(AnchorPane parent, String targetPlayer){
        popUp = new PopUp(parent);
        codexOthers = new CodexOthers(targetPlayer);
        codexOthers.attachToCodex();

        popUp.getContent().getChildren().add(codexOthers.getCodex());
    }

    public void open(){
        popUp.open();
    }
}
