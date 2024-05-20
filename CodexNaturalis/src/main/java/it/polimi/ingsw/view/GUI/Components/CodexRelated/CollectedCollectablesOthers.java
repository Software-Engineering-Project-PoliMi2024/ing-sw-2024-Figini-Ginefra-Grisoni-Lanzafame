package it.polimi.ingsw.view.GUI.Components.CodexRelated;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCodex;
import it.polimi.ingsw.view.GUI.GUI;

public class CollectedCollectablesOthers extends CollectedCollectablesGUI{
    private final String targetPlayer;

    public CollectedCollectablesOthers(String targetPlayer){
        super();
        this.targetPlayer = targetPlayer;
    }

    public String getTargetPlayer(){
        return targetPlayer;
    }

    @Override
    protected LightCodex getLightCodex(){
        return GUI.getLightGame().getCodexMap().get(targetPlayer);
    }
}
