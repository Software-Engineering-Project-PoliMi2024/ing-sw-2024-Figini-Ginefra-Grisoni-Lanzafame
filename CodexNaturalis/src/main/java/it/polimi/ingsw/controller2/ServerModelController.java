package it.polimi.ingsw.controller2;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffs.LobbyListDiff;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.lightModel.diffObserverInterface.DiffSubscriber;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.rmi.RemoteException;
import java.util.ArrayList;

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
                games.subscribe(view);
                try {
                    view.log(LogsFromServer.SERVER_JOINED.getMessage());
                    view.transitionTo(ViewState.JOIN_LOBBY);
                }catch (RemoteException r) {
                    r.printStackTrace();
                }
            }
        }
    }

    @Override
    public void getActiveLobbyList() throws RemoteException{
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

    private LobbyListDiff getAddLobbyDiff(Lobby lobby) throws RemoteException{
        ArrayList<LightLobby> listDiffAdd = new ArrayList<>();
        listDiffAdd.add(Lightifier.lightify(lobby));
        return new LobbyListDiff(listDiffAdd, new ArrayList<>());
    }

    @Override
    public void joinLobby(String lobbyName) throws RemoteException{
        Lobby lobbyToJoin = this.games.getLobby(lobbyName);
        if(lobbyToJoin!=null){
            Boolean result = lobbyToJoin.addUserName(this.nickname);
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
                    Game newGame = new Game(lobbyToJoin);
                    for (DiffSubscriber diffSub : lobbyToJoin.getSubscribers()) {
                        lobbyToJoin.unsubscribe(diffSub);
                        this.joinGame(newGame, LogsFromServer.NEW_GAME_JOINED);
                    }
                }
            }
        }else{
            try {
                view.log(LogsFromServer.LOBBY_INEXISTENT.getMessage());
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
    private LobbyListDiff getRemoveLobbyDiff(Lobby lobby) throws RemoteException{
        ArrayList<LightLobby> listDiffRmv = new ArrayList<>();
        listDiffRmv.add(Lightifier.lightify(lobby));
        return new LobbyListDiff(new ArrayList<>(), listDiffRmv);
    }
    public void joinGame(Game game, LogsFromServer log) throws RemoteException{
        game.subcribe(view, this.nickname);
        try {
            view.transitionTo(ViewState.CHOOSE_START_CARD);
            view.log(log.getMessage());
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

    @Override
    public void selectStartCardFace(LightCard card, CardFace cardFace) throws RemoteException{

    }

    @Override
    public void choseSecretObjective(LightCard objectiveCard) throws RemoteException{

    }

    @Override
    public void place(LightPlacement placement) throws RemoteException{

    }

    @Override
    public void draw(DrawableCard deckID, int cardID) throws RemoteException{

    }
}
