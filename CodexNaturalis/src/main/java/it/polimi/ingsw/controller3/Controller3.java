package it.polimi.ingsw.controller3;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.controller4.Interfaces.ControllerInterface;
import it.polimi.ingsw.controller3.mediators.gameJoinerAndTurnTakerMediators.GameJoiner;
import it.polimi.ingsw.controller3.mediators.gameJoinerAndTurnTakerMediators.TurnTaker;
import it.polimi.ingsw.lightModel.Heavifier;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.Reception;
import it.polimi.ingsw.model.cardReleted.cards.*;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.util.*;

/*
TODO test deck (when drawing all cards it remains a card)
TODO test when the decks finish the cards
TODO saveGame
*/

public class Controller3 implements ControllerInterface, TurnTaker, GameJoiner {
    private final ViewInterface view;
    private final Reception reception;

    private String nickname;

    public Controller3(Reception reception, ViewInterface view) {
        this.view = view;
        this.reception = reception;
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
        if(nickname == null){
            malevolentConsequences();
            return;
        }

        //check if the nickname is already taken
        if(!this.reception.isUnique(nickname)) {
            logErr(LogsOnClientStatic.NAME_TAKEN);
            transitionTo(ViewState.LOGIN_FORM);
            //check if the nickname is valid
        }else if(nickname.matches(Configs.invalidNicknameRegex)){
            logErr(LogsOnClientStatic.NOT_VALID_NICKNAME);
            transitionTo(ViewState.LOGIN_FORM);
        }else{
            //Client is now logged-In. If he disconnects we have to update the model
            this.nickname = nickname;
            this.reception.addUser(nickname);
            System.out.println(this.nickname + " has connected");

            //check if the player was playing a game before disconnecting
            if(reception.isInGameParty(nickname)){
                Game gameToJoin = reception.getGameFromUserNick(nickname);

                gameToJoin.join(nickname, view, this, this);
            }else{
                //subscribe the view to the lobbyList mediator
                reception.subscribe(nickname, view);
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
        if(gameName == null || isNotLogged() || reception.isInGameParty(this.nickname) || reception.isInLobby(this.nickname)){
            malevolentConsequences();
            return;
        }

        //check if the lobby name is already taken
        if(reception.getLobbyByName(gameName)!=null || reception.getGameByName(gameName)!=null) {
            logErr(LogsOnClientStatic.LOBBY_NAME_TAKEN);
            transitionTo(ViewState.JOIN_LOBBY);
            //check if the lobby name is valid
        }else if(gameName.matches(Configs.invalidLobbyNameRegex)) {
            logErr(LogsOnClientStatic.NOT_VALID_LOBBY_NAME);
            transitionTo(ViewState.JOIN_LOBBY);
        }else if(maxPlayerCount < 2 || maxPlayerCount > 4){
            logErr(LogsOnClientStatic.INVALID_MAX_PLAYER_COUNT);
            transitionTo(ViewState.JOIN_LOBBY);
        }else { //create the lobby
            System.out.println(this.nickname + " create" + gameName + " lobby");
            Lobby lobbyCreated = new Lobby(maxPlayerCount, this.nickname, gameName);
            //add the lobby to the model
            reception.addLobby(lobbyCreated);
            //disconnect from lobbyList mediator and subscribe to the new lobby
            reception.unsubscribe(this.nickname); //unsubscribe the view from the lobbyList mediator
            reception.notifyNewLobby(this.nickname, lobbyCreated); //notify the lobbyList mediator of the new lobby creation
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
        if (lobbyName == null || isNotLogged() || reception.isInGameParty(this.nickname) || reception.isInLobby(this.nickname)) {
            malevolentConsequences();
            return;
        }

        Lobby lobbyToJoin = this.reception.getLobbyByName(lobbyName);
        if (lobbyToJoin == null) {
            logErr(LogsOnClientStatic.LOBBY_NONEXISTENT);
            transitionTo(ViewState.JOIN_LOBBY);
        } else if (reception.isLobbyFull(lobbyName)) {
            logErr(LogsOnClientStatic.LOBBY_IS_FULL);
            transitionTo(ViewState.JOIN_LOBBY);
        } else {
            System.out.println(this.nickname + " joined " + lobbyName + " lobby");

            //add the player to the lobby, updated model
            reception.addPlayerToLobby(lobbyName, this.nickname);
            //disconnect from lobbyList mediator and subscribe to the new lobby
            reception.unsubscribe(this.nickname);
            lobbyToJoin.subscribe(this.nickname, this.view, this);

            if (!reception.isLobbyFull(lobbyToJoin.getLobbyName())) {
                transitionTo(ViewState.LOBBY);
            }else{
                if(reception.createGameFromLobby(lobbyToJoin)) {
                    reception.notifyLobbyRemoved(this.nickname, lobbyToJoin);
                    lobbyToJoin.notifyGameStart();
                }
            }
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
        if (isNotLogged() || (!reception.isInGameParty(this.nickname) && !reception.isInLobby(this.nickname))) {
            malevolentConsequences();
            return;
        }
        Lobby lobbyToLeave = reception.leaveLobby(this.nickname);
        if(lobbyToLeave!=null) {
            System.out.println(this.nickname + " left lobby");
            //update the model
            if(reception.checkIfLobbyToRemove(lobbyToLeave.getLobbyName())){
                reception.notifyLobbyRemoved(this.nickname, lobbyToLeave);
            }
            lobbyToLeave.unsubscribe(this.nickname);
            reception.subscribe(this.nickname, view);

            transitionTo(ViewState.JOIN_LOBBY);
        }
    }

    @Override
    public synchronized void chooseSecretObjective(LightCard objectiveCard) {
        if(objectiveCard == null || isNotLogged() || !reception.isInGameParty(this.nickname) ||
                !reception.getGameFromUserNick(this.nickname).inInSecretObjState() &&
                        reception.getUserFromNick(this.nickname).hasChosenObjective()){
            malevolentConsequences();
            return;
        }

        System.out.println(this.nickname + " chose secret objective");
        Game game = reception.getGameFromUserNick(this.nickname);

        transitionTo(ViewState.WAITING_STATE);
        ObjectiveCard objChoice = Heavifier.heavifyObjectCard(objectiveCard, reception.getCardTable());

        //game.chooseSecretObjective(this.nickname, objChoice);
    }

    @Override
    public synchronized void place(LightPlacement placement) {
        if(placement == null || isNotLogged() || !reception.isInGameParty(this.nickname) ||
                !reception.getGameFromUserNick(this.nickname).isYourTurnToPlace(this.nickname)) {
            malevolentConsequences();
            return;
        }
        Game game = reception.getGameFromUserNick(this.nickname);
        User user = game.getUserFromNick(this.nickname);
        Placement heavyPlacement;
        try{
            if(placement.position().equals(new Position(0,0))){
                heavyPlacement = Heavifier.heavifyStartCardPlacement(placement, reception.getCardTable());
                if(!user.getUserHand().getStartCard().equals(heavyPlacement.card())){
                    throw new IllegalArgumentException("The card in the placement in position (0,0) is not the start card in hand");
                }
            }else {
                heavyPlacement = Heavifier.heavify(placement, reception.getCardTable());
                if(!user.getUserHand().getHand().contains(heavyPlacement.card()) || !user.getUserCodex().getFrontier().isInFrontier(placement.position())){
                    throw new IllegalArgumentException("The card in the placement is not in the hand or the position is not in the frontier");
                }
            }
        }catch (Exception e) {
            malevolentConsequences();
            return;
        }
        System.out.println(this.nickname + " placed a card");

        if(!user.hasPlacedStartCard()) {
            transitionTo(ViewState.WAITING_STATE);
            game.placeStartCard(nickname, heavyPlacement);
        }else{
            game.place(nickname, heavyPlacement);
            transitionTo(ViewState.DRAW_CARD);
        }

    }

    private void setupSecretObjectives(){
        Game game = reception.getGameFromUserNick(this.nickname);
        for(User user : game.getUsersList()){
            drawObjectiveCard(user);
        }
        game.secretObjectiveSetup();
    }

    private void moveToSecretObjectivePhase(Game game){
        this.setupSecretObjectives();
        game.notifyMoveToSelectObjState();
    }

    @Override
    public synchronized void draw(DrawableCard deckType, int cardID) {
        if(isNotLogged() || !reception.isInGameParty(this.nickname)){
            malevolentConsequences();
            return;
        }
        System.out.println(this.nickname + " drew a card");

        Game game = reception.getGameFromUserNick(this.nickname);

        game.draw(this.nickname, deckType, cardID);
    }

    private void leaveGame() {
        if(reception.isInGameParty(this.nickname)) {
            Game gameToLeave = reception.getGameFromUserNick(this.nickname);
            gameToLeave.leave(this.nickname);
        }
    }


    @Override
    public synchronized void disconnect() {
        if(this.nickname == null){
            System.out.println("User disconnected before logging in");
        }else{
            if(reception.isInLobby(this.nickname) || reception.isInGameParty(this.nickname)) {
                //TODO multiGame.leave()
                leaveLobby();
                leaveGame();
            }
            reception.removeUser(this.nickname);
            System.out.println(this.nickname + " has disconnected");
        }
    }

    //turnTaker methods
    @Override
    public synchronized void joinStartGame() {
        Game gameToJoin = reception.getGameFromUserNick(this.nickname);

        gameToJoin.joinStartGame(this.nickname, this.view, this);

        User user = gameToJoin.getUserFromNick(this.nickname);
        System.out.println(this.nickname + " joined the game");
        if(!user.hasPlacedStartCard())
            transitionTo(ViewState.CHOOSE_START_CARD);
        else
            transitionTo(ViewState.WAITING_STATE);
    }

    @Override
    public synchronized void chooseObjective() {
        User user = reception.getUserFromNick(this.nickname);
        if(!user.hasChosenObjective()){
            transitionTo(ViewState.SELECT_OBJECTIVE);
        }else
            transitionTo(ViewState.WAITING_STATE);

    }

    @Override
    public void endGame() {
        transitionTo(ViewState.GAME_ENDING);
    }

    /**
     * draw objectiveCard from the deck and set them in the userHand
     * @param user which is drawing the objectiveCard
     */
    private void drawObjectiveCard(User user){
        List<ObjectiveCard> objectiveCards = new ArrayList<>();
        for(int i=0;i<2;i++){
            objectiveCards.add(reception.getGameFromUserNick(this.nickname).getObjectiveCardDeck().drawFromDeck());
        }
        user.getUserHand().setSecretObjectiveChoice(objectiveCards);
    }

    @Override
    public synchronized void takeTurn() {
        Game game = reception.getGameFromUserNick(this.nickname);
        if(game.getCurrentPlayer().getNickname().equals(this.nickname)) {
            transitionTo(ViewState.PLACE_CARD);
        }else{
            transitionTo(ViewState.IDLE);
        }
    }

    //malevolent user checker
    private boolean isNotLogged(){
        return this.nickname == null || !reception.getUsernames().contains(this.nickname);
    }

    private void malevolentConsequences(){
        System.out.println(nickname + " is a malevolent user");
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
