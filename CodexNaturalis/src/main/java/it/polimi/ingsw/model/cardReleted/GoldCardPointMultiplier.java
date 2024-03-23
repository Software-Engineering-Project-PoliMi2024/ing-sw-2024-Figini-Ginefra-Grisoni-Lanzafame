package it.polimi.ingsw.model.cardReleted;

import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.Placement;

public interface GoldCardPointMultiplier {
    public int getMultiplier(Codex codex);
}
