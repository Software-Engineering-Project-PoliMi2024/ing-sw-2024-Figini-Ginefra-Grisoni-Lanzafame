package it.polimi.ingsw.model.cardReleted;

import it.polimi.ingsw.model.playerReleted.Codex;

public class LCardPointMultiplier implements ObjectiveCardPointMultiplier{
    final private CardCorner corner;
    final private Resource singleResource;
    final private Resource doubleResource;

    public LCardPointMultiplier(){
        corner = null;
        singleResource = null;
        doubleResource = null;
    }

    @Override
    public int getMultiplier(Codex codex) {
        return 0;
    }
}
