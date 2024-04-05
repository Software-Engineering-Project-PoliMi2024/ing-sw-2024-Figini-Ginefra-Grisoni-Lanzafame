package it.polimi.ingsw.view.stateMachine;

import it.polimi.ingsw.view.Action;
import it.polimi.ingsw.view.Actions.GoTo;
import it.polimi.ingsw.view.Actions.JoinGame;
import it.polimi.ingsw.view.Actions.Peek;
import it.polimi.ingsw.view.Actions.SelectCardFace;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.Visualization;

import java.util.Arrays;
import java.util.List;

public class CreateGameState extends ViewState{
    static final List<Action> actions = List.of(new GoTo(), new JoinGame());
    static final List<Visualization> visualizations = List.of(Visualization.CREATE_GAME_FORM);
    public static void run(View view) {
        view.clearVisualizations();
        view.clearActions();
        view.setVisualizations(visualizations);
        view.setActions(actions);

    }
}
