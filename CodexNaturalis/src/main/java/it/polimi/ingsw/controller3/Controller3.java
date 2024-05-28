package it.polimi.ingsw.controller3;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.controller.ControllerInterface;
import it.polimi.ingsw.controller3.mediators.gameJoinerAndTurnTakerMediators.GameJoiner;
import it.polimi.ingsw.controller3.mediators.gameJoinerAndTurnTakerMediators.TurnTaker;
import it.polimi.ingsw.lightModel.Heavifier;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.cardReleted.cards.*;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Deck;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.util.*;

/*
TODO  test when the decks finish the cards
*/

public class Controller3 implements ControllerInterface, TurnTaker, GameJoiner {
    private final ViewInterface view;
    private final MultiGame multiGame;

    private String nickname;

    public Controller3(MultiGame multiGame, ViewInterface view) {
        this.view = view;
        this.multiGame = multiGame;
    }

    /**
     * This method is called when the client wants to log-in
     * It checks if the nickname is valid and if it is not already taken
     * if the nickname was in a game before disconnecting, it will rejoin the game
     * else it will join the lobbyList receiving the list of Lobby and
     * subscribing to the lobbyList mediator in order to receive updates on the lobbyList
     * @param nickname the nickname chosen by the player
     */
    @Override
    public synchronized void login(String nickname){
        //check if the nickname is already taken
        if(!this.multiGame.isUnique(nickname)) {
            logErr(LogsOnClientStatic.NAME_TAKEN);
            transitionTo(ViewState.LOGIN_FORM);
            //check if the nickname is valid
        }else if(nickname.matches(Configs.invalidNicknameRegex)){
            logErr(LogsOnClientStatic.NOT_VALID_NICKNAME);
            transitionTo(ViewState.LOGIN_FORM);
        }else{
            //Client is now logged-In. If he disconnects we have to update the model
            this.nickname = nickname;
            this.multiGame.addUser(nickname);
            System.out.println(this.nickname + " has connected");

            //check if the player was playing a game before disconnecting
            if(multiGame.isInGameParty(nickname)){
                Game gameToJoin = multiGame.getGameFromUserNick(nickname);
                if(gameToJoin.isInStartCardState()) {
                    this.joinGame();
                }else if(gameToJoin.inInSecretObjState()){
                    gameToJoin.subscribe(nickname, view, this, true);
                    gameToJoin.joinSecretObjective(nickname, gameToJoin);
                    this.chooseObjective();
                }else {
                    gameToJoin.subscribe(nickname, view, this, true);
                    gameToJoin.joinMidGame(nickname, gameToJoin);
                    this.takeTurn();
                }
            }else{
                //subscribe the view to the lobbyList mediator
                multiGame.subscribe(nickname, view);
                transitionTo(ViewState.JOIN_LOBBY);
            }
        }
    }

    /**
     * This method is called when the client wants to create a new lobby
     * It checks if the player is a malevolent user
     * checks if the lobby name is already taken or if it is valid
     * it creates the lobby and adds it to the model
     * it unsubscribes the view from the lobbyList mediator and subscribes it to the new lobby mediator
     * @param gameName the name of the lobby
     * @param maxPlayerCount the maximum number of players that can join the lobby
     */
    @Override
    public synchronized void createLobby(String gameName, int maxPlayerCount) {
        //check if the player is a malevolent user
        if(isNotLogged() || multiGame.isInGameParty(this.nickname) || multiGame.isInLobby(this.nickname)){
            malevolentConsequences();
            return;
        }

        //check if the lobby name is already taken
        if(multiGame.getLobbyByName(gameName)!=null || multiGame.getGameByName(gameName)!=null) {
            logErr(LogsOnClientStatic.LOBBY_NAME_TAKEN);
            transitionTo(ViewState.JOIN_LOBBY);
            //check if the lobby name is valid
        }else if(gameName.matches(Configs.invalidLobbyNameRegex)){
            logErr(LogsOnClientStatic.NOT_VALID_LOBBY_NAME);
            transitionTo(ViewState.JOIN_LOBBY);
        }else { //create the lobby
            System.out.println(this.nickname + " create" + gameName + " lobby");
            Lobby lobbyCreated = new Lobby(maxPlayerCount, this.nickname, gameName);
            //add the lobby to the model
            multiGame.addLobby(lobbyCreated);
            //disconnect from lobbyList mediator and subscribe to the new lobby
            multiGame.unsubscribe(this.nickname); //unsubscribe the view from the lobbyList mediator
            multiGame.notifyNewLobby(this.nickname, lobbyCreated); //notify the lobbyList mediator of the new lobby creation
            lobbyCreated.subscribe(this.nickname, this.view, this);

            transitionTo(ViewState.LOBBY);
        }
    }

