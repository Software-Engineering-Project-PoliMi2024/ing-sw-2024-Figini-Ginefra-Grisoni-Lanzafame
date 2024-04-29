package it.polimi.ingsw.controller2;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.LightPlacement;
import it.polimi.ingsw.lightModel.diffLists.DiffSubscriber;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;
import it.polimi.ingsw.view.ViewState;

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
    public void login(String nickname) {
        if(!this.games.isUnique(nickname)){
            view.log(LogsFromServer.NAME_TAKEN.getMessage());
        }else{
            if(this.games.inGame(nickname)!=null){
                this.joinGame(this.games.inGame(nickname), LogsFromServer.MID_GAME_JOINED);
            }else{
                this.nickname = nickname;
                this.games.addUser(this, nickname);
                games.subscribe(view);
                view.log(LogsFromServer.SERVER_JOINED.getMessage());
                view.transitionTo(ViewState.JOIN_LOBBY);
            }
        }
    }

    @Override
    public void getActiveLobbyList() {
        games.subscribe(view);
    }

    @Override
    public void createLobby(String gameName, int maxPlayerCount) {
        Lobby newLobby = new Lobby(maxPlayerCount, this.nickname, gameName);
        this.games.addLobby(newLobby);
        games.unsubscribe(this.view);
        newLobby.subscribe(this.view, this.nickname);
        view.log(LogsFromServer.LOBBY_CREATED.getMessage());
        view.transitionTo(ViewState.LOBBY);
    }

    @Override
    public void joinLobby(String lobbyName) {
        Lobby lobbyToJoin = this.games.getLobby(lobbyName);
        Boolean result = lobbyToJoin.addUserName(this.nickname);
        if(result){
            lobbyToJoin.subscribe(this.view, this.nickname);
            view.log(LogsFromServer.LOBBY_JOINED.getMessage());
            view.transitionTo(ViewState.LOBBY);
            if(lobbyToJoin.getLobbyPlayerList().size() == lobbyToJoin.getNumberOfMaxPlayer()){
                //Handle the creation of a new game from the lobby
                Game newGame = new Game(lobbyToJoin);
                for(DiffSubscriber diffSub : lobbyToJoin.getSubscribers()){
                    lobbyToJoin.unsubscribe(diffSub);
                    this.joinGame(newGame, LogsFromServer.NEW_GAME_JOINED);
                }
            }
        }else{
            view.log(LogsFromServer.LOBBY_IS_FULL.getMessage());
        }
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void leaveLobby() {
        Lobby lobbyToLeave = null;
        for(Lobby lobby : games.getLobbies()){
            if(lobby.getLobbyName().contains(this.nickname)){
                 lobbyToLeave = lobby;
                break;
            }
        }
        if(lobbyToLeave==null){
            throw new IllegalCallerException();
        }else{
            lobbyToLeave.unsubscribe(view);
            games.subscribe(view);
            lobbyToLeave.removeUserName(this.nickname);
            view.log(LogsFromServer.LOBBY_LEFT.getMessage());
            view.transitionTo(ViewState.JOIN_LOBBY);
            if(lobbyToLeave.getLobbyPlayerList().isEmpty()){
                this.games.removeLobby(lobbyToLeave);
            }
        }
    }
    public void joinGame(Game game, LogsFromServer log) {
        game.subcribe(view, this.nickname);
        view.transitionTo(ViewState.CHOOSE_START_CARD);
        view.log(log.getMessage());
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
