package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.controller.Interfaces.ControllerInterface;
import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;

/**
 * Message containing the call of the controller for creating a new lobby and the parameters needed
 */
public class CreateLobbyMsg extends ClientMsg {
    /**
     * The name of new the game
     */
    private final String gameName;

    /**
     * The number of players needed to start the game
     */
    private final int maxPlayerCount;

    private final int numberOfAgents;

    /**
     * Constructor
     * @param gameName the name of the new game
     * @param maxPlayerCount the number of players needed to start the game
     */
    public CreateLobbyMsg(String gameName, int maxPlayerCount, int numberOfAgents) {
        this.gameName = gameName;
        this.maxPlayerCount = maxPlayerCount;
        this.numberOfAgents = numberOfAgents;
    }

    /**
     * Call the controller for creating a new lobby
     * @param clientHandler the clientHandler that will process the message
     * @throws Exception If an error occurs during the processing of the message
     */
    @Override
    public void processMsg(ClientHandler clientHandler) throws Exception {
        ControllerInterface controller = clientHandler.getController();
        controller.createLobby(gameName, maxPlayerCount, numberOfAgents);
    }
}
