package it.polimi.ingsw.model.cardReleted;

import it.polimi.ingsw.model.playerReleted.Codex;

import java.util.Map;

public class WritingMaterialsCardPointMultiplier implements GoldCardPointMultiplier{
    final private Map<WritingMaterial, Integer> targets;

    public WritingMaterialsCardPointMultiplier(){
        targets = null;
    }

    @Override
    public int getMultiplier(Codex codex) {
        return 0;
    }
}
