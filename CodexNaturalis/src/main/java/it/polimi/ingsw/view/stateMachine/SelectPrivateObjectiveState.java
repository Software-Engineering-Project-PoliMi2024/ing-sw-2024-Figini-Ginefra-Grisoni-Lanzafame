package it.polimi.ingsw.view.stateMachine;

import it.polimi.ingsw.view.Actions.SelectObjective;
import it.polimi.ingsw.view.Visualization;
import it.polimi.ingsw.view.Action;
import it.polimi.ingsw.view.View;
import java.util.List;

public class SelectPrivateObjectiveState extends ViewState {
    static final List<Action> actions = List.of(new SelectObjective());
    static final List<Visualization> visualizations = List.of( Visualization.SHOW_CODEX, Visualization.SHOW_DECK, Visualization.PEEK_FORM, Visualization.SHOW_HAND, Visualization.SHOW_OBJECTIVE_OPTIONS);
    public static void run(View view) {
        view.setVisualizations(visualizations);
        view.setActions(actions);

    }
}








