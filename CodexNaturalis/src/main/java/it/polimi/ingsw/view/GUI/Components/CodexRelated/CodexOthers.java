package it.polimi.ingsw.view.GUI.Components.CodexRelated;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCodex;
import it.polimi.ingsw.view.GUI.GUI;

/**
 * This class represents the codex of a player that is not the player that is using the GUI.
 */
public class CodexOthers extends CodexGUI{
    /** The player that this codex represents. */
    private final String targetPlayer;

    /**
     * Creates a new CodexOthers.
     * @param targetPlayer the player that this codex represents.
     */
    public CodexOthers(String targetPlayer){
        super();
        this.targetPlayer = targetPlayer;
    }

    /**
     * Returns the player that this codex represents.
     * @return the player that this codex represents.
     */
    @Override
    public String getTargetPlayer(){
        return targetPlayer;
    }

    /**
     * Returns the LightCodex of the player that this codex represents.
     * @return the LightCodex of the player that this codex represents.
     */
    @Override
    protected LightCodex getLightCodex(){
        return GUI.getLightGame().getCodexMap().get(targetPlayer);
    }
}
