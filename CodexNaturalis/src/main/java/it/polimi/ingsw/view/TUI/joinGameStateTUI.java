package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.GameParty;
import it.polimi.ingsw.view.States.joinGameState;

import java.util.Arrays;
import java.util.List;

public class joinGameStateTUI extends joinGameState {
    private final static List<ActionsTUI> availableActions = Arrays.asList(ActionsTUI.CREATE_GAME, ActionsTUI.JOIN_GAME, ActionsTUI.REFRESH);
    /**@param games List of games available in a state*/
    public static void run(List<Game> games) {
        printGameList(games);
        printActionList();
    }
    /**@param games list of games from which to print the gameList */
    public static void printGameList(List<Game> games){
        for (Game game : games) {
            System.out.println(game.getName() + ":\n" + gamePartyTUI(game.getGameParty()) + "\n"); //to string game party
        }
    }
    /**@param game game from which to print the gameParty
     * @return a string representing the gamePartyTUI*/
    public static String gamePartyTUI(GameParty game){
        return    "Players=" + game.getUsersList().stream().reduce("", (partialString, user) -> partialString + user.getNickname() + " ", String::concat) + "\n"
                + "Players= " + game.getCurrentPlayer() + "/" + game.getNumberOfMaxPlayer() + "\n";

    }
    /**@return List of the action that can be performed in the state*/
    public static List<ActionsTUI> getAvailableActions() {
        return availableActions;
    }
    /**Prints the list of actions that can be performed in the state*/
    public static void printActionList(){
        for (ActionsTUI action : getAvailableActions()) {
            System.out.println("Write" + " " + action.getInputExpected() + " " + action.getParameterExpected() + " " + action.getPrintedDescription());
        }

    }
}
