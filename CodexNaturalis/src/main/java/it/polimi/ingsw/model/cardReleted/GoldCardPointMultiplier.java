package it.polimi.ingsw.model.cardReleted;

import it.polimi.ingsw.model.playerReleted.Codex;

import java.io.Serializable;

public interface GoldCardPointMultiplier extends Serializable {
    int getMultiplier(Codex codex);
}
