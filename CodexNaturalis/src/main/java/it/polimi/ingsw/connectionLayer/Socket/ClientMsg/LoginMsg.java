package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;

/**
 * LoginMsg is a message sent from the client that contains the call to the controller for login and the user nick
 */
public class LoginMsg extends ClientMsg{
    /**
     * The username of the player
     */
    private final String username;

    /**
     * Constructor
     * @param username the username of the player
     */
    public LoginMsg(String username) {
        this.username = username;
    }

    /**
     * Call the controller for login
     * @param clientHandler the clientHandler that will process the message
     * @throws Exception If an error occurs during the processing of the message
     */
    @Override
    public void processMsg(ClientHandler clientHandler) throws Exception{
        clientHandler.getController().login(username);
    }
}
