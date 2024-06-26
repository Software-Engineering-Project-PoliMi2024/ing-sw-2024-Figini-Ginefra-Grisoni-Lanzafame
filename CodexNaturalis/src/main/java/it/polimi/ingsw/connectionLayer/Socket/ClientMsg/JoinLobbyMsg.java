package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;

public class JoinLobbyMsg extends ClientMsg{
    /**
     * The name of the lobby that the player wants to join
     */
    private String lobbyName;

    /**
     * Constructor of the class
     * @param lobbyName the name of the lobby that the player wants to join
     */
    public JoinLobbyMsg(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    /**
     * Call the controller for joining a lobby
     * @param clientHandler the serverHandler that will process the message
     * @throws Exception If an error occurs during the processing of the message
     */
    @Override
    public void processMsg(ClientHandler clientHandler) throws Exception{
        clientHandler.getController().joinLobby(lobbyName);
    }
}