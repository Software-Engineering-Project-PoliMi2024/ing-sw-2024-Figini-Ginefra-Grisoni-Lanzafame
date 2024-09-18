package it.polimi.ingsw.model.playerReleted.AgentRelated.ActionRelated;

import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.AgentRelated.Node;

public class DrawOthersAction extends DrawAction {
    public DrawOthersAction(DrawableCard targetDeck, int targetPosition) {
        super(targetDeck, targetPosition);
    }

    @Override
    public float getSelectionScore(Node node) {
        return 1 / UCB1.score(node);
    }
}
