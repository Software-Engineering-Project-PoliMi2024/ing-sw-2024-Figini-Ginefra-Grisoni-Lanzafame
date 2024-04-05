package it.polimi.ingsw.view.stateMachine;

import it.polimi.ingsw.view.Action;
import it.polimi.ingsw.view.Actions.Peek;
import it.polimi.ingsw.view.Actions.PlaceCard;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.Visualization;

import java.util.Arrays;
import java.util.List;

public class PlaceCardState {
    static final List<Action> actions = List.of(new PlaceCard(), new Peek());
    static final List<Visualization> visualizations = List.of( Visualization.SHOW_CODEX, Visualization.SHOW_DECK, Visualization.PEEK_FORM, Visualization.PLACEMENT_FORM, Visualization.SHOW_LOG);
    public static void run(View view) {
        view.setVisualizations(visualizations);
        view.setActions(actions);

    }
}
