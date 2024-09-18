package it.polimi.ingsw.model.playerReleted.AgentRelated;

import it.polimi.ingsw.model.playerReleted.AgentRelated.ActionRelated.Action;
import it.polimi.ingsw.model.playerReleted.AgentRelated.ExpansionRelated.Expander;
import it.polimi.ingsw.model.playerReleted.AgentRelated.ExpansionRelated.PlaceExpander;
import it.polimi.ingsw.model.playerReleted.AgentRelated.StateRelated.DeckBeliefBuilder;
import it.polimi.ingsw.model.playerReleted.AgentRelated.StateRelated.State;
import it.polimi.ingsw.model.tableReleted.Game;

import java.util.LinkedList;
import java.util.List;

public class MCTSearcher {
    private final Node root;
    private final State initialState;

    public MCTSearcher(Game game, String perspectivePlayer){
        this.initialState = new State(
                game.getGameParty().getPlayerFromNick(perspectivePlayer),
                DeckBeliefBuilder.buildDeckBeliefResourceCard(game, game.getResourceCardDeck(), perspectivePlayer),
                DeckBeliefBuilder.buildDeckBeliefGoldCard(game, game.getGoldCardDeck(), perspectivePlayer),
                game.getCommonObjective(),
                game.getGameParty().getNumberOfMaxPlayer()
                );
        PlaceExpander expander = new PlaceExpander(initialState);
        this.root = new Node(null, null, 0, expander);
        expander.setExpandable(root);
    }

    public Action searchBestAction(int iterations){
        for(int i = 0; i < iterations; i++){
            State state = new State(initialState);
            Node selectedNode = selection(root, state);
            Node childNode = selectedNode.expand(state);
            selectedNode.addChild(childNode);
            int reward = childNode.getAction().simulate(state);
            childNode.backPropagate(reward);
        }

        return bestChild(root).getAction();
    }

    /**
     * Returns the node's child with the height number of visits
     * @param node the node to search the best child of
     * @return the best child of the node
     */
    private Node bestChild(Node node){
        return node.getChildren().stream().max((n1, n2) -> {
            double n1Value = n1.getNumberOfVisits();
            double n2Value = n2.getNumberOfVisits();
            return Double.compare(n1Value, n2Value);
        }).orElseThrow();
    }

    private Node selection(Node node, State state){
        if(node.isExpandable())
            return node;

        float bestScore = Integer.MIN_VALUE;
        List<Node> bestChildren = new LinkedList<>();
        for(Node child : node.getChildren()){
            float score = child.getSelectionScore();
            if(score > bestScore){
                bestScore = score;
                bestChildren.clear();
                bestChildren.add(child);
            } else if(score == bestScore){
                bestChildren.add(child);
            }
        }
        System.out.println("Selected node with score: " + bestScore + " and a number of candidates: " + bestChildren.size());
        Node bestChild = bestChildren.get((int) (Math.random() * bestChildren.size()));
        bestChild.getAction().actOnState(state);
        return selection(bestChild, state);
    }


}
