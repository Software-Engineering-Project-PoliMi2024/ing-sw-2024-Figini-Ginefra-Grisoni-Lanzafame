package it.polimi.ingsw.controller4;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.controller3.LogsOnClientStatic;
import it.polimi.ingsw.controller4.Interfaces.GameControllerInterface;
import it.polimi.ingsw.lightModel.DiffGenerator;
import it.polimi.ingsw.lightModel.Heavifier;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffs.game.CodexDiffSetFinalPoints;
import it.polimi.ingsw.lightModel.diffs.game.GameDiffPublicObj;
import it.polimi.ingsw.lightModel.diffs.game.GameDiffWinner;
import it.polimi.ingsw.lightModel.diffs.game.codexDiffs.CodexDiffPlacement;
import it.polimi.ingsw.lightModel.diffs.game.deckDiffs.DeckDiffDeckDraw;
import it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs.GameDiffCurrentPlayer;
import it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs.GameDiffPlayerActivity;
import it.polimi.ingsw.lightModel.diffs.game.handDiffOther.HandOtherDiff;
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
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.utilities.Pair;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.util.*;
import java.util.function.Predicate;

public class GameController implements GameControllerInterface {
    private final CardTable cardTable;
    private final Game game;
    private final Map<String, ViewInterface> playerViewMap = new HashMap<>();

    public GameController(Game game, CardTable cardTable){
        this.game = game;
        this.cardTable = cardTable;
    }

    public synchronized Map<String, ViewInterface> getPlayerViewMap(){
        return new HashMap<>(playerViewMap);
    }
    public synchronized List<String> getGamePlayers(){
        return game.getUsersList().stream().map(User::getNickname).toList();
    }

    //TODO test deck (when drawing all cards it remains a card)
    //TODO test when the decks finish the cards

    public synchronized void join(String joinerNickname, ViewInterface view){
        playerViewMap.put(joinerNickname, view);

        if(!isCurrentPlayerActive()){
            int currentPlayerIndex = game.getUsersList().indexOf(game.getUserFromNick(joinerNickname));
            game.setCurrentPlayerIndex(currentPlayerIndex);
        }

        if(game.isInStartCardState()) {
            this.notifyJoinGame(joinerNickname, false);
            this.updateJoinStartGame(joinerNickname);
            startCardStateTransition(joinerNickname);
        }else {
            notifyJoinGame(joinerNickname, true);
            if (game.inInSecretObjState()) {
                this.updateJoinSecretObjective(joinerNickname, game);
                this.objectiveChoiceStateTransition(joinerNickname);
            } else if (game.hasEnded()) {
                //this.updateJoinActualGame(joinerNickname, game); if other information are necessary
                try {
                    view.updateGame(new CodexDiffSetFinalPoints(game.getPointPerPlayerMap()));
                    view.updateGame(new GameDiffWinner(game.getWinners()));
                    view.transitionTo(ViewState.GAME_ENDING);
                }catch (Exception ignored){}
            } else {
                this.updateJoinActualGame(joinerNickname, game);
                this.takeTurn(joinerNickname);
            }
        }
    }

    private synchronized boolean isCurrentPlayerActive(){
        String currentPlayerNick = game.getCurrentPlayer().getNickname();
        return playerViewMap.containsKey(currentPlayerNick);
    }

    private synchronized void updateJoinStartGame(String joinerNickname){
        User user = game.getUserFromNick(joinerNickname);

        if (user.getUserHand().getStartCard() == null && !user.hasPlacedStartCard()) {
            StartCard startCard = game.drawStartCard();
            user.getUserHand().setStartCard(startCard);
        }
        this.updateJoinStartCard(joinerNickname);
    }

    private synchronized void startCardStateTransition(String nickname){
        ViewInterface view = playerViewMap.get(nickname);
        try {
            if (!game.getUserFromNick(nickname).hasPlacedStartCard())
                view.transitionTo(ViewState.CHOOSE_START_CARD);
            else
                view.transitionTo(ViewState.WAITING_STATE);
        }catch (Exception ignored){}
    }

    private synchronized void updateJoinSecretObjective(String joiner, Game game){
        List<String> activePlayers = new ArrayList<>(playerViewMap.keySet().stream().toList());
        try{
            playerViewMap.get(joiner).updateGame(DiffGenerator.diffJoinSecretObj(game, joiner, activePlayers));
            playerViewMap.get(joiner).logGame(LogsOnClientStatic.GAME_JOINED);
        } catch (Exception ignored) {}
    }

