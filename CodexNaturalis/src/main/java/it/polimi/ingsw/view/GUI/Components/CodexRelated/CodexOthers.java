package it.polimi.ingsw.view.GUI.Components.CodexRelated;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCodex;
import it.polimi.ingsw.view.GUI.GUI;

public class CodexOthers extends CodexGUI{
    private final String targetPlayer;

    public CodexOthers(String targetPlayer){
        super();
        this.targetPlayer = targetPlayer;
    }

    @Override
    public String getTargetPlayer(){
        return targetPlayer;
    }

    @Override
    protected LightCodex getLightCodex(){
        //System.out.println(GUI.getLightGame().getCodexMap().get(targetPlayer));
        return GUI.getLightGame().getCodexMap().get(targetPlayer);
    }
}
