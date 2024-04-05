package it.polimi.ingsw.view.stateMachine;

import it.polimi.ingsw.view.Action;
import it.polimi.ingsw.view.Actions.GoTo;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.Visualization;

import java.util.List;

public class PostGameState extends ViewState{
    static final List<Action> actions = List.of(new GoTo( ));
        static final List<Visualization> visualizations = List.of( Visualization.POST_GAME_VIEW);
        public static void run(View view) {
        view.setVisualizations(visualizations);
        view.setActions(actions);
    }
}