    /**
     * This method is called when the client wants to join a lobby
     * It checks if the player is a malevolent user
     * checks if the lobby exists and if it is full and determines if the player can join the lobby
     * it adds the player to the lobby and updates the model
     * it unsubscribes the view from the lobbyList mediator and subscribes it to the new lobby mediator
     * if the lobby is full after the join it starts the game
     * @param lobbyName the name of the lobby the user wants to join
     */
    @Override
    public synchronized void joinLobby(String lobbyName) {
        //check if the player is a malevolent user
        if (isNotLogged() || multiGame.isInGameParty(this.nickname) || multiGame.isInLobby(this.nickname)) {
            malevolentConsequences();
            return;
        }

        Lobby lobbyToJoin = this.multiGame.getLobbyByName(lobbyName);
        if (lobbyToJoin == null) {
            logErr(LogsOnClientStatic.LOBBY_NONEXISTENT);
            transitionTo(ViewState.JOIN_LOBBY);
        } else if (lobbyToJoin.getLobbyPlayerList().size() == lobbyToJoin.getNumberOfMaxPlayer()) {
            logErr(LogsOnClientStatic.LOBBY_IS_FULL);
            transitionTo(ViewState.JOIN_LOBBY);
        } else {
            System.out.println(this.nickname + " joined " + lobbyName + " lobby");
            //add the player to the lobby, updated model
            multiGame.addPlayerToLobby(lobbyName, this.nickname);
            //disconnect from lobbyList mediator and subscribe to the new lobby
            multiGame.unsubscribe(this.nickname);
            lobbyToJoin.subscribe(this.nickname, this.view, this);
            lobbyToJoin.lock();
            if (lobbyToJoin.getLobbyPlayerList().size() != lobbyToJoin.getNumberOfMaxPlayer()) {
                transitionTo(ViewState.LOBBY);
            }else{
                Game createdGame = multiGame.createGame(lobbyToJoin);
                multiGame.addGame(createdGame);
                multiGame.notifyLobbyRemoved(this.nickname, lobbyToJoin);
                lobbyToJoin.notifyGameStart();
                multiGame.removeLobby(lobbyToJoin);
            }
            lobbyToJoin.unlock();
        }
    }

    /**
     * This method is called when the client wants to leave the lobby
     * It checks if the player is a malevolent user
     * it removes the player from the lobby and updates the model
     * if the lobby is empty it removes the lobby from the model
     * it subscribes the view to the lobbyList mediator
     */
    @Override
    public synchronized void leaveLobby() {
        if (isNotLogged() || !multiGame.isInLobby(this.nickname)) {
            malevolentConsequences();
            return;
        }


        Lobby lobbyToLeave = multiGame.getUserLobby(this.nickname);
        System.out.println(this.nickname + " left" + lobbyToLeave.getLobbyName() + " lobby");
        //update the model
        lobbyToLeave.removeUserName(this.nickname);


        if (lobbyToLeave.getLobbyPlayerList().isEmpty()) {
            multiGame.removeLobby(lobbyToLeave);
            multiGame.notifyLobbyRemoved(this.nickname, lobbyToLeave);
        }
        lobbyToLeave.unsubscribe(this.nickname);
        multiGame.subscribe(this.nickname, view);

        transitionTo(ViewState.JOIN_LOBBY);
    }

