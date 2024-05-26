package it.polimi.ingsw.controller3.mediators.loggerAndUpdaterMediators;

import it.polimi.ingsw.controller3.LogsOnClientStatic;
import it.polimi.ingsw.lightModel.DiffGenerator;
import it.polimi.ingsw.lightModel.LightModelUpdaterInterfaces.LightGameUpdater;
import it.polimi.ingsw.lightModel.diffs.game.GameDiffPlayerActivity;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.view.ViewInterface;

import java.util.ArrayList;
import java.util.List;

public class GameMediator extends LoggerAndUpdaterMediator<LightGameUpdater, LightGame> {
    /**
     * This method is called when a player joins the game
     * it is added to the list of active players
     * the players already in the game are notified of the new player
     * the new player is notified with the current state of the game
     * all players are notified with a log of the event
     * @param nicknameJoin the nickname of the player who joined
     * @param LoggerAndUpdater the view of the player who joined
     * @param game the game the player joined
     * @param firstTime true if the player is joining for the first time, false if he is rejoining
     */
    public synchronized void subscribe(String nicknameJoin, ViewInterface LoggerAndUpdater, Game game, boolean firstTime){
        GameDiffPlayerActivity communicateJoin = new GameDiffPlayerActivity(List.of(nicknameJoin), new ArrayList<>());
        for(String subscriberNick : subscribers.keySet()){
            if(!subscriberNick.equals(nicknameJoin)) {
                try {
                    subscribers.get(subscriberNick).first().updateGame(communicateJoin);
                    if(!firstTime){
                        subscribers.get(subscriberNick).second().log(nicknameJoin + LogsOnClientStatic.PLAYER_REJOINED);
                    }
                } catch (Exception e) {
                    System.out.println("GameMediator: subscriber " + subscriberNick + " not reachable" + e.getMessage());
                }
            }
        }
        List<String> activePlayers = new ArrayList<>(subscribers.keySet().stream().toList());
        activePlayers.add(nicknameJoin);
        try {
            if (firstTime) {
                LoggerAndUpdater.updateGame(DiffGenerator.diffFirstTimeJoin(game, nicknameJoin, activePlayers));
                LoggerAndUpdater.log(LogsOnClientStatic.NEW_GAME_JOINED);
            }else{
                LoggerAndUpdater.updateGame(DiffGenerator.diffRejoin(game, nicknameJoin, activePlayers));
                LoggerAndUpdater.log(LogsOnClientStatic.MID_GAME_JOINED);
            }
        }catch (Exception e){
            System.out.println("GameMediator: new subscriber " + nicknameJoin + " not reachable" + e.getMessage());
        }
        super.subscribe(nicknameJoin, LoggerAndUpdater, LoggerAndUpdater);
    }

    public synchronized void unsubscribe(String nickname){


        super.unsubscribe(nickname);
    }
}
