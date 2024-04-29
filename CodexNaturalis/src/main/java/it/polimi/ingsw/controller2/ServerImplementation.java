package it.polimi.ingsw.controller2;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.LightPlacement;
import it.polimi.ingsw.lightModel.diffLists.DiffSubscriber;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.tableReleted.Lobby;
import it.polimi.ingsw.view.ViewState;

public class ServerImplementation implements ControllerInterfaceServer, Runnable{
    private final MultiGame games;
    private final ViewInterface view;
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public ServerImplementation(MultiGame games, ViewInterface view) {
        this.games = games;
        this.view = view;
    }
    public void run() {
    }

    @Override
    public void login(String nickname) {

    }

    @Override
    public void getActiveLobbyList(DiffSubscriber diffSubscriber) {
        games.subscribe(diffSubscriber);
    }

    @Override
    public void createLobby(String gameName, int maxPlayerCount, DiffSubscriber diffSubscriber) {
        Lobby newLobby = new Lobby(maxPlayerCount, this.nickname, gameName);
        //TODO check if the lobby name is taken
        //TODO the addLobby method should generate the diff to subscribe to the publisher
        this.games.addLobby(newLobby);

        newLobby.subscribe(diffSubscriber);
        view.log(LogsFromServer.LOBBY_CREATED.getMessage());
        view.transitionTo(ViewState.LOBBY);
    }

    @Override
    public void joinLobby(String lobbyName, DiffSubscriber diffSubscriber) {
        Lobby lobbyToJoin = this.games.getLobby(lobbyName);
        Boolean result = lobbyToJoin.addUserName(this.nickname);
        if(result){
            //TODO the subscribe should be implemented in lobby.addUserName
            lobbyToJoin.subscribe(diffSubscriber);
            view.log(LogsFromServer.LOBBY_JOINED.getMessage());
            view.transitionTo(ViewState.LOBBY);
            if(lobbyToJoin.getLobbyPlayerList().size() == lobbyToJoin.getNumberOfMaxPlayer()){

            }
        }else{
            view.log(LogsFromServer.LOBBY_IS_FULL.getMessage());
        }
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void leaveLobby(DiffSubscriber diffSubscriber) {
        //Lobby lobbyToLeave = this.games.getLobby(diffSubscriber.getTableName());
        //TODO lobby.remove user should implement unsubscribe
        lobbyToLeave.unsubscribe(diffSubscriber);
        lobbyToLeave.removeUserName(diffSubscriber.getNickname());
        diffSubscriber.log(LogsFromServer.LOBBY_LEFT.getMessage());
        diffSubscriber.transitionTo(ViewState.JOIN_GAME);
        synchronized (lobbyToLeave){
            if(lobbyToLeave.getLobbyPlayerList().isEmpty()){
                this.games.removeLobby(lobbyToLeave);
            }
        }
    }

    @Override
    public void joinGame(Lobby lobby) {
        //lobby.getSubscribers();
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
    //TODO diff get-set (codex) modify the lightModel directly
    //TODO diff to set lobbyName
}
