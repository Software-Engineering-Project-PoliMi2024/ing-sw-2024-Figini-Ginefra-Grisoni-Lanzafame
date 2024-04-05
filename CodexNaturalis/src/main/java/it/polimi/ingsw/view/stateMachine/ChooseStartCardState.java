package it.polimi.ingsw.view.stateMachine;

import it.polimi.ingsw.view.Action;
import it.polimi.ingsw.view.Actions.Peek;
import it.polimi.ingsw.view.Actions.SelectCardFace;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.Visualization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChooseStartCardState extends ViewState{
    static final List<Action> actions = List.of(new SelectCardFace(), new Peek());
    static final List<Visualization> visualizations = List.of(Visualization.SHOW_START_CARD, Visualization.SHOW_CODEX, Visualization.SHOW_DECK, Visualization.PEEK_FORM);
    public static void run(View view) {
        view.setVisualizations(visualizations);
        view.setActions(actions);

    }
}

