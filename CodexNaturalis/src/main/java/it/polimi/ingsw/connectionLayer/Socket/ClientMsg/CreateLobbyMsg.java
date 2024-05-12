package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;
public class CreateLobbyMsg extends ClientMsg {
    private final String gameName;
    private final int maxPlayerCount;


    public CreateLobbyMsg(String gameName, int maxPlayerCount) {
        this.gameName = gameName;
        this.maxPlayerCount = maxPlayerCount;
    }

    public String getGameName() {
        return gameName;
    }

    public int getMaxPlayerCount() {
        return maxPlayerCount;
    }
    @Override
    public void processMsg(ClientHandler clientHandler) throws Exception {
        ControllerInterface controller = clientHandler.getController();
        controller.createLobby(gameName, maxPlayerCount);
    }
}