    @Override
    public synchronized void choseSecretObjective(LightCard objectiveCard) {
        if(isNotLogged() || !multiGame.isInGameParty(this.nickname) ||
                !multiGame.getGameFromUserNick(this.nickname).inInSecretObjState() &&
                        multiGame.getUserFromNick(this.nickname).hasChosenObjective()){
            malevolentConsequences();
            return;
        }

        System.out.println(this.nickname + " chose secret objective");

        Game game = multiGame.getGameFromUserNick(this.nickname);
        User user = game.getUserFromNick(this.nickname);

        user.setSecretObjective(Heavifier.heavifyObjectCard(objectiveCard, multiGame));
        game.notifySecretObjectiveChoice(this.nickname, objectiveCard);

        if(game.othersHadAllChooseSecretObjective(this.nickname)) {
            game.notifyEndSetupStartActualGame();
        }else
            transitionTo(ViewState.WAITING_STATE);
    }

    @Override
    public synchronized void place(LightPlacement placement) {
        if(isNotLogged() || !multiGame.isInGameParty(this.nickname) ||
                !multiGame.getGameFromUserNick(this.nickname).isYourTurnToPlace(this.nickname)) {
            malevolentConsequences();
            return;
        }
        //TODO check placement: card in mano e position in frontiera

        System.out.println(this.nickname + " placed a card");

        Game game = multiGame.getGameFromUserNick(this.nickname);
        User user = game.getUserFromNick(this.nickname);
        if(!user.hasPlacedStartCard()) {
            Placement heavyPlacement = Heavifier.heavifyStartCardPlacement(placement, multiGame);

            //model: place startCard
            user.placeStartCard(heavyPlacement);
            //model: add cards to hand
            for (int i = 0; i < 2; i++) {
                CardInHand resourceCard = game.getResourceCardDeck().drawFromDeck();
                user.getUserHand().addCard(resourceCard);
            }
            CardInHand goldCard = game.getGoldCardDeck().drawFromDeck();
            user.getUserHand().addCard(goldCard);
            //notify everyone and update my lightModel
            LightBack resourceBack = new LightBack(game.getResourceCardDeck().showTopCardOfDeck().getIdBack());
            LightBack goldBack = new LightBack(game.getGoldCardDeck().showTopCardOfDeck().getIdBack());
            game.notifyStartCardFaceChoice(this.nickname, user, placement, resourceBack, goldBack);

            //check if lastToPlace
            if (game.othersHadAllPlacedStartCard(this.nickname)) {
                this.setupSecretObjectives();
                game.notifyMoveToSelectObjState();
            }else{
                transitionTo(ViewState.WAITING_STATE);
            }
        }else{
            Placement heavyPlacement = Heavifier.heavify(placement, multiGame);
            Codex codexBeforePlacement = new Codex(user.getUserCodex());
            user.playCard(heavyPlacement);
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
            game.notifyPlacement(this.nickname, placement, user.getUserCodex(), FrontIdToPlayability);

            transitionTo(ViewState.DRAW_CARD);
        }

    }

    private void setupSecretObjectives(){
        Game game = multiGame.getGameFromUserNick(this.nickname);
        for(User user : game.getUsersList()){
                drawObjectiveCard(user);
        }
        game.secretObjectiveSetup();
    }

    @Override
    public synchronized void draw(DrawableCard deckID, int cardID) {
        if(isNotLogged() || !multiGame.isInGameParty(this.nickname)){
            malevolentConsequences();
            return;
        }
        System.out.println(this.nickname + " drew a card");

        Game game = multiGame.getGameFromUserNick(this.nickname);
        User user = multiGame.getUserFromNick(this.nickname);
        CardInHand drawnCard;
        if(deckID == DrawableCard.GOLDCARD){
            drawnCard = drawACard(game.getGoldCardDeck(), cardID);
        }else {
            drawnCard = drawACard(game.getResourceCardDeck(), cardID);
        }
        user.getUserHand().addCard(drawnCard);
        game.notifyTurn(game.getCurrentPlayer().getNickname());
        game.notifyDraw(deckID, cardID, Lightifier.lightifyToCard(drawnCard), this.nickname, drawnCard.canBePlaced(user.getUserCodex()));
        //TODO check for chicken dinner

        //turn
        game.setPlayerIndex(getNextActivePlayerIndex());
        game.notifyTurn(game.getCurrentPlayer().getNickname());
        //TODO saveGame
    }