    private synchronized void objectiveChoiceStateTransition(String nickname){
        User user = game.getUserFromNick(nickname);
        try {
            if (!user.hasChosenObjective()) {
                playerViewMap.get(nickname).transitionTo(ViewState.SELECT_OBJECTIVE);
            } else
                playerViewMap.get(nickname).transitionTo(ViewState.WAITING_STATE);
        }catch (Exception ignored){}
    }

    private synchronized void updateJoinStartCard(String joiner){
        List<String> activePlayers = new ArrayList<>(playerViewMap.keySet().stream().toList());
        try {
            playerViewMap.get(joiner).updateGame(DiffGenerator.diffJoinStartCard(game, joiner, activePlayers));
            playerViewMap.get(joiner).logGame(LogsOnClientStatic.GAME_JOINED);
        }catch (Exception ignored){}
    }

    private synchronized void notifyJoinGame(String joinerNickname, boolean rejoining){
        GameDiffPlayerActivity communicateJoin = new GameDiffPlayerActivity(List.of(joinerNickname), new ArrayList<>());
        playerViewMap.forEach((nickname, view)->{
            if(!nickname.equals(joinerNickname)) {
                try {
                    view.updateGame(communicateJoin);
                    if(rejoining){
                        view.logOthers(joinerNickname + LogsOnClientStatic.PLAYER_REJOINED);
                    }
                } catch (Exception ignored) {}
            }
        });
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
        }else{
            try{playerViewMap.get(nickname).transitionTo(ViewState.WAITING_STATE);}catch (Exception e){}
        }
    }

    private synchronized void notifyStartCardFaceChoice(String placer, User user, LightPlacement placement, LightBack resourceBack, LightBack goldBack){
        playerViewMap.forEach((nickname, view)->{
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
            }catch (Exception ignored){}
        });
    }

    public synchronized void chooseSecretObjective(String nickname, LightCard lightObjChoice){
        ObjectiveCard objChoice = Heavifier.heavifyObjectCard(lightObjChoice, cardTable);
        User user = game.getUserFromNick(nickname);

        user.setSecretObjective(objChoice);
        this.notifySecretObjectiveChoice(nickname, lightObjChoice);

        if(otherHaveAllChosen(nickname)) {
            this.removeInactivePlayers(User::hasChosenObjective);
            this.notifySecretObjectiveSetup();
            for(String players : playerViewMap.keySet())
                this.takeTurn(players);
        }else{
            this.objectiveChoiceStateTransition(nickname);
        }
    }

    private synchronized void  notifySecretObjectiveChoice(String chooser, LightCard objChoice){
        playerViewMap.forEach((nickname, view)->{
            try {
                if(nickname.equals(chooser)) {
                    view.updateGame(new HandDiffSetObj(objChoice));
                    view.log(LogsOnClientStatic.YOU_CHOSE);
                    view.logGame(LogsOnClientStatic.WAIT_SECRET_OBJECTIVE);
                }else{
                    view.logOthers(chooser + LogsOnClientStatic.PLAYER_CHOSE);
                }
            } catch (Exception ignored) {}
        });
    }

    private synchronized void takeTurn(String nickname) {
        ViewInterface view = playerViewMap.get(nickname);
        try {
            if (game.getCurrentPlayer().getNickname().equals(nickname)) {
                view.transitionTo(ViewState.PLACE_CARD);
            } else {
                view.transitionTo(ViewState.IDLE);
            }
        } catch (Exception ignored) {}
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
        this.notifyPlacement(nickname, placement, user.getUserCodex(), FrontIdToPlayability);
        try{playerViewMap.get(nickname).transitionTo(ViewState.DRAW_CARD);}catch (Exception e){}
    }

    private synchronized void notifyPlacement(String placer, LightPlacement newPlacement, Codex placerCodex, Map<LightCard, Boolean> playability){
        playerViewMap.forEach((nickname, view)->{
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
            } catch (Exception ignored) {}
        });
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

            this.notifyDraw(nickname, deckType, cardID, Lightifier.lightifyToCard(drawnCard),
                    Lightifier.lightifyToCard(cardReplacement),
                    drawnCard.canBePlaced(user.getUserCodex()));
        }

        if(game.checkForChickenDinner() && !game.duringLastTurns()){
            game.startLastTurnsCounter();
            this.notifyLastTurn();
        }

        if(Objects.equals(nickname, getLastActivePlayer())){
            game.decrementLastTurnsCounter();
        }

        if(Objects.equals(this.getLastActivePlayer(), nickname) && game.hasEnded()){
            //model update with points
            game.addObjectivePoints();
            //notify
            this.notifyGameEnded(game.getPointPerPlayerMap(), game.getWinners());
        }else{
            //turn
            int nextPlayerIndex = game.getNextActivePlayerIndex();
            String nextPlayer = game.getUsersList().get(nextPlayerIndex).getNickname();
            if(nextPlayer.equals(nickname)){
                //TODO timer
            }else {
                game.setCurrentPlayerIndex(this.getNextActivePlayerIndex());
                this.notifyTurnChange(nextPlayer);
                this.takeTurn(nickname);
                this.takeTurn(nextPlayer);
            }
        }
    }

    private synchronized void notifyDraw(String drawerNickname, DrawableCard deckType, int pos, LightCard drawnCard, LightCard drawnReplace, boolean playability){
        playerViewMap.forEach((nickname, view)->{
            try {
                if (!nickname.equals(drawerNickname)) {
                    view.logOthers(drawerNickname + LogsOnClientStatic.PLAYER_DRAW);
                    LightBack backOfDrawnCard = new LightBack(drawnCard.idBack());
                    view.updateGame(new HandOtherDiffAdd(backOfDrawnCard, drawerNickname));
                } else {
                    view.log(LogsOnClientStatic.YOU_DRAW);
                    view.updateGame(new HandDiffAdd(drawnCard, playability));
                }
                view.updateGame(DiffGenerator.draw(deckType, pos, drawnReplace));
            } catch (Exception ignored) {}
        });
    }

    private synchronized String getLastActivePlayer(){
        List<String> activePlayers = playerViewMap.keySet().stream().toList();
        ArrayList<String> turnsOrder = new ArrayList<>(game.getUsersList().stream().map(User::getNickname).toList());
        Collections.reverse(turnsOrder);

        return turnsOrder.stream().filter(activePlayers::contains).findFirst().orElse(null);
    }

    private synchronized void notifyGameEnded(Map<String, Integer> pointsPerPlayerMap, List<String> ranking){
        playerViewMap.forEach((nickname, view)->{
            try{
                view.updateGame(new CodexDiffSetFinalPoints(pointsPerPlayerMap));
                view.updateGame(new GameDiffWinner(ranking));
                view.logGame(LogsOnClientStatic.GAME_END);
            }catch (Exception ignored){}
        });
    }

    private synchronized void notifyLastTurn(){
        playerViewMap.forEach((nickname, view)->{
            try {
                view.logGame(LogsOnClientStatic.LAST_TURN);
            }catch (Exception ignored){}
        });
    }

    private synchronized int getNextActivePlayerIndex() {
        List<String> userList = game.getUsersList().stream().map(User::getNickname).toList();
        List<String> activePlayer = playerViewMap.keySet().stream().toList();
        int currentPlayerIndex = game.getCurrentPlayerIndex();
        int nextPlayerIndex;
        do{
            nextPlayerIndex = (currentPlayerIndex + 1) % userList.size();
        }while (!activePlayer.contains(userList.get(nextPlayerIndex)));

        return nextPlayerIndex;
    }

    private synchronized void notifyTurnChange(String nextPlayer){
        playerViewMap.forEach((nickname, view)->{
            try {
                view.updateGame(new GameDiffCurrentPlayer(nextPlayer));
                if (!nickname.equals(nextPlayer)) {
                    view.logOthers(nextPlayer + LogsOnClientStatic.PLAYER_TURN);
                } else {
                    view.log(LogsOnClientStatic.YOUR_TURN);
                }
            } catch (Exception e) {
                System.out.println("GameController.notifyTurnChange: player " + nickname + " not reachable" + e.getMessage());
            }
        });
    }

    public synchronized void leave(String nickname){
        ViewInterface view = playerViewMap.get(nickname);
        playerViewMap.remove(nickname);
        User you = game.getUserFromNick(nickname);

        if (game.isInStartCardState()) {
            if(otherHaveAllSelected(nickname) && !you.hasPlacedStartCard()){
                this.removeInactivePlayers(User::hasPlacedStartCard);
                this.moveToSecretObjectivePhase();
            }
        } else if (game.inInSecretObjState()) {
            if (otherHaveAllChosen(nickname) && !you.hasChosenObjective()) {
                this.removeInactivePlayers(User::hasChosenObjective);

                String currentPlayer = game.getCurrentPlayer().getNickname();
                if(currentPlayer.equals(nickname)) {
                    game.setCurrentPlayerIndex(getNextActivePlayerIndex());
                }
                this.notifySecretObjectiveSetup();
                for(String players : playerViewMap.keySet())
                    this.takeTurn(players);
            }
        } else if(!game.hasEnded()) { //if the game is in the actual game phase
            if (game.getCurrentPlayer().getNickname().equals(nickname)) { //if current player leaves
                //check if the user has disconnected after placing
                if (you.getHandSize() < 3 && !game.areDeckEmpty()) {
                    DrawableCard deckType;
                    int pos;
                    CardInHand cardDrawn;
                    CardInHand cardReplacement;
                    do {
                        deckType = randomDeckType();
                        pos = randomDeckPosition();

                        Pair<CardInHand, CardInHand> cardDrawnAndReplacement = game.drawAndGetReplacement(deckType, pos);
                        cardDrawn = cardDrawnAndReplacement.first();
                        cardReplacement = cardDrawnAndReplacement.second();
                    } while (cardDrawn == null);

                    this.draw(nickname, this.randomDeckType(), this.randomDeckPosition());
                }
                //move on with the turns for the other players
                if(!this.playerViewMap.keySet().isEmpty()) {
                    int nextPlayerIndex = this.getNextActivePlayerIndex();
                    String nextPlayerNick = game.getUsersList().get(nextPlayerIndex).getNickname();
                    game.setCurrentPlayerIndex(nextPlayerIndex);
                    this.notifyTurnChange(nextPlayerNick);
                    this.takeTurn(nextPlayerNick);
                }//TODO else stop timer
            }
        }

        try {
            view.transitionTo(ViewState.LOGIN_FORM);
        }catch (Exception ignored){}
    }


    private synchronized void drawForAllSecretObjective(){
        for(User user : game.getUsersList()){
            this.drawObjectiveCard(user);
        }
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


        playerViewMap.forEach((nickname, view)->{
            try {
                view.logGame(LogsOnClientStatic.EVERYONE_PLACED_STARTCARD);
                view.transitionTo(ViewState.SELECT_OBJECTIVE);
            }catch (Exception ignored){}
        });
    }

    private synchronized void notifySecretObjectiveSetup() {
        playerViewMap.forEach((nickname, view) -> {
            try {
                List<ObjectiveCard> secretObjChoices = game.getUserFromNick(nickname).getUserHand().getSecretObjectiveChoices();
                List<LightCard> lightSecretObjChoices = secretObjChoices.stream().map(Lightifier::lightifyToCard).toList();
                for (LightCard secretObj : lightSecretObjChoices) {
                    view.updateGame(new HandDiffAddOneSecretObjectiveOption(secretObj));
                }
                for (HandOtherDiff handDiff : DiffGenerator.getHandOtherCurrentState(game, nickname)) {
                    view.updateGame(handDiff);
                }
                for (User user : game.getUsersList()) {
                    //update with the diff of the startCard placement
                    if (!user.getNickname().equals(nickname)) {
                        Placement startCardPlacement = user.getUserCodex().getPlacementAt(new Position(0, 0));
                        view.updateGame(DiffGenerator.placeCodexDiff(user.getNickname(), Lightifier.lightify(startCardPlacement), user.getUserCodex()));
                    }
                }
                view.updateGame(new GameDiffPublicObj(game.getCommonObjective().stream().map(Lightifier::lightifyToCard).toArray(LightCard[]::new)));
                view.logGame(LogsOnClientStatic.EVERYONE_CHOSE);
            } catch (Exception ignored) {}
        });
    }

    private synchronized void removeInactivePlayers(Predicate<User> check) {
        for (User user : game.getUsersList()) {
            List<String> activePlayer = playerViewMap.keySet().stream().toList();
            if (!activePlayer.contains(user.getNickname())) {
                if(check.test(user)){
                    game.removeUser(user.getNickname());
                }
            }
        }
    }

    private synchronized boolean otherHaveAllSelected(String nicknamePerspective){
        boolean allPlaced = true;
        List<String> activePlayer = playerViewMap.keySet().stream().toList();
        for (String nick : activePlayer) {
            if (!nick.equals(nicknamePerspective) && !game.getUserFromNick(nick).hasPlacedStartCard()) {
                allPlaced = false;
            }
        }
        return allPlaced;
    }

    public synchronized void updateJoinActualGame(String joiner, Game game){
        List<String> activePlayer = playerViewMap.keySet().stream().toList();
        try{
            ViewInterface joinerView = playerViewMap.get(joiner);
            joinerView.updateGame(DiffGenerator.diffJoinMidGame(game, joiner, activePlayer));
            joinerView.logGame(LogsOnClientStatic.GAME_JOINED);
        } catch (Exception ignored){}

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
