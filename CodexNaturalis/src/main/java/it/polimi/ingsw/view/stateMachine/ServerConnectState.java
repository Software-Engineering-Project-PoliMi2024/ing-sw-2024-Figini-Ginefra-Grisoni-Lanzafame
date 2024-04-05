package it.polimi.ingsw.view.stateMachine;

import it.polimi.ingsw.view.Action;
import it.polimi.ingsw.view.Actions.Connect;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.Visualization;

import java.util.List;

public class ServerConnectState extends ViewState{
        static final List<Action> actions = List.of(new Connect());
        static final List<Visualization> visualizations = List.of( Visualization.CONNECT_FORM);
        public static void run(View view) {
            view.setVisualizations(visualizations);
            view.setActions(actions);
    }
}
