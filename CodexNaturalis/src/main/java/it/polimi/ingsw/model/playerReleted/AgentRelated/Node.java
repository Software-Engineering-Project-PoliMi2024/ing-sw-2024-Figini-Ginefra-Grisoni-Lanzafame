package it.polimi.ingsw.model.playerReleted.AgentRelated;

import it.polimi.ingsw.model.playerReleted.AgentRelated.ActionRelated.Action;
import it.polimi.ingsw.model.playerReleted.AgentRelated.ExpansionRelated.Expander;
import it.polimi.ingsw.model.playerReleted.AgentRelated.StateRelated.State;

import java.util.LinkedList;
import java.util.List;

public class Node {
    private final Node parent;
    private final List<Node> children;
    private final Action action;
    private final int depth;
    private final Expander expander;
    private int totalReward;
    private int numberOfVisits;
    private boolean isExpandable = false;

    public Node(Node parent, Action action, int depth, Expander expander) {
        this.parent = parent;
        this.children = new LinkedList<>();
        this.action = action;
        this.depth = depth;
        this.totalReward = 0;
        this.numberOfVisits = 0;
        this.expander = expander;
    }

    public List<Node> getChildren() {
        return children;
    }

    public Action getAction() {
        return action;
    }

    public int getDepth() {
        return depth;
    }

    public int getTotalReward() {
        return totalReward;
    }

    public int getNumberOfVisits() {
        return numberOfVisits;
    }

    public int getSelectionScore(){
        return action.getSelectionScore(this);
    }

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    public Node expand(State state){
        return expander.expand(this, state);
    }

    public void addToTotalReward(int reward){
        totalReward += reward;
    }

    public void backPropagate(int reward){
        totalReward += reward;
        numberOfVisits++;
        if(parent != null)
            parent.backPropagate(reward);
    }
}
