package it.polimi.ingsw.view.GUI.Components.CodexRelated;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCodex;
import it.polimi.ingsw.view.GUI.GUI;

public class CodexOthers extends CodexGUI{
    private final String targetPlayer;

    public CodexOthers(String targetPlayer){
        super();
        this.targetPlayer = targetPlayer;
    }

    public String getTargetPlayer(){
        return targetPlayer;
    }

    @Override
    protected LightCodex getLightCodex(){
        //System.out.println(GUI.getLightGame().getCodexMap().get("Player2"));
        return GUI.getLightGame().getCodexMap().get(targetPlayer);
    }
}
