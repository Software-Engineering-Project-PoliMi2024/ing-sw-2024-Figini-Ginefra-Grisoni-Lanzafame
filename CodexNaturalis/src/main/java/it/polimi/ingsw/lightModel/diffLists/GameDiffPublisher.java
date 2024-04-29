package it.polimi.ingsw.lightModel.diffLists;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.LightCodex;
import it.polimi.ingsw.lightModel.LightGame;
import it.polimi.ingsw.lightModel.LightHandOthers;
import it.polimi.ingsw.lightModel.diffs.*;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.tableReleted.Game;

import java.util.*;

public class GameDiffPublisher implements DiffPublisher{
    private final Map<String, List<GameDiff>> gameDiffMap;
    private final List<DiffSubscriber> activeSubscribers;
    private final LightGame lightGame;
    public GameDiffPublisher(LightGame lightGame) {
        this.gameDiffMap = new HashMap<>();
        this.activeSubscribers = new ArrayList<>();
        this.lightGame = lightGame;
    }

    @Override
    public synchronized void subscribe(DiffSubscriber diffSubscriber) {
        boolean firstTime = !gameDiffMap.containsKey(diffSubscriber.getNickname());
        if(firstTime) {
            gameDiffMap.put(diffSubscriber.getNickname(), new ArrayList<>());
        }
        GameDiffPlayerActivity communicateJoin = new GameDiffPlayerActivity(List.of(diffSubscriber.getNickname()), new ArrayList<>());
        gameDiffMap.get(diffSubscriber.getNickname()).addAll(getTotalCurrentState(diffSubscriber));
        activeSubscribers.add(diffSubscriber);
        for(DiffSubscriber subscriber : activeSubscribers){
            gameDiffMap.get(subscriber.getNickname()).add(communicateJoin);
        }
        notifySubscriber();
    }
    @Override
    public synchronized void unsubscribe(DiffSubscriber diffSubscriber) {
        GameDiffPlayerActivity communicateLeave = new GameDiffPlayerActivity(new ArrayList<>(), List.of(diffSubscriber.getNickname()));
        activeSubscribers.remove(diffSubscriber);
        for(DiffSubscriber subscriber : activeSubscribers)
            gameDiffMap.get(subscriber.getNickname()).add(communicateLeave);
        notifySubscriber();
    }
    @Override
    public synchronized void notifySubscriber() {
        for (DiffSubscriber subscriber : activeSubscribers) {
            for(GameDiff diff : gameDiffMap.get(subscriber.getNickname())){
                subscriber.updateGame(diff);
                gameDiffMap.get(subscriber.getNickname()).remove(diff);
            }
        }
    }

    private NewGameDiff newGameDiffAdder(DiffSubscriber diffSubscriber){
        return new NewGameDiff(
                lightGame.getLightGameParty().getGameName(),
                lightGame.getLightGameParty().getPlayerActiveList(),
                lightGame.getLightGameParty().getCurrentPlayer(),
                lightGame.getDecks()
        );
    }
    private List<CodexDiff> getCodexCurrentState(DiffSubscriber diffSubscriber){
        List<CodexDiff> codexDiff = new ArrayList<>();
        for(String nickname : gameDiffMap.keySet()){
            LightCodex codex = lightGame.getCodexMap().get(nickname);
            codexDiff.add(new CodexDiff(
                    nickname,
                    codex.getPoints(),
                    codex.getEarnedCollectables(),
                    new HashMap<>(),
                    codex.getPlacementHistory().values().stream().toList(),
                    codex.getFrontier().frontier(),
                    new ArrayList<>()
            ));
        }
        return codexDiff;
    }
    private List<HandDiff> getHandCurrentState(DiffSubscriber diffSubscriber){
        List<HandDiff> handDiffAdd = new ArrayList<>();
        for(LightCard card : lightGame.getHand().getCards()){
            handDiffAdd.add(new HandDiffAdd(card, lightGame.getHand().isPlayble(card)));
        }
        handDiffAdd.add(new HandDiffSetObj(lightGame.getHand().getSecretObjective()));
        return handDiffAdd;
    }
    private List<HandOtherDiff> getHandOtherCurrentState(DiffSubscriber diffSubscriber){
        List<HandOtherDiff> handOtherDiff = new ArrayList<>();
        for(String nickname : gameDiffMap.keySet()){
            LightHandOthers otherHand = lightGame.getHandOthers().get(nickname);
            for(Resource card : otherHand.getCards()){
                handOtherDiff.add(new HandOtherDiffAdd(card, nickname));
            }
        }
        return handOtherDiff;
    }
    private GameDiffPublicObj getPublicObjectiveCurrentState(DiffSubscriber diffSubscriber){
        return new GameDiffPublicObj(lightGame.getPublicObjective());

    }
    private List<GameDiff> getTotalCurrentState(DiffSubscriber diffSubscriber){
        List<GameDiff> totalDiff = new ArrayList<>();
        totalDiff.add(newGameDiffAdder(diffSubscriber));
        totalDiff.addAll(getCodexCurrentState(diffSubscriber));
        totalDiff.addAll(getHandCurrentState(diffSubscriber));
        totalDiff.addAll(getHandOtherCurrentState(diffSubscriber));
        totalDiff.add(getPublicObjectiveCurrentState(diffSubscriber));
        return totalDiff;
    }
}
