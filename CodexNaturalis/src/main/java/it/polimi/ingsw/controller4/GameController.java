package it.polimi.ingsw.controller4;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.controller3.LogsOnClientStatic;
import it.polimi.ingsw.controller4.Interfaces.GameControllerInterface;
import it.polimi.ingsw.lightModel.DiffGenerator;
import it.polimi.ingsw.lightModel.Heavifier;
import it.polimi.ingsw.lightModel.LightModelUpdaterInterfaces.LightGameUpdater;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffs.game.GameDiffPublicObj;
import it.polimi.ingsw.lightModel.diffs.game.codexDiffs.CodexDiffPlacement;
import it.polimi.ingsw.lightModel.diffs.game.deckDiffs.DeckDiffDeckDraw;
import it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs.GameDiffPlayerActivity;
import it.polimi.ingsw.lightModel.diffs.game.handDiffOther.HandOtherDiffAdd;
import it.polimi.ingsw.lightModel.diffs.game.handDiffOther.HandOtherDiffRemove;
import it.polimi.ingsw.lightModel.diffs.game.handDiffs.*;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.cards.CardTable;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.utilities.Pair;
import it.polimi.ingsw.view.LoggerInterface;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GameController implements GameControllerInterface {
    private final CardTable cardTable;
    private final Game game;
    private final Map<String, ViewInterface> playerViewMap = new HashMap<>();

    public GameController(Game game, CardTable cardTable){
        this.game = game;
        this.cardTable = cardTable;
    }

    public synchronized void join(String joinerNickname, ViewInterface view){
        playerViewMap.put(joinerNickname, view);

        if(game.isInStartCardState()) {
            this.notifyJoinGame(joinerNickname, false);
            this.joinStartGame(joinerNickname);
        }else if(game.inInSecretObjState()){
            this.notifyJoinGame(joinerNickname, true);
            game.joinSecretObjective(joinerNickname, game);
            controller.chooseObjective();
        }else if(/*TODO check game ending*/){
        }else {
            notifyJoinGame(joinerNickname, true);
            game.joinMidGame(joinerNickname);
            controller.takeTurn();
        }
    }

    public void joinStartGame(String joinerNickname){
        User user = game.getUserFromNick(joinerNickname);
        ViewInterface view = playerViewMap.get(joinerNickname);

        if (user.getUserHand().getStartCard() == null && !user.hasPlacedStartCard()) {
            StartCard startCard = game.drawStartCard();
            user.getUserHand().setStartCard(startCard);
        }
        this.updateJoinStartCard(joinerNickname);
        System.out.println(joinerNickname + " joined the game");

        if(!user.hasPlacedStartCard())
            try{view.transitionTo(ViewState.CHOOSE_START_CARD);}catch (Exception e){}
        else
            try{view.transitionTo(ViewState.WAITING_STATE);}catch (Exception e){}
    }

    private synchronized void updateJoinStartCard(String joiner){
        List<String> activePlayers = new ArrayList<>(playerViewMap.keySet().stream().toList());
        try {
            playerViewMap.get(joiner).updateGame(DiffGenerator.diffJoinStartCard(game, joiner, activePlayers));
            playerViewMap.get(joiner).logGame(LogsOnClientStatic.GAME_JOINED);
        }catch (Exception e){}
    }

    private synchronized void notifyJoinGame(String joinerNickname, boolean rejoining){
        GameDiffPlayerActivity communicateJoin = new GameDiffPlayerActivity(List.of(joinerNickname), new ArrayList<>());
        for (Map.Entry<String, ViewInterface> playerViewMap : playerViewMap.entrySet()) {
            if(!playerViewMap.getKey().equals(joinerNickname)) {
                try {
                    playerViewMap.getValue().updateGame(communicateJoin);
                    if(rejoining){
                        playerViewMap.getValue().logOthers(joinerNickname + LogsOnClientStatic.PLAYER_REJOINED);
                    }
                } catch (Exception e) {
                    System.out.println("GameController.notifyJoinGame: subscriber " + playerViewMap.getKey() + " unreachable" + e.getMessage());
                }
            }
        }
    }

    public synchronized void placeStartCard(String nickname, Placement startCardPlacement){
        User user = game.getUserFromNick(nickname);
        user.placeStartCard(startCardPlacement);
        //model: add cards to hand
        CardInHand resourceDeck = null;
        for (int i = 0; i < 2; i++) {
            Pair<CardInHand, CardInHand> resourceDrawnAndReplacement = game.drawAndGetReplacement(DrawableCard.RESOURCECARD, Configs.actualDeckPos);
            user.getUserHand().addCard(resourceDrawnAndReplacement.first());
            resourceDeck = resourceDrawnAndReplacement.second();
        }
        Pair<CardInHand, CardInHand> goldDrawnAndReplacement = game.drawAndGetReplacement(DrawableCard.GOLDCARD, Configs.actualDeckPos);
        user.getUserHand().addCard(goldDrawnAndReplacement.first());
        //notify everyone and update my lightModel
        LightBack resourceBack = new LightBack(resourceDeck.getIdBack());
        LightBack goldBack = new LightBack(goldDrawnAndReplacement.second().getIdBack());

        this.notifyStartCardFaceChoice(nickname, user, Lightifier.lightify(startCardPlacement), resourceBack, goldBack);

        if(otherHaveAllSelected(nickname)){
            this.removeInactivePlayers(User::hasPlacedStartCard);
            this.moveToSecretObjectivePhase();
        }
    }

    private synchronized void notifyStartCardFaceChoice(String placer, User user, LightPlacement placement, LightBack resourceBack, LightBack goldBack){
        for(Map.Entry<String, ViewInterface> playerViewMap : playerViewMap.entrySet()){
            ViewInterface view = playerViewMap.getValue();
            String nickname = playerViewMap.getKey();
            try {
                view.updateGame(new DeckDiffDeckDraw(DrawableCard.RESOURCECARD, resourceBack));
                view.updateGame(new DeckDiffDeckDraw(DrawableCard.GOLDCARD, goldBack));
                if (nickname.equals(placer)) {
                    view.log(LogsOnClientStatic.YOU_PLACE_STARTCARD);
                    view.logGame(LogsOnClientStatic.WAIT_STARTCARD);
                    view.updateGame(new HandDiffRemove(placement.card()));
                    view.updateGame(new CodexDiffPlacement(placer, user.getUserCodex().getPoints(),
                            user.getUserCodex().getEarnedCollectables(), List.of(placement), user.getUserCodex().getFrontier().getFrontier()));
                    for(HandDiff handDiff : DiffGenerator.getHandYourCurrentState(user)){
                        view.updateGame(handDiff);
                    }
                }else {
                    view.logOthers(placer + LogsOnClientStatic.PLAYER_PLACE_STARTCARD);
                }
            }catch (Exception e){
                System.out.println("GameController.notifyStartCardFaceChoice: subscriber " + nickname + " unreachable" + e.getMessage());
            }
        }
    }

    //TODO continue
    public synchronized void chooseSecretObjective(String nickname, LightCard lightObjChoice){
        ObjectiveCard objChoice = Heavifier.heavifyObjectCard(lightObjChoice, cardTable);
        User user = game.getUserFromNick(nickname);

        user.setSecretObjective(objChoice);
        this.notifySecretObjectiveChoice(nickname, lightObjChoice);

        if(otherHaveAllChosen(nickname)) {
            this.removeInactivePlayers(User::hasChosenObjective);
            List<LightCard> lightCommonObj = game.getCommonObjective().stream().map(Lightifier::lightifyToCard).toList();
            this.notifyAllChoseSecretObjective(lightCommonObj);
            this.takeTurn();
        }
    }

    private synchronized void  notifySecretObjectiveChoice(String chooser, LightCard objChoice){
        for(Map.Entry<String, ViewInterface> playerViewMap : playerViewMap.entrySet()){
            ViewInterface playerView = playerViewMap.getValue();
            String playerNick = playerViewMap.getKey();
            try {
                if(playerNick.equals(chooser)) {
                    playerView.updateGame(new HandDiffSetObj(objChoice));
                    playerView.log(LogsOnClientStatic.YOU_CHOSE);
                    playerView.logGame(LogsOnClientStatic.WAIT_SECRET_OBJECTIVE);
                }else{
                    playerView.logOthers(chooser + LogsOnClientStatic.PLAYER_CHOSE);
                }
            } catch (Exception e) {
                System.out.println("GameController.notifySecretObjectiveChoice: subscriber " + playerNick + " unreachable" + e.getMessage());
            }
        }
    }

    private synchronized void notifyAllChoseSecretObjective(List<LightCard> commonObjectives){
        for(Map.Entry<String, ViewInterface> playerViewMap : playerViewMap.entrySet()){
            try {
                ViewInterface view = playerViewMap.getValue();
                view.updateGame(new GameDiffPublicObj(commonObjectives.toArray(new LightCard[0])));
                view.logGame(LogsOnClientStatic.EVERYONE_CHOSE);
            } catch (Exception e) {
                System.out.println("GameController.notifyAllChoseSecretObjective: subscriber " + playerViewMap.getKey() + " unreachable" + e.getMessage());
            }
        }
    }

    private synchronized void takeTurn(){
        for(Map.Entry<String, ViewInterface> playerViewMap : playerViewMap.entrySet()) {
            ViewInterface view = playerViewMap.getValue();
            String nickname = playerViewMap.getKey();
            if (game.getCurrentPlayer().getNickname().equals(nickname)) {
                try{view.transitionTo(ViewState.PLACE_CARD);}catch (Exception e){}
            } else {
                try{view.transitionTo(ViewState.IDLE);}catch (Exception e){}
            }
        }
    }

    public synchronized void place(String nickname, LightPlacement placement){
        User user = game.getUserFromNick(nickname);
        Codex codexBeforePlacement = new Codex(user.getUserCodex());
        user.playCard(Heavifier.heavify(placement, cardTable));
        Set<CardInHand> hand = user.getUserHand().getHand();
        Codex codexAfterPlacement = user.getUserCodex();
        //update playability
        Map<LightCard, Boolean> FrontIdToPlayability = new HashMap<>();
        for(CardInHand cardInHand: hand){
            boolean oldPlayability = cardInHand.canBePlaced(codexBeforePlacement);
            boolean newPlayability = cardInHand.canBePlaced(codexAfterPlacement);
            if(oldPlayability != newPlayability){
                FrontIdToPlayability.put(Lightifier.lightifyToCard(cardInHand), newPlayability);
            }
        }

        //notify everyone
        game.notifyPlacement(nickname, placement, user.getUserCodex(), FrontIdToPlayability);

    }

    private synchronized void notifyPlacement(String placer, LightPlacement newPlacement, Codex placerCodex, Map<LightCard, Boolean> playability){
        for(Map.Entry<String, ViewInterface> playerViewMap : playerViewMap.entrySet()){
            String nickname = playerViewMap.getKey();
            ViewInterface view = playerViewMap.getValue();
            try {
                view.updateGame(DiffGenerator.placeCodexDiff(placer, newPlacement, placerCodex));

                if(nickname.equals(placer)){
                    view.updateGame(new HandDiffRemove(newPlacement.card()));
                    for(LightCard card : playability.keySet())
                        view.updateGame(new HandDiffUpdatePlayability(card, playability.get(card)));
                    view.log(LogsOnClientStatic.YOU_PLACED);
                }else {
                    LightBack newPlacementBack = new LightBack(newPlacement.card().idBack());
                    view.updateGame(new HandOtherDiffRemove(newPlacementBack, placer));
                    view.logOthers(placer + LogsOnClientStatic.PLAYER_PLACED);
                }
            } catch (Exception e) {
                System.out.println("GameController.notifyPlacement: subscriber " + nickname + " unreachable" + e.getMessage());
            }
        }
    }

    public synchronized void draw(String nickname, DrawableCard deckType, int cardID){
        User user = game.getUserFromNick(nickname);

        if(!game.areDeckEmpty()) {
            CardInHand drawnCard;
            CardInHand cardReplacement;
            Pair<CardInHand, CardInHand> drawnAndReplacement = game.drawAndGetReplacement(deckType, cardID);
            drawnCard = drawnAndReplacement.first();
            cardReplacement = drawnAndReplacement.second();

            user.getUserHand().addCard(drawnCard);

            game.notifyDraw(deckType, cardID, Lightifier.lightifyToCard(drawnCard),
                    Lightifier.lightifyToCard(cardReplacement), nickname,
                    drawnCard.canBePlaced(user.getUserCodex()));
        }

        if(game.checkForChickenDinner() && Objects.equals(game.getFirstActivePlayer(), nickname) && !game.isLastTurn()){
            game.setLastTurn(true);
            game.notifyLastTurn();
        }

        if(Objects.equals(game.getLastActivePlayer(), nickname) && game.isLastTurn()){
            //model update with points
            game.addObjectivePoints();
            //notify
            game.notifyGameEnded(game.getPointPerPlayerMap(), game.getWinners());
        }else{
            //turn
            int nextPlayerIndex = game.getNextActivePlayerIndex();
            User nextPlayer = game.getUsersList().get(nextPlayerIndex);
            if(nextPlayer.getNickname().equals(nickname)){
                //TODO timer
            }else {
                game.setPlayerIndex(nextPlayerIndex);
                game.notifyTurn(game.getCurrentPlayer().getNickname());
            }
        }
    }
    /*
    public synchronized void leave(String nickname){
        game.unsubscribe(nickname);
        User you = game.getUserFromNick(nickname);

        if (game.isInStartCardState()) {

            if(otherHaveAllSelected(nickname) && !you.hasPlacedStartCard()){
                this.removeInactivePlayers(User::hasPlacedStartCard);
                moveToSecretObjectivePhase(game);
            }
        } else if (game.inInSecretObjState()) {
            if (otherHaveAllChosen(nickname) && !you.hasChosenObjective()) {
                this.removeInactivePlayers(User::hasChosenObjective);
                User currentUserPlayer = game.getCurrentPlayer();
                if(currentUserPlayer.getNickname().equals(nickname)) {
                    int nextPlayerIndex = game.getNextPlayerIndex();
                    String nextPlayerNick = game.getUsersList().get(nextPlayerIndex).getNickname();

                    game.setPlayerIndex(nextPlayerIndex);
                    game.notifyFirstTurn(nextPlayerNick);
                }
                game.notifyEndSetupStartActualGame();
            }
        } else if (!game.isInSetup()) { //if the game is in the actual game phase
            if (game.getCurrentPlayer().getNickname().equals(nickname)) { //if current player leaves
                //check if the user haven't placed

                //check if the user has disconnected after placing
                if (you.getHandSize() < 3) {
                    DrawableCard deckType;
                    int pos;
                    do {
                        deckType = randomDeckType();
                        pos = randomDeckPosition();

                        cardDrawn = drawnAndReplacement.first();
                        cardReplacement = drawnAndReplacement.second();
                    } while (cardDrawn == null);

                    this.draw(nickname, this.randomDeckType(), this.randomDeckPosition());
                }
                int nextPlayerIndex = game.getNextActivePlayerIndex();
                String nextPlayerNick = game.getPlayerFromIndex(nextPlayerIndex).getNickname();
                //move on with the turns for the other players
                game.setPlayerIndex(nextPlayerIndex);
                game.notifyTurn(nextPlayerNick);
            }
        } else //TODO if game is in EndGame state
            throw new IllegalStateException("Controller.leaveGame: Game is in an invalid state");
        //If the game is empty, remove it from the MultiGame

        if (gameToLeave.getGameLoopController().getActivePlayers().isEmpty()) {
            multiGame.removeGame(gameToLeave);
        }
    }*/


    private synchronized void drawForAllSecretObjective(){
        for(User user : game.getUsersList()){
            this.drawObjectiveCard(user);
        }
        game.secretObjectiveSetup();
    }

    private synchronized void drawObjectiveCard(User user){
        List<ObjectiveCard> objectiveCards = new ArrayList<>();
        for(int i=0;i<2;i++){
            objectiveCards.add(game.drawObjectiveCard());
        }
        user.getUserHand().setSecretObjectiveChoice(objectiveCards);
    }

    private synchronized void moveToSecretObjectivePhase(){
        this.drawForAllSecretObjective();

        for(ViewInterface playerView : playerViewMap.values()){
            try {
                playerView.logGame(LogsOnClientStatic.EVERYONE_PLACED_STARTCARD);
            } catch (Exception e) {}
        }

        for(Map.Entry<String, ViewInterface> playerViewMap : playerViewMap.entrySet()) {
            User user = game.getUserFromNick(playerViewMap.getKey());
            try {
                if (!user.hasChosenObjective()) {
                    playerViewMap.getValue().transitionTo(ViewState.SELECT_OBJECTIVE);
                } else
                    playerViewMap.getValue().transitionTo(ViewState.WAITING_STATE);
            }catch (Exception e){}
        }
    }

    private synchronized void removeInactivePlayers(Predicate<User> check) {
        for (User user : game.getUsersList()) {
            if (!game.getActivePlayers().contains(user.getNickname())) {
                if(check.test(user)){
                    game.removeUser(user.getNickname());
                }
            }
        }
    }

    private synchronized boolean otherHaveAllSelected(String nicknamePerspective){
        boolean allPlaced = true;
        for (String nick : game.getActivePlayers()) {
            if (!nick.equals(nicknamePerspective) && !game.getUserFromNick(nick).hasPlacedStartCard()) {
                allPlaced = false;
            }
        }
        return allPlaced;
    }

    private synchronized boolean otherHaveAllChosen(String nicknamePerspective){
        boolean allChosen = true;
            for (User user : game.getUsersList()) {
                if (!user.getNickname().equals(nicknamePerspective) && !user.hasChosenObjective()) {
                    allChosen = false;
                }
            }
        return allChosen;
    }

    /**
     * @return a random card from a non-empty deck. If all decks are empty return null
     */

    private DrawableCard randomDeckType(){
        Random random = new Random();
        int deckNumber = random.nextInt(2);
        return deckNumber == 0 ? DrawableCard.RESOURCECARD : DrawableCard.GOLDCARD;
    }

    private int randomDeckPosition(){
        Random random = new Random();
        return random.nextInt(3);
    }

}
