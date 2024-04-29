package it.polimi.ingsw.controller2;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.LightPlacement;
import it.polimi.ingsw.lightModel.diffLists.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffLists.GameDiffPublisher;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.GameParty;
import it.polimi.ingsw.model.tableReleted.Lobby;
import it.polimi.ingsw.view.ViewState;

public class ServerImplementation implements ControllerInterfaceServer, Runnable{
    protected final MultiGame games;

    public ServerImplementation(MultiGame games) {
        this.games = games;
    }
    public void run() {
    }

    @Override
    public void login(String nickname, DiffSubscriber diffSubscriber) {
        if(!this.games.isUnique(nickname)){
            diffSubscriber.log(LogsFromServer.NAME_TAKEN.getMessage());
        }else{
            if(this.games.inGame(nickname)!=null){
                this.joinGame(this.games.inGame(nickname), diffSubscriber, LogsFromServer.MID_GAME_JOINED);
            }else{
                this.games.addUser(nickname);
                games.subscribe(diffSubscriber);
                diffSubscriber.log(LogsFromServer.SERVER_JOINED.getMessage());
                diffSubscriber.transitionTo(ViewState.JOIN_LOBBY);
            }
        }
    }

    @Override
    public void getActiveLobbyList(DiffSubscriber diffSubscriber) {
        games.subscribe(diffSubscriber);
    }

    @Override
    public void createLobby(String gameName, int maxPlayerCount, DiffSubscriber diffSubscriber) {
        Lobby newLobby = new Lobby(maxPlayerCount, diffSubscriber.getNickname(), gameName);
        this.games.addLobby(newLobby);
        games.unsubscribe(diffSubscriber);
        newLobby.subscribe(diffSubscriber);
        diffSubscriber.log(LogsFromServer.LOBBY_CREATED.getMessage());
        diffSubscriber.transitionTo(ViewState.LOBBY);
    }

    @Override
    public void joinLobby(String lobbyName, DiffSubscriber diffSubscriber) {
        Lobby lobbyToJoin = this.games.getLobby(lobbyName);
        Boolean result = lobbyToJoin.addUserName(diffSubscriber.getNickname());
        if(result){
            lobbyToJoin.subscribe(diffSubscriber);
            diffSubscriber.log(LogsFromServer.LOBBY_JOINED.getMessage());
            diffSubscriber.transitionTo(ViewState.LOBBY);
            if(lobbyToJoin.getLobbyPlayerList().size() == lobbyToJoin.getNumberOfMaxPlayer()){
                //Handle the creation of a new game from the lobby
                Game newGame = new Game(lobbyToJoin);
                for(DiffSubscriber diffSub : lobbyToJoin.getSubscribers()){
                    lobbyToJoin.unsubscribe(diffSub);
                    this.joinGame(newGame, diffSub, LogsFromServer.NEW_GAME_JOINED);
                }
            }
        }else{
            diffSubscriber.log(LogsFromServer.LOBBY_IS_FULL.getMessage());
        }
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void leaveLobby(DiffSubscriber diffSubscriber) {
        Lobby lobbyToLeave = this.games.getLobby(diffSubscriber.getTableName());
        lobbyToLeave.unsubscribe(diffSubscriber);
        games.subscribe(diffSubscriber);
        lobbyToLeave.removeUserName(diffSubscriber.getNickname());
        diffSubscriber.log(LogsFromServer.LOBBY_LEFT.getMessage());
        diffSubscriber.transitionTo(ViewState.JOIN_LOBBY);
        synchronized (lobbyToLeave){
            if(lobbyToLeave.getLobbyPlayerList().isEmpty()){
                this.games.removeLobby(lobbyToLeave);
            }
        }
    }

    @Override
    public void joinGame(Game game, DiffSubscriber diffSubscriber, LogsFromServer log) {
        game.subcribe(diffSubscriber);
        diffSubscriber.transitionTo(ViewState.CHOOSE_START_CARD);
        diffSubscriber.log(log.getMessage());
    }

    @Override
    public void leaveGame() {

    }

    @Override
    public void selectStartCardFace(LightCard card, CardFace cardFace) {

    }

    @Override
    public void choseSecretObjective(LightCard objectiveCard) {

    }

    @Override
    public void place(LightPlacement placement) {

    }

    @Override
    public void draw(DrawableCard deckID, int cardID) {

    }
}
