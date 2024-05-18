package it.polimi.ingsw.lightModel.diffPublishers;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffs.game.*;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.GadgetGame;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCodex;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightHand;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightHandOthers;
import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Deck;
import it.polimi.ingsw.model.tableReleted.Game;

import java.io.Serializable;
import java.util.*;

public class GameDiffPublisher implements Serializable {
    private final Map<DiffSubscriberGame, String> activeSubscribers;
    private final Game game;
    public GameDiffPublisher(Game game) {
        this.activeSubscribers = new HashMap<>();
        this.game = game;
    }

    /**
     * Subscribes a new subscriber to the game
     * the subscriber will receive the current state of the game
     * the other players will receive the new subscriber nickname
     * @param diffSubscriber the subscriber to the game
     * @param nickname the nickname of the subscriber
     */
    public synchronized void subscribe(DiffSubscriber diffSubscriber, String nickname) {
        GameDiffPlayerActivity communicateJoin = new GameDiffPlayerActivity(List.of(nickname), new ArrayList<>());
        for(DiffSubscriberGame subscriber : activeSubscribers.keySet()){
            notifySubscriber(subscriber, communicateJoin);
        }
        activeSubscribers.put(diffSubscriber, nickname);
        notifySubscriber(diffSubscriber, getTotalCurrentState(diffSubscriber));

    }

    /**
     * Unsubscribes a subscriber from the gamePublisher
     * the unsubscribed subscriber will receive a diff that formats the subscriber saves
     * the other subscribers will receive a notification informing of the leaving subscriber
     * @param diffSubscriber the subscriber to unsubscribe
     */
    public synchronized void unsubscribe(DiffSubscriber diffSubscriber) {
        GameDiffPlayerActivity communicateLeave = new GameDiffPlayerActivity(new ArrayList<>(), List.of(activeSubscribers.get(diffSubscriber)));
        activeSubscribers.remove(diffSubscriber);
        for(DiffSubscriberGame subscriber : activeSubscribers.keySet()){
            notifySubscriber(subscriber, communicateLeave);
        }


    }

    /**
     * sends a diff to all the subscrnotifySubscriber(diffSubscriber, new GadgetGame());ibers of the gamePublisher
     * @param diff the diff to send
     */
    public synchronized void subscribe(GameDiff diff) {
        for(DiffSubscriberGame diffSubscriber : activeSubscribers.keySet()){
            notifySubscriber(diffSubscriber, diff);
        }
    }

    /**
     * sends two different diffs to the subscribers of the gamePublisher:
     * one to the subscriber given as parameter, the other to the other subscribers
     * @param diffSubscriber the subscriber to send the first diff
     * @param playerGameDiff the diff to send to the diffSubscriber given as parameter
     * @param otherGameDiff the diff to send to the other subscribers
     */
    public synchronized void subscribe(DiffSubscriber diffSubscriber, GameDiff playerGameDiff, GameDiff otherGameDiff){
        for(DiffSubscriberGame subscriber : activeSubscribers.keySet()) {
            if (activeSubscribers.get(diffSubscriber).equals(activeSubscribers.get(subscriber))) {
                 notifySubscriber(subscriber, playerGameDiff);
            } else {
                notifySubscriber(subscriber, otherGameDiff);
            }
        }
    }

    /**
     * sends a diff to a single subscriber
     * @param diffSubscriber the subscriber to send the diff
     * @param gameDiff the diff to send
     */
    public synchronized void notifySubscriber(DiffSubscriberGame diffSubscriber, GameDiff gameDiff){
        diffSubscriber.updateGame(gameDiff);
    }

    /**
     * @return a list of diffs containing the current state of the decks in the game
     */
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

    /**
     * @return a diff containing the current state of the players in the game
     */
    private GameDiffPlayerActivity getPlayerActivity(){
        return new GameDiffPlayerActivity(activeSubscribers.values().stream().toList(), new ArrayList<>());
    }

    /**
     * @return a list of diffs containing the current state of the codexes in the game
     */
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

    /**
     * @param diffSubscriber the subscriber to get the hand from
     * @return a list of diffs containing the current state of the hand of the subscriber
     */
    private List<HandDiff> getHandCurrentState(DiffSubscriber diffSubscriber){
        List<HandDiff> handDiffAdd = new ArrayList<>();
        User user = game.getGameParty()
                .getUsersList().stream()
                .filter(u->u.getNickname()
                        .equals(activeSubscribers.get(diffSubscriber)))
                .findFirst().orElse(null);
        if(user!=null) {
            LightHand subscriberHand = Lightifier.lightifyYour(user.getUserHand(), user);
            for (LightCard card : subscriberHand.getCards()) {
                if(card!=null)
                    handDiffAdd.add(new HandDiffAdd(card, subscriberHand.isPlayble(card)));
            }
            if(subscriberHand.getSecretObjective() !=null)
                handDiffAdd.add(new HandDiffSetObj(subscriberHand.getSecretObjective()));
        }
        return handDiffAdd;
    }

    /**
     * @param diffSubscriber the subscriber to which send the hand of the other players
     * @return a list of diffs containing the current state of the hand of the other players
     */
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

    /**
     * @return a diff containing the current state of the public objectives in the game
     */
    private GameDiffPublicObj getPublicObjectiveCurrentState(){
        LightCard[] publicObj = game.getCommonObjective().stream().map(Lightifier::lightifyToCard).toArray(LightCard[]::new);
        if(publicObj.length == 2)
            return new GameDiffPublicObj(publicObj);
        return new GameDiffPublicObj();
    }

    /**
     * @param diffSubscriber the subscriber from which perspective to get the total state of the game
     * @return a list of diffs containing the current state of the game
     */
    private GameDiffNewGame getTotalCurrentState(DiffSubscriber diffSubscriber){
        GameDiffNewGame newGameDiff = new GameDiffNewGame(
                new GameDiffInitialization(game.getGameParty().getUsersList().stream().map(User::getNickname).toList(), new GameDiffGameName(game.getName()),new GameDiffYourName(activeSubscribers.get(diffSubscriber)) ),
                getPlayerActivity(),
                getDeckCurrentState(),
                getCodexCurrentState(),
                getHandCurrentState(diffSubscriber),
                getHandOtherCurrentState(diffSubscriber),
                getPublicObjectiveCurrentState()
                );
        return newGameDiff;
    }
}