    private void leaveGame() {
        //TODO timer
        Game gameToLeave = multiGame.getGameFromUserNick(this.nickname);
        gameToLeave.unsubscribe(this.nickname);
        User you = gameToLeave.getUserFromNick(this.nickname);

        if (gameToLeave.isInStartCardState()) {
            if (gameToLeave.othersHadAllPlacedStartCard(this.nickname) && !you.hasPlacedStartCard()) {
                gameToLeave.removeUser(this.nickname);
                gameToLeave.notifyMoveToSelectObjState();
            }
        } else if (gameToLeave.inInSecretObjState()) {

            if (gameToLeave.othersHadAllChooseSecretObjective(this.nickname) && !you.hasChosenObjective()) {
                gameToLeave.removeUser(this.nickname);
                User currentUserPlayer = gameToLeave.getCurrentPlayer();
                int nextPlayerIndex = getNextActivePlayerIndex();
                String nextPlayerNick = gameToLeave.getPlayerFromIndex(nextPlayerIndex).getNickname();
                if (Objects.equals(currentUserPlayer.getNickname(), this.nickname)) {
                    gameToLeave.setPlayerIndex(nextPlayerIndex);
                }
                gameToLeave.notifyTurn(nextPlayerNick);
                gameToLeave.notifyEndSetupStartActualGame();
            }

        } else if (!gameToLeave.isInSetup()) { //if the game is in the actual game phase
            if (gameToLeave.getCurrentPlayer().getNickname().equals(this.nickname)) { //if current player leaves

                int nextPlayerIndex = getNextActivePlayerIndex();
                String nextPlayerNick = gameToLeave.getPlayerFromIndex(nextPlayerIndex).getNickname();
                //check if the user has disconnected after placing
                if (you.getHandSize() < 3 && !gameToLeave.areDeckEmpty()) {
                    CardInHand card;
                    DrawableCard deckType;
                    int pos;
                    do {
                        deckType = randomDeckType();
                        pos = randomDeckPosition();
                        if (deckType == DrawableCard.RESOURCECARD)
                            card = gameToLeave.drawACard(gameToLeave.getResourceCardDeck(), pos);
                        else
                            card = gameToLeave.drawACard(gameToLeave.getGoldCardDeck(), pos);
                    } while (card == null);

                    you.getUserHand().addCard(card);
                    boolean playability = card.canBePlaced(you.getUserCodex());
                    gameToLeave.notifyDraw(deckType, pos, Lightifier.lightifyToCard(card), you.getNickname(), playability);
                }
                //move on with the turns for the other players
                gameToLeave.setPlayerIndex(nextPlayerIndex);
                gameToLeave.notifyTurn(nextPlayerNick);
            }
        } else
            throw new IllegalStateException("Controller.leaveGame: Game is in an invalid state");
        //If the game is empty, remove it from the MultiGame
        if (gameToLeave.getGameLoopController().getActivePlayers().isEmpty()) {
            multiGame.removeGame(gameToLeave);
        }

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

    private int getNextActivePlayerIndex(){
        Game game = multiGame.getGameFromUserNick(this.nickname);

        User nextPlayer = game.getUsersList().get(game.getNextPlayerIndex());
        while(!game.isPlayerActive(nextPlayer.getNickname())){
            nextPlayer = game.getUsersList().get(game.getNextPlayerIndex());
        }

        return game.getUsersList().indexOf(nextPlayer);
    }

    @Override
    public synchronized void disconnect() {
        if(this.nickname == null){
            System.out.println("User disconnected before logging in");
            return;
        }
        if(multiGame.getUserLobby(this.nickname) != null){
            Lobby lobbyToLeave = multiGame.getUserLobby(this.nickname);
            lobbyToLeave.lock();
            lobbyToLeave = multiGame.getUserLobby(this.nickname);
            if(lobbyToLeave != null){
                try{leaveLobby();}
                finally {lobbyToLeave.unlock();}
            }else{
                this.disconnect();
            }
        }else if(multiGame.isInGameParty(this.nickname)) {
            //block the entire game
            leaveGame();
        }else{
            multiGame.removeUser(this.nickname);
        }

        multiGame.removeUser(this.nickname);
        System.out.println(this.nickname + " has disconnected");
    }

    //turnTaker methods
    @Override
    public synchronized void joinGame() {
        System.out.println(this.nickname + " joined the game");
        if(multiGame.getUserLobby(this.nickname) != null)
            multiGame.getUserLobby(this.nickname).unsubscribe(this.nickname);
        Game gameToJoin = multiGame.getGameFromUserNick(this.nickname);
        User user = gameToJoin.getUserFromNick(this.nickname);
        if (user.getUserHand().getStartCard() == null && !user.hasPlacedStartCard()) {
            StartCard startCard = gameToJoin.getStartingCardDeck().drawFromDeck();
            user.getUserHand().setStartCard(startCard);
        }
        gameToJoin.subscribe(nickname, view, this, false);
        gameToJoin.joinStartCard(nickname, gameToJoin);
        if(!user.hasPlacedStartCard())
            transitionTo(ViewState.CHOOSE_START_CARD);
        else
            transitionTo(ViewState.WAITING_STATE);
    }

    @Override
    public synchronized void chooseObjective() {
        User user = multiGame.getUserFromNick(this.nickname);
        if(!user.hasChosenObjective()){
            transitionTo(ViewState.SELECT_OBJECTIVE);
        }else
            transitionTo(ViewState.WAITING_STATE);

    }

    /**
     * draw objectiveCard from the deck and set them in the userHand
     * @param user which is drawing the objectiveCard
     */
    private void drawObjectiveCard(User user){
        List<ObjectiveCard> objectiveCards = new ArrayList<>();
        for(int i=0;i<2;i++){
            objectiveCards.add(multiGame.getGameFromUserNick(this.nickname).getObjectiveCardDeck().drawFromDeck());
        }
        user.getUserHand().setSecretObjectiveChoice(objectiveCards);
    }

     /**
     * @param deck from which drawn a Card
     * @param cardID the position from where draw the card (buffer/deck)
     * @return the card drawn
     * @param <T> a CardInHand (GoldCard/ResourceCard)
     */
    private <T extends CardInHand> T drawACard(Deck<T> deck, int cardID) {
        T drawCard;
        if (cardID == Configs.actualDeckPos) {
            drawCard = deck.drawFromDeck();
        } else {
            drawCard = deck.drawFromBuffer(cardID);}
        return drawCard;
    }

    @Override
    public synchronized void takeTurn() {
        Game game = multiGame.getGameFromUserNick(this.nickname);
        if(game.getCurrentPlayer().getNickname().equals(this.nickname)) {
            transitionTo(ViewState.PLACE_CARD);
        }else{
            transitionTo(ViewState.IDLE);
        }
    }

    //malevolent user checker
    private boolean isNotLogged(){
        return this.nickname == null || !multiGame.getUsernames().contains(this.nickname);
    }

    private void malevolentConsequences(){
        System.out.println(nickname + "is a malevolent user");
        this.disconnect();
    }

    //view methods used by the controller
    private void logErr(String logMsg) {
        try {
            view.logErr(logMsg);
        } catch (Exception e) {
            logErr("Error in logErr: " + e.getMessage());
        }
    }

    private void transitionTo(ViewState state) {
        try {
            view.transitionTo(state);
        } catch (Exception e) {
            logErr("Error in transitionTo: " + e.getMessage());
        }
    }
}
