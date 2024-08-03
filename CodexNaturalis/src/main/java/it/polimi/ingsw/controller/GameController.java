package it.polimi.ingsw.controller;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.controller.Interfaces.GameControllerInterface;
import it.polimi.ingsw.controller.Interfaces.GameList;
import it.polimi.ingsw.controller.Interfaces.MalevolentPlayerManager;
import it.polimi.ingsw.controller.persistence.PersistenceFactory;
import it.polimi.ingsw.lightModel.diffs.DiffGenerator;
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
import it.polimi.ingsw.model.tableReleted.GameState;
import it.polimi.ingsw.model.utilities.Pair;
import it.polimi.ingsw.utils.CardChecks;
import it.polimi.ingsw.utils.logger.LoggerLevel;
import it.polimi.ingsw.utils.logger.LoggerSources;
import it.polimi.ingsw.utils.logger.ServerLogger;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Predicate;

/**
 * This class is the controller of the game, it manages the game state and the players' actions
 * It is responsible for the game turns logic and the communication between the model and players,
 * and the communication between the model and the persistence layer
 */
public class GameController implements GameControllerInterface {
    /** the class used to map the cards' ids to the corresponding cards objects*/
    private final transient CardTable cardTable;
    /** the class responsible for managing the game list, used to remove the game if it ends*/
    private final transient GameList gameList;
    /** the class responsible for managing the persistence property, used to save periodically the game*/
    private final transient PersistenceFactory persistenceFactory;
    /** the class responsible for managing the malevolent players, used to handle the players who try to perform unauthorized actions*/
    private final transient MalevolentPlayerManager malevolentPlayerManager;
    /** the class responsible for logging the game events to the server log*/
    private final transient ServerLogger logger;

    /** the game object managed by this class containing the game state and the players' data*/
    private final Game game;
    /** the map containing the active players' nicknames and the corresponding view objects*/
    private final Map<String, ViewInterface> playerViewMap = new HashMap<>();
    /** the future object used to manage the last player in game timer that when expired, declares the last player in game as the winner*/
    private transient ScheduledFuture<?> lastInGameFuture = null;
    /** the executor used to manage the last player in game timer*/
    private transient ScheduledExecutorService countdownExecutor = Executors.newSingleThreadScheduledExecutor();

    /**
     * The constructor of the class GameController creates a new GameController object
     * @param game the game object managed by this class containing the game state and the players' data
     * @param cardTable the cardTable object used to map the cards' ids to the corresponding cards objects
     * @param persistenceFactory the class responsible for managing the persistence property, used to save periodically the game
     * @param gameList the class that manages the game list, used to remove the game if it ends
     * @param malevolentPlayerManager the class that manage the consequences of the malevolent players
     */
    public GameController(Game game, CardTable cardTable, PersistenceFactory persistenceFactory, GameList gameList, MalevolentPlayerManager malevolentPlayerManager) {
        if(game.getState().equals(GameState.END_GAME)) {
            persistenceFactory.delete(game.getName());
            gameList.deleteGame(game.getName());
        }
        this.game = game;
        this.cardTable = cardTable;
        this.persistenceFactory = persistenceFactory;
        this.gameList = gameList;
        this.malevolentPlayerManager = malevolentPlayerManager;
        this.logger = new ServerLogger(LoggerSources.GAME_CONTROLLER, game.getName());
    }

    /**
     * Method used to get the active players map containing
     * their nicknames and the corresponding view objects
     * @return the active players map
     */
    public synchronized Map<String, ViewInterface> getPlayerViewMap() {
        return new HashMap<>(playerViewMap);
    }

    /**
     * Method used to get the list of nicknames of the players that are part of the game,
     * i.e. the players that can join the game
     * @return the list of nicknames of the players that are part of the game
     */
    public synchronized List<String> getGamePlayers() {
        return game.getPlayersList().stream().map(Player::getNickname).toList();
    }

    /**
     * Method used to join the game managed by this controller
     * if the player joining is no more part of the game, the player is moved to the login form
     * else it logs the join event on the server, saves the game state, updates the model,
     * updates the view of the joining player with the data corresponding with the current phase
     * of the game, and notifies the other players of the join event
     * If the player joining is the only player in the game, it starts the last player in game timer
     * Logs the event on the server
     * @param joinerNickname the nickname of the player joining the game
     * @param view the view object of the player joining the game
     * @param reconnected a boolean value that is true if the player is reconnected to the game,
     *                    false if is connecting for the first time
     */
    public synchronized void join(String joinerNickname, ViewInterface view, boolean reconnected){
        if(getGamePlayers().contains(joinerNickname) && !game.getState().equals(GameState.END_GAME)) {
            this.save();
            logger.log(LoggerLevel.INFO, joinerNickname + " joined the game");

            this.resetLastPlayerTimer();

            playerViewMap.put(joinerNickname, view);
            this.notifyJoinGame(joinerNickname, reconnected);


            if (game.getState().equals(GameState.CHOOSE_START_CARD)) {
                this.updateJoinStartCard(joinerNickname);
                startCardStateTransition(joinerNickname);
            } else if (game.getState().equals(GameState.CHOOSE_SECRET_OBJECTIVE)) {
                this.updateJoinSecretObjective(joinerNickname, game);
                this.objectiveChoiceStateTransition(joinerNickname);
            } else if (game.getState().equals(GameState.CHOOSE_PAWN)) {
                this.updateJoinPawnChoice(joinerNickname);
                this.pawnChoiceStateTransition(joinerNickname);
            } else {
                if (playerViewMap.size() == 2) {
                    String otherPlayerNick = playerViewMap.keySet().stream().filter(n -> !n.equals(joinerNickname)).toList().getFirst();
                    if (game.getPlayerFromNick(otherPlayerNick).getState().equals(PlayerState.WAIT)) {
                        //set as current player the joining player
                        game.setCurrentPlayerIndex(game.getPlayersList().indexOf(game.getPlayerFromNick(joinerNickname)));
                        this.notifyTurnChange(joinerNickname);
                        game.getPlayerFromNick(otherPlayerNick).setState(PlayerState.IDLE);
                        this.takeTurn(otherPlayerNick);
                    }
                }
                this.updateJoinActualGame(joinerNickname, game);
                this.takeTurn(joinerNickname);
            }

            this.loadChat(joinerNickname, view);
            if (playerViewMap.size() == 1 && reconnected) {
                this.startLastPlayerTimer();
            }

        }else{
            try {
                view.logErr(LogsOnClient.GAME_END);
                view.transitionTo(ViewState.LOGIN_FORM);
            } catch (Exception ignored) {}
        }
    }

