package it.polimi.ingsw.model.cardReleted;

import it.polimi.ingsw.model.playerReleted.Codex;

public class ObjectiveCard extends Card{
    private final ObjectiveCardPointMultiplier multiplier;

    public ObjectiveCard(){
        super(0);
        multiplier = null;
    }

    public int getEarnedPoints(Codex codex){
        return 0;
    }
}
