package it.polimi.ingsw.controller2;

import it.polimi.ingsw.lightModel.Heavifier;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffs.*;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.lightModel.diffObserverInterface.DiffSubscriber;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.cardReleted.cards.Card;
import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
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
import java.util.Map;

public class ServerModelController implements ControllerInterface {
    private final MultiGame games;
    private final ViewInterface view;
    private String nickname;


    public ServerModelController(MultiGame games, ViewInterface view) {
        this.games = games;
        this.view = view;
    }
    public void run() {
    }

    @Override
    public void login(String nickname) throws RemoteException {
        if(!this.games.isUnique(nickname)){
            try {
                view.log(LogsFromServer.NAME_TAKEN.getMessage());
            }catch (RemoteException r){
                r.printStackTrace();
            }
        }else{
            if(games.inGame(nickname)){
                this.joinGame(this.games.getUserGame(nickname), LogsFromServer.MID_GAME_JOINED);
            }else{
                this.nickname = nickname;
                this.games.addUser(this, nickname);
                getActiveLobbyList();
                try {
                    view.log(LogsFromServer.SERVER_JOINED.getMessage());
                    view.transitionTo(ViewState.JOIN_LOBBY);
                }catch (RemoteException r) {
                    r.printStackTrace();
                }
            }
        }
    }
    private void getActiveLobbyList() {
        games.subscribe(view);
    }

    @Override
    public void createLobby(String gameName, int maxPlayerCount) throws RemoteException {
        if(games.getLobby(gameName)!=null){
            view.log(LogsFromServer.LOBBY_NAME_TAKEN.getMessage());
        }else {
            Lobby newLobby = new Lobby(maxPlayerCount, this.nickname, gameName);
            this.games.addLobby(newLobby);
            games.unsubscribe(this.view);
            newLobby.subscribe(this.view, this.nickname);
            games.subscribe(getAddLobbyDiff(newLobby));
            try {
                view.log(LogsFromServer.LOBBY_CREATED.getMessage());
                view.transitionTo(ViewState.LOBBY);
            } catch (RemoteException r) {
                r.printStackTrace();
            }
        }
    }

    private LobbyListDiffEdit getAddLobbyDiff(Lobby lobby) throws RemoteException{
        ArrayList<LightLobby> listDiffAdd = new ArrayList<>();
        listDiffAdd.add(Lightifier.lightify(lobby));
        return new LobbyListDiffEdit(listDiffAdd, new ArrayList<>());
    }

    @Override
    public void joinLobby(String lobbyName) throws RemoteException{
        Lobby lobbyToJoin = this.games.getLobby(lobbyName);
        if(lobbyToJoin!=null){
            Boolean result = this.games.addPlayerToLobby(lobbyName, this.nickname);
            if(result){
                lobbyToJoin.subscribe(this.view, this.nickname);
                try {
                    view.log(LogsFromServer.LOBBY_JOINED.getMessage());
                    view.transitionTo(ViewState.LOBBY);
                }catch (RemoteException r){
                    r.printStackTrace();
                }
                if(lobbyToJoin.getLobbyPlayerList().size() == lobbyToJoin.getNumberOfMaxPlayer()) {
                    //Handle the creation of a new game from the lobby
                    Game newGame = games.createGame(lobbyToJoin);
                    for (DiffSubscriber diffSub : lobbyToJoin.getSubscribers()) {
                        lobbyToJoin.unsubscribe(diffSub);
                        this.joinGame(newGame, LogsFromServer.NEW_GAME_JOINED);
                    }
                }
            }else{
                view.log(LogsFromServer.LOBBY_IS_FULL.getMessage());
            }
        }else{
            try {
                view.log(LogsFromServer.LOBBY_NONEXISTENT.getMessage());
            }catch (RemoteException r){
                r.printStackTrace();
            }
        }
    }

    @Override
    public void disconnect() throws RemoteException{

    }