    /**
     * Method used by the user to choose its pawn color
     * If the chosen color is not available, the player is notified and the prompted to re-choose a color
     * If the player is not in the correct state to choose a pawn,
     * the player the malevolent consequences are triggered
     * If the player chooses the black pawn, the player is marked as malevolent
     * Otherwise the model is updated with the chosen color, the player is moved to the wait state,
     * waiting for the other players to choose their pawn and game is saved on disk
     * Logs the event on the server
     * The chooser view and the other players are notified of the choice
     * If all the players have chosen their pawn, the game moves to the secret objective phase
     * @param nickname the nickname of the player choosing the pawn color
     * @param color the pawn color chosen by the player
     */
    @Override
    public synchronized void choosePawn(String nickname, PawnColors color) {
        Player player = game.getPlayerFromNick(nickname);

        if(player.getState().equals(PlayerState.CHOOSE_PAWN) && color != PawnColors.BLACK) {
            ViewInterface view = playerViewMap.get(nickname);
            if (game.getPawnChoices().contains(color)) {
                game.getPlayerFromNick(nickname).setState(PlayerState.WAIT);
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

                logger.log(LoggerLevel.INFO, nickname + " chose pawn");

                if (otherHaveAllChoosePawn(nickname)) {
                    game.getPawnChoices().clear();
                    this.removeInactivePlayers(p -> p.getState().equals(PlayerState.WAIT));
                    this.moveToSecretObjectivePhase();
                    this.save();
                } else {
                    moveToWait(nickname);
                }
            } else {
                try {
                    view.logErr(LogsOnClient.PAWN_TAKEN);
                    this.pawnChoiceStateTransition(nickname);
                } catch (Exception ignored) {
                }
            }
        }else{
            this.malevolentPlayer(nickname);
        }
    }

    /**
     * Method used to choose the secret objective card of the player
     * If the chosen card is not valid or the player isn't in the correct state,
     * the player is marked as malevolent
     * Otherwise the model is updated with the chosen card, the player is moved to the wait state,
     * the player choosing it is notified of the choice and its view updated,
     * and the other players are notified of the choice
     * If all the players have chosen their secret objective,
     * the game moves to the actual game phase and the game is saved on disk
     * Logs the event on the server
     * @param nickname the nickname of the player choosing the secret objective card
     * @param lightObjChoice the secret objective card chosen by the player
     */
    @Override
    public synchronized void chooseSecretObjective(String nickname, LightCard lightObjChoice) {
        Player player = game.getPlayerFromNick(nickname);
        ObjectiveCard objChoice;
        try{
            objChoice = Heavifier.heavifyObjectCard(lightObjChoice, cardTable);
        }catch (Exception e){
            this.malevolentPlayer(nickname);
            return;
        }

        if(player.getState().equals(PlayerState.CHOOSE_SECRET_OBJECTIVE)
                && CardChecks.objectiveCardsCheck.apply(lightObjChoice.idFront(), lightObjChoice.idBack())){

            game.getPlayerFromNick(nickname).setState(PlayerState.WAIT);

            player.setSecretObjective(objChoice);
            this.notifySecretObjectiveChoice(nickname, lightObjChoice);

            logger.log(LoggerLevel.INFO, nickname + " chose secret objective");
            if (otherHaveAllChosenObjective(nickname)) {
                this.removeInactivePlayers(p -> p.getState().equals(PlayerState.WAIT));
                this.moveToActualGame();
                this.save();
            } else {
                moveToWait(nickname);
            }
        }else{
            this.malevolentPlayer(nickname);
        }
    }

