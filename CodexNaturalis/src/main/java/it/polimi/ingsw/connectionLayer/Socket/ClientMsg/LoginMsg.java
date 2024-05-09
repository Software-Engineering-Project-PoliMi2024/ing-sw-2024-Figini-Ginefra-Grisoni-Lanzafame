package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;

public class LoginMsg extends ClientMsg{
    private String username;
    public LoginMsg(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void processMsg(ClientHandler clientHandler) throws Exception{
        clientHandler.getController().login(username);
    }
}