    @Override
    public void leaveLobby() throws RemoteException{
        Lobby lobbyToLeave = games.getUserLobby(this.nickname);
        if(lobbyToLeave==null){
            throw new IllegalCallerException();
        }else{
            lobbyToLeave.unsubscribe(view);
            games.subscribe(view);
            lobbyToLeave.removeUserName(this.nickname);
            try {
                view.log(LogsFromServer.LOBBY_LEFT.getMessage());
                view.transitionTo(ViewState.JOIN_LOBBY);
            }catch (RemoteException r){
                r.printStackTrace();
            }
            if(lobbyToLeave.getLobbyPlayerList().isEmpty()){
                this.games.removeLobby(lobbyToLeave);
                games.subscribe(getRemoveLobbyDiff(lobbyToLeave));
            }
        }
    }
    private LobbyListDiffEdit getRemoveLobbyDiff(Lobby lobby) throws RemoteException{
        ArrayList<LightLobby> listDiffRmv = new ArrayList<>();
        listDiffRmv.add(Lightifier.lightify(lobby));
        return new LobbyListDiffEdit(new ArrayList<>(), listDiffRmv);
    }

    /**
     * Connects the user to the game. If it is the first time (the game is just being created), transitions the view
     * to the CHOOSE_START_CARD state; otherwise, transitions the view to IDLE.
     * @param game The game being accessed.
     * @param log The log to be sent to the view (NEW_GAME_JOINED/MID_GAME_JOINED).
     * @throws RemoteException If an error occurs during the sending of Diffs.
     */
    public void joinGame(Game game, LogsFromServer log) throws RemoteException{
        game.subcribe(view, this.nickname);
        try {
            LightCard lightStartCard = Lightifier.lightifyToCard(game.getStartingCardDeck().drawFromDeck());
            game.subcribe(new HandDiffAdd(lightStartCard, true));
            view.log(log.getMessage());
            if(log == LogsFromServer.NEW_GAME_JOINED){
                view.transitionTo(ViewState.CHOOSE_START_CARD);
            }else{
                view.transitionTo(ViewState.IDLE);
            }
        }catch (RemoteException r){
            r.printStackTrace();
        }
    }

    private void leaveGame(){
        Game gameToLeave = games.getUserGame(this.nickname);
        if(gameToLeave == null){
            throw new IllegalCallerException(nickname + " is not in any game");
        }else{
            gameToLeave.unsubscrive(view);
        }
    }

    /**
     * Set the StartCard chose by the user. Transition the view to the SELECET_OBJECTIVE
     * @param card which is the StartCard chose by the user
     * @param cardFace the startCard Face wich is visible in the codex
     * @throws RemoteException if something goes with the sending of the Diffs
     */
    @Override
    public void selectStartCardFace(LightCard card, CardFace cardFace) throws RemoteException{
        User user = games.getUserFromNick(this.nickname);
        Placement heavyPlacement = new Placement(new Position(0,0), Heavifier.heavifyStartCard(card, games), cardFace);
        user.playCard(heavyPlacement);
        Game userGame = games.getUserGame(this.nickname);
        view.updateGame(new HandDiffRemove(card));
        userGame.subcribe(new CodexDiff(this.nickname, user.getUserCodex().getPoints(),
                user.getUserCodex().getEarnedCollectables(), getPlacementList(Lightifier.lightify(heavyPlacement)), user.getUserCodex().getFrontier().getFrontier()));
        view.log(LogsFromServer.START_CARD_PLACED.getMessage());
        //Send the secretObjectiveCard choice to the view
        for(LightCard secretObjectiveCardChoice : drawObjectiveCard()){
            view.updateGame(new HandDiffAdd(secretObjectiveCardChoice, true));
        }
        view.transitionTo(ViewState.SELECT_OBJECTIVE);
    }

    /**
     * Set the secretObjectiveCard chose by the user
     * Start the game loop from the currentPlayer by placing his view to PLACECARD. The others view are in Idle
     * @param card which represent the secret objective choose by the user
     * @throws RemoteException if something goes with the sending of the Diffs
     */
    @Override
    public void choseSecretObjective(LightCard card) throws RemoteException{
        User userToEdit = games.getUserFromNick(this.nickname);
        Game userGame = games.getUserGame(nickname);
        userToEdit.setSecretObject(Heavifier.heavifyObjectCard(card, games));
        view.log(LogsFromServer.SECRET_OBJECTIVE_CHOSE.getMessage());
        String nextPlayerNick = userGame.getGameParty().getCurrentPlayer().getNickname();
        if(this.nickname.equals(nextPlayerNick)){
            view.transitionTo(ViewState.IDLE);
        }else{
            view.transitionTo(ViewState.PLACE_CARD);
        }
    }

