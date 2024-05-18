package it.polimi.ingsw.controller2;


import it.polimi.ingsw.lightModel.Heavifier;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffs.*;
import it.polimi.ingsw.lightModel.diffs.game.*;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyDiffEdit;
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
     * @throws RemoteException in an error occurs during the sending/receiving of the data
     */
    @Override
    public void login(String nickname) throws RemoteException {
        if(!this.games.isUnique(nickname)) {
            logErr(LogsOnClient.NAME_TAKEN);
        }else if(Objects.equals(nickname, "")){
            logErr(LogsOnClient.EMPTY_NAME);
        }else{
            //Client is now logged-In. If he disconnects we have to update the model
            this.nickname = nickname;
            System.out.println(this.nickname + " has connected");
            if(games.isInGameParty(nickname)){
                //The player must join a game
                this.games.getUserGame(nickname).getGameLoopController().joinGame(nickname, this);
            }else{
                this.games.addUser(this, nickname);
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
     * @throws RemoteException If an error occurs during the sending or receiving of data.
     */
    @Override
    public void createLobby(String gameName, int maxPlayerCount) throws RemoteException {
        if(games.getLobbyByName(gameName)!=null){
            logErr(LogsOnClient.LOBBY_NAME_TAKEN);
        }else {
            Lobby newLobby = new Lobby(maxPlayerCount, this.nickname, gameName);
            this.games.addLobby(newLobby);
            unsubscribeLobbyList();
            //Create a new LobbyDiffEditLogin
            subscribeLobby(newLobby);
            newLobby.setPlayerControllers(this, this.nickname);
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
    public void joinLobby(String lobbyName) throws RemoteException{
        Lobby lobbyToJoin = this.games.getLobbyByName(lobbyName);
        if(lobbyToJoin!=null){
            Boolean result = this.games.addPlayerToLobby(lobbyName, this.nickname);
            if(result){
                //create a new lobbyDiffEditLogin
                lobbyToJoin.setPlayerControllers(this, this.nickname);
                unsubscribeLobbyList();
                if(!(lobbyToJoin.getLobbyPlayerList().size() == lobbyToJoin.getNumberOfMaxPlayer())) {
                    subscribeLobby(lobbyToJoin);
                    logYou(LogsOnClient.LOBBY_JOINED);
                    transitionTo(ViewState.LOBBY);
                }else{
                    //Handle the creation of a new game from the lobbyToJoin
                    Game newGame = games.createGame(lobbyToJoin);
                    games.addGame(newGame);

                    //TODO: REMOVE
                    for(ServerModelController controller : lobbyToJoin.getPlayerController().values()){
                        controller.logGame(LogsOnClient.GAME_CREATED);
                    }
                    lobbyToJoin.clearPublisher();
                    //TODO: REMOVE
                    games.removeLobby(lobbyToJoin);
                    newGame.getGameLoopController().joinGame();
                }
            }else{
                logErr(LogsOnClient.LOBBY_IS_FULL);
            }
        }else{
            logErr(LogsOnClient.LOBBY_NONEXISTENT);
        }
    }

    private void joinGame(){
        this.updateLobbyYou(new LittleBoyLobby());
        this.logGame(LogsOnClient.GAME_CREATED);

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

    private void leaveGame(){
        Game gameToLeave = games.getUserGame(this.nickname);
        if(gameToLeave == null){
            throw new IllegalCallerException(nickname + " is not in any game");
        }else{
            gameToLeave.unsubscribe(this);
        }
    }

    /**
     * Set the StartCard chose by the user
     * @param cardFace the startCard Face wich is visible in the codex
     * @throws RemoteException if something goes with the sending of the Diffs
     */
    @Override
    public void selectStartCardFace(CardFace cardFace) throws RemoteException{
        User user = games.getUserFromNick(this.nickname);
        CardWithCorners card = user.getUserHand().getStartCard();
        Placement heavyPlacement = new Placement(new Position(0,0), card, cardFace);
        user.placeStartCard(heavyPlacement);

        Game userGame = games.getUserGame(this.nickname);
        this.updateGame(new HandDiffRemove(Lightifier.lightifyToCard(card))); //remove the startCard from the Hand
        userGame.subscribe(new CodexDiff(this.nickname, user.getUserCodex().getPoints(),
                user.getUserCodex().getEarnedCollectables(), getPlacementList(Lightifier.lightify(heavyPlacement)), user.getUserCodex().getFrontier().getFrontier()));
        userGame.getGameLoopController().startCardPlaced(this);
    }

    /**
     * Set the secretObjectiveCard chose by the user
     * @param card which represent the secret objective choose by the user
     * @throws RemoteException if something goes with the sending of the Diffs
     */
    @Override
    public void choseSecretObjective(LightCard card) throws RemoteException{
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
     * @throws RemoteException if something goes with the sending of the Diffs
     */
    @Override
    public void place(LightPlacement placement) throws RemoteException {
        User user = games.getUserFromNick(nickname);
        Placement heavyPlacement = Heavifier.heavify(placement, this.games);
        user.playCard(heavyPlacement); //place the card and remove it from the hand

        Game userGame = this.games.getUserGame(this.nickname);
        userGame.subscribe(this, new HandDiffRemove(placement.card()), new HandOtherDiffRemove(
                heavyPlacement.card().getPermanentResources(CardFace.BACK).stream().findFirst().orElse(null), this.nickname));
        userGame.subscribe(new CodexDiff(this.nickname, user.getUserCodex().getPoints(),
                user.getUserCodex().getEarnedCollectables(), getPlacementList(placement), user.getUserCodex().getFrontier().getFrontier()));
        for(ServerModelController allControllers : userGame.getGameLoopController().getActivePlayers().values()){
                if(!allControllers.equals(this)){
                    this.logOther(this.nickname, LogsOnClient.PLAYER_PLACED);
                }else{
                    this.logYou(LogsOnClient.YOU_PLACED);
                }
        }
        transitionTo(ViewState.DRAW_CARD);
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
     * @throws RemoteException if something goes with the sending of the Diffs
     */
    @Override
    public void draw(DrawableCard deckID, int cardID) throws RemoteException {
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
                    new HandOtherDiffAdd(drawCard.getPermanentResources(CardFace.BACK).stream().toList().getFirst(), this.nickname));
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
                    deck.showTopCardOfDeck().getPermanentResources(CardFace.BACK).stream().toList().getFirst()));
        } else {
            drawCard = deck.drawFromBuffer(cardID);
            userGame.subscribe(new DeckDiffDeckDraw(drawableCard,
                    deck.showCardFromBuffer(cardID).getPermanentResources(CardFace.BACK).stream().toList().getFirst()));
        }
        return drawCard;
    }

    /**
     * Check where the Player his and remove the reference to his controller.
     * If the player his in a game, unsubscribe his controller and delegate to the GameLoopController what to remove next
     */
    @Override
    public void disconnect(){
        if(this.nickname == null){
            System.out.println("Client disconnected before logging in");
            return; //The client disconnected before logging in, no action needed
        }else{
            System.out.println(this.nickname + " has disconnected");
            this.games.removeUser(this.nickname);//Free the nick from the server, so it can be re-used by other people
            if(games.isInGameParty(this.nickname)){ //Handle the removing of the user from a game
                games.getUserGame(this.nickname).unsubscribe(this);
                this.games.getUserGame(nickname).getGameLoopController().leaveGame(this, this.nickname);
            } else if (games.getUserLobby(this.nickname)!=null) { //Handle the removing of the user from a lobby
                this.leaveLobby();
                games.getUserLobby(this.nickname).unsubscribe(this);
            }else{//Remove the user from the lobbyDiffPublisher list
                games.unsubscribe(this);
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
        }catch (RemoteException r){
            r.printStackTrace();
        }
    }
    public void logOther(String prefix, LogsOnClient log){
        try{
            view.logOthers(prefix + log.getMessage());
        }catch (RemoteException r){
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
