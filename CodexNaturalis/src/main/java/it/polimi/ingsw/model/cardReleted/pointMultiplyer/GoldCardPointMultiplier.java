package it.polimi.ingsw.model.cardReleted.pointMultiplyer;

import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.WritingMaterial;
import it.polimi.ingsw.model.playerReleted.Codex;

import java.io.Serializable;

public interface GoldCardPointMultiplier extends Serializable {
    int getMultiplier(Codex codex, GoldCard goldCard);

    public GoldCardPointMultiplier getCopy();

    public WritingMaterial getTarget();
}
