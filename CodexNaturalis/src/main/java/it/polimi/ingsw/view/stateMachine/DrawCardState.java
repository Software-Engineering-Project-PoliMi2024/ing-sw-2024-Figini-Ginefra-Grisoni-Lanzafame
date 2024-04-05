package it.polimi.ingsw.view.stateMachine;

import it.polimi.ingsw.view.Action;
import it.polimi.ingsw.view.Actions.ChooseDraw;
import it.polimi.ingsw.view.Actions.GoTo;
import it.polimi.ingsw.view.Actions.JoinGame;
import it.polimi.ingsw.view.Actions.Peek;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.Visualization;

import java.util.List;

public class DrawCardState {
    static final List<Action> actions = List.of(new ChooseDraw(), new Peek());
    static final List<Visualization> visualizations = List.of(Visualization.SHOW_CODEX, Visualization.SHOW_DECK, Visualization.SHOW_HAND, Visualization.PEEK_FORM, Visualization.DRAW_FORM, Visualization.SHOW_LOG);
    public static void run(View view) {
        view.clearVisualizations();
        view.clearActions();
        view.setVisualizations(visualizations);
        view.setActions(actions);
    }
}
