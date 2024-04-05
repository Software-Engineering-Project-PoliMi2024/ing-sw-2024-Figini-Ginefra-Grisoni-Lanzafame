package it.polimi.ingsw.view;

import it.polimi.ingsw.view.Action;
import it.polimi.ingsw.view.Visualization;

import java.util.List;

public abstract class View {
    List<Visualization> visualizations;
    List<Action> actions;
    public void clearActions(){}
    public void clearVisualizations(){}
    public void render(){}
    /**@param actions the list of action to add to the view*/
    public void setActions(List<Action> actions){}
    /**@param  visualizations the list of visualizations to add to the view*/
    public void setVisualizations(List<Visualization> visualizations){}
}
