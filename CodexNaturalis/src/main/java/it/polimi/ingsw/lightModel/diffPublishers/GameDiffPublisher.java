package it.polimi.ingsw.lightModel.diffPublishers;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.GadgetGame;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCodex;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightHand;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightHandOthers;
import it.polimi.ingsw.lightModel.diffs.*;
import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Deck;
import it.polimi.ingsw.model.tableReleted.Game;

import java.rmi.RemoteException;
import java.util.*;

public class GameDiffPublisher {
    private final Map<DiffSubscriber, String> activeSubscribers;
    private final Game game;
    public GameDiffPublisher(Game game) {
        this.activeSubscribers = new HashMap<>();
        this.game = game;
    }

    public synchronized void subscribe(DiffSubscriber diffSubscriber, String nickname) {
        GameDiffPlayerActivity communicateJoin = new GameDiffPlayerActivity(List.of(nickname), new ArrayList<>());
        for(DiffSubscriber subscriber : activeSubscribers.keySet()){
            notifySubscriber(subscriber, communicateJoin);
        }
        activeSubscribers.put(diffSubscriber, nickname);
        List<GameDiff> yours = getTotalCurrentState(diffSubscriber);
        notifySubscriber(diffSubscriber, yours);

    }
    public synchronized void unsubscribe(DiffSubscriber diffSubscriber) {
        GameDiffPlayerActivity communicateLeave = new GameDiffPlayerActivity(new ArrayList<>(), List.of(activeSubscribers.get(diffSubscriber)));
        activeSubscribers.remove(diffSubscriber);
        for(DiffSubscriber subscriber : activeSubscribers.keySet()){
            notifySubscriber(subscriber, communicateLeave);
        }

        notifySubscriber(diffSubscriber, new GadgetGame());
    }

    public synchronized void subscribe(GameDiff diff) {
        for(DiffSubscriber diffSubscriber : activeSubscribers.keySet()){
            notifySubscriber(diffSubscriber, diff);
        }
    }
    public synchronized void subscribe(DiffSubscriber diffSubscriber, GameDiff playerGameDiff, GameDiff otherGameDiff){
        for(DiffSubscriber subscriber : activeSubscribers.keySet()) {
            if (activeSubscribers.get(diffSubscriber).equals(activeSubscribers.get(subscriber))) {
                 notifySubscriber(subscriber, playerGameDiff);
            } else {
                notifySubscriber(subscriber, otherGameDiff);
            }
        }
    }
    public synchronized void notifySubscriber(DiffSubscriber diffSubscriber, GameDiff gameDiff){
        try{
            diffSubscriber.updateGame(gameDiff);
        }catch (RemoteException r){
            r.printStackTrace();
        }
    }
    public synchronized void notifySubscriber(DiffSubscriber diffSubscriber, List<GameDiff> gameDiffs){
        for(GameDiff gameDiff : gameDiffs){
            notifySubscriber(diffSubscriber, gameDiff);
        }
    }
    private List<DeckDiff> getDeckCurrentState(){
        List<DeckDiff> deckDiff = new ArrayList<>();
        Deck<ResourceCard> resourceCardDeck = game.getResourceCardDeck();
        Deck<GoldCard> goldCardDeck = game.getGoldCardDeck();
        for(int i=0; i < resourceCardDeck.getBuffer().toArray().length; i++){
            deckDiff.add(new DeckDiffBufferDraw(Lightifier.lightifyToCard(resourceCardDeck.getBuffer().stream().toList().get(i)), i, DrawableCard.RESOURCECARD));
        }
        for(int i=0; i < goldCardDeck.getBuffer().toArray().length; i++){
            deckDiff.add(new DeckDiffBufferDraw(Lightifier.lightifyToCard(goldCardDeck.getBuffer().stream().toList().get(i)), i, DrawableCard.GOLDCARD));
        }
        deckDiff.add(new DeckDiffDeckDraw(DrawableCard.RESOURCECARD, Lightifier.lightifyToResource(resourceCardDeck.getActualDeck().stream().toList().getFirst())));
        deckDiff.add(new DeckDiffDeckDraw(DrawableCard.GOLDCARD, Lightifier.lightifyToResource(goldCardDeck.getActualDeck().stream().toList().getFirst())));
        return deckDiff;
    }
    private GameDiffPlayerActivity getPlayerActivity(){
        return new GameDiffPlayerActivity(activeSubscribers.values().stream().toList(), new ArrayList<>());
    }
    private List<CodexDiff> getCodexCurrentState(){
        List<CodexDiff> codexDiff = new ArrayList<>();
        for(String nickname : game.getGameParty().getUsersList().stream().map(User::getNickname).toList()){
            LightCodex codex = Lightifier.lightify(game.getGameParty().getUsersList().stream().filter(user -> user.getNickname().equals(nickname)).findFirst().get().getUserCodex());
            codexDiff.add(new CodexDiff(
                    nickname,
                    codex.getPoints(),
                    codex.getEarnedCollectables(),
                    codex.getPlacementHistory().values().stream().toList(),
                    codex.getFrontier().frontier()
            ));
        }
        return codexDiff;
    }
    private List<HandDiff> getHandCurrentState(DiffSubscriber diffSubscriber){
        List<HandDiff> handDiffAdd = new ArrayList<>();
        LightHand subscriberHand = Lightifier.lightifyYour(game.getGameParty()
                .getUsersList().stream()
                .filter(user->user.getNickname()
                        .equals(activeSubscribers.get(diffSubscriber)))
                .findFirst().get().getUserHand());
        for(LightCard card : subscriberHand.getCards()){
            handDiffAdd.add(new HandDiffAdd(card, subscriberHand.isPlayble(card)));
        }
        handDiffAdd.add(new HandDiffSetObj(subscriberHand.getSecretObjective()));
        return handDiffAdd;
    }
    private List<HandOtherDiff> getHandOtherCurrentState(DiffSubscriber diffSubscriber){
        List<HandOtherDiff> handOtherDiff = new ArrayList<>();
        for(String nickname : game.getGameParty().getUsersList().stream().map(User::getNickname).toList()){
            if(!nickname.equals(activeSubscribers.get(diffSubscriber))){
                LightHandOthers otherHand = Lightifier.lightifyOthers(game.getGameParty().getUsersList().stream().filter(user->user.getNickname().equals(nickname)).findFirst().get().getUserHand());
                for(Resource card : otherHand.getCards()){
                    handOtherDiff.add(new HandOtherDiffAdd(card, nickname));
                }
            }
        }
        return handOtherDiff;
    }
    private GameDiffPublicObj getPublicObjectiveCurrentState(DiffSubscriber diffSubscriber){
        LightCard[] publicObj = game.getObjectiveCardDeck().getBuffer().stream().map(Lightifier::lightifyToCard).toArray(LightCard[]::new);
        return new GameDiffPublicObj(publicObj);
    }
    private List<GameDiff> getTotalCurrentState(DiffSubscriber diffSubscriber){
        List<GameDiff> totalDiff = new ArrayList<>(getDeckCurrentState());
        totalDiff.add(getPlayerActivity());
        totalDiff.addAll(getCodexCurrentState());
        totalDiff.addAll(getHandCurrentState(diffSubscriber));
        totalDiff.addAll(getHandOtherCurrentState(diffSubscriber));
        totalDiff.add(getPublicObjectiveCurrentState(diffSubscriber));
        return totalDiff;
    }
}
