package it.polimi.ingsw.controller;


import it.polimi.ingsw.controller3.Controller;
import it.polimi.ingsw.lightModel.Heavifier;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffs.*;
import it.polimi.ingsw.lightModel.diffs.game.*;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyDiffEditLogin;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyListDiffEdit;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.FatManLobbyList;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.LittleBoyLobby;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.lightModel.diffPublishers.DiffSubscriber;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.cards.CardWithCorners;
import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.*;
import it.polimi.ingsw.model.tableReleted.Deck;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServerModelController implements ControllerInterface, DiffSubscriber {
    private final MultiGame games;
    private final ViewInterface view;
    private String nickname;
    private final Object freezeDisconnect = new Object();
    /**
     * The constructor of the class
     * @param games the reference to the Multi-game on the server
     * @param view The client's view interface associated with this controller.
     */
    public ServerModelController(MultiGame games, ViewInterface view) {
        this.games = games;
        this.view = view;
    }

    /**
     * Log the player into the server.
     * Check if his username is unique and if he is already in a game or not
     * @param nickname of the client who is trying to loggin in
     */
    @Override
    public void login(String nickname) {
        if(!this.games.isUnique(nickname)) {
            logErr(LogsOnClient.NAME_TAKEN);
            transitionTo(ViewState.LOGIN_FORM);
        }else if(Objects.equals(nickname, "")){
            logErr(LogsOnClient.EMPTY_NAME);
            transitionTo(ViewState.LOGIN_FORM);
        }else{
            //Client is now logged-In. If he disconnects we have to update the model
            this.nickname = nickname;
            System.out.println(this.nickname + " has connected");
            if(games.isInGameParty(nickname)){
                //The player must join a game
                this.games.getUserGame(nickname).getGameLoopController().joinGame(nickname, this);
            }else{
                this.games.addUser(nickname);
                subscribeLobbyList();
                logYou(LogsOnClient.SERVER_JOINED);
                transitionTo(ViewState.JOIN_LOBBY);
            }
        }
    }

    private void subscribeLobbyList() {
        games.subscribe(this);
        this.updateLobbyList(new LobbyListDiffEdit(new ArrayList<>(games.getLobbies().stream().map(Lightifier::lightify).toList()),new ArrayList<>()));
    }

    /**
     * Creates a newLobby with the specified game name and maximum player count,
     * and sends the diff  to all subscribers in the gamesPublisher list.
     * @param gameName The name of the new lobby being created.
     * @param maxPlayerCount The number of players needed to exit the lobby and start the game.
     */
    @Override
    public void createLobby(String gameName, int maxPlayerCount) {
        if(games.getLobbyByName(gameName)!=null || games.getGameByName(gameName)!=null  || gameName.isEmpty()) {
            logErr(LogsOnClient.LOBBY_NAME_TAKEN);
            transitionTo(ViewState.JOIN_LOBBY);
        }else {
            Lobby newLobby = new Lobby(maxPlayerCount, this.nickname, gameName);
            this.games.addLobby(newLobby);
            unsubscribeLobbyList();
            //Create a new LobbyDiffEditLogin
            subscribeLobby(newLobby);
            logGame(LogsOnClient.LOBBY_CREATED);
            logYou(LogsOnClient.LOBBY_JOINED);
            transitionTo(ViewState.LOBBY);
        }
    }

    private void unsubscribeLobbyList(){
        games.unsubscribe(this);
        this.updateLobbyList(new FatManLobbyList());
    }

    private void subscribeLobby(Lobby lobbyToJoin){
        lobbyToJoin.subscribe(this);
        ArrayList<String> addNicknames = new ArrayList<>(lobbyToJoin.getLobbyPlayerList());
        this.updateLobbyYou(new LobbyDiffEditLogin(addNicknames, new ArrayList<>(), lobbyToJoin.getLobbyName(), lobbyToJoin.getNumberOfMaxPlayer()));
    }

    @Override
    public void joinLobby(String lobbyName){
        Lobby lobbyToJoin = this.games.getLobbyByName(lobbyName);
        if(lobbyToJoin!=null){
            Boolean result = this.games.addPlayerToLobby(lobbyName, this.nickname);
            if(result){
                //create a new lobbyDiffEditLogin
                unsubscribeLobbyList();
                subscribeLobby(lobbyToJoin);
                if(!(lobbyToJoin.getLobbyPlayerList().size() == lobbyToJoin.getNumberOfMaxPlayer())) {
                    logYou(LogsOnClient.LOBBY_JOINED);
                    transitionTo(ViewState.LOBBY);
                }else{
                    //Handle the creation of a new game from the lobbyToJoin
                    synchronized (freezeDisconnect) {
                        Game newGame = games.createGame(lobbyToJoin);
                        games.addGame(newGame);
                        lobbyToJoin.notifyStartGame();
                        games.removeLobby(lobbyToJoin);
                        newGame.getGameLoopController().joinGame();
                    }
                }
            }else{
                logErr(LogsOnClient.LOBBY_IS_FULL);
                transitionTo(ViewState.JOIN_LOBBY);
            }
        }else{
            logErr(LogsOnClient.LOBBY_NONEXISTENT);
            transitionTo(ViewState.JOIN_LOBBY);
        }
    }

    public void gameStarted(){
        this.updateLobbyYou(new LittleBoyLobby());
        this.logGame(LogsOnClient.GAME_CREATED);
        Game newGame = games.getUserGame(this.nickname);
        if(newGame != null)
            newGame.getGameLoopController().addActivePlayer(this.nickname, this);
        else
            throw new IllegalCallerException("The game has not been created");
    }

    @Override
    public void leaveLobby(){
        Lobby lobbyToLeave = games.getUserLobby(this.nickname);
        if(lobbyToLeave==null){
            throw new IllegalCallerException();
        }else{
            lobbyToLeave.removeUserName(this.nickname);
            if(lobbyToLeave.getLobbyPlayerList().isEmpty()){
                this.games.removeLobby(lobbyToLeave);
            }
            unsubscribeLobby(lobbyToLeave);
            subscribeLobbyList();
            logYou(LogsOnClient.LOBBY_LEFT);
            transitionTo(ViewState.JOIN_LOBBY);
        }
    }

    private void unsubscribeLobby(Lobby lobbyToLeave){
        lobbyToLeave.unsubscribe(this);
        updateLobbyYou(new LittleBoyLobby());
    }

    /**
     * Set the secretObjectiveCard chose by the user
     * @param card which represent the secret objective choose by the user
     */
    @Override
    public void choseSecretObjective(LightCard card){
        User userToEdit = games.getUserFromNick(this.nickname);
        Game userGame = games.getUserGame(nickname);
        userToEdit.setSecretObject(Heavifier.heavifyObjectCard(card, games));
        userToEdit.getUserHand().setSecretObjectiveChoice(null);
        this.updateGame(new HandDiffSetObj(card));
        userGame.getGameLoopController().secretObjectiveChose(this);
    }

    /**
     * Update the deck, Hand, Codex for the player and others
     * @param placement the LightPlacement created by placing the card
     */
    @Override
    public void place(LightPlacement placement) {
        User user = games.getUserFromNick(nickname);
        //if the card place is the startCard
        if(user.getUserCodex().getFrontier().isInFrontier(new Position(0,0))){
            Placement heavyPlacement = Heavifier.heavifyStartCardPlacement(placement, this.games);
            user.placeStartCard(heavyPlacement);

            Game userGame = games.getUserGame(this.nickname);
            this.updateGame(new HandDiffRemove(placement.card())); //remove the startCard from the Hand
            userGame.subscribe(new CodexDiff(this.nickname, user.getUserCodex().getPoints(),
                    user.getUserCodex().getEarnedCollectables(), getPlacementList(placement), user.getUserCodex().getFrontier().getFrontier()));
            userGame.getGameLoopController().startCardPlaced(this);
        }else {
            Placement heavyPlacement = Heavifier.heavify(placement, this.games);
            user.playCard(heavyPlacement); //place the card and remove it from the hand

            Game userGame = this.games.getUserGame(this.nickname);
            userGame.subscribe(this, new HandDiffRemove(placement.card()), new HandOtherDiffRemove(
                    new LightBack(heavyPlacement.card().getIdBack()), this.nickname));
            userGame.subscribe(new CodexDiff(this.nickname, user.getUserCodex().getPoints(),
                    user.getUserCodex().getEarnedCollectables(), getPlacementList(placement), user.getUserCodex().getFrontier().getFrontier()));
            for (ServerModelController allControllers : userGame.getGameLoopController().getActivePlayers().values()) {
                if (!allControllers.equals(this)) {
                    this.logOther(this.nickname, LogsOnClient.PLAYER_PLACED);
                } else {
                    this.logYou(LogsOnClient.YOU_PLACED);
                }
            }
            transitionTo(ViewState.DRAW_CARD);
        }
    }

    /**
     * @param placement the new Placement that is being added
     * @return a mockList containing only the newPlacement
     */
    private List<LightPlacement> getPlacementList(LightPlacement placement){
        List<LightPlacement> list = new ArrayList<>();
        list.add(placement);
        return list;
    }

    /**
     * Draw a card from the deckID-deck in the cardID-position
     * Transition to the newPlayer and set the current one to Idle
     * @param deckID the deck from which the card is drawn
     * @param cardID the position from where draw the card (buffer/deck)
     */
    @Override
    public void draw(DrawableCard deckID, int cardID) {
        CardInHand drawCard;
        if(cardID<0 || cardID>2){
            throw  new IllegalArgumentException(cardID + " is out of bound");
        }else{
            Game userGame = games.getUserGame(this.nickname);
            if(deckID == DrawableCard.GOLDCARD){
                Deck<GoldCard> goldDeck = userGame.getGoldCardDeck();
                drawCard = drawACard(goldDeck, DrawableCard.GOLDCARD, cardID, userGame);
            }else {
                Deck<ResourceCard> resourceDeck = userGame.getResourceCardDeck();
                drawCard = drawACard(resourceDeck, DrawableCard.RESOURCECARD, cardID, userGame);
            }
            logYou(LogsOnClient.CARD_DRAWN);
            User user = games.getUserFromNick(this.nickname);
            user.getUserHand().addCard(drawCard);
            userGame.subscribe(this, new HandDiffAdd(Lightifier.lightifyToCard(drawCard), drawCard.canBePlaced(user.getUserCodex())),
                    new HandOtherDiffAdd(new LightBack(drawCard.getIdBack()), this.nickname));
            userGame.getGameLoopController().cardDrawn(this);
        }
    }

    /**
     * @param deck from which drawn a Card
     * @param drawableCard the type of card being drawn
     * @param cardID the position from where draw the card (buffer/deck)
     * @param userGame  the game where the card is being drawn
     * @return the card drawn
     * @param <T> a CardInHand (GoldCard/ResourceCard)
     */
    private <T extends CardInHand> T drawACard(Deck<T> deck, DrawableCard drawableCard, int cardID, Game userGame) {
        T drawCard;
        if (cardID == 2) {
            drawCard = deck.drawFromDeck();
            userGame.subscribe(new DeckDiffDeckDraw(drawableCard,
                    new LightBack(deck.showTopCardOfDeck().getIdBack())));
        } else {
            drawCard = deck.drawFromBuffer(cardID);
            userGame.subscribe(new DeckDiffBufferDraw(Lightifier.lightifyToCard(drawCard),cardID, drawableCard));
        }
        return drawCard;
    }

    /**
     * Check where the Player his and remove the reference to his controller.
     * If the player his in a game, unsubscribe his controller and delegate to the GameLoopController what to remove next
     */
    @Override
    public void disconnect(){
        synchronized (freezeDisconnect) {
            if (this.nickname == null) {
                System.out.println("Client disconnected before logging in");
                return; //The client disconnected before logging in, no action needed
            } else {
                System.out.println(this.nickname + " has disconnected");
                this.games.removeUser(this.nickname);//Free the nick from the server, so it can be re-used by other people
                if (games.isInGameParty(this.nickname)) { //Handle the removing of the user from a game
                    Game userGame = games.getUserGame(this.nickname);
                    userGame.unsubscribe(this);
                    userGame.getGameLoopController().leaveGame(this);
                    //If the game is empty, remove it from the MultiGame
                    if(userGame.getGameLoopController().getActivePlayers().isEmpty()){
                        games.removeGame(userGame);
                    }
                } else if (games.getUserLobby(this.nickname) != null) { //Handle the removing of the user from a lobby
                    this.leaveLobby();
                    games.getUserLobby(this.nickname).unsubscribe(this);
                } else {//Remove the user from the lobbyDiffPublisher list
                    games.unsubscribe(this);
                }
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ServerModelController) {
            ServerModelController other = (ServerModelController) obj;
            return this.nickname.equals(other.nickname);
        } else {
            return false;
        }
    }



    public ViewInterface getView() {
        return view;
    }
    public void log(LogsOnClient log){
        try {
            view.log(log.getMessage());
        }catch (Exception r){
            r.printStackTrace();
        }
    }
    public void transitionTo(ViewState state){
        System.out.println(nickname + ":" + state);
        try {
            view.transitionTo(state);
        }catch (Exception r){
            r.printStackTrace();
        }
    }
    public void updateLobbyYou(ModelDiffs<LightLobby> diff){
        try {
            view.updateLobby(diff);
        }catch (Exception r){
            r.printStackTrace();
        }
    }
    public void updateLobby(ModelDiffs<LightLobby> diff){
        try {
            view.logOthers(this.nickname + LogsOnClient.PLAYER_JOIN_LOBBY.getMessage());
            view.updateLobby(diff);
        }catch (Exception r){
            r.printStackTrace();
        }
    }
    public void updateLobbyList(ModelDiffs<LightLobbyList> diff){
        System.out.println(nickname + " updated lobbyList");
        try {
            view.updateLobbyList(diff);
        }catch (Exception r){
            r.printStackTrace();
        }
    }
    public void updateGame(ModelDiffs<LightGame> diff){
        try {
            view.updateGame(diff);
        }catch (Exception r) {
            r.printStackTrace();
        }
    }

    public void logYou(LogsOnClient log){
        try {
            view.log(log.getMessage());
        }catch (Exception r){
            r.printStackTrace();
        }
    }
    public void logOther(String prefix, LogsOnClient log){
        try{
            view.logOthers(prefix + log.getMessage());
        }catch (Exception r){
            r.printStackTrace();
        }
    }
    public void logErr(LogsOnClient log){
        try {
            view.logErr(log.getMessage());
        }catch (Exception r) {
            r.printStackTrace();
        }
    }

    public void logGame(LogsOnClient log){
        try {
            view.logGame(log.getMessage());
        }catch (Exception r){
            r.printStackTrace();
        }
    }
    /**
     * @return user nick
     */
    public String getNickname() {
        return nickname;
    }
}