    /**
     * @return two random ObjectiveCard from the ObjectiveCard deck
     */
    private List<LightCard> drawObjectiveCard(){
        Game userGame = games.getUserGame(this.nickname);
        List<LightCard> cardList = new ArrayList<>();
        for(int i=0;i<1;i++){
            cardList.add(Lightifier.lightifyToCard(userGame.getObjectiveCardDeck().drawFromDeck()));
        }
        return cardList;
    }

    /**
     * Update the deck, trasition the player to DrawCard
     * @param placement the LightPlacement created by placing the card
     * @throws RemoteException if something goes with the sending of the Diffs
     */
    @Override
    public void place(LightPlacement placement) throws RemoteException {
        User user = games.getUserFromNick(nickname);
        Placement heavyPlacement = Heavifier.heavify(placement, this.games);
        user.playCard(heavyPlacement);
        Game userGame = this.games.getUserGame(this.nickname);
        userGame.subcribe(view, new HandDiffRemove(placement.card()), new HandOtherDiffRemove(
                heavyPlacement.card().getPermanentResources(CardFace.BACK).stream().toList().getFirst(), this.nickname));
        userGame.subcribe(new CodexDiff(this.nickname, user.getUserCodex().getPoints(),
                user.getUserCodex().getEarnedCollectables(), getPlacementList(placement), user.getUserCodex().getFrontier().getFrontier()));
        view.log(LogsFromServer.CARD_PLACED.getMessage());
        view.transitionTo(ViewState.DRAW_CARD);
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
            User user = games.getUserFromNick(this.nickname);
            user.getUserHand().addCard(drawCard);
            userGame.subcribe(view, new HandDiffAdd(Lightifier.lightifyToCard(drawCard), drawCard.canBePlaced()),
                    new HandOtherDiffAdd(drawCard.getPermanentResources(CardFace.BACK).stream().toList().getFirst(), this.nickname));

            userGame.getGameParty().nextPlayer();
            String nextPlayerNickname = userGame.getGameParty().getCurrentPlayer().getNickname();
            userGame.subcribe(new GameDiffRound(nextPlayerNickname));
            ViewInterface nextPlayerView = nextPlayer(nextPlayerNickname).getView();
            nextPlayerView.log(LogsFromServer.YOUR_TURN.getMessage());
            nextPlayerView.transitionTo(ViewState.PLACE_CARD);

            view.log(LogsFromServer.CARD_DRAWN.getMessage());
            view.transitionTo(ViewState.IDLE);
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
            userGame.subcribe(new DeckDiffDeckDraw(drawableCard,
                    deck.showTopCardOfDeck().getPermanentResources(CardFace.BACK).stream().toList().getFirst()));
        } else {
            drawCard = deck.drawFromBuffer(cardID);
            userGame.subcribe(new DeckDiffDeckDraw(drawableCard,
                    deck.showCardFromBuffer(cardID).getPermanentResources(CardFace.BACK).stream().toList().getFirst()));
        }
        return drawCard;
    }

    /**
     * @param nextPlayerNickName the nickname of the new currentPlayer
     * @return the Controller of the new currentPlayer
     */
    public ServerModelController nextPlayer(String nextPlayerNickName){
        ServerModelController nextplayerContoller  = null;
       for(Map.Entry<ServerModelController, String> entry : this.games.getUsernameMap().entrySet()){
           if(entry.getValue().equals(nextPlayerNickName)){
               nextplayerContoller=entry.getKey();
               break;
           }
       }
       if (nextplayerContoller == null){
           throw new IllegalCallerException(nextPlayerNickName + " is not in the server");
       }else{
           return nextplayerContoller;
       }
    }

    public ViewInterface getView() {
        return view;
    }

}
