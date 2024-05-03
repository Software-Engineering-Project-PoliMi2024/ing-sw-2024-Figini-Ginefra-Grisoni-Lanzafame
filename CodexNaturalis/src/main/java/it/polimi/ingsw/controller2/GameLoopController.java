package it.polimi.ingsw.controller2;

import it.polimi.ingsw.lightModel.diffObserverInterface.DiffSubscriber;
import it.polimi.ingsw.view.ViewState;

import java.util.Map;

public class GameLoopController {
    private final Map<ServerModelController, String> players;

    GameLoopController(Map<ServerModelController, String> players) {
        this.players = players;
    }
    public void joinGame(ServerModelController serverController, String nickname){
        players.put(serverController, nickname);
        for(ServerModelController controller : players.keySet()){
            controller.log(LogsFromServer.NEW_GAME_JOINED);
            controller.transitionTo(ViewState.CHOOSE_START_CARD);
        }
    }
    public void leaveGame(ServerModelController serverController){
        players.remove(serverController);
    }
    public void drawCard(){

    }
    public void placeCard(){

    }
}
