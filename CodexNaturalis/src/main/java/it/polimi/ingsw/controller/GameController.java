package it.polimi.ingsw.controller;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.controller.Interfaces.FinishedGameDeleter;
import it.polimi.ingsw.controller.Interfaces.GameControllerInterface;
import it.polimi.ingsw.controller.persistence.PersistenceFactory;
import it.polimi.ingsw.lightModel.DiffGenerator;
import it.polimi.ingsw.lightModel.Heavifier;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffs.game.GameDiffPublicObj;
import it.polimi.ingsw.lightModel.diffs.game.GameDiffWinner;
import it.polimi.ingsw.lightModel.diffs.game.chatDiffs.ChatDiffs;
import it.polimi.ingsw.lightModel.diffs.game.codexDiffs.CodexDiffPlacement;
import it.polimi.ingsw.lightModel.diffs.game.codexDiffs.CodexDiffSetFinalPoints;
import it.polimi.ingsw.lightModel.diffs.game.deckDiffs.DeckDiff;
import it.polimi.ingsw.lightModel.diffs.game.deckDiffs.DeckDiffDeckDraw;
import it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs.GameDiffCurrentPlayer;
import it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs.GameDiffFirstPlayer;
import it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs.GameDiffPlayerActivity;
import it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs.GameDiffRemovePlayers;
import it.polimi.ingsw.lightModel.diffs.game.handDiffOther.HandOtherDiff;
import it.polimi.ingsw.lightModel.diffs.game.handDiffOther.HandOtherDiffAdd;
import it.polimi.ingsw.lightModel.diffs.game.handDiffOther.HandOtherDiffRemove;
import it.polimi.ingsw.lightModel.diffs.game.handDiffs.*;
import it.polimi.ingsw.lightModel.diffs.game.pawnChoiceDiffs.GameDiffSetPawns;
import it.polimi.ingsw.lightModel.diffs.game.pawnChoiceDiffs.GameDiffSetPlayerColor;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.GadgetGame;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.cards.CardTable;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.*;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.utilities.Pair;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.util.*;
import java.util.function.Predicate;

public class GameController implements GameControllerInterface {
    private final transient CardTable cardTable;
    private final FinishedGameDeleter finishedGameDeleter;
    private final Game game;
    private final Map<String, ViewInterface> playerViewMap = new HashMap<>();

    private transient Timer countdownTimer = null;

    public GameController(Game game, CardTable cardTable, FinishedGameDeleter finishedGameDeleter) {
        this.game = game;
        this.cardTable = cardTable;
        this.finishedGameDeleter = finishedGameDeleter;
        this.save();
    }

    public synchronized Map<String, ViewInterface> getPlayerViewMap() {
        return new HashMap<>(playerViewMap);
    }

    public synchronized List<String> getGamePlayers() {
        return game.getUsersList().stream().map(Player::getNickname).toList();
    }

    //TODO test deck (when drawing all cards it remains a card)
    //TODO test when the decks finish the cards
    //TODO separate leave from disconnect; game ending view call leaveGame
    public synchronized void join(String joinerNickname, ViewInterface view, boolean reconnected){
        if(game.hasEnded()){

        }
        this.resetLastPlayerTimer();
        playerViewMap.put(joinerNickname, view);

        this.notifyJoinGame(joinerNickname, reconnected);

        this.loadChat(joinerNickname, view);

        if (game.isInStartCardState()) {
            this.updateJoinStartCard(joinerNickname);
            startCardStateTransition(joinerNickname);
            if (playerViewMap.size() == 2 && reconnected) {
                String otherPlayer = playerViewMap.keySet().stream().filter(nick -> !nick.equals(joinerNickname)).findFirst().orElse(null);
                startCardStateTransition(otherPlayer);
            }
        } else if (game.inInSecretObjState()) {
            this.updateJoinSecretObjective(joinerNickname, game);
            this.objectiveChoiceStateTransition(joinerNickname);
            if (playerViewMap.size() == 2 && reconnected) {
                String otherPlayer = playerViewMap.keySet().stream().filter(nick -> !nick.equals(joinerNickname)).findFirst().orElse(null);
                objectiveChoiceStateTransition(otherPlayer);
            }
        } else if (game.isInPawnChoiceState()) {
            this.updateJoinPawnChoice(joinerNickname);
            this.pawnChoiceStateTransition(joinerNickname);
            if (playerViewMap.size() == 2 && reconnected){
                String otherPlayer = playerViewMap.keySet().stream().filter(nick -> !nick.equals(joinerNickname)).findFirst().orElse(null);
                pawnChoiceStateTransition(otherPlayer);
            }
        } else if (game.hasEnded() && game.getCurrentPlayer().getNickname().equals(getLastActivePlayer())) {

        } else {
            if (playerViewMap.size() == 2) {
                //TODO test this
                //set as current player the joining player
                game.setCurrentPlayerIndex(game.getUsersList().indexOf(game.getUserFromNick(joinerNickname)));
                this.notifyTurnChange(joinerNickname);
                String otherPlayer = playerViewMap.keySet().stream().filter(nick -> !nick.equals(joinerNickname)).findFirst().orElse(null);
                this.takeTurn(otherPlayer);
            }
            this.updateJoinActualGame(joinerNickname, game);
            this.takeTurn(joinerNickname);
        }

        if(playerViewMap.size() == 1 && reconnected){
            this.notifyLastInGameTimer();
            this.startLastPlayerTimer();
        }

    }

