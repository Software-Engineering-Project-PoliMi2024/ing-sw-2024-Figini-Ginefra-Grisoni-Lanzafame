package it.polimi.ingsw.controller3;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.controller.ControllerInterface;
import it.polimi.ingsw.controller.LogsOnClient;
import it.polimi.ingsw.controller.ServerModelController;
import it.polimi.ingsw.controller3.mediators.gameJoinerAndTurnTakerMediators.GameJoiner;
import it.polimi.ingsw.controller3.mediators.gameJoinerAndTurnTakerMediators.TurnTaker;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffs.game.HandOtherDiffAdd;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Deck;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.util.Objects;
import java.util.Random;

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
    public void login(String nickname){
        //check if the nickname is already taken
        if(!this.multiGame.isUnique(nickname)) {
            logErr(LogsOnClientStatic.NAME_TAKEN);
            transitionTo(ViewState.LOGIN_FORM);
            //check if the nickname is valid
        }else if(nickname.matches(Configs.validNicknameRegex)){
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
                    gameToJoin.subscribe(nickname, view, this, false);
                    transitionTo(ViewState.CHOOSE_START_CARD);
                }else{
                    gameToJoin.subscribe(nickname, view, this, false);
                    if(gameToJoin.inInSecretObjState()){
                        transitionTo(ViewState.SELECT_OBJECTIVE);
                    }else if(gameToJoin.getCurrentPlayer().getNickname().equals(this.nickname)){
                        takeTurn();
                    }else{
                        transitionTo(ViewState.IDLE);
                    }
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
    public void createLobby(String gameName, int maxPlayerCount) {
        //check if the player is a malevolent user
        if(isNotLogged()){
            malevolentConsequences();
            return;
        }

        //check if the lobby name is already taken
        if(multiGame.getLobbyByName(gameName)!=null || multiGame.getGameByName(gameName)!=null) {
            logErr(LogsOnClientStatic.LOBBY_NAME_TAKEN);
            transitionTo(ViewState.JOIN_LOBBY);
            //check if the lobby name is valid
        }else if(gameName.matches(Configs.validLobbyNameRegex)){
            logErr(LogsOnClientStatic.NOT_VALID_LOBBY_NAME);
            transitionTo(ViewState.JOIN_LOBBY);
        }else { //create the lobby
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
    public void joinLobby(String lobbyName) {
        //check if the player is a malevolent user
        if (isNotLogged()) {
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
    public void leaveLobby() {
        if (isNotLogged() || !multiGame.isInLobby(this.nickname)) {
            malevolentConsequences();
            return;
        }

        Lobby lobbyToLeave = multiGame.getUserLobby(this.nickname);
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
    public void choseSecretObjective(LightCard objectiveCard) {
        //TODO settare a null secretObjectiveChoices
    }

    @Override
    public void place(LightPlacement placement) {
        //TODO startCard settare a null nel model la startCard (user.gethand.getStartCard)
    }

    @Override
    public void draw(DrawableCard deckID, int cardID) {
        //TODO saveGame
        //TODO change current player lock
    }

    private void leaveGame() {
        //when leaving a Game check if currentPlayer
        Game gameToLeave = multiGame.getGameFromUserNick(this.nickname);
        gameToLeave.unsubscribe(this.nickname);

        if (gameToLeave.isInStartCardState()) {

            if (checkIfLastToPlaceStartCard()) {
                gameToLeave.removeUser(this.nickname);
                gameToLeave.fromStartCardMoveOnToSecretObjectiveSelection();
            }
        } else if (gameToLeave.inInSecretObjState()) {

            if (checkIfLastToChooseSecretObjective()) {
                gameToLeave.removeUser(this.nickname);
                User currentUserInGame = gameToLeave.getCurrentPlayer();
                int nextPlayerIndex = getNextActivePlayerIndex();
                String nextPlayerNick = gameToLeave.getPlayerFromIndex(nextPlayerIndex).getNickname();
                if (Objects.equals(currentUserInGame.getNickname(), this.nickname)) {
                    gameToLeave.setPlayerIndex(nextPlayerIndex);
                }
                gameToLeave.fromSecretObjectiveMoveOnToGame(nextPlayerNick);
            }
        } else if (!gameToLeave.isInSetup()) { //if the game is in the actual game phase
            if (gameToLeave.getCurrentPlayer().getNickname().equals(this.nickname)) {
                User userLeaving = gameToLeave.getUserFromNick(this.nickname);
                int nextPlayerIndex = getNextActivePlayerIndex();
                String nextPlayerNick = gameToLeave.getPlayerFromIndex(nextPlayerIndex).getNickname();
                //check if the user has disconnected after placing
                if (userLeaving.getHandSize() < 3 && !gameToLeave.areDeckEmpty()) {
                    CardInHand card;
                    DrawableCard deckType;
                    int pos;
                    do {
                        deckType = randomDeckType(gameToLeave);
                        pos = randomDeckPosition(gameToLeave, deckType);
                        if (deckType == DrawableCard.RESOURCECARD)
                            card = gameToLeave.drawACard(gameToLeave.getResourceCardDeck(), pos);
                        else
                            card = gameToLeave.drawACard(gameToLeave.getGoldCardDeck(), pos);
                    } while (card == null);

                    userLeaving.getUserHand().addCard(card);
                    gameToLeave.notifyDraw(deckType, pos, Lightifier.lightifyToCard(card), userLeaving.getNickname());

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

    private boolean checkIfLastToPlaceStartCard(){
        Game game = multiGame.getGameFromUserNick(this.nickname);
        boolean check = false;

        if(!game.isInStartCardState())
            throw new IllegalCallerException("Controller.checkIfLastToPlaceStartCard: Game is not in StartCardState");

        if(game.getGameParty().getUsersList().stream().allMatch(user -> !user.getNickname().equals(this.nickname)
                && user.hasPlacedStartCard())){
            check = true;
        }

        return check;
    }

    private boolean checkIfLastToChooseSecretObjective(){
        Game game = multiGame.getGameFromUserNick(this.nickname);
        boolean check = false;

        if(!game.inInSecretObjState())
            throw new IllegalCallerException("Controller.checkIfLastToChooseSecretObjective: Game is not in SelectSecretObjectiveState");

        if(game.getGameParty().getUsersList().stream().allMatch(user -> !user.getNickname().equals(this.nickname)
                && user.hasChosenObjective())){
            check = true;
        }

        return check;
    }

    /**
     * @return a random card from a non-empty deck. If all decks are empty return null
     */

    private DrawableCard randomDeckType(Game game){
        Random random = new Random();
        int deckNumber = random.nextInt(2);
        DrawableCard drawableCard = deckNumber == 0 ? DrawableCard.RESOURCECARD : DrawableCard.GOLDCARD;


        return drawableCard;
    }

    private int randomDeckPosition(Game game, DrawableCard deckType){
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
    public void disconnect() {
        if(this.nickname == null){
            System.out.println("User disconnected before logging in");
            return;
        }

        if(multiGame.getUserLobby(this.nickname) != null){
            Lobby lobbyToLeave = multiGame.getUserLobby(this.nickname);
            lobbyToLeave.lock();
            leaveLobby();
            lobbyToLeave.unlock();
        }else if(multiGame.isInGameParty(this.nickname)) {
            Game gameToLeave = multiGame.getGameFromUserNick(this.nickname);
            gameToLeave.lockCurrentPlayer();
            leaveGame();
            gameToLeave.unlockCurrentPlayer();
        }else{
            multiGame.removeUser(this.nickname);
        }

        multiGame.removeUser(this.nickname);
        System.out.println(this.nickname + " has disconnected");
    }

    //turnTaker methods
    @Override
    public void joinGame() {
        multiGame.getUserLobby(this.nickname).unsubscribe(this.nickname);
        Game gameToJoin = multiGame.getGameFromUserNick(this.nickname);
        User user = gameToJoin.getUserFromNick(this.nickname);

        if (user.getUserHand().getStartCard() != null)
            throw new IllegalStateException("Controller.joinGame: User already has startCard");

        StartCard startCard = gameToJoin.getStartingCardDeck().drawFromDeck();
        user.getUserHand().setStartCard(startCard);
        gameToJoin.subscribe(nickname, view, this, false);

        transitionTo(ViewState.CHOOSE_START_CARD);
    }

    @Override
    public void chooseObjective() {

    }

    @Override
    public void takeTurn() {

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
