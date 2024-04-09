package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.GameParty;
import it.polimi.ingsw.view.TUI.ActionsTUI;
import it.polimi.ingsw.view.joinGameState;

import java.util.Arrays;
import java.util.List;

public class joinGameStateTUI extends joinGameState {
    private final static List<ActionsTUI> availableActions = Arrays.asList(ActionsTUI.CREATE_GAME, ActionsTUI.JOIN_GAME, ActionsTUI.REFRESH);
    public static void run(List<Game> games) {
        printGameList(games);
        printActionList();
    }
    public static void printGameList(List<Game> games){
        for (Game game : games) {
            System.out.println(game.getName() + ":\n" + gamePartyTUI(game.getGameParty()) + "\n"); //to string game party
        }
    }

    public static String gamePartyTUI(GameParty game){
        return    "Players=" + game.getUsersList().stream().reduce("", (partialString, user) -> partialString + user.getNickname() + " ", String::concat) + "\n"
                + "Players= " + game.getCurrentPlayer() + "/" + game.getNumberOfMaxPlayer() + "\n";

    }

    public static List<ActionsTUI> getAvailableActions() {
        return availableActions;
    }
    public static void printActionList(){
        for (ActionsTUI action : getAvailableActions()) {
            System.out.println("Write" + " " + action.getInputExpected() + " " + action.getParameterExpected() + " " + action.getPrintedDescription());
        }

    }
}