    /**
     * Method used to place the start card or the other card in hand by the player
     * If the card placed is not valid or its position isn't in the frontier the player is marked as malevolent
     * Otherwise it recognizes if the card placed is the start card or another card in hand and calls
     * the corresponding method to place the card
     * Logs the event on the server
     * @param nickname the nickname of the player placing the card
     * @param placement the placement of the card in the player codex,
     *                  specifying the card placed, its position
     *                  and the face shawn
     */
    @Override
    public void place(String nickname, LightPlacement placement) {
        Placement heavyPlacement;
        Player player = game.getPlayerFromNick(nickname);
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
        } catch (Exception e) {
            this.malevolentPlayer(nickname);
        }
    }

    /**
     * Method used to place the start card in the player codex at the stating position (0,0)
     * If the player isn't in the correct state to place the start card, the player is marked as malevolent
     * Otherwise the model is updated with the placed start card
     * and the views both of placing player and the others are updated
     * If all the players have placed their start card, the game moves to the choose pawn phase and saves the game on disk
     * Logs the event on the server
     * @param nickname the nickname of the player drawing the card
     * @param startCardPlacement the placement of the start card in the player codex,
     *                           specifying the card placed, its position (0,0) and the face shawn
     */
    private synchronized void placeStartCard(String nickname, Placement startCardPlacement) {
        Player player = game.getPlayerFromNick(nickname);
        if(player.getState().equals(PlayerState.CHOOSE_START_CARD) &&
                CardChecks.startCardCheck.apply(startCardPlacement.card().getIdFront(), startCardPlacement.card().getIdBack())){

            player.placeStartCard(startCardPlacement);
            game.getPlayerFromNick(nickname).setState(PlayerState.WAIT);

            this.notifyStartCardFaceChoice(nickname, Lightifier.lightify(startCardPlacement));

            logger.log(LoggerLevel.INFO, nickname + " placed start card");
            if (otherHaveAllSelectedStartCard(nickname)) {
                this.removeInactivePlayers(p -> p.getState().equals(PlayerState.WAIT));
                this.moveToChoosePawn();
                this.save();
            } else {
                moveToWait(nickname);
            }
        }else {
            this.malevolentPlayer(nickname);
        }
    }

    /**
     * Method used to place a card in the player codex at the specified position and face
     * If the player isn't in the correct state to place the card, the player is marked as malevolent
     * Otherwise the model is updated with the placed card, the views both of placing player and the others are updated
     * If the decks aren't empty, the player is moved to the draw state and the view is updated accordingly
     * If the decks are empty, the game moves on to the next player turn
     * If the ending conditions are met, the game moves to the last turns phase moving towards the end game
     * It save the game state on disk
     * Logs the event on the server
     * @param nickname the nickname of the player placing the card
     * @param placement the placement of the card in the player codex,
     *                  specifying the card placed, its position and the face shawn
     */
    private synchronized void placeCard(String nickname, LightPlacement placement) {
        Player player = game.getPlayerFromNick(nickname);

        if(player.getState().equals(PlayerState.PLACE) &&
                (CardChecks.resourceCardCheck.apply(placement.card().idFront(), placement.card().idBack())
                        || CardChecks.goldCardCheck.apply(placement.card().idFront(), placement.card().idBack()))){
            Codex codexBeforePlacement = new Codex(player.getUserCodex());
            CardInHand card = Heavifier.heavifyCardInHand(placement.card(), cardTable);
            if (!card.canBePlaced(codexBeforePlacement) && placement.face() == CardFace.FRONT) {
                try {
                    playerViewMap.get(nickname).logErr(LogsOnClient.CARD_NOT_PLACEABLE);
                    playerViewMap.get(nickname).transitionTo(ViewState.PLACE_CARD);
                } catch (Exception ignored) {
                }
            } else {
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

                logger.log(LoggerLevel.INFO, nickname + " placed card");
                if (!game.areDeckEmpty()) {
                    player.setState(PlayerState.DRAW);
                    try {
                        playerViewMap.get(nickname).transitionTo(ViewState.DRAW_CARD);
                    } catch (Exception ignored) {
                    }
                } else {
                    moveOnWithTurnsAndCheckForWinners(nickname, getLastActivePlayer());
                }
            }
        }else {
            this.malevolentPlayer(nickname);
        }
    }

    /**
     * Method used to draw a card from the specified deck in the specified position
     * If the player isn't in the correct state to draw the card, the player is marked as malevolent
     * If deck are not empty and the card is drawn, the model is updated with the drawn card,
     * and the view is updated accordingly with the drawn card in hand and the decks
     * After the draw the game moves on to the next player turn,
     * it checks if the ending conditions are met and if so moves to the last turns phase
     * It save the game state on disk
     * Logs the event on the server
     * @param nickname the nickname of the player drawing the card
     * @param deckType the deck from which the card is drawn (either Resource or Gold)
     * @param cardID the position of the card to draw (0,1 for the buffer, 2 for the deck)
     */
    @Override
    public synchronized void draw(String nickname, DrawableCard deckType, int cardID) {
        Player player = game.getPlayerFromNick(nickname);
        if(player.getState().equals(PlayerState.DRAW) && deckType != null && CardChecks.validDeckPosition.apply(cardID)){
            logger.log(LoggerLevel.INFO, nickname + " drew card");
            if (!game.areDeckEmpty()) {
                CardInHand drawnCard;
                Pair<CardInHand, CardInHand> drawnAndReplacement = game.drawAndGetReplacement(deckType, cardID);
                drawnCard = drawnAndReplacement.first();

                if(drawnCard != null) {
                    player.getUserHand().addCard(drawnCard);

                    this.notifyDraw(nickname, deckType, cardID, Lightifier.lightifyToCard(drawnCard),
                            drawnCard.canBePlaced(player.getUserCodex()));

                    moveOnWithTurnsAndCheckForWinners(nickname, getLastActivePlayer());
                }else {
                    try {
                        playerViewMap.get(nickname).transitionTo(ViewState.DRAW_CARD);
                    }catch (Exception ignored) {}
                    notifyEmptyDeckPosition(nickname);
                }
            }else{
                moveOnWithTurnsAndCheckForWinners(nickname, getLastActivePlayer());
            }
        }else {
            this.malevolentPlayer(nickname);
        }
    }

    /**
     * Method used by the player to leave the game managed by this controller
     * The player is removed from the active players map, and the leave is notified to the other players views
     * The view of the leaver is erased
     * If the player is leaving while the game is in setup (choose start card, choose pawn, choose secret objective),
     * the player leaving hasn't chosen yet while the other players have, the game moves on to the next phase
     * If the player is leaving during its turn the game turns moves on,
     * if the player should draw a card, the card is randomly drawn and the game moves on to the next player turn
     * If after the player leaves it remains only one player in the game, the last player in game timer is started
     * If the game is in the end game phase and the last player leaves, the game is deleted from the game list and from disk
     * If all the players leaves while the game isn't in the end game phase,
     * the game is saved and stored until reconnection or expiration defined by the gameSaveExpirationTimeMinutes
     * @param nickname the nickname of the player leaving the game
     */
    public synchronized void leave(String nickname) {
        ViewInterface view = playerViewMap.get(nickname);
        String lastActivePlayerPreDisconnect = this.getLastActivePlayer();
        playerViewMap.remove(nickname);
        Player leaver = game.getPlayerFromNick(nickname);

        if (playerViewMap.isEmpty()) {
            this.resetLastPlayerTimer();
        }else {

            this.notifyGameLeft(nickname);

            if (game.getState().equals(GameState.CHOOSE_START_CARD)) {
                if (otherHaveAllSelectedStartCard(nickname)) {
                    this.removeInactivePlayers(p->p.getState().equals(PlayerState.WAIT));
                    this.moveToChoosePawn();
                }
            } else if (game.getState().equals(GameState.CHOOSE_PAWN)) {
                if (otherHaveAllChoosePawn(nickname)) {
                    game.getPawnChoices().clear();
                    this.removeInactivePlayers(p->p.getState().equals(PlayerState.WAIT));
                    this.moveToSecretObjectivePhase();
                }
            } else if (game.getState().equals(GameState.CHOOSE_SECRET_OBJECTIVE)) {
                if (otherHaveAllChosenObjective(nickname)) {
                    game.setState(GameState.ACTUAL_GAME);
                    this.removeInactivePlayers(p->p.getState().equals(PlayerState.WAIT));

                    String currentPlayer = game.getCurrentPlayer().getNickname();
                    if (currentPlayer.equals(nickname)) {
                        game.setCurrentPlayerIndex(getNextActivePlayerIndex());
                    }
                    this.moveToActualGame();
                }
            } else if (!game.getState().equals(GameState.END_GAME)) { //if the game is in the actual game phase
                if (game.getCurrentPlayer().getNickname().equals(nickname)) { //if current player leaves
                    //check if the user has disconnected after placing
                    if (leaver.getHandSize() < 3 && !game.areDeckEmpty()) {
                        this.drawRandomCard(nickname);
                    }

                    this.moveOnWithTurnsAndCheckForWinners(nickname, lastActivePlayerPreDisconnect);
                }
            }
        }

        if(game.getState().equals(GameState.END_GAME) && playerViewMap.isEmpty()){
            persistenceFactory.delete(game.getName());
            gameList.deleteGame(game.getName());
        }else {
            this.save();
            if (playerViewMap.size() == 1 && !game.getState().equals(GameState.END_GAME)) {
                this.startLastPlayerTimer();
            }
        }

        try {
            view.updateGame(new GadgetGame());
        } catch (Exception ignored) {}
    }

    /**
     * This method is used to draw a random card from the decks and add it to the player hand
     * @param nickname the nickname of the player drawing the random card
     */
    private synchronized void drawRandomCard(String nickname){
        Player player = game.getPlayerFromNick(nickname);
        DrawableCard deckType;
        int pos;
        Pair<CardInHand, CardInHand> cardDrawnAndReplacement;
        do {
            deckType = randomDeckType();
            pos = randomDeckPosition();
            cardDrawnAndReplacement = game.drawAndGetReplacement(deckType, pos);
        } while (cardDrawnAndReplacement.first() == null);

        player.getUserHand().addCard(cardDrawnAndReplacement.first());

        this.notifyDraw(nickname, deckType, pos, Lightifier.lightifyToCard(cardDrawnAndReplacement.first()),
                cardDrawnAndReplacement.first().canBePlaced(player.getUserCodex()));
    }

    /**
     * Method used to move to the actual game phase
     * The game state is set to actual game, the player state are updated accordingly
     * with the current player (the current player state is set to place and the other players state is set to idle)
     * The players views are updated accordingly with the common objective cards
     * and the current player is prompted to place the first card
     */
    private synchronized void moveToActualGame(){
        game.setState(GameState.ACTUAL_GAME);
        this.notifyActualGameSetup();
        for (String playerNick : game.getPlayersList().stream().map(Player::getNickname).toList()) {
            Player playerFromNick = game.getPlayerFromNick(playerNick);
            if (playerNick.equals(game.getCurrentPlayer().getNickname())){
                playerFromNick.setState(PlayerState.PLACE);
            }else {
                playerFromNick.setState(PlayerState.IDLE);
            }
            this.takeTurn(playerNick);
        }
    }

    /**
     * Method used to move on with the turns of the players in the game
     * It calculate the next player turn and updates the current player index to the next player
     * It updates the current player's state to place and the other players' state to idle
     * and notifies the player's views of the turn change
     * If the player is alone in the game, it just moves the player to the wait state,
     * waiting for the another player to join
     * It checks if the game ending conditions are met and if so moves to the last turns phase, 
     * setting the last_turns state in the game
     * If the game is during the last turns, it decrements the ending turns counter
     * When ending turns counter reaches 0, moves the game to the end game phase
     * setting the end_game state in the game and declaring the winners of the game
     * @param nickname the nickname of the player that has ended its turn
     * @param lastActivePlayer the last player in turn that is active
     */
    private synchronized void moveOnWithTurnsAndCheckForWinners(String nickname, String lastActivePlayer) {
        this.save();
        if (game.checkForChickenDinner() && !game.duringEndingTurns()) {
            game.setState(GameState.LAST_TURNS);
            game.startEndingTurnsCounter();
            this.notifyLastTurn();
        }

        if (game.duringEndingTurns() && Objects.equals(nickname, lastActivePlayer)) {
            game.decrementEndingTurnsCounter();
        }
        if (Objects.equals(lastActivePlayer, nickname) && game.noMoreTurns()) {
            //model update with points
            declareWinners();
            moveToEndGame();
        } else if(!this.playerViewMap.isEmpty()){
            //turn
            int nextPlayerIndex = this.getNextActivePlayerIndex();
            String nextPlayer = game.getPlayersList().get(nextPlayerIndex).getNickname();
            if (!nextPlayer.equals(nickname)) {
                game.setCurrentPlayerIndex(nextPlayerIndex);
                game.getPlayerFromNick(nextPlayer).setState(PlayerState.PLACE);
                game.getPlayerFromNick(nickname).setState(PlayerState.IDLE);
                this.notifyTurnChange(nextPlayer);
                this.takeTurn(nickname);
                this.takeTurn(nextPlayer);
            }else{
                moveToWait(nickname);
            }
        }
    }

    /**
     * Method used to notify the view of the player that is trying to draw
     * from a deck position that doesn't contain any card
     * @param nickname the nickname of the player trying to draw from an empty deck position
     */
    private synchronized void notifyEmptyDeckPosition(String nickname){
        try {
            playerViewMap.get(nickname).logErr(LogsOnClient.EMPTY_DECK_POSITION);
        }catch (Exception ignored){}
    }

    /**
     * Method used to transition the player to the choose start card state 
     * if the player has already chosen the start card, it transitions the player to the wait state
     * otherwise it transitions the player to the choose start card state
     * @param nickname of the player whose view needs to be transitioned
     */
    private synchronized void startCardStateTransition(String nickname) {
        ViewInterface view = playerViewMap.get(nickname);
        Player player = game.getPlayerFromNick(nickname);
        try {
            if (player.getState().equals(PlayerState.CHOOSE_START_CARD)) {
                view.transitionTo(ViewState.CHOOSE_START_CARD);
            }else {
                moveToWait(nickname);
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Method used to update the view of the player that has joined the game in
     * the chooseSecretObjective state with the data corresponding
     * The view is notified with which player are active and inactive, all the 
     * startCard placed and all the pawn chosen by all the players, its hand,
     * the back of the hand of other players, the deck status 
     * and the secretObjectiveChoices if the player haven't chosen yet, or 
     * the secretObjective chosen by the player if it has already chosen
     * @param joiner the nickname of the player that has joined the game
     * @param game the game object containing the game state and the players' data
     */
    private synchronized void updateJoinSecretObjective(String joiner, Game game) {
        List<String> activePlayers = new ArrayList<>(playerViewMap.keySet().stream().toList());
        try {
            playerViewMap.get(joiner).updateGame(DiffGenerator.updateJoinSecretObj(game, activePlayers, joiner));
            playerViewMap.get(joiner).logGame(LogsOnClient.GAME_JOINED);
        } catch (Exception ignored) {
        }
    }

    /**
     * Method used to transition the player to the choose secret objective state 
     * if the player has already chosen the secret objective, 
     * it transitions the player to the wait state
     * otherwise it transitions the player to the choose secret objective state
     * @param nickname of the player whose view needs to be transitioned
     */
    private synchronized void objectiveChoiceStateTransition(String nickname) {
        Player player = game.getPlayerFromNick(nickname);
        try {
            if (player.getState().equals(PlayerState.CHOOSE_SECRET_OBJECTIVE)) {
                playerViewMap.get(nickname).transitionTo(ViewState.SELECT_OBJECTIVE);
            } else {
                moveToWait(nickname);
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Method used to transition the player to the choose pawn state 
     * if the player has already chosen the pawn, 
     * it transitions the player to the wait state
     * otherwise it transitions the player to the choose pawn objective state
     * @param nickname of the player whose view needs to be transitioned
     */
    private synchronized void pawnChoiceStateTransition(String nickname) {
        Player player = game.getPlayerFromNick(nickname);
        try {
            if (player.getState().equals(PlayerState.CHOOSE_PAWN)) {
                playerViewMap.get(nickname).transitionTo(ViewState.CHOOSE_PAWN);
            } else {
                moveToWait(nickname);
            }
        } catch (Exception ignored) {}
    }

    /**
     * Method used to update the view of the player that has joined the game in
     * the pawnChoice state with the data corresponding
     * The view is notified with which player are active and inactive, all the 
     * startCard placed and all the pawn chosen by others, its hand,
     * the back of the hand of other players, the deck status 
     * and the pawns not already chosen if the player haven't chosen yet, or 
     * just its pawn if it has already chosen
     * @param joiner the nickname of the player that has joined the game
     */
    private synchronized void updateJoinPawnChoice(String joiner) {
        List<String> activePlayers = new ArrayList<>(playerViewMap.keySet().stream().toList());
        try {
            playerViewMap.get(joiner).updateGame(DiffGenerator.updateChosePawn(game, activePlayers, joiner));
            playerViewMap.get(joiner).logGame(LogsOnClient.GAME_JOINED);
        } catch (Exception ignored) {
        }
    }

    /**
     Method used to update the view of the player that has joined the game in
     * the selectStartCardFace state with the data corresponding
     * The view is notified with which player are active and inactive, the 
     * startCard placed by others, the deck status 
     * and the start card of the player if the player haven't chosen yet, or 
     * the start card chosen by the player if it has already chosen
     * @param joiner the nickname of the player that has joined the game
     */
    private synchronized void updateJoinStartCard(String joiner) {
        List<String> activePlayers = new ArrayList<>(playerViewMap.keySet().stream().toList());
        try {
            playerViewMap.get(joiner).updateGame(DiffGenerator.updateJoinStartCard(game, activePlayers, joiner));
            playerViewMap.get(joiner).logGame(LogsOnClient.GAME_JOINED);
        } catch (Exception ignored) {
        }
    }
    
    /**
     * Method used to update the view of the player that has joined the game in
     * the actualGame state with the data corresponding
     * The view is notified with which player are active and inactive, all the 
     * placement history, its hand, the back of the hand of other players,
     * the deck status and the current player turn
     * @param joinerNickname the nickname of the player that has joined the game
     * @param reconnected a boolean value that is true if the player is reconnected to the game,
     *                    false if is connecting for the first time
     */
    private synchronized void notifyJoinGame(String joinerNickname, boolean reconnected) {
        GameDiffPlayerActivity communicateJoin = new GameDiffPlayerActivity(List.of(joinerNickname), new ArrayList<>());
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            if (!nickname.equals(joinerNickname)) {
                try {
                    view.updateGame(communicateJoin);
                    if(reconnected) {
                        view.logOthers(joinerNickname + LogsOnClient.PLAYER_JOINED);
                    }
                } catch (Exception ignored) {}
            }
        });
    }

    /**
     * Method used to notify the views of the leaving player
     * @param leaver the nickname of the player that has left the game
     */
    private synchronized void notifyGameLeft(String leaver) {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            if (!nickname.equals(leaver)) {
                try {
                    view.updateGame(new GameDiffPlayerActivity(new ArrayList<>(), List.of(leaver)));
                    view.logOthers(leaver + LogsOnClient.PLAYER_GAME_LEFT);
                } catch (Exception ignored) {}
            }
        });
    }

    /**
     * Method used to stop the lastPlayerInGameTimer if it is running
     * It resets the countdownExecutor
     * It logs the event on the server
     */
    private synchronized void resetLastPlayerTimer(){
        if(lastInGameFuture != null){
            lastInGameFuture.cancel(true);
            countdownExecutor.shutdownNow();
            countdownExecutor = Executors.newSingleThreadScheduledExecutor();
            lastInGameFuture = null;
            logger.log(LoggerLevel.INFO, "stopped last player timer");
            this.notifyLastInGameTimerStop();
        }
    }

    /**
     * Method used to start the lastPlayerInGameTimer
     * If it's not interrupted it declares the last player in game as the winner
     * It logs the event on the server
     */
    private synchronized void startLastPlayerTimer() {
        String lastPlayerInGame = playerViewMap.keySet().stream().findFirst().orElse("");
        this.notifyLastInGameTimer();
        Runnable declareLastPlayerWinner = () -> {
            synchronized (this) {
                if (!Thread.currentThread().isInterrupted()) {
                    if (!this.playerViewMap.isEmpty()) {
                        winsTheLastPlayerInGame(lastPlayerInGame);
                        lastInGameFuture = null;
                        countdownExecutor.shutdownNow();
                    } else {
                        this.save();
                        lastInGameFuture = null;
                    }
                }
            }
        };

        lastInGameFuture = countdownExecutor.schedule(declareLastPlayerWinner, Configs.lastInGameTimerSeconds, TimeUnit.SECONDS);
        logger.log(LoggerLevel.INFO, "started last player timer");
    }

    /**
     * Method used to declare as winner of the game the last player in game
     * @param winner the nickname of the player that was the last in game
     */
    private synchronized void winsTheLastPlayerInGame(String winner){
        logger.log(LoggerLevel.INFO, "ended");
        game.addObjectivePoints();
        this.notifyGameEnded(game.getPointPerPlayerMap(), List.of(winner));
        moveToEndGame();
    }

    /**
     * Method used to move the game and all player state to the end game
     * and transition all the players views to the end game state
     */
    private synchronized void moveToEndGame(){
        game.setState(GameState.END_GAME);
        for(String player : game.getPlayersList().stream().map(Player::getNickname).toList()){
            game.getPlayerFromNick(player).setState(PlayerState.END_GAME);
        }

        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.transitionTo(ViewState.GAME_ENDING);
            } catch (Exception ignored) {
            }
        });
    }

    /**
     * It calculates the player(s) with the max amount of points,
     * if there is only one player with the max amount of points it declares it as the winner,
     * otherwise it calculates the number of objective cards completed by the player(s) with the max amount of points
     * if there is only one player with the max amount of objective cards completed it declares it as the winner,
     * otherwise it declares all the players with the max amount of points and objective cards completed as the winners
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
            game.getPlayersList().forEach(user ->{
                if(playerMaxPoint.contains(user.getNickname())){
                    int completedObj = 0;
                    for(ObjectiveCard obj : game.getCommonObjective()){
                        if(obj.getPoints() != 0)
                            completedObj += obj.getPoints(user.getUserCodex()) / obj.getPoints();
                    }
                    if(user.getUserHand().getSecretObjective() != null) {
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

    /**
     * method used to notify the view of the last player in game
     * that it is the last player in game and the countdown is starting,
     * if no one else joins the game it will be declared as the winner
     */
    private synchronized void notifyLastInGameTimer() {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.log(LogsOnClient.LAST_PLAYER);
                view.logGame(LogsOnClient.COUNTDOWN_START);
            } catch (Exception ignored) {
            }
        });
    }

    /**
     * method used to notify that the timer has been interrupted
     * caused by a player joining the game
     */
    private synchronized void notifyLastInGameTimerStop() {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.logGame(LogsOnClient.COUNTDOWN_INTERRUPTED);
            } catch (Exception ignored) {}
        });
    }
    /**
     * Method used to set the player state to waiting and
     * transition the player view to the waiting state
     */
    private synchronized void moveToWait(String nickname) {
        Player player = game.getPlayerFromNick(nickname);
        player.setState(PlayerState.WAIT);
        try{
            playerViewMap.get(nickname).transitionTo(ViewState.WAITING_STATE);
        }catch (Exception ignored){}
    }

    /**
     * Method used to move to the choosePawn game phase
     * It sets the game state to choosePawn and the player state to choosePawn
     * It updates all the active players views with the pawn choices
     * It transitions all the active players views to the choosePawn state logging the event
     */
    private synchronized void moveToChoosePawn() {
        game.setState(GameState.CHOOSE_PAWN);
        for(Player player : game.getPlayersList()){
            player.setState(PlayerState.CHOOSE_PAWN);
        }
        this.notifyPawnChoiceSetup();

        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.logGame(LogsOnClient.EVERYONE_PLACED_STARTCARD);
                view.transitionTo(ViewState.CHOOSE_PAWN);
            } catch (Exception ignored) {
            }
        });
    }

    /**
     * Method used to set up the active players views with the pawn choices available
     */
    private synchronized void notifyPawnChoiceSetup() {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.updateGame(new GameDiffSetPawns(game.getPawnChoices()));
            } catch (Exception ignored) {
            }
        });
    }

    /**
     * Method used to notify all the player of the choice of the pawn by a player
     * It updates all the active players views with the pawn choices available removing the chosen pawn
     * It updates the chooser view with its choice of pawn
     * It updates the views with the decks status updated by the setup of the hand
     * It logs the event on the players view
     * @param chooser the nickname of the player that has chosen the pawn
     * @param color the color of the pawn chosen
     * @param resourceBack the back of the resource card deck new top
     * @param goldBack the back of the gold card deck new top
     */
    private synchronized void notifyPawnChoice(String chooser, PawnColors color, LightBack resourceBack, LightBack goldBack) {
        Player player = game.getPlayerFromNick(chooser);
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.updateGame(new GameDiffSetPawns(game.getPawnChoices()));
                view.updateGame(new GameDiffSetPlayerColor(chooser, color));

                view.updateGame(new DeckDiffDeckDraw(DrawableCard.RESOURCECARD, resourceBack));
                view.updateGame(new DeckDiffDeckDraw(DrawableCard.GOLDCARD, goldBack));

                if (nickname.equals(chooser)) {
                    view.log(LogsOnClient.YOU_CHOSE_PAWN);
                    view.logGame(LogsOnClient.WAIT_PAWN);

                    for (HandDiff handDiff : DiffGenerator.getHandYourCurrentState(player)) {
                        view.updateGame(handDiff);
                    }
                } else {
                    view.logOthers(chooser + LogsOnClient.PLAYER_CHOSE_PAWN);
                }
            } catch (Exception ignored) {
            }
        });
    }

    /**
     * Method used to notify all the player of the choice of the start card face by a player
     * It updates all the active players views with the start card placement,
     * removing the chosen card from the deck and updating the player codex
     * @param placer the nickname of the player that has chosen the start card
     * @param placement the placement of the start card in the player codex
     */
    private synchronized void notifyStartCardFaceChoice(String placer, LightPlacement placement) {
        Player player = game.getPlayerFromNick(placer);
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                if (nickname.equals(placer)) {
                    view.log(LogsOnClient.YOU_PLACE_STARTCARD);
                    view.logGame(LogsOnClient.WAIT_STARTCARD);
                    view.updateGame(new HandDiffRemove(placement.card()));
                    view.updateGame(new CodexDiffPlacement(placer, player.getUserCodex().getPoints(),
                            player.getUserCodex().getEarnedCollectables(), List.of(placement), player.getUserCodex().getFrontier().getFrontier()));
                } else {
                    view.logOthers(placer + LogsOnClient.PLAYER_PLACE_STARTCARD);
                }
            } catch (Exception ignored) {
            }
        });
    }

    /**
     * Method used to check if all the active players have chosen the Pawn
     * the check is done from the perspective of the player with the nickname passed as parameter
     * i.e. not considering the player with the nickname passed as parameter
     * @param nicknamePerspective the nickname of the player from which perspective the check is made
     * @return true if all the active players excluded the nickname perspective have chosen the pawn, false otherwise
     */
    private synchronized boolean otherHaveAllChoosePawn(String nicknamePerspective) {
        boolean allChose = true;
        List<String> activePlayer = playerViewMap.keySet().stream().toList();
        for (String nick : activePlayer) {
            if (!nick.equals(nicknamePerspective) && game.getPlayerFromNick(nick).getState().equals(PlayerState.CHOOSE_PAWN)) {
                allChose = false;
            }
        }
        return allChose;
    }

    /**
     * Method used to notify all the players views of the choice of the secretObjective with a log of the operation
     * the chooser view is updated with the chosen secretObjective which is set in its hand
     * @param chooser the nickname of the player that has chosen the secretObjective
     * @param objChoice the secretObjective chosen by the player
     */
    private synchronized void notifySecretObjectiveChoice(String chooser, LightCard objChoice) {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                if (nickname.equals(chooser)) {
                    view.updateGame(new HandDiffSetObj(objChoice));
                    view.log(LogsOnClient.YOU_CHOSE_SECRET_OBJ);
                    view.logGame(LogsOnClient.WAIT_SECRET_OBJECTIVE);
                } else {
                    view.logOthers(chooser + LogsOnClient.PLAYER_CHOSE_SECRET_OBJ);
                }
            } catch (Exception ignored) {
            }
        });
    }

    /**
     * Method used to transition views to place card if the player is the current player
     * or to idle if the player is not the current player
     * @param nickname the nickname of the player whose view needs to be transitioned
     */
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

    /**
     * Method used to notify all the players views of the placement of a card by a player
     * All the active players views are updated with the placement of the card in the player codex
     * and the removal of the card from the player hand
     * The placer view hand is updated with the playability of the cards in the hand given the
     * new placement of the card
     * The operation is logged on the players views
     * @param placer the nickname of the player that has placed the card
     * @param newPlacement the placement of the card in the player codex
     * @param placerCodex the codex of the player that has placed the card
     * @param playability a map containing the updated playability of the cards in the player hand
     */
    private synchronized void notifyPlacement(String placer, LightPlacement newPlacement, Codex placerCodex, Map<LightCard, Boolean> playability) {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.updateGame(DiffGenerator.placeCodexDiff(placer, newPlacement, placerCodex));

                if (nickname.equals(placer)) {
                    view.updateGame(new HandDiffRemove(newPlacement.card()));
                    for (LightCard card : playability.keySet())
                        view.updateGame(new HandDiffUpdatePlayability(card, playability.get(card)));
                    view.log(LogsOnClient.YOU_PLACED);
                } else {
                    LightBack newPlacementBack = new LightBack(newPlacement.card().idBack());
                    view.updateGame(new HandOtherDiffRemove(newPlacementBack, placer));
                    view.logOthers(placer + LogsOnClient.PLAYER_PLACED);
                }
            } catch (Exception ignored) {
            }
        });
    }

    /**
     * Method used to declare the winners of the game
     * It adds the objective points to the players points
     * notifies all the players views of the ending of the game with the final points and the winners
     * It logs the event on the server
     */
    private void declareWinners() {
        game.addObjectivePoints();
        //notify
        this.notifyGameEnded(game.getPointPerPlayerMap(), this.getWinners());
        logger.log(LoggerLevel.INFO, "ended");
    }

    /**
     * Method used to save on disk the current state of the game
     */
    private void save() {
        persistenceFactory.save(game);
    }

    /**
     * Method used to notify all the players views of the drawing of a card by a player
     * All the active players views are updated with the drawing of the card from the deck
     * updating the deck status and the drawer hand.
     * The drawn card is added to the drawer hand,
     * while other players views receive only the back of the drawn card to add to the drawer hand
     * The operation is logged on the players views
     * @param drawerNickname the nickname of the player that has drawn the card
     * @param deckType the type of the deck from which the card has been drawn
     * @param pos the position of the deck from which the card has been drawn
     * @param drawnCard the card drawn by the player
     * @param playability a boolean value that is true if the drawn card can be placed in the player codex, false otherwise
     */
    private synchronized void notifyDraw(String drawerNickname, DrawableCard deckType, int pos, LightCard drawnCard, boolean playability) {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                for(DeckDiff diff : DiffGenerator.draw(deckType, pos, game))
                    view.updateGame(diff);

                if (!nickname.equals(drawerNickname)) {
                    view.logOthers(drawerNickname + LogsOnClient.PLAYER_DRAW);
                    LightBack backOfDrawnCard = new LightBack(drawnCard.idBack());
                    view.updateGame(new HandOtherDiffAdd(backOfDrawnCard, drawerNickname));
                } else {
                    view.log(LogsOnClient.YOU_DRAW);
                    view.updateGame(new HandDiffAdd(drawnCard, playability));
                }
            } catch (Exception ignored) {
            }
        });
    }

    /**
     * Method used to get the last in turn active player
     * @return the nickname of the active last in turn player
     */
    private synchronized String getLastActivePlayer() {
        List<String> activePlayers = playerViewMap.keySet().stream().toList();
        ArrayList<String> turnsOrder = new ArrayList<>(game.getPlayersList().stream().map(Player::getNickname).toList());
        Collections.reverse(turnsOrder);

        return turnsOrder.stream().filter(activePlayers::contains).findFirst().orElse(null);
    }

    /**
     * Method used to notify all the players of the end of the game
     * It updates all the active players views with the points given by the objective cards
     * adding them to the points already obtained during the game
     * It updates all the active players views with the winners of the game
     * @param pointsPerPlayerMap the map containing the points of the players
     * @param winners the list of the winners of the game
     */
    private synchronized void notifyGameEnded(Map<String, Integer> pointsPerPlayerMap, List<String> winners) {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.updateGame(new CodexDiffSetFinalPoints(pointsPerPlayerMap));
                view.updateGame(new GameDiffWinner(winners));
                view.logGame(LogsOnClient.GAME_END);
            } catch (Exception ignored) {
            }
        });
    }

    /**
     * Method used to notify all the players of the last turn of the game
     * It logs the event on the players views
     */
    private synchronized void notifyLastTurn() {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.logGame(LogsOnClient.LAST_TURN);
            } catch (Exception ignored) {
            }
        });
    }

    /**
     * Method used to get the next active player index
     * It calculates the next player index that is active
     * If there are no active players it returns the current player index
     * @return the index of the next active player or the current player index if there are no active players
     */
    private synchronized int getNextActivePlayerIndex() {
        List<String> userList = game.getPlayersList().stream().map(Player::getNickname).toList();
        List<String> activePlayer = playerViewMap.keySet().stream().toList();
        int currentPlayerIndex = game.getCurrentPlayerIndex();
        int currentPlayerIteration = 0;
        int nextPlayerIndex;
        do {
            currentPlayerIteration += 1;
            nextPlayerIndex = (currentPlayerIndex + currentPlayerIteration) % userList.size();
        } while (!activePlayer.contains(userList.get(nextPlayerIndex)) && currentPlayerIteration <= userList.size());

        return nextPlayerIndex;
    }

    /**
     * Method used to notify all the players of the change of the current player
     * It updates all the active players views with the new current player
     * @param nextPlayer the nickname of the player that is the current player
     */
    private synchronized void notifyTurnChange(String nextPlayer) {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.updateGame(new GameDiffCurrentPlayer(nextPlayer));
                if (!nickname.equals(nextPlayer)) {
                    view.logOthers(nextPlayer + LogsOnClient.PLAYER_TURN);
                } else {
                    view.log(LogsOnClient.YOUR_TURN);
                }
            } catch (Exception ignored) {
            }
        });
    }

    /**
     * Method used to update the model with the objective card choices
     * @param player the player to which add the objective card choices
     */
    private synchronized void drawObjectiveCard(Player player) {
        List<ObjectiveCard> objectiveCards = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            objectiveCards.add(game.drawObjectiveCard());
        }
        player.getUserHand().setSecretObjectiveChoice(objectiveCards);
    }

    /**
     * Method used to move the game to the secret objective phase
     * It sets the game state to chooseSecretObjective and the player state to chooseSecretObjective
     * It updates the model with the objective card choices for all the players
     * It notifies all the active players views with the objective card choices
     * and logs the event on the players views
     */
    private synchronized void moveToSecretObjectivePhase() {
        game.setState(GameState.CHOOSE_SECRET_OBJECTIVE);
        for (Player player : game.getPlayersList()) {
            this.drawObjectiveCard(player);
            player.setState(PlayerState.CHOOSE_SECRET_OBJECTIVE);
        }
        this.notifySecretObjectiveSetup();
    }

    /**
     * Method used to set up the chooseSecretObjective phase
     * It transitions all the active players views to the chooseSecretObjective state
     * and logs the event on the players views
     * It updates all players lightModel with their hans and the back of the hand of the other players
     * It updates all the players lightModel with their secret objective choices
     * It updates all the players lightModel with the placement of the startCard of the other players
     */
    private synchronized void notifySecretObjectiveSetup() {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.logGame(LogsOnClient.EVERYONE_CHOSE_PAWN);
                view.transitionTo(ViewState.SELECT_OBJECTIVE);
            } catch (Exception ignored) {
            }
        });

        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                List<ObjectiveCard> secretObjChoices = game.getPlayerFromNick(nickname).getUserHand().getSecretObjectiveChoices();
                List<LightCard> lightSecretObjChoices = secretObjChoices.stream().map(Lightifier::lightifyToCard).toList();
                for (LightCard secretObj : lightSecretObjChoices) {
                    view.updateGame(new HandDiffAddOneSecretObjectiveOption(secretObj));
                }

                for (HandOtherDiff handDiff : DiffGenerator.getHandOtherCurrentState(game, nickname)) {
                    view.updateGame(handDiff);
                }
                for (Player player : game.getPlayersList()) {
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

    /**
     * Method used to notify all the players of the setup of the actual game
     * It updates all the active players views with the common objective cards
     * and logs the event on the players views
     */
    private synchronized void notifyActualGameSetup() {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.updateGame(new GameDiffPublicObj(game.getCommonObjective().stream().map(Lightifier::lightifyToCard).toArray(LightCard[]::new)));
                view.logGame(LogsOnClient.EVERYONE_CHOSE_OBJ);
            } catch (Exception ignored) {
            }
        });
    }

    /**
     * Method used to kick the inactive players from the game that does not meet
     * the condition passed as parameter
     * If the player kicked is the first player in the game, the first player is updated
     * @param checkProperInactive the condition that the player must meet to be
     *                            left in the game even if inactive
     */
    private synchronized void removeInactivePlayers(Predicate<Player> checkProperInactive) {
        List<String> activePlayer = playerViewMap.keySet().stream().toList();
        List<String> playersToRemove = new ArrayList<>();
        for (Player player : game.getPlayersList()) {
            if (!activePlayer.contains(player.getNickname()) && !checkProperInactive.test(player) && !game.isAnAgent(player.getNickname())) {
                playersToRemove.add(player.getNickname());
            }
        }

        if (!playersToRemove.isEmpty()) {
            String oldFirstPlayer = game.getPlayersList().getFirst().getNickname();
            for (String playerNick : playersToRemove) {
                game.removePlayer(playerNick);
            }
            this.updateRemovedPlayers(playersToRemove);

            String newFirstPlayer = game.getPlayersList().getFirst().getNickname();
            if (!oldFirstPlayer.equals(newFirstPlayer)) {
                this.notifyFirstPlayerChange(newFirstPlayer);
            }
        }
        this.save();
    }

    /**
     * Method used to update the lightGames of the active players with the players kicked from the game
     * @param playersToRemove the list of the nicknames of the players kicked from the game
     */
    private synchronized void updateRemovedPlayers(List<String> playersToRemove) {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.updateGame(new GameDiffRemovePlayers(playersToRemove));
            } catch (Exception ignored) {
            }
        });
    }

    /**
     * Method used to update the lightModel of all players of the change of the first player in the game
     * @param newFirstPlayer the nickname of the new first player in the game
     */
    private synchronized void notifyFirstPlayerChange(String newFirstPlayer) {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.updateGame(new GameDiffFirstPlayer(newFirstPlayer));
            } catch (Exception ignored) {
            }
        });
    }

    /**
     * Method used to check if all the active players have selected the start card face
     * the check is done from the perspective of the player with the nickname passed as parameter
     * i.e. not considering the player with the nickname passed as parameter
     * @param nicknamePerspective the nickname of the player from which perspective the check is made
     * @return true if all the active players excluded the nickname perspective have chosen their startcard face,
     *         false otherwise
     */
    private synchronized boolean otherHaveAllSelectedStartCard(String nicknamePerspective) {
        boolean allPlaced = true;
        List<String> activePlayer = playerViewMap.keySet().stream().toList();
        for (String nick : activePlayer) {
            if (!nick.equals(nicknamePerspective) && game.getPlayerFromNick(nick).getState().equals(PlayerState.CHOOSE_START_CARD)) {
                allPlaced = false;
            }
        }
        return allPlaced;
    }

    /**
     * Method used to update the lightModel of the user joining during the actualGame phase
     * with the data of the game
     * The active and inactive players are updated, the placement history is updated,
     * the hand of the player is updated, the back of the hand of the other players is updated,
     * the deck status is updated and the current player turn is updated
     * @param joiner the nickname of the player that has joined the game
     * @param game the game to which the player has joined
     */
    public synchronized void updateJoinActualGame(String joiner, Game game) {
        List<String> activePlayers = playerViewMap.keySet().stream().toList();
        try {
            ViewInterface joinerView = playerViewMap.get(joiner);
            joinerView.updateGame(DiffGenerator.updateJoinActualGame(game, activePlayers, joiner));
            joinerView.logGame(LogsOnClient.GAME_JOINED);
        } catch (Exception ignored) {
        }

    }

    /**
     * Method used to check if all the active players have chosen the secret objective
     * the check is done from the perspective of the player with the nickname passed as parameter
     * i.e. not considering the player with the nickname passed as parameter
     * @param nicknamePerspective the nickname of the player from which perspective the check is made
     * @return true if all the active players excluded the nickname perspective have chosen the secret objective,
     *         false otherwise
     */
    private synchronized boolean otherHaveAllChosenObjective(String nicknamePerspective) {
        boolean allChosen = true;
        for (String nickname : playerViewMap.keySet()) {
            Player player = game.getPlayerFromNick(nickname);
            if (!player.getNickname().equals(nicknamePerspective) && player.getState().equals(PlayerState.CHOOSE_SECRET_OBJECTIVE)) {
                allChosen = false;
            }
        }
        return allChosen;
    }

    /**
     * @return a random deck type
     */
    private DrawableCard randomDeckType() {
        Random random = new Random();
        int deckNumber = random.nextInt(2);
        return deckNumber == 0 ? DrawableCard.RESOURCECARD : DrawableCard.GOLDCARD;
    }

    /**
     * @return a random deck position
     */
    private int randomDeckPosition() {
        Random random = new Random();
        return random.nextInt(Configs.actualDeckPos + 1);
    }

    /**
     * Method used to send a chat message to the game
     * checks if the message is empty, if the receiver is empty or not in the game
     * if not it sends the message to the receiver
     * updating all the players lightChat if the message is public
     * or only the sender and the receiver lightChat if the message is private
     * @param chatMessage the chat message sent by the player
     */
    public void sendChatMessage(ChatMessage chatMessage) {
        if(!emptyMessage(chatMessage)){
            if (chatMessage.getPrivacy() == ChatMessage.MessagePrivacy.PUBLIC) {
                this.updateGlobalChat(chatMessage);
            } else { //private message
                if(!goodReceiver(chatMessage)){
                    return;
                }
            }
            this.game.getGameParty().getChatManager().addMessage(chatMessage);
        }
    }

    /**
     * Method used to load all the chat history of the player that has joined the game
     * @param joinerNickname the nickname of the player that has joined the game
     * @param view the view of the player that has joined the game
     */
    private void loadChat(String joinerNickname, ViewInterface view) {
        try{
            view.updateGame(new ChatDiffs(game.getGameParty().getChatManager().retrieveChat(joinerNickname)));
        }catch (Exception ignored){}
    }

    /**
     * Method used to update all the players lightChat with the chat message
     * when the message is public
     * it adds the message to the chat history of the game
     * @param chatMessage the chat message sent by the player
     */
    private synchronized void updateGlobalChat(ChatMessage chatMessage) {
        new HashMap<>(playerViewMap).forEach((nickname, view) -> {
            try {
                view.updateGame(new ChatDiffs(List.of(chatMessage)));
                if(chatMessage.getSender().equals(nickname)){
                    view.logChat(LogsOnClient.SENT_PUBLIC_MESSAGE);
                }else{
                    view.logChat(LogsOnClient.RECEIVED_PUBLIC_MESSAGE);
                }
            } catch (Exception ignored) {
            }
        });
    }

    /**
     * Method used to check if the message is empty
     * If the message is empty it logs an error on the sender chat
     * @param chatMessage the chat message sent by the player
     * @return true if the message is empty, false otherwise
     */
    private boolean emptyMessage(ChatMessage chatMessage) {
        ViewInterface senderView = playerViewMap.get(chatMessage.getSender());
        try {
            if (chatMessage.getMessage().isBlank()) {
                senderView.logChat(LogsOnClient.NO_EMPTY_MESSAGE);
                return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * the method checks if the receiver to which the message is sent is valid
     * if the receiver is empty it logs an error on the sender chat
     * if the receiver is not valid it logs an error on the sender chat
     * @param chatMessage the chat message sent by the player which receiver is checked
     * @return true if the receiver is valid, false otherwise
     */
    private boolean goodReceiver(ChatMessage chatMessage){
        ViewInterface senderView = playerViewMap.get(chatMessage.getSender());
        try{
            if(chatMessage.getReceiver().isBlank()){
                senderView.logChat(LogsOnClient.EMPTY_RECEIVER);
                return false;
            }else if(!game.getGameParty().getPlayersList().stream().map(Player::getNickname).toList().contains(chatMessage.getReceiver())){
                senderView.logChat(String.format(LogsOnClient.RECEIVER_NOT_FOUND, chatMessage.getReceiver()));
                return false;
            }else if (chatMessage.getSender().equals(chatMessage.getReceiver())) {
                //Nothing to see here, move on
                if(chatMessage.getMessage().toLowerCase().contains("ferrari")){
                    senderView.logChat(LogsOnClient.FERRARI);
                }
                senderView.logChat(LogsOnClient.NO_SELF_MESSAGE);
                return false;
            }else{
                this.updatePrivateChat(chatMessage);
            }
        }catch (Exception ignored){}
        return true;
    }

    /**
     * Method used to update the lightChat of the sender and the receiver of the private message
     * @param chatMessage the chat message sent by the player
     */
    private synchronized void updatePrivateChat(ChatMessage chatMessage) {
        try {
            playerViewMap.get(chatMessage.getSender()).updateGame(new ChatDiffs(List.of(chatMessage)));
            playerViewMap.get(chatMessage.getSender()).logChat(LogsOnClient.SENT_PRIVATE_MESSAGE + chatMessage.getReceiver());

            playerViewMap.get(chatMessage.getReceiver()).updateGame(new ChatDiffs(List.of(chatMessage)));
            playerViewMap.get(chatMessage.getReceiver()).logChat(LogsOnClient.RECEIVED_PRIVATE_MESSAGE + chatMessage.getSender());
        } catch (Exception ignored) {
        }
    }

    /**
     * Method used to manage the malevolent players,
     * i.e. those players that have tried to cheat, perform illegal actions
     * @param player the nickname of the player that has been malevolent
     */
    private synchronized void malevolentPlayer(String player) {
        malevolentPlayerManager.manageMalevolentPlayer(player);
    }
}
