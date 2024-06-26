package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;

/**
 * Message that contains the secret objective chosen by the player
 */
public class ChoseSecretObjectiveMsg extends ClientMsg {
    /**
     * The secret objective chosen by the player
     */
    private LightCard secretObjective;

/**
     * Constructor
     * @param secretObjectiveCard the secret objective chosen by the player
     */
    public ChoseSecretObjectiveMsg(LightCard secretObjectiveCard) {
        this.secretObjective = secretObjectiveCard;
    }

    /**
     * Pass to the controller the secret objective chosen by the player
     * @param clientHandler the clientHandler that will process the message
     * @throws Exception If an error occurs during the processing of the message
     */
    @Override
    public void processMsg(ClientHandler clientHandler) throws Exception {
        clientHandler.getController().chooseSecretObjective(secretObjective);
    }
}