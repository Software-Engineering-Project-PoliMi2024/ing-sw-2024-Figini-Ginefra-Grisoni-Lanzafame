package it.polimi.ingsw.controller3.mediators.loggerAndUpdaterMediators;

import it.polimi.ingsw.controller3.LogsOnClientStatic;
import it.polimi.ingsw.lightModel.DiffGenerator;
import it.polimi.ingsw.lightModel.LightModelUpdaterInterfaces.LightGameUpdater;
import it.polimi.ingsw.lightModel.diffs.game.GameDiffPlayerActivity;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.GadgetGame;
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
     * @param rejoining false if the player is joining for the first time, true if he is rejoining
     */
    public synchronized void subscribe(String nicknameJoin, ViewInterface LoggerAndUpdater, Game game, boolean rejoining){
        GameDiffPlayerActivity communicateJoin = new GameDiffPlayerActivity(List.of(nicknameJoin), new ArrayList<>());
        for(String subscriberNick : subscribers.keySet()){
            if(!subscriberNick.equals(nicknameJoin)) {
                try {
                    subscribers.get(subscriberNick).first().updateGame(communicateJoin);
                    if(rejoining){
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
            if (rejoining) {
                LoggerAndUpdater.updateGame(DiffGenerator.diffRejoin(game, nicknameJoin, activePlayers));
                LoggerAndUpdater.log(LogsOnClientStatic.MID_GAME_JOINED);
            }else{
                LoggerAndUpdater.updateGame(DiffGenerator.diffFirstTimeJoin(game, nicknameJoin, activePlayers));
                LoggerAndUpdater.log(LogsOnClientStatic.NEW_GAME_JOINED);
            }
        }catch (Exception e){
            System.out.println("GameMediator: new subscriber " + nicknameJoin + " not reachable" + e.getMessage());
        }
        super.subscribe(nicknameJoin, LoggerAndUpdater, LoggerAndUpdater);
    }

    /**
     * unsubscribe a player from the activeTurnTakerMediator
     * removing them from the list of players that will receive notifications
     * @param nickname the nickname of the player that is unsubscribing
     */
    public synchronized void unsubscribe(String nickname){
        for(String subscriberNick : subscribers.keySet()){
            if(!subscriberNick.equals(nickname)) {
                try {
                    subscribers.get(subscriberNick).first().updateGame(DiffGenerator.diffRemoveUserFromGame(nickname));
                    subscribers.get(subscriberNick).second().log(nickname + LogsOnClientStatic.PLAYER_GAME_LEFT);
                } catch (Exception e) {
                    System.out.println("GameMediator: subscriber " + subscriberNick + " not reachable" + e.getMessage());
                }
            }
        }
        try {
            subscribers.get(nickname).first().updateGame(new GadgetGame());
            subscribers.get(nickname).second().log("You"+ LogsOnClientStatic.PLAYER_GAME_LEFT);
        }catch (Exception e){
            System.out.println("GameMediator: subscriber " + nickname + " not reachable" + e.getMessage());
        }
        super.unsubscribe(nickname);
    }

    /**
     * notify all players that the startCardFace selection phase has finished
     */
    public synchronized void notifyAllChoseStartCardFace(){
        for(String subscriberNick : subscribers.keySet()){
            try {
                subscribers.get(subscriberNick).second().log(LogsOnClientStatic.EVERYONE_PLACED_STARTCARD);
            } catch (Exception e) {
                System.out.println("GameMediator: subscriber " + subscriberNick + " not reachable" + e.getMessage());
            }
        }
    }

    /**
     * notify all players that the secretObjective selection phase has finished
     */
    public synchronized void notifyAllChoseSecretObjective(){
        for(String subscriberNick : subscribers.keySet()){
            try {
                subscribers.get(subscriberNick).second().log(LogsOnClientStatic.EVERYONE_CHOSE);
            } catch (Exception e) {
                System.out.println("GameMediator: subscriber " + subscriberNick + " not reachable" + e.getMessage());
            }
        }
    }
}