    @Override
    public synchronized void chooseSecretObjective(String nickname, LightCard lightObjChoice) {
        ObjectiveCard objChoice = Heavifier.heavifyObjectCard(lightObjChoice, cardTable);
        Player player = game.getUserFromNick(nickname);

        player.setSecretObjective(objChoice);
        this.notifySecretObjectiveChoice(nickname, lightObjChoice);

        if (otherHaveAllChosenObjective(nickname)) {
            this.removeInactivePlayers(Player::hasChosenObjective);
            this.notifyActualGameSetup();
            for (String players : playerViewMap.keySet())
                this.takeTurn(players);
        } else {
            this.objectiveChoiceStateTransition(nickname);
        }
    }

    @Override
    public synchronized void choosePawn(String nickname, PawnColors color) {
        Player player = game.getUserFromNick(nickname);
        ViewInterface view = playerViewMap.get(nickname);
        if (game.getPawnChoices().contains(color)) {
            player.setPawnColor(color);
            game.removeChoice(color);

            //model: add cards to hand
            CardInHand resourceDeck = null;
            for (int i = 0; i < 2; i++) {
                Pair<CardInHand, CardInHand> resourceDrawnAndReplacement = game.drawAndGetReplacement(DrawableCard.RESOURCECARD, Configs.actualDeckPos);
                player.getUserHand().addCard(resourceDrawnAndReplacement.first());
                resourceDeck = resourceDrawnAndReplacement.second();
            }
            Pair<CardInHand, CardInHand> goldDrawnAndReplacement = game.drawAndGetReplacement(DrawableCard.GOLDCARD, Configs.actualDeckPos);
            player.getUserHand().addCard(goldDrawnAndReplacement.first());
            //notify everyone and update my lightModel
            LightBack resourceBack = new LightBack(resourceDeck.getIdBack());
            LightBack goldBack = new LightBack(goldDrawnAndReplacement.second().getIdBack());

            this.notifyPawnChoice(nickname, color, resourceBack, goldBack);

            if (otherHaveAllChoosePawn(nickname)) {
                game.getPawnChoices().clear();
                this.removeInactivePlayers(Player::hasChosenPawnColor);
                this.moveToSecretObjectivePhase();
            } else {
                this.pawnChoiceStateTransition(nickname);
            }
        } else {
            try {
                view.logGame(LogsOnClientStatic.PAWN_TAKEN);
                this.pawnChoiceStateTransition(nickname);
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void place(String nickname, LightPlacement placement) {
        Placement heavyPlacement;
        Player player = game.getUserFromNick(nickname);
        try {
            if (placement.position().equals(new Position(0, 0))) {
                heavyPlacement = Heavifier.heavifyStartCardPlacement(placement, cardTable);
                if (!player.getUserHand().getStartCard().equals(heavyPlacement.card())) {
                    throw new IllegalArgumentException("The card in the placement in position (0,0) is not the start card in hand");
                }

                this.placeStartCard(nickname, heavyPlacement);
            } else {
                heavyPlacement = Heavifier.heavify(placement, cardTable);
                List<Integer> handCardIds = player.getUserHand().getHand().stream().map(CardInHand::getIdFront).toList();
                if (!handCardIds.contains(heavyPlacement.card().getIdFront()) || !player.getUserCodex().getFrontier().isInFrontier(placement.position())) {
                    throw new IllegalArgumentException("The card in the placement is not in the hand or the position is not in the frontier");
                }

                this.placeCard(nickname, placement);
            }
            System.out.println(nickname + " placed card");
        } catch (Exception e) {
            throw new IllegalArgumentException("The placement is not valid " + e.getMessage());
        }
    }

    private synchronized void placeStartCard(String nickname, Placement startCardPlacement) {
        Player player = game.getUserFromNick(nickname);
        player.placeStartCard(startCardPlacement);

        this.notifyStartCardFaceChoice(nickname, Lightifier.lightify(startCardPlacement));

        if (otherHaveAllSelectedStartCard(nickname)) {
            this.removeInactivePlayers(Player::hasPlacedStartCard);
            this.moveToChoosePawn();

        } else {
            try {
                playerViewMap.get(nickname).transitionTo(ViewState.WAITING_STATE);
            } catch (Exception ignored) {
            }
        }

    }

    private synchronized void placeCard(String nickname, LightPlacement placement) {
        Player player = game.getUserFromNick(nickname);
        Codex codexBeforePlacement = new Codex(player.getUserCodex());
        CardInHand card = Heavifier.heavifyCardInHand(placement.card(), cardTable);
        if(!card.canBePlaced(codexBeforePlacement) && placement.face() == CardFace.FRONT) {
            try {
                playerViewMap.get(nickname).logErr(LogsOnClientStatic.CARD_NOT_PLACEABLE);
                playerViewMap.get(nickname).transitionTo(ViewState.PLACE_CARD);
            }catch (Exception ignored){}
        }else {
            player.playCard(Heavifier.heavify(placement, cardTable));
            Set<CardInHand> hand = player.getUserHand().getHand();
            Codex codexAfterPlacement = player.getUserCodex();
            //update playability
            Map<LightCard, Boolean> frontIdToPlayability = new HashMap<>();
            for (CardInHand cardInHand : hand) {
                boolean oldPlayability = cardInHand.canBePlaced(codexBeforePlacement);
                boolean newPlayability = cardInHand.canBePlaced(codexAfterPlacement);
                if (oldPlayability != newPlayability) {
                    frontIdToPlayability.put(Lightifier.lightifyToCard(cardInHand), newPlayability);
                }
            }

            //notify everyone
            this.notifyPlacement(nickname, placement, player.getUserCodex(), frontIdToPlayability);
            try {
                playerViewMap.get(nickname).transitionTo(ViewState.DRAW_CARD);
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public synchronized void draw(String nickname, DrawableCard deckType, int cardID) {
        Player player = game.getUserFromNick(nickname);
        System.out.println(nickname + " drew card");
        if (!game.areDeckEmpty()) {
            CardInHand drawnCard;
            Pair<CardInHand, CardInHand> drawnAndReplacement = game.drawAndGetReplacement(deckType, cardID);
            drawnCard = drawnAndReplacement.first();

            player.getUserHand().addCard(drawnCard);

            this.notifyDraw(nickname, deckType, cardID, Lightifier.lightifyToCard(drawnCard),
                    drawnCard.canBePlaced(player.getUserCodex()));
        }

        if (game.checkForChickenDinner() && !game.duringLastTurns()) {
            game.startLastTurnsCounter();
            this.notifyLastTurn();
        }

        if (game.duringLastTurns() && Objects.equals(nickname, getLastActivePlayer())) {
            game.decrementLastTurnsCounter();
        }

        if (Objects.equals(this.getLastActivePlayer(), nickname) && game.hasEnded()) {
            //model update with points
            declareWinners();
        } else {
            //turn
            int nextPlayerIndex = this.getNextActivePlayerIndex();
            String nextPlayer = game.getUsersList().get(nextPlayerIndex).getNickname();
            if (!nextPlayer.equals(nickname)) {
                game.setCurrentPlayerIndex(this.getNextActivePlayerIndex());
                this.notifyTurnChange(nextPlayer);
                this.takeTurn(nickname);
                this.takeTurn(nextPlayer);
            }
            this.save();
        }
    }

    public synchronized void leave(String nickname) {
        ViewInterface view = playerViewMap.get(nickname);
        playerViewMap.remove(nickname);
        Player you = game.getUserFromNick(nickname);

        this.notifyGameLeft(nickname);

        if (game.isInStartCardState()) {
            if (otherHaveAllSelectedStartCard(nickname)) {
                this.removeInactivePlayers(Player::hasPlacedStartCard);
                this.moveToChoosePawn();
            }
        } else if (game.isInPawnChoiceState()) {
            if (otherHaveAllChoosePawn(nickname)) {
                game.getPawnChoices().clear();
                this.removeInactivePlayers(Player::hasChosenPawnColor);
                this.moveToSecretObjectivePhase();
            }
        } else if (game.inInSecretObjState()) {
            if (otherHaveAllChosenObjective(nickname) && !you.hasChosenObjective()) {
                this.removeInactivePlayers(Player::hasChosenObjective);

                String currentPlayer = game.getCurrentPlayer().getNickname();
                if (currentPlayer.equals(nickname)) {
                    game.setCurrentPlayerIndex(getNextActivePlayerIndex());
                }
                for (String players : playerViewMap.keySet())
                    this.takeTurn(players);
            }
        } else if (!game.hasEnded()) { //if the game is in the actual game phase
            if (game.getCurrentPlayer().getNickname().equals(nickname)) { //if current player leaves
                //check if the user has disconnected after placing
                if (you.getHandSize() < 3 && !game.areDeckEmpty()) {
                    DrawableCard deckType;
                    int pos;
                    CardInHand cardDrawn;
                    do {
                        deckType = randomDeckType();
                        pos = randomDeckPosition();
                        Pair<CardInHand, CardInHand> cardDrawnAndReplacement = game.drawAndGetReplacement(deckType, pos);
                        cardDrawn = cardDrawnAndReplacement.first();
                    } while (cardDrawn == null);

                    this.draw(nickname, this.randomDeckType(), this.randomDeckPosition());
                    this.notifyDraw(nickname, deckType, pos, Lightifier.lightifyToCard(cardDrawn) ,cardDrawn.canBePlaced(game.getUserFromNick(nickname).getUserCodex()));
                }
                //move on with the turns for the other players
                if (!this.playerViewMap.keySet().isEmpty()) {
                    int nextPlayerIndex = this.getNextActivePlayerIndex();
                    String nextPlayerNick = game.getUsersList().get(nextPlayerIndex).getNickname();
                    game.setCurrentPlayerIndex(nextPlayerIndex);
                    this.notifyTurnChange(nextPlayerNick);
                    this.takeTurn(nextPlayerNick);
                } else {
                    this.resetLastPlayerTimer();
                }
            }
        }else{
            finishedGameDeleter.deleteGame(game.getName());
        }

        if(playerViewMap.size() == 1) {
            this.notifyLastInGameTimer();
            this.startLastPlayerTimer();
        }

        if(playerViewMap.isEmpty()){
            this.resetLastPlayerTimer();
        }

        try {
            view.updateGame(new GadgetGame());
            view.transitionTo(ViewState.LOGIN_FORM);
        } catch (Exception ignored) {
        }
    }



    private synchronized void startCardStateTransition(String nickname) {
        ViewInterface view = playerViewMap.get(nickname);
        try {
            if (!game.getUserFromNick(nickname).hasPlacedStartCard())
                view.transitionTo(ViewState.CHOOSE_START_CARD);
            else
                view.transitionTo(ViewState.WAITING_STATE);
        } catch (Exception ignored) {
        }
    }

    private synchronized void updateJoinSecretObjective(String joiner, Game game) {
        List<String> activePlayers = new ArrayList<>(playerViewMap.keySet().stream().toList());
        try {
            playerViewMap.get(joiner).updateGame(DiffGenerator.updateJoinSecretObj(game, activePlayers, joiner));
            playerViewMap.get(joiner).logGame(LogsOnClientStatic.GAME_JOINED);
        } catch (Exception ignored) {
        }
    }

    private synchronized void objectiveChoiceStateTransition(String nickname) {
        Player player = game.getUserFromNick(nickname);
        try {
            if (!player.hasChosenObjective()) {
                playerViewMap.get(nickname).transitionTo(ViewState.SELECT_OBJECTIVE);
            } else
                playerViewMap.get(nickname).transitionTo(ViewState.WAITING_STATE);
        } catch (Exception ignored) {
        }
    }

    private synchronized void pawnChoiceStateTransition(String nickname) {
        Player player = game.getUserFromNick(nickname);
        try {
            if (!player.hasChosenPawnColor()) {
                playerViewMap.get(nickname).transitionTo(ViewState.CHOOSE_PAWN);
            } else
                playerViewMap.get(nickname).transitionTo(ViewState.WAITING_STATE);
        } catch (Exception ignored) {}
    }

    private synchronized void updateJoinPawnChoice(String joiner) {
        List<String> activePlayers = new ArrayList<>(playerViewMap.keySet().stream().toList());
        try {
            playerViewMap.get(joiner).updateGame(DiffGenerator.updateChosePawn(game, activePlayers, joiner));
            playerViewMap.get(joiner).logGame(LogsOnClientStatic.GAME_JOINED);
        } catch (Exception ignored) {
        }
    }

    private synchronized void updateJoinStartCard(String joiner) {
        List<String> activePlayers = new ArrayList<>(playerViewMap.keySet().stream().toList());
        try {
            playerViewMap.get(joiner).updateGame(DiffGenerator.updateJoinStartCard(game, activePlayers, joiner));
            playerViewMap.get(joiner).logGame(LogsOnClientStatic.GAME_JOINED);
        } catch (Exception ignored) {
        }
    }

    private synchronized void notifyJoinGame(String joinerNickname, boolean reconnected) {
        GameDiffPlayerActivity communicateJoin = new GameDiffPlayerActivity(List.of(joinerNickname), new ArrayList<>());
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            if (!nickname.equals(joinerNickname)) {
                try {
                    view.updateGame(communicateJoin);
                    if(reconnected) {
                        view.logOthers(joinerNickname + LogsOnClientStatic.PLAYER_JOINED);
                    }
                } catch (Exception ignored) {}
            }
        });
    }

    private synchronized void notifyGameLeft(String leaver) {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            if (!nickname.equals(leaver)) {
                try {
                    view.updateGame(new GameDiffPlayerActivity(new ArrayList<>(), List.of(leaver)));
                    view.logOthers(leaver + LogsOnClientStatic.PLAYER_GAME_LEFT);
                } catch (Exception ignored) {
                }
            }
        });
    }

    private synchronized void resetLastPlayerTimer(){
        if(countdownTimer != null){
            countdownTimer.cancel();
            countdownTimer = null;
            System.out.println(game.getName() + " stopped last player timer");
        }
    }

    private synchronized void startLastPlayerTimer() {
        countdownTimer = new Timer();
        String lastPlayerInGame = playerViewMap.keySet().stream().findFirst().orElse("");
        countdownTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                winsTheLastPlayerInGame(lastPlayerInGame);
            }
        }, Configs.lastInGameTimerSeconds * 1000L);
        System.out.println(game.getName() + " started last player timer");
    }

    private synchronized void winsTheLastPlayerInGame(String winner){
        game.addObjectivePoints();
        this.notifyGameEnded(game.getPointPerPlayerMap(), List.of(winner));

        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.transitionTo(ViewState.GAME_ENDING);
            } catch (Exception ignored) {
            }
        });
        System.out.println(game.getName() + " ended");
    }

    /**
     * @return a List containing the winner(s) of the game
     */
    public List<String> getWinners(){
        Map<String, Integer> pointsPerPlayer = game.getPointPerPlayerMap();
        //Calculate all possibleWinners player(s) who scored the max amount of points in the game
        int maxPoint = pointsPerPlayer.values().stream().max(Integer::compareTo).orElse(0);
        List<String> playerMaxPoint = new ArrayList<>(pointsPerPlayer.keySet().stream().filter(nick -> pointsPerPlayer.get(nick) == maxPoint).toList());
        //calculate the number of objective cards completed
        Map<String, Integer> objectiveCompleted = new HashMap<>();
        if(playerMaxPoint.size() > 1){
            game.getUsersList().forEach(user ->{
                if(playerMaxPoint.contains(user.getNickname())){
                    int completedObj = 0;
                    for(ObjectiveCard obj : game.getCommonObjective()){
                        if(obj.getPoints() != 0)
                            completedObj += obj.getPoints(user.getUserCodex()) / obj.getPoints();
                    }
                    if(user.getUserHand().getSecretObjective() != null) {
                        //TODO check if in actual game
                        if(user.getUserHand().getSecretObjective().getPoints() != 0)
                            completedObj += user.getUserHand().getSecretObjective().getPoints(user.getUserCodex()) / user.getUserHand().getSecretObjective().getPoints();
                        objectiveCompleted.put(user.getNickname(), completedObj);
                    }
                }
            });
            int maxObj = objectiveCompleted.values().stream().max(Integer::compareTo).orElse(0);
            List<String> playerMaxObj = objectiveCompleted.keySet().stream().filter(nick -> objectiveCompleted.get(nick) == maxObj).toList();

            //intersect the two lists to get the winner(s)
            playerMaxPoint.retainAll(playerMaxObj);
        }

        return playerMaxPoint;
    }

    private synchronized void notifyLastInGameTimer() {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.logGame(LogsOnClientStatic.LAST_PLAYER);
            } catch (Exception ignored) {
            }
        });
    }

    private synchronized void moveToChoosePawn() {
        this.notifyPawnChoiceSetup();

        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.logGame(LogsOnClientStatic.EVERYONE_CHOSE_PAWN);
                view.transitionTo(ViewState.CHOOSE_PAWN);
            } catch (Exception ignored) {
            }
        });
    }

    private synchronized void notifyPawnChoiceSetup() {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.updateGame(new GameDiffSetPawns(game.getPawnChoices()));
            } catch (Exception ignored) {
            }
        });
    }

    private synchronized void notifyPawnChoice(String chooser, PawnColors color, LightBack resourceBack, LightBack goldBack) {
        Player player = game.getUserFromNick(chooser);
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.updateGame(new GameDiffSetPawns(game.getPawnChoices()));
                view.updateGame(new GameDiffSetPlayerColor(chooser, color));

                view.updateGame(new DeckDiffDeckDraw(DrawableCard.RESOURCECARD, resourceBack));
                view.updateGame(new DeckDiffDeckDraw(DrawableCard.GOLDCARD, goldBack));

                if (nickname.equals(chooser)) {
                    view.log(LogsOnClientStatic.YOU_CHOSE_PAWN);
                    view.logGame(LogsOnClientStatic.WAIT_PAWN);

                    for (HandDiff handDiff : DiffGenerator.getHandYourCurrentState(player)) {
                        view.updateGame(handDiff);
                    }
                } else {
                    view.logOthers(chooser + LogsOnClientStatic.PLAYER_CHOSE_PAWN);
                }
            } catch (Exception ignored) {
            }
        });
    }

    private synchronized void notifyStartCardFaceChoice(String placer, LightPlacement placement) {
        Player player = game.getUserFromNick(placer);
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                if (nickname.equals(placer)) {
                    view.log(LogsOnClientStatic.YOU_PLACE_STARTCARD);
                    view.logGame(LogsOnClientStatic.WAIT_STARTCARD);
                    view.updateGame(new HandDiffRemove(placement.card()));
                    view.updateGame(new CodexDiffPlacement(placer, player.getUserCodex().getPoints(),
                            player.getUserCodex().getEarnedCollectables(), List.of(placement), player.getUserCodex().getFrontier().getFrontier()));
                } else {
                    view.logOthers(placer + LogsOnClientStatic.PLAYER_PLACE_STARTCARD);
                }
            } catch (Exception ignored) {
            }
        });
    }

    private synchronized boolean otherHaveAllChoosePawn(String nicknamePerspective) {
        boolean allChose = true;
        List<String> activePlayer = playerViewMap.keySet().stream().toList();
        for (String nick : activePlayer) {
            if (!nick.equals(nicknamePerspective) && !game.getUserFromNick(nick).hasChosenPawnColor()) {
                allChose = false;
            }
        }
        return allChose;
    }

    private synchronized void notifySecretObjectiveChoice(String chooser, LightCard objChoice) {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                if (nickname.equals(chooser)) {
                    view.updateGame(new HandDiffSetObj(objChoice));
                    view.log(LogsOnClientStatic.YOU_CHOSE_SECRET_OBJ);
                    view.logGame(LogsOnClientStatic.WAIT_SECRET_OBJECTIVE);
                } else {
                    view.logOthers(chooser + LogsOnClientStatic.PLAYER_CHOSE_SECRET_OBJ);
                }
            } catch (Exception ignored) {
            }
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
        } catch (Exception ignored) {
        }
    }

    private synchronized void notifyPlacement(String placer, LightPlacement newPlacement, Codex placerCodex, Map<LightCard, Boolean> playability) {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.updateGame(DiffGenerator.placeCodexDiff(placer, newPlacement, placerCodex));

                if (nickname.equals(placer)) {
                    view.updateGame(new HandDiffRemove(newPlacement.card()));
                    for (LightCard card : playability.keySet())
                        view.updateGame(new HandDiffUpdatePlayability(card, playability.get(card)));
                    view.log(LogsOnClientStatic.YOU_PLACED);
                } else {
                    LightBack newPlacementBack = new LightBack(newPlacement.card().idBack());
                    view.updateGame(new HandOtherDiffRemove(newPlacementBack, placer));
                    view.logOthers(placer + LogsOnClientStatic.PLAYER_PLACED);
                }
            } catch (Exception ignored) {
            }
        });
    }

    private void declareWinners() {
        game.addObjectivePoints();
        //notify
        this.notifyGameEnded(game.getPointPerPlayerMap(), this.getWinners());

        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.transitionTo(ViewState.GAME_ENDING);
            } catch (Exception ignored) {
            }
        });
        System.out.println(game.getName() + " ended");
    }

    public void save() {
        PersistenceFactory.save(game);
    }

    private synchronized void notifyDraw(String drawerNickname, DrawableCard deckType, int pos, LightCard drawnCard, boolean playability) {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                for(DeckDiff diff : DiffGenerator.draw(deckType, pos, game))
                    view.updateGame(diff);

                if (!nickname.equals(drawerNickname)) {
                    view.logOthers(drawerNickname + LogsOnClientStatic.PLAYER_DRAW);
                    LightBack backOfDrawnCard = new LightBack(drawnCard.idBack());
                    view.updateGame(new HandOtherDiffAdd(backOfDrawnCard, drawerNickname));
                } else {
                    view.log(LogsOnClientStatic.YOU_DRAW);
                    view.updateGame(new HandDiffAdd(drawnCard, playability));
                }
            } catch (Exception ignored) {
            }
        });
    }

    private synchronized String getLastActivePlayer() {
        List<String> activePlayers = playerViewMap.keySet().stream().toList();
        ArrayList<String> turnsOrder = new ArrayList<>(game.getUsersList().stream().map(Player::getNickname).toList());
        Collections.reverse(turnsOrder);

        return turnsOrder.stream().filter(activePlayers::contains).findFirst().orElse(null);
    }

    private synchronized void notifyGameEnded(Map<String, Integer> pointsPerPlayerMap, List<String> winners) {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.updateGame(new CodexDiffSetFinalPoints(pointsPerPlayerMap));
                view.updateGame(new GameDiffWinner(winners));
                view.logGame(LogsOnClientStatic.GAME_END);
            } catch (Exception ignored) {
            }
        });
    }

    private synchronized void notifyLastTurn() {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.logGame(LogsOnClientStatic.LAST_TURN);
            } catch (Exception ignored) {
            }
        });
    }

    private synchronized int getNextActivePlayerIndex() {
        List<String> userList = game.getUsersList().stream().map(Player::getNickname).toList();
        List<String> activePlayer = playerViewMap.keySet().stream().toList();
        int currentPlayerIndex = game.getCurrentPlayerIndex();
        int nextPlayerIndex;
        do {
            nextPlayerIndex = (currentPlayerIndex + 1) % userList.size();
        } while (!activePlayer.contains(userList.get(nextPlayerIndex)));

        return nextPlayerIndex;
    }

    private synchronized void notifyTurnChange(String nextPlayer) {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.updateGame(new GameDiffCurrentPlayer(nextPlayer));
                if (!nickname.equals(nextPlayer)) {
                    view.logOthers(nextPlayer + LogsOnClientStatic.PLAYER_TURN);
                } else {
                    view.log(LogsOnClientStatic.YOUR_TURN);
                }
            } catch (Exception ignored) {
            }
        });
    }

    private synchronized void drawObjectiveCard(Player player) {
        List<ObjectiveCard> objectiveCards = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            objectiveCards.add(game.drawObjectiveCard());
        }
        player.getUserHand().setSecretObjectiveChoice(objectiveCards);
    }

    private synchronized void moveToSecretObjectivePhase() {
        for (Player player : game.getUsersList()) {
            this.drawObjectiveCard(player);
        }

        this.notifySecretObjectiveSetup();

        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.logGame(LogsOnClientStatic.EVERYONE_PLACED_STARTCARD);
                view.transitionTo(ViewState.SELECT_OBJECTIVE);
            } catch (Exception ignored) {
            }
        });

    }

    private synchronized void notifySecretObjectiveSetup() {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                List<ObjectiveCard> secretObjChoices = game.getUserFromNick(nickname).getUserHand().getSecretObjectiveChoices();
                List<LightCard> lightSecretObjChoices = secretObjChoices.stream().map(Lightifier::lightifyToCard).toList();
                for (LightCard secretObj : lightSecretObjChoices) {
                    view.updateGame(new HandDiffAddOneSecretObjectiveOption(secretObj));
                }

                for (HandOtherDiff handDiff : DiffGenerator.getHandOtherCurrentState(game, nickname)) {
                    view.updateGame(handDiff);
                }
                for (Player player : game.getUsersList()) {
                    //update with the diff of the startCard placement
                    if (!player.getNickname().equals(nickname)) {
                        Placement startCardPlacement = player.getUserCodex().getPlacementAt(new Position(0, 0));
                        view.updateGame(DiffGenerator.placeCodexDiff(player.getNickname(), Lightifier.lightify(startCardPlacement), player.getUserCodex()));
                    }
                }
            } catch (Exception ignored) {
            }
        });
    }

    private synchronized void notifyActualGameSetup() {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.updateGame(new GameDiffPublicObj(game.getCommonObjective().stream().map(Lightifier::lightifyToCard).toArray(LightCard[]::new)));
                view.logGame(LogsOnClientStatic.EVERYONE_CHOSE_OBJ);
            } catch (Exception ignored) {
            }
        });
    }

    private synchronized void removeInactivePlayers(Predicate<Player> checkProperInactive) {
        List<String> activePlayer = playerViewMap.keySet().stream().toList();
        List<String> playersToRemove = new ArrayList<>();
        for (Player player : game.getUsersList()) {
            if (!activePlayer.contains(player.getNickname()) && !checkProperInactive.test(player)) {
                playersToRemove.add(player.getNickname());
            }
        }

        if (!playersToRemove.isEmpty()) {
            String oldFirstPlayer = game.getUsersList().getFirst().getNickname();
            for (String playerNick : playersToRemove) {
                game.removeUser(playerNick);
            }
            this.updateRemovedPlayers(playersToRemove);

            String newFirstPlayer = game.getUsersList().getFirst().getNickname();
            if (!oldFirstPlayer.equals(newFirstPlayer)) {
                this.notifyFirstPlayerChange(newFirstPlayer);
            }
        }
        this.save();
    }

    private synchronized void updateRemovedPlayers(List<String> playersToRemove) {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.updateGame(new GameDiffRemovePlayers(playersToRemove));
            } catch (Exception ignored) {
            }
        });
    }

    private synchronized void notifyFirstPlayerChange(String newFirstPlayer) {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.updateGame(new GameDiffFirstPlayer(newFirstPlayer));
            } catch (Exception ignored) {
            }
        });
    }

    private synchronized boolean otherHaveAllSelectedStartCard(String nicknamePerspective) {
        boolean allPlaced = true;
        List<String> activePlayer = playerViewMap.keySet().stream().toList();
        for (String nick : activePlayer) {
            if (!nick.equals(nicknamePerspective) && !game.getUserFromNick(nick).hasPlacedStartCard()) {
                allPlaced = false;
            }
        }
        return allPlaced;
    }

    public synchronized void updateJoinActualGame(String joiner, Game game) {
        List<String> activePlayers = playerViewMap.keySet().stream().toList();
        try {
            ViewInterface joinerView = playerViewMap.get(joiner);
            joinerView.updateGame(DiffGenerator.updateJoinActualGame(game, activePlayers, joiner));
            joinerView.logGame(LogsOnClientStatic.GAME_JOINED);
        } catch (Exception ignored) {
        }

    }

    private synchronized boolean otherHaveAllChosenObjective(String nicknamePerspective) {
        boolean allChosen = true;
        for (String nickname : playerViewMap.keySet()) {
            Player player = game.getUserFromNick(nickname);
            if (!player.getNickname().equals(nicknamePerspective) && !player.hasChosenObjective()) {
                allChosen = false;
            }
        }
        return allChosen;
    }

    private DrawableCard randomDeckType() {
        Random random = new Random();
        int deckNumber = random.nextInt(2);
        return deckNumber == 0 ? DrawableCard.RESOURCECARD : DrawableCard.GOLDCARD;
    }

    private int randomDeckPosition() {
        Random random = new Random();
        return random.nextInt(3);
    }

    public void sendChatMessage(ChatMessage chatMessage) {
        this.game.getGameParty().getChatManager().addMessage(chatMessage);
        if (chatMessage.getPrivacy() == ChatMessage.MessagePrivacy.PUBLIC) {
            this.updateGlobalChat(chatMessage);
        } else {
            this.updatePrivateChat(chatMessage);
        }
    }

    private void loadChat(String joinerNickname, ViewInterface view) {
        try{
            view.updateGame(new ChatDiffs(game.getGameParty().getChatManager().retrieveChat(joinerNickname)));
        }catch (Exception ignored){}
    }

    private synchronized void updateGlobalChat(ChatMessage chatMessage) {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.updateGame(new ChatDiffs(List.of(chatMessage)));
                if(chatMessage.getSender().equals(nickname)){
                    view.logChat(LogsOnClientStatic.SENT_PUBLIC_MESSAGE);
                }else{
                    view.logChat(LogsOnClientStatic.RECEIVED_PUBLIC_MESSAGE);
                }
            } catch (Exception ignored) {
            }
        });
    }

    private synchronized void updatePrivateChat(ChatMessage chatMessage) {
        try {
            playerViewMap.get(chatMessage.getSender()).updateGame(new ChatDiffs(List.of(chatMessage)));
            playerViewMap.get(chatMessage.getSender()).logChat(LogsOnClientStatic.SENT_PRIVATE_MESSAGE + chatMessage.getReceiver());

            playerViewMap.get(chatMessage.getReceiver()).updateGame(new ChatDiffs(List.of(chatMessage)));
            playerViewMap.get(chatMessage.getReceiver()).logChat(LogsOnClientStatic.RECEIVED_PRIVATE_MESSAGE + chatMessage.getSender());
        } catch (Exception ignored) {
        }
    }
}
