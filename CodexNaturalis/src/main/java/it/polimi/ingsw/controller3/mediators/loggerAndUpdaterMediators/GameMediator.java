package it.polimi.ingsw.controller3.mediators.loggerAndUpdaterMediators;

import it.polimi.ingsw.controller3.LogsOnClientStatic;
import it.polimi.ingsw.lightModel.DiffGenerator;
import it.polimi.ingsw.lightModel.LightModelUpdaterInterfaces.LightGameUpdater;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffs.game.*;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.GadgetGame;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.view.LoggerInterface;
import it.polimi.ingsw.view.ViewInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameMediator extends LoggerAndUpdaterMediator<LightGameUpdater, LightGame> {
    /**
     * This method is called when a player joins the game
     * it is added to the list of active players
     * the players already in the game are notified of the new player
     * the new player is notified with the current state of the game
     * all players are notified with a log of the event
     * @param nicknameJoin the nickname of the player who joined
     * @param LoggerAndUpdater the view of the player who joined
     * @param rejoining false if the player is joining for the first time, true if he is rejoining
     */
    public synchronized void subscribe(String nicknameJoin, ViewInterface LoggerAndUpdater, boolean rejoining){
        GameDiffPlayerActivity communicateJoin = new GameDiffPlayerActivity(List.of(nicknameJoin), new ArrayList<>());
        for(String subscriberNick : subscribers.keySet()){
            if(!subscriberNick.equals(nicknameJoin)) {
                try {
                    subscribers.get(subscriberNick).first().updateGame(communicateJoin);
                    if(rejoining){
                        subscribers.get(subscriberNick).second().logOthers(nicknameJoin + LogsOnClientStatic.PLAYER_REJOINED);
                    }
                } catch (Exception e) {
                    System.out.println("GameMediator: subscriber " + subscriberNick + " not reachable" + e.getMessage());
                }
            }
        }
        super.subscribe(nicknameJoin, LoggerAndUpdater, LoggerAndUpdater);
    }

    public synchronized void updateJoinStartCard(String joiner, Game game){
        List<String> activePlayers = new ArrayList<>(subscribers.keySet().stream().toList());
        try {
            subscribers.get(joiner).first().updateGame(DiffGenerator.diffJoinStartCard(game, joiner, activePlayers));
            subscribers.get(joiner).second().logGame(LogsOnClientStatic.GAME_JOINED);
        } catch (Exception e){
            System.out.println("GameMediator: new subscriber " + joiner + " not reachable" + e.getMessage());
        }
    }

    public synchronized void updateJoinObjectiveSelect(String joiner, Game game){
        List<String> activePlayers = new ArrayList<>(subscribers.keySet().stream().toList());
        try{
            subscribers.get(joiner).first().updateGame(DiffGenerator.diffJoinSecretObj(game, joiner, activePlayers));
            subscribers.get(joiner).second().logGame(LogsOnClientStatic.GAME_JOINED);
        } catch (Exception e){
            System.out.println("GameMediator: rejoining subscriber " + joiner + " not reachable" + e.getMessage());
        }
    }

    public synchronized void updateJoinActualGame(String joiner, Game game){
        List<String> activePlayers = new ArrayList<>(subscribers.keySet().stream().toList());
        try{
            subscribers.get(joiner).first().updateGame(DiffGenerator.diffJoinMidGame(game, joiner, activePlayers));
            subscribers.get(joiner).second().logGame(LogsOnClientStatic.GAME_JOINED);
        } catch (Exception e){
            System.out.println("GameMediator: rejoining subscriber " + joiner + " not reachable" + e.getMessage());
        }

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
                    subscribers.get(subscriberNick).second().logOthers(nickname + LogsOnClientStatic.PLAYER_GAME_LEFT);
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
     * notify that a player has chosen a startCardFace
     * logs on the chooser and other player in game
     * update the lightModel of the chooser with the new startCardFace
     * update the lightModel of the chooser with the modified codex
     * update the lightModel of the chooser with the hand drawn
     * @param placer the nickname of the player that placed the card
     * @param user the user that placed the card
     * @param placement the placement of the startCard card
     */
    public synchronized void notifyStartCardFaceChoice(String placer, User user, LightPlacement placement){
        for(String subscriberNick : subscribers.keySet()){
            try {
                if (subscriberNick.equals(placer)) {
                    subscribers.get(subscriberNick).second().log(LogsOnClientStatic.YOU_PLACE_STARTCARD);
                    subscribers.get(subscriberNick).second().logGame(LogsOnClientStatic.WAIT_STARTCARD);
                    LightGameUpdater updater = subscribers.get(subscriberNick).first();
                    updater.updateGame(new HandDiffRemove(placement.card()));
                    updater.updateGame(new CodexDiff(placer, user.getUserCodex().getPoints(),
                            user.getUserCodex().getEarnedCollectables(), List.of(placement), user.getUserCodex().getFrontier().getFrontier()));
                    for(HandDiff handDiff : DiffGenerator.getHandYourCurrentState(user)){
                        updater.updateGame(handDiff);
                    }
                }else {
                    subscribers.get(subscriberNick).second().logOthers(placer + LogsOnClientStatic.PLAYER_PLACE_STARTCARD);
                }
            }catch (Exception e){
                System.out.println("GameMediator: subscriber " + subscriberNick + " not reachable" + e.getMessage());
            }
        }
    }
    /**
     * notify all players that the startCardFace selection phase has finished
     */
    public synchronized void notifyAllChoseStartCardFace(){
        for(String subscriberNick : subscribers.keySet()){
            try {
                subscribers.get(subscriberNick).second().logGame(LogsOnClientStatic.EVERYONE_PLACED_STARTCARD);
            } catch (Exception e) {
                System.out.println("GameMediator: subscriber " + subscriberNick + " not reachable" + e.getMessage());
            }
        }
    }

    public synchronized void secretObjectiveSetup(Game game){
        for(String subscriberNick : subscribers.keySet()){
            try {
                LightGameUpdater updater = subscribers.get(subscriberNick).first();
                List<ObjectiveCard> secretObjChoices = game.getUserFromNick(subscriberNick).getUserHand().getSecretObjectiveChoices();
                List<LightCard> lightSecretObjChoices = secretObjChoices.stream().map(Lightifier::lightifyToCard).toList();
                for(LightCard secretObj : lightSecretObjChoices){
                    updater.updateGame(new HandDiffAddOneSecretObjectiveOption(secretObj));
                }
                for(HandOtherDiff handDiff : DiffGenerator.getHandOtherCurrentState(game, subscriberNick)) {
                    updater.updateGame(handDiff);
                }
            } catch (Exception e) {
                System.out.println("GameMediator: subscriber " + subscriberNick + " not reachable" + e.getMessage());
            }
        }
    }

    public synchronized void notifySecretObjectiveChoice(String chooser, LightCard objCard){
        for(String nickname : subscribers.keySet()){
            try {
                LoggerInterface logger = subscribers.get(nickname).second();
                if(nickname.equals(chooser)) {
                    LightGameUpdater updater = subscribers.get(nickname).first();
                    updater.updateGame(new HandDiffRemove(objCard));
                    logger.log(LogsOnClientStatic.YOU_CHOSE);
                    logger.logGame(LogsOnClientStatic.WAIT_SECRET_OBJECTIVE);
                }else{
                    logger.logOthers(chooser + LogsOnClientStatic.PLAYER_CHOSE);
                }
            } catch (Exception e) {
                System.out.println("GameMediator: subscriber " + nickname + " not reachable" + e.getMessage());
            }
        }
    }
    /**
     * notify all players that the secretObjective selection phase has finished
     */
    public synchronized void notifyAllChoseSecretObjective(List<LightCard> commonObjectives){
        for(String subscriberNick : subscribers.keySet()){
            try {
                LoggerInterface logger = subscribers.get(subscriberNick).second();
                LightGameUpdater updater = subscribers.get(subscriberNick).first();
                updater.updateGame(new GameDiffPublicObj(commonObjectives.toArray(new LightCard[1])));
                logger.logGame(LogsOnClientStatic.EVERYONE_CHOSE);
            } catch (Exception e) {
                System.out.println("GameMediator: subscriber " + subscriberNick + " not reachable" + e.getMessage());
            }
        }
    }

    /**
     * notify all players the change of turn
     * @param playerNickname the nickname of the player that will play next
     */
    public synchronized void notifyTurnChange(String playerNickname){
        for(String subscriberNick : subscribers.keySet()){
            try {
                LightGameUpdater updater = subscribers.get(subscriberNick).first();
                updater.updateGame(new GameDiffCurrentPlayer(playerNickname));
                LoggerInterface logger = subscribers.get(subscriberNick).second();
                if (!subscriberNick.equals(playerNickname)) {
                    logger.logOthers(playerNickname + LogsOnClientStatic.PLAYER_TURN);
                } else {
                    logger.log(LogsOnClientStatic.YOUR_TURN);
                }
            } catch (Exception e) {
                System.out.println("GameMediator: subscriber " + subscriberNick + " not reachable" + e.getMessage());
            }
        }
    }

    public synchronized void notifyPlacement(String placer, LightPlacement newPlacement, Codex placerCodex, Map<LightCard, Boolean> playability){
        for(String subscriberNick : subscribers.keySet()){
            try {
                LightGameUpdater updater = subscribers.get(subscriberNick).first();
                LoggerInterface logger = subscribers.get(subscriberNick).second();

                updater.updateGame(DiffGenerator.placeCodexDiff(placer, newPlacement, placerCodex));

                if(subscriberNick.equals(placer)){
                    updater.updateGame(new HandDiffRemove(newPlacement.card()));
                    for(LightCard card : playability.keySet())
                        updater.updateGame(new HandDiffUpdatePlayability(card, playability.get(card)));
                    logger.log(LogsOnClientStatic.YOU_PLACED);
                }else {
                    LightBack newPlacementBack = new LightBack(newPlacement.card().idBack());
                    updater.updateGame(new HandOtherDiffRemove(newPlacementBack, placer));
                    logger.logOthers(placer + LogsOnClientStatic.PLAYER_PLACED);
                }
            } catch (Exception e) {
                System.out.println("GameMediator: subscriber " + subscriberNick + " not reachable" + e.getMessage());
            }
        }
    }

    /**
     * notify players someone has drawn a card
     * @param card the card drawn
     * @param pos the position of the card in the deck
     * @param deckType the type of deck the card was drawn from
     * @param drawerNickname the nickname of the player that drew the card
     */
    public synchronized void notifyDraw(String drawerNickname, DrawableCard deckType, int pos, LightCard card){
        for(String subscriberNick : subscribers.keySet()){
            try {
                if (!subscriberNick.equals(drawerNickname)) {
                    subscribers.get(subscriberNick).second().logOthers(drawerNickname + LogsOnClientStatic.PLAYER_DRAW);
                } else {
                    subscribers.get(subscriberNick).second().log(LogsOnClientStatic.YOU_DRAW);
                }
                subscribers.get(subscriberNick).first().updateGame(DiffGenerator.draw(deckType, pos, card));
            } catch (Exception e) {
                System.out.println("GameMediator: subscriber " + subscriberNick + " not reachable" + e.getMessage());
            }
        }
    }
}
