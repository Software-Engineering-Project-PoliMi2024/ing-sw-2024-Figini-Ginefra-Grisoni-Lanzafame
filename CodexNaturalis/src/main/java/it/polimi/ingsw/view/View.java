package it.polimi.ingsw.view;

import it.polimi.ingsw.view.Action;
import it.polimi.ingsw.view.stateMachine.Visualization;

import java.util.List;

public abstract class View {
    List<Visualization> visualizations;
    List<Action> actions;
    void clearActions(){}
    void clearVisualizations(){}
    void render(){}

}
