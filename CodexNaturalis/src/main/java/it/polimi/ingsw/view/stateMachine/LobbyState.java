package it.polimi.ingsw.view.stateMachine;

import it.polimi.ingsw.view.Action;
import it.polimi.ingsw.view.Actions.LeaveLobby;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.Visualization;

import java.util.List;

public class LobbyState extends ViewState{

    static final List<Action> actions = List.of( new LeaveLobby());
    static final List<Visualization> visualizations = List.of(Visualization.SHOW_LOBBY);
    public static void run(View view) {
        view.setVisualizations(visualizations);
        view.setActions(actions);

    }
}
