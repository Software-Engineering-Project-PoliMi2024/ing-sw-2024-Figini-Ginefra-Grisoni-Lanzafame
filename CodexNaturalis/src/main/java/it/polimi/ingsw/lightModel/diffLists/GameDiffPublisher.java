package it.polimi.ingsw.lightModel.diffLists;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.LightCodex;
import it.polimi.ingsw.lightModel.diffs.*;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;

import java.util.*;

public class GameDiffPublisher implements DiffPublisherDouble<GameDiff>{
    public final Map<String, List<GameDiff>> gameDiffMap;
    public final List<DiffSubscriber> activeSubscribers;
    public GameDiffPublisher() {
        this.gameDiffMap = new HashMap<>();
        this.activeSubscribers = new ArrayList<>();
    }

    @Override
    public void subscribe(DiffSubscriber diffSubscriber) {
        synchronized (activeSubscribers){
            activeSubscribers.add(diffSubscriber);
        }
        boolean firstTime = false;
        synchronized (gameDiffMap){
            if(!gameDiffMap.containsKey(diffSubscriber.getNickname())){
                firstTime = true;
            }
        }
        if(firstTime){
            //create the NewGameDiff as the subscriber is a new user
            NewGameDiff newGameDiff = new NewGameDiff(
                    diffSubscriber.getTableName(),
                    diffSubscriber.getGamePlayerList(),
                    diffSubscriber.getCurrentPlayer(),
                    diffSubscriber.getDeckMap()
            );
            // create the gameDiff for the new user and add the new game diff
            synchronized (gameDiffMap){
                gameDiffMap.put(diffSubscriber.getNickname(), new ArrayList<>());
                gameDiffMap.get(diffSubscriber.getNickname()).add(newGameDiff);
            }
            // get the current active player and create a diff for the new user
            // create a diff for the others that a player has joined
            GameDiffPlayerActivity activePlayers = null;
            List<String> imActive = new ArrayList<>();
            imActive.add(diffSubscriber.getCurrentPlayer());
            GameDiffPlayerActivity confirmActive = new GameDiffPlayerActivity(imActive, new ArrayList<>());
            synchronized (activeSubscribers){
                List<String> playersToSetActive = new ArrayList<>();
                for(DiffSubscriber subscriber : activeSubscribers){
                    playersToSetActive.add(diffSubscriber.getNickname());
                }
                //generate yours message to get all active user
                activePlayers = new GameDiffPlayerActivity(playersToSetActive, new ArrayList<>());
            }
            synchronized(gameDiffMap){
                for(String nickname : gameDiffMap.keySet()){
                    if(!nickname.equals(diffSubscriber.getNickname())){
                        gameDiffMap.get(nickname).add(confirmActive);
                    }
                }
            }
            synchronized (gameDiffMap){
                gameDiffMap.get(diffSubscriber.getNickname()).add(activePlayers);
            }

            notifySubscriber();
        }else{
            //TODO if the user disconnected and reaccessed the game
        }
    }

    @Override
    public void unsubscribe(DiffSubscriber diffSubscriber) {
        synchronized (activeSubscribers){
            activeSubscribers.remove(diffSubscriber);
        }
        synchronized (gameDiffMap){
            NewGameDiff currentGameState = new NewGameDiff(
                    diffSubscriber.getTableName(),
                    diffSubscriber.getGamePlayerList(),
                    diffSubscriber.getCurrentPlayer(),
                    diffSubscriber.getDeckMap()
            );
            List<CodexDiff> codexDiff = new ArrayList<>();
            for(String nickname : gameDiffMap.keySet()){
                LightCodex codex = diffSubscriber.getCodex(nickname);
                codexDiff.add(new CodexDiff(
                        nickname,
                        codex.getPoints(),
                        codex.getEarnedCollectables(),
                        new HashMap<Collectable, Integer>(),
                        codex.getPlacementHistory().values().stream().toList(),
                        codex.getFrontier().frontier(),
                        new ArrayList<>()
                ));
            }
            //TODO add the other diffs, from Hand onward in LightGame, remember Hand has secret obj

        }
    }

    @Override
    public void notifySubscriber() {

    }

    @Override
    public void notifySubscriber(DiffSubscriber diffSubscriber, GameDiff gameDiff) {

    }

    @Override
    public void subscribe(GameDiff diffSubscriber) {

    }

    @Override
    public void unsubscribe(GameDiff diffSubscriber) {

    }
}
