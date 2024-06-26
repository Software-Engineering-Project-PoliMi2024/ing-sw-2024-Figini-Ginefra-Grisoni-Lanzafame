package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;

public class LeaveLobbyMsg extends ClientMsg{
    /**
     * Call the controller for leaving the lobby
     * @param clientHandler the serverHandler that will process the message
     * @throws Exception If an error occurs during the processing of the message
     */
    @Override
    public void processMsg(ClientHandler clientHandler) throws Exception{
        clientHandler.getController().leaveLobby();
    }
}