package it.polimi.ingsw.view.GUI.Components.CodexRelated;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCodex;
import it.polimi.ingsw.view.GUI.GUI;

/**
 * This class represents the codex of a player that is not the player that is using the GUI.
 */
public class CollectedCollectablesOthers extends CollectedCollectablesGUI{
    /** The player that this component refers to. */
    private final String targetPlayer;

    /**
     * Creates a new CollectedCollectablesOthers.
     * @param targetPlayer the player that this component refers to.
     */
    public CollectedCollectablesOthers(String targetPlayer){
        super();
        this.targetPlayer = targetPlayer;
    }

    /**
     * Returns the player that this component refers to.
     * @return the player that this component refers to.
     */
    @Override
    protected LightCodex getLightCodex(){
        return GUI.getLightGame().getCodexMap().get(targetPlayer);
    }
}
