package it.polimi.ingsw.model.playerReleted.AgentRelated.ActionRelated;

import it.polimi.ingsw.model.playerReleted.AgentRelated.Node;

public class UCB1 {
    static private final float c = 1;
    public static float score(Node node){
        return (float) node.getTotalReward() / node.getNumberOfVisits() + c * (float) Math.sqrt(Math.log(node.getParent().getNumberOfVisits()) / node.getNumberOfVisits());
    }
}
