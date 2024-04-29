package it.polimi.ingsw.lightModel.diffLists;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCodex;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightHandOthers;
import it.polimi.ingsw.lightModel.diffs.*;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;

import java.util.*;

public class GameDiffPublisher implements DiffPublisherNick {
    private final Map<String, List<GameDiff>> gameDiffMap;
    private final Map<DiffSubscriber, String> activeSubscribers;
    private final LightGame lightGame;
    public GameDiffPublisher(LightGame lightGame) {
        this.gameDiffMap = new HashMap<>();
        this.activeSubscribers = new HashMap<>();
        this.lightGame = lightGame;
    }

    @Override
    public synchronized void subscribe(DiffSubscriber diffSubscriber, String nickname) {
        boolean firstTime = !gameDiffMap.containsKey(nickname);
        if(firstTime) {
            gameDiffMap.put(nickname, new ArrayList<>());
        }
        GameDiffPlayerActivity communicateJoin = new GameDiffPlayerActivity(List.of(nickname), new ArrayList<>());
        gameDiffMap.get(nickname).addAll(getTotalCurrentState(diffSubscriber));
        activeSubscribers.put(diffSubscriber, nickname);
        for(String nick : activeSubscribers.values()){
            gameDiffMap.get(nick).add(communicateJoin);
        }
        notifySubscriber();
    }
    @Override
    public synchronized void unsubscribe(DiffSubscriber diffSubscriber) {
        GameDiffPlayerActivity communicateLeave = new GameDiffPlayerActivity(new ArrayList<>(), List.of(activeSubscribers.get(diffSubscriber)));
        activeSubscribers.remove(diffSubscriber);
        for(DiffSubscriber subscriber : activeSubscribers.keySet())
            gameDiffMap.get(activeSubscribers.get(subscriber)).add(communicateLeave);
        notifySubscriber();
    }
    @Override
    public synchronized void notifySubscriber() {
        for (DiffSubscriber subscriber : activeSubscribers.keySet()) {
            for(GameDiff diff : gameDiffMap.get(activeSubscribers.get(subscriber))){
                subscriber.updateGame(diff);
                gameDiffMap.get(activeSubscribers.get(subscriber)).remove(diff);
            }
        }
    }

    public void notifySubscriber(DiffSubscriber diffSubscriber, GameDiff gameDiff) {

    }

    public void subscribe(GameDiff diffSubscriber) {

    }

    public void unsubscribe(GameDiff diffSubscriber) {

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
